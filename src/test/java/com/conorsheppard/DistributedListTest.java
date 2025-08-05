package com.conorsheppard;

import com.conorsheppard.distributedlist.DistributedList;
import com.conorsheppard.distributedlist.SimpleStoreClient;
import com.conorsheppard.distributedlist.StoreClient;
import com.conorsheppard.distributedlist.StringSerializer;
import com.conorsheppard.distributedlist.IntegerSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
