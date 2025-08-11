package com.conorsheppard.distributedlist.store;

public interface StoreClient<K, V> {
    V get(K key);
    void set(K key, V value);
    int incrementAndGet(K key);
}
