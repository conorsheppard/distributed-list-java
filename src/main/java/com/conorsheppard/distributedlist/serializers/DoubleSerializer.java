package com.conorsheppard.distributedlist.serializers;

public class DoubleSerializer implements Serializer<Double> {
    @Override
    public String serialize(Double value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Double deserialize(String data) {
        return data != null ? Double.parseDouble(data) : null;
    }
}
