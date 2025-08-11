package com.conorsheppard.distributedlist.serializers;

public class StringSerializer implements Serializer<String> {
    @Override
    public String serialize(String value) {
        return value;
    }

    @Override
    public String deserialize(String data) {
        return data;
    }
} 