package com.conorsheppard;

import com.conorsheppard.distributedlist.DistributedList;
import com.conorsheppard.distributedlist.RedisStoreClient;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.Jedis;

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
        DistributedList<String> list = new DistributedList<>(storeClient, "test:list");

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
        DistributedList<String> list = new DistributedList<>(storeClient, "test:removal");

        list.add("apple");
        list.add("banana");
        list.add("cherry");

        list.remove(1); // remove "banana"

        assertEquals(2, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("cherry", list.get(1));
    }

    @Test
    void testMultipleListsRedisIsolation() {
        DistributedList<String> list1 = new DistributedList<>(storeClient, "list1");
        DistributedList<String> list2 = new DistributedList<>(storeClient, "list2");

        list1.add("x");
        list2.add("y");

        assertEquals(1, list1.size());
        assertEquals(1, list2.size());
        assertEquals("x", list1.get(0));
        assertEquals("y", list2.get(0));
    }
}
