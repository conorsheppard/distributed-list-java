package com.conorsheppard;

import com.conorsheppard.distributedlist.list.DistributedList;
import com.conorsheppard.distributedlist.store.SimpleStoreClient;
import com.conorsheppard.distributedlist.store.StoreClient;
import com.conorsheppard.distributedlist.serializers.StringSerializer;
import com.conorsheppard.distributedlist.serializers.IntegerSerializer;
import com.conorsheppard.distributedlist.serializers.SerializerFactory;
import com.conorsheppard.distributedlist.serializers.Serializer;
import com.conorsheppard.distributedlist.model.Cat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DistributedListTest {
    static Stream<Arguments> listInputProvider() {
        return Stream.of(
                Arguments.of("list1", List.of("a", "b", "c")),
                Arguments.of("list2", List.of("one", "two")),
                Arguments.of("list3", List.of("foo")),
                Arguments.of("list4", List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("listInputProvider")
    void testAddAndGet(String listName, List<String> items) {
        StoreClient<String, String> storeClient = new SimpleStoreClient();
        DistributedList<Integer, String> distributedList = new DistributedList<>(
            storeClient, listName, new IntegerSerializer(), new StringSerializer());

        // Add items
        for (String item : items) {
            distributedList.add(item);
        }

        // Check size
        assertEquals(items.size(), distributedList.size(), "Size mismatch for list: " + listName);

        // Verify contents
        for (int i = 0; i < items.size(); i++) {
            assertEquals(items.get(i), distributedList.get(i), "Mismatch at index " + i);
        }
    }

    @Test
    void testRemove() {
        StoreClient<String, String> storeClient = new SimpleStoreClient();
        DistributedList<Integer, String> list = new DistributedList<>(
            storeClient, "removalTest", new IntegerSerializer(), new StringSerializer());

        list.add("alpha");
        list.add("beta");
        list.add("gamma");

        list.remove(1); // remove "beta"

        assertEquals(2, list.size());
        assertEquals("alpha", list.get(0));
        assertEquals("gamma", list.get(1));
    }

    @Test
    void testMultipleListsIsolation() {
        StoreClient<String, String> storeClient = new SimpleStoreClient();
        DistributedList<Integer, String> listA = new DistributedList<>(
            storeClient, "listA", new IntegerSerializer(), new StringSerializer());
        DistributedList<Integer, String> listB = new DistributedList<>(
            storeClient, "listB", new IntegerSerializer(), new StringSerializer());

        listA.add("A1");
        listB.add("B1");

        assertEquals("A1", listA.get(0));
        assertEquals("B1", listB.get(0));

        assertEquals(1, listA.size());
        assertEquals(1, listB.size());
    }

    @Test
    void testGenericWithInteger() {
        StoreClient<String, String> storeClient = new SimpleStoreClient();
        DistributedList<Integer, Integer> intList = new DistributedList<>(
            storeClient, "intList", new IntegerSerializer(), new IntegerSerializer());

        intList.add(42);
        intList.add(100);
        intList.add(999);

        assertEquals(3, intList.size());
        assertEquals(Integer.valueOf(42), intList.get(0));
        assertEquals(Integer.valueOf(100), intList.get(1));
        assertEquals(Integer.valueOf(999), intList.get(2));
    }

    @Test
    void testMainExampleStringList() {
        StoreClient<String, String> stringStore = new SimpleStoreClient();
        DistributedList<Integer, String> stringList = new DistributedList<>(
            stringStore, "strings", new IntegerSerializer(), new StringSerializer());

        stringList.add("Hello");
        stringList.add("World");
        stringList.add("Generic");
        stringList.add("List");

        assertEquals(4, stringList.size());
        assertEquals("Hello", stringList.get(0));
        assertEquals("World", stringList.get(1));
        assertEquals("Generic", stringList.get(2));
        assertEquals("List", stringList.get(3));
    }

    @Test
    void testMainExampleIntegerList() {
        StoreClient<String, String> intStore = new SimpleStoreClient();
        DistributedList<Integer, Integer> intList = new DistributedList<>(
            intStore, "integers", new IntegerSerializer(), new IntegerSerializer());

        intList.add(42);
        intList.add(100);
        intList.add(999);
        intList.add(12345);

        assertEquals(4, intList.size());
        assertEquals(Integer.valueOf(42), intList.get(0));
        assertEquals(Integer.valueOf(100), intList.get(1));
        assertEquals(Integer.valueOf(999), intList.get(2));
        assertEquals(Integer.valueOf(12345), intList.get(3));
    }

    @Test
    void testMainExampleMixedList() {
        StoreClient<String, String> mixedStore = new SimpleStoreClient();
        DistributedList<String, Integer> mixedList = new DistributedList<>(
            mixedStore, "mixed", new StringSerializer(), new IntegerSerializer());

        mixedList.add(10);
        mixedList.add(20);
        mixedList.add(30);

        assertEquals(3, mixedList.size());
        assertEquals(Integer.valueOf(10), mixedList.get("0"));
        assertEquals(Integer.valueOf(20), mixedList.get("1"));
        assertEquals(Integer.valueOf(30), mixedList.get("2"));
    }

    @Test
    void testMainExampleRemoval() {
        StoreClient<String, String> stringStore = new SimpleStoreClient();
        DistributedList<Integer, String> stringList = new DistributedList<>(
            stringStore, "removalTest", new IntegerSerializer(), new StringSerializer());

        stringList.add("Hello");
        stringList.add("World");
        stringList.add("Generic");
        stringList.add("List");

        stringList.remove(1); // remove "World"

        assertEquals(3, stringList.size());
        assertEquals("Hello", stringList.get(0));
        assertEquals("Generic", stringList.get(1));
        assertEquals("List", stringList.get(2));
    }

    @Test
    void testSerializerFactoryPattern() {
        StoreClient<String, String> store = new SimpleStoreClient();
        
        // Test automatic serializer creation with Class parameters
        DistributedList<Integer, String> autoList = new DistributedList<>(
            store, "auto-list", 
            Integer.class, 
            String.class
        );

        autoList.add("Hello");
        autoList.add("World");

        assertEquals(2, autoList.size());
        assertEquals("Hello", autoList.get(0));
        assertEquals("World", autoList.get(1));
    }

    @Test
    void testSerializerFactoryWithDifferentTypes() {
        StoreClient<String, String> store = new SimpleStoreClient();
        
        // Test Long and Double types
        DistributedList<Long, Double> numberList = new DistributedList<>(
            store, "numbers", 
            Long.class, 
            Double.class
        );

        numberList.add(3.14);
        numberList.add(2.718);

        assertEquals(2, numberList.size());
        assertEquals(3.14, numberList.get(0L));
        assertEquals(2.718, numberList.get(1L));
    }

    @Test
    void testSerializerFactoryWithBooleanAndCharacter() {
        StoreClient<String, String> store = new SimpleStoreClient();
        
        DistributedList<Boolean, Character> boolCharList = new DistributedList<>(
            store, "bool-chars", 
            Boolean.class, 
            Character.class
        );

        boolCharList.add('A');
        boolCharList.add('B');

        assertEquals(2, boolCharList.size());
        assertEquals('A', boolCharList.get(false));
        assertEquals('B', boolCharList.get(true));
    }

    @Test
    void testSerializerFactoryWithCatModel() {
        StoreClient<String, String> store = new SimpleStoreClient();
        
        // Test that Cat class works automatically via SerializerFactory
        DistributedList<Integer, Cat> catList = new DistributedList<>(
            store, "cats",
            Integer.class,
            Cat.class    // Factory automatically creates a serializer for Cat!
        );

        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");

        catList.add(whiskers);
        catList.add(mittens);

        assertEquals(2, catList.size());
        assertEquals("Whiskers", catList.get(0).getName());
        assertEquals("Mittens", catList.get(1).getName());
    }

    @Test
    void testSerializerFactoryCapabilities() {
        // Test that the factory can handle various types
        assertTrue(SerializerFactory.hasSerializer(Cat.class));
        assertTrue(SerializerFactory.hasSerializer(String.class));
        assertTrue(SerializerFactory.hasSerializer(Integer.class));
        assertTrue(SerializerFactory.hasSerializer(Long.class));
        assertTrue(SerializerFactory.hasSerializer(Double.class));
        assertTrue(SerializerFactory.hasSerializer(Boolean.class));
        assertTrue(SerializerFactory.hasSerializer(Character.class));
    }

    @Test
    void testCatModelSerialization() {
        // Test that Cat objects can be properly serialized and deserialized
        SerializerFactory.registerSerializer(Cat.class, SerializerFactory.createSerializer(Cat.class));
        
        Cat originalCat = new Cat("Whiskers");
        Serializer<Cat> catSerializer = SerializerFactory.createSerializer(Cat.class);
        
        String serialized = catSerializer.serialize(originalCat);
        Cat deserialized = catSerializer.deserialize(serialized);
        
        assertEquals("Whiskers", deserialized.getName());
        assertEquals(originalCat.getName(), deserialized.getName());
    }

    @Test
    void testMixedTypeOperations() {
        StoreClient<String, String> store = new SimpleStoreClient();
        
        // Test complex scenario with multiple types
        DistributedList<Integer, String> stringList = new DistributedList<>(
            store, "complex-strings", new IntegerSerializer(), new StringSerializer());
        DistributedList<Integer, Integer> intList = new DistributedList<>(
            store, "complex-ints", new IntegerSerializer(), new IntegerSerializer());
        DistributedList<Integer, Cat> catList = new DistributedList<>(
            store, "complex-cats", Integer.class, Cat.class);

        // Add to all lists
        stringList.add("Hello");
        intList.add(42);
        catList.add(new Cat("Whiskers"));

        // Verify all work independently
        assertEquals(1, stringList.size());
        assertEquals(1, intList.size());
        assertEquals(1, catList.size());

        assertEquals("Hello", stringList.get(0));
        assertEquals(Integer.valueOf(42), intList.get(0));
        assertEquals("Whiskers", catList.get(0).getName());
    }
}
