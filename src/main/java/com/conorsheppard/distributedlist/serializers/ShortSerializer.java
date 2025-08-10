package com.conorsheppard.distributedlist.serializers;

public class ShortSerializer implements Serializer<Short> {
    @Override
    public String serialize(Short value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Short deserialize(String data) {
        return data != null ? Short.parseShort(data) : null;
    }
}
