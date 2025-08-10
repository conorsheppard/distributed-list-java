package com.conorsheppard.distributedlist.list;

import com.conorsheppard.distributedlist.serializers.Serializer;
import com.conorsheppard.distributedlist.serializers.SerializerFactory;
import com.conorsheppard.distributedlist.store.StoreClient;

public class DistributedList<K, V> {
    private final StoreClient<String, String> storeClient;
    private final String listIdentifier;
    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;

    /**
     * Constructor that automatically creates serializers using SerializerFactory.
     * This eliminates the need to manually specify serializers for common types.
     * 
     * @param storeClient the storage client
     * @param listName the name of the list
     * @param keyClass the class of the key type
     * @param valueClass the class of the value type
     */
    public DistributedList(StoreClient<String, String> storeClient, String listName, 
                         Class<K> keyClass, Class<V> valueClass) {
        this(storeClient, listName, 
             SerializerFactory.createSerializer(keyClass),
             SerializerFactory.createSerializer(valueClass));
    }

    /**
     * Constructor that requires explicit serializers.
     * Use this when you need custom serialization behavior.
     */
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
        
        // Convert index to int for position checking
        int targetIndex = Integer.parseInt(keySerializer.serialize(index));
        if (targetIndex < 0 || targetIndex >= currentSize) return;
        
        // Shift all elements after the removed index left by one position
        for (int i = targetIndex; i < currentSize - 1; i++) {
            K currentKey = keySerializer.deserialize(String.valueOf(i));
            K nextKey = keySerializer.deserialize(String.valueOf(i + 1));
            
            // Get the next element
            String nextValue = storeClient.get(listIdentifier + keySerializer.serialize(nextKey));
            
            // Move it to the current position
            storeClient.set(listIdentifier + keySerializer.serialize(currentKey), nextValue);
        }
        
        // Remove the last element (now duplicated)
        K lastIndex = keySerializer.deserialize(String.valueOf(currentSize - 1));
        storeClient.set(listIdentifier + keySerializer.serialize(lastIndex), null);
        
        // Update size
        storeClient.set(listIdentifier + "size", String.valueOf(currentSize - 1));
    }

    public int size() {
        String raw = storeClient.get(listIdentifier + "size");
        return raw == null ? 0 : Integer.parseInt(raw);
    }
}
