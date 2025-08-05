package com.conorsheppard.distributedlist;

public interface Serializer<T> {
    String serialize(T value);
    T deserialize(String data);
} 