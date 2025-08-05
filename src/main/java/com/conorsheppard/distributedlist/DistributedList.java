package com.conorsheppard.distributedlist;

public class DistributedList<K, V> {
    private final StoreClient<String, String> storeClient;
    private final String listIdentifier;
    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;

    public DistributedList(StoreClient<String, String> storeClient, String listName, 
                         Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        if (storeClient == null || listName == null || listName.isEmpty() || 
            keySerializer == null || valueSerializer == null) {
            throw new IllegalArgumentException("StoreClient, listName, keySerializer, and valueSerializer must be non-null and non-empty.");
        }
        this.storeClient = storeClient;
        this.listIdentifier = listName + ":";
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    public V get(K index) {
        String serializedKey = keySerializer.serialize(index);
        String serializedValue = storeClient.get(listIdentifier + serializedKey);
        return serializedValue != null ? valueSerializer.deserialize(serializedValue) : null;
    }

    public void add(V element) {
        int size = size();
        K index = keySerializer.deserialize(String.valueOf(size));
        String serializedKey = keySerializer.serialize(index);
        storeClient.set(listIdentifier + serializedKey, valueSerializer.serialize(element));
        
        // Update size
        storeClient.set(listIdentifier + "size", String.valueOf(size + 1));
    }

    public void remove(K index) {
        int currentSize = size();
        if (currentSize == 0) return;
        
        // Get the last element
        K lastIndex = keySerializer.deserialize(String.valueOf(currentSize - 1));
        String lastSerializedKey = keySerializer.serialize(lastIndex);
        String lastValue = storeClient.get(listIdentifier + lastSerializedKey);
        
        // Remove the target element
        String serializedKey = keySerializer.serialize(index);
        storeClient.set(listIdentifier + serializedKey, null);
        
        // If we're not removing the last element, move the last element to the removed position
        if (!index.equals(lastIndex)) {
            storeClient.set(listIdentifier + serializedKey, lastValue);
        }
        
        // Remove the last element
        storeClient.set(listIdentifier + lastSerializedKey, null);
        
        // Update size
        storeClient.set(listIdentifier + "size", String.valueOf(currentSize - 1));
    }

    public int size() {
        String raw = storeClient.get(listIdentifier + "size");
        return raw == null ? 0 : Integer.parseInt(raw);
    }
}
