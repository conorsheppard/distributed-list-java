package com.conorsheppard.distributedlist;

public interface StoreClient<K, V> {
    V get(K key);
    void set(K key, V value);
    int incrementAndGet(K key);
}
