package com.conorsheppard.distributedlist;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory class that automatically creates serializers for common Java types.
 * Uses reflection and built-in type handling to eliminate the need for explicit serializer implementations.
 */
public class SerializerFactory {
    
    private static final Map<Class<?>, Serializer<?>> BUILTIN_SERIALIZERS = new HashMap<>();
    
    static {
        // Register built-in serializers for common types
        BUILTIN_SERIALIZERS.put(String.class, new StringSerializer());
        BUILTIN_SERIALIZERS.put(Integer.class, new IntegerSerializer());
        BUILTIN_SERIALIZERS.put(Long.class, new LongSerializer());
        BUILTIN_SERIALIZERS.put(Double.class, new DoubleSerializer());
        BUILTIN_SERIALIZERS.put(Float.class, new FloatSerializer());
        BUILTIN_SERIALIZERS.put(Boolean.class, new BooleanSerializer());
        BUILTIN_SERIALIZERS.put(Byte.class, new ByteSerializer());
        BUILTIN_SERIALIZERS.put(Short.class, new ShortSerializer());
        BUILTIN_SERIALIZERS.put(Character.class, new CharacterSerializer());
    }
    
    /**
     * Creates a serializer for the given type.
     * First checks for built-in serializers, then attempts to create one using reflection.
     * 
     * @param <T> the type to serialize
     * @param clazz the class of the type
     * @return a serializer for the type
     * @throws IllegalArgumentException if no serializer can be created
     */
    @SuppressWarnings("unchecked")
    public static <T> Serializer<T> createSerializer(Class<T> clazz) {
        // Check if we have a built-in serializer
        Serializer<?> builtin = BUILTIN_SERIALIZERS.get(clazz);
        if (builtin != null) {
            return (Serializer<T>) builtin;
        }
        
        // Try to create a serializer using reflection
        Serializer<T> reflective = createReflectiveSerializer(clazz);
        if (reflective != null) {
            return reflective;
        }
        
        throw new IllegalArgumentException(
            "No serializer available for type: " + clazz.getName() + 
            ". Consider implementing Serializer<" + clazz.getSimpleName() + "> or " +
            "ensuring the type has a String constructor and toString() method."
        );
    }
    
    /**
     * Attempts to create a serializer using reflection.
     * Works for types that have a String constructor and toString() method.
     */
    private static <T> Serializer<T> createReflectiveSerializer(Class<T> clazz) {
        try {
            // Check if the class has a String constructor
            Constructor<T> stringConstructor = clazz.getConstructor(String.class);
            
            return new Serializer<T>() {
                @Override
                public String serialize(T value) {
                    return value != null ? value.toString() : null;
                }
                
                @Override
                public T deserialize(String data) {
                    try {
                        return data != null ? stringConstructor.newInstance(data) : null;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to deserialize " + clazz.getName() + " from: " + data, e);
                    }
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    /**
     * Registers a custom serializer for a specific type.
     * Useful for overriding built-in serializers or adding support for custom types.
     */
    public static <T> void registerSerializer(Class<T> clazz, Serializer<T> serializer) {
        BUILTIN_SERIALIZERS.put(clazz, serializer);
    }
    
    /**
     * Checks if a serializer is available for the given type.
     */
    public static boolean hasSerializer(Class<?> clazz) {
        if (BUILTIN_SERIALIZERS.containsKey(clazz)) {
            return true;
        }
        
        try {
            clazz.getConstructor(String.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
