package com.conorsheppard.distributedlist.serializers;

public class BooleanSerializer implements Serializer<Boolean> {
    @Override
    public String serialize(Boolean value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Boolean deserialize(String data) {
        return data != null ? Boolean.parseBoolean(data) : null;
    }
}
