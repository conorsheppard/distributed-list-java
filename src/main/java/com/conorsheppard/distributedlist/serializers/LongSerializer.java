package com.conorsheppard.distributedlist.serializers;

public class LongSerializer implements Serializer<Long> {
    @Override
    public String serialize(Long value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Long deserialize(String data) {
        return data != null ? Long.parseLong(data) : null;
    }
}
