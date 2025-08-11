package com.conorsheppard.distributedlist.serializers;

public class CharacterSerializer implements Serializer<Character> {
    @Override
    public String serialize(Character value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Character deserialize(String data) {
        return data != null && data.length() > 0 ? data.charAt(0) : null;
    }
}
