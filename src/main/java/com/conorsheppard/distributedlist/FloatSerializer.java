package com.conorsheppard.distributedlist;

public class FloatSerializer implements Serializer<Float> {
    @Override
    public String serialize(Float value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Float deserialize(String data) {
        return data != null ? Float.parseFloat(data) : null;
    }
}
