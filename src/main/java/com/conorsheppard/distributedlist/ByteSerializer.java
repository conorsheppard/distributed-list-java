package com.conorsheppard.distributedlist;

public class ByteSerializer implements Serializer<Byte> {
    @Override
    public String serialize(Byte value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Byte deserialize(String data) {
        return data != null ? Byte.parseByte(data) : null;
    }
}
