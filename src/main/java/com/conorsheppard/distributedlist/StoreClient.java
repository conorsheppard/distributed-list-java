package com.conorsheppard.distributedlist;

public interface StoreClient<E> {
    E get(String key);
    void set(String key, String value);
    int incrementAndGet(String key);
}
