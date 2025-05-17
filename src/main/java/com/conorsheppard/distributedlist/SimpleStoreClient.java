package com.conorsheppard.distributedlist;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleStoreClient implements StoreClient<String> {
    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Override
    public String get(String key) {
        if (key.contains(":size")) return getSize(key);
        else return store.get(key);
    }

    private String getSize(String key) {
        return counters.getOrDefault(key, new AtomicInteger(0)).get() + "";
    }

    @Override
    public void set(String key, String value) {
        if (key.contains(":size")) setSize(key, value);
        else if (value == null) store.remove(key);
        else store.put(key, value);
    }

    private void setSize(String key, String value) {
        counters.put(key, new AtomicInteger(Integer.parseInt(value)));
    }

    @Override
    public int incrementAndGet(String key) {
        return counters.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }
}
