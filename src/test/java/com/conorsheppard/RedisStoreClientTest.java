package com.conorsheppard;

import com.conorsheppard.distributedlist.list.DistributedList;
import com.conorsheppard.distributedlist.serializers.IntegerSerializer;
import com.conorsheppard.distributedlist.serializers.StringSerializer;
import com.conorsheppard.distributedlist.store.RedisStoreClient;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisStoreClientTest {

    private static RedisStoreClient storeClient;
    private static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:latest"))
                    .withExposedPorts(6379);

    @BeforeAll
    static void startContainer() {
        REDIS_CONTAINER.start();
        System.setProperty("redis.uri", "redis://" + REDIS_CONTAINER.getHost() + ":" + REDIS_CONTAINER.getMappedPort(6379));
        storeClient = new RedisStoreClient(REDIS_CONTAINER.getHost(), REDIS_CONTAINER.getMappedPort(6379));
    }

    @BeforeEach
    void cleanRedisKeys() {
        try (Jedis jedis = new Jedis(REDIS_CONTAINER.getHost(), REDIS_CONTAINER.getMappedPort(6379))) {
            jedis.flushDB(); // Clear Redis between test runs
        }
    }

    @AfterAll
    static void tearDown() {
        storeClient.close();
        REDIS_CONTAINER.stop();
    }

    @Test
    void testAddAndGetFromRedis() {
        DistributedList<Integer, String> list = new DistributedList<>(
            storeClient, "test:list", new IntegerSerializer(), new StringSerializer());

        list.add("dog");
        list.add("cat");
        list.add("bird");

        assertEquals(3, list.size());
        assertEquals("dog", list.get(0));
        assertEquals("cat", list.get(1));
        assertEquals("bird", list.get(2));
    }

    @Test
    void testRemoveFromRedis() {
        DistributedList<Integer, String> list = new DistributedList<>(
            storeClient, "test:removal", new IntegerSerializer(), new StringSerializer());

        list.add("apple");
        list.add("banana");
        list.add("cherry");
        list.add("pear");

        list.remove(1); // remove "banana"

        assertEquals(3, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("cherry", list.get(1));
    }

    @Test
    void testMultipleListsRedisIsolation() {
        DistributedList<Integer, String> list1 = new DistributedList<>(
            storeClient, "list1", new IntegerSerializer(), new StringSerializer());
        DistributedList<Integer, String> list2 = new DistributedList<>(
            storeClient, "list2", new IntegerSerializer(), new StringSerializer());

        list1.add("x");
        list2.add("y");

        assertEquals(1, list1.size());
        assertEquals(1, list2.size());
        assertEquals("x", list1.get(0));
        assertEquals("y", list2.get(0));
    }

    @Test
    void testIncrementAndGet() {
        String key = "test:counter";
        
        // First increment should return 1
        int result1 = storeClient.incrementAndGet(key);
        assertEquals(1, result1);
        
        // Second increment should return 2
        int result2 = storeClient.incrementAndGet(key);
        assertEquals(2, result2);
        
        // Third increment should return 3
        int result3 = storeClient.incrementAndGet(key);
        assertEquals(3, result3);
    }

    @Test
    void testIncrementAndGetMultipleKeys() {
        String key1 = "test:counter1";
        String key2 = "test:counter2";
        
        // Increment first key
        assertEquals(1, storeClient.incrementAndGet(key1));
        assertEquals(2, storeClient.incrementAndGet(key1));
        
        // Increment second key (should be independent)
        assertEquals(1, storeClient.incrementAndGet(key2));
        assertEquals(2, storeClient.incrementAndGet(key2));
        
        assertEquals(3, storeClient.incrementAndGet(key1));
    }

    @Test
    void testIncrementAndGetWithExistingValue() {
        String key = "test:existing";
        
        // Set an initial value
        storeClient.set(key, "5");
        
        // Increment should start from 6
        assertEquals(6, storeClient.incrementAndGet(key));
        assertEquals(7, storeClient.incrementAndGet(key));
    }

    @Test
    void testIncrementAndGetWithNonNumericValue() {
        String key = "test:nonnumeric";
        
        // Set a non-numeric value
        storeClient.set(key, "hello");
        
        // Redis INCR will throw an error when trying to increment a non-numeric string
        // We expect an JedisDataException to be thrown
        try {
            storeClient.incrementAndGet(key);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (JedisDataException e) {
            assertEquals("ERR value is not an integer or out of range", e.getMessage());
        }
    }

    @Test
    void testIncrementAndGetWithNegativeValue() {
        String key = "test:negative";
        
        // Set a negative value
        storeClient.set(key, "-5");
        
        // Increment should start from -4
        assertEquals(-4, storeClient.incrementAndGet(key));
        assertEquals(-3, storeClient.incrementAndGet(key));
    }

    @Test
    void testIncrementAndGetWithLargeValue() {
        String key = "test:large";
        
        // Set a value close to Integer.MAX_VALUE
        storeClient.set(key, String.valueOf(Integer.MAX_VALUE - 2));
        
        // These should work fine
        assertEquals(Integer.MAX_VALUE - 1, storeClient.incrementAndGet(key));
        assertEquals(Integer.MAX_VALUE, storeClient.incrementAndGet(key));
    }

    @Test
    void testIncrementAndGetOverflow() {
        String key = "test:overflow";
        
        // Set a value at Integer.MAX_VALUE
        storeClient.set(key, String.valueOf(Integer.MAX_VALUE));
        
        // This increment should throw an exception due to overflow
        try {
            storeClient.incrementAndGet(key);
            // If we get here, the test should fail
            throw new AssertionError("Expected IllegalArgumentException for overflow");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Value 2147483648 is out of int range", e.getMessage());
        }
    }
}
