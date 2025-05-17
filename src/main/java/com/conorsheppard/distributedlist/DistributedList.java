package com.conorsheppard.distributedlist;

import java.util.Objects;

public class DistributedList<E> {
    private final StoreClient<String> storeClient;
    private final String listIdentifier;

    public DistributedList(StoreClient<String> storeClient, String listName) {
        if (storeClient == null || listName == null || listName.isEmpty()) {
            throw new IllegalArgumentException("StoreClient and listName must be non-null and non-empty.");
        }
        this.storeClient = storeClient;
        this.listIdentifier = listName + ":";
    }

    public E get(int index) {
        String serialized = storeClient.get(listIdentifier + index);
        // Assumes E is String for now (can add serialization later)
        return (E) serialized;
    }

    public void add(E element) {
        int index = storeClient.incrementAndGet(listIdentifier + "size") - 1;
        storeClient.set(listIdentifier + index, Objects.toString(element));
    }

    public void remove(int index) {
        // minus 1 since we're removing 1 element
        int updatedSize = Integer.parseInt(storeClient.get(listIdentifier + "size")) - 1; // No type checks for brevity
        // Set the updated post-removal list size
        storeClient.set(listIdentifier + "size", String.valueOf(updatedSize));

        for (int i = index; i < updatedSize; i++) {
            String next = storeClient.get(listIdentifier + (i + 1));
            storeClient.set(listIdentifier + i, next);
        }

        // Remove last entry
        storeClient.set(listIdentifier + updatedSize, null);
    }

    public int size() {
        String raw = storeClient.get(listIdentifier + "size");
        return raw == null ? 0 : Integer.parseInt(raw);
    }
}
