package com.conorsheppard.distributedlist;

public class IntegerSerializer implements Serializer<Integer> {
    @Override
    public String serialize(Integer value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Integer deserialize(String data) {
        return data != null ? Integer.parseInt(data) : null;
    }
} 