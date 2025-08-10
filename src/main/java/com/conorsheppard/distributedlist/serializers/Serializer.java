package com.conorsheppard.distributedlist.serializers;

public interface Serializer<T> {
    String serialize(T value);
    T deserialize(String data);
} 