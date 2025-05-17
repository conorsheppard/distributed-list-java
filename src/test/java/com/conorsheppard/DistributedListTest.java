package com.conorsheppard;

import com.conorsheppard.distributedlist.DistributedList;
import com.conorsheppard.distributedlist.SimpleStoreClient;
import com.conorsheppard.distributedlist.StoreClient;
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
        StoreClient<String> storeClient = new SimpleStoreClient();
        DistributedList<String> distributedList = new DistributedList<>(storeClient, listName);

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
        StoreClient<String> storeClient = new SimpleStoreClient();
        DistributedList<String> list = new DistributedList<>(storeClient, "removalTest");

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
        StoreClient<String> storeClient = new SimpleStoreClient();
        DistributedList<String> listA = new DistributedList<>(storeClient, "listA");
        DistributedList<String> listB = new DistributedList<>(storeClient, "listB");

        listA.add("A1");
        listB.add("B1");

        assertEquals("A1", listA.get(0));
        assertEquals("B1", listB.get(0));

        assertEquals(1, listA.size());
        assertEquals(1, listB.size());
    }
}
