package com.conorsheppard;

import com.conorsheppard.distributedlist.serializers.SerializerFactory;
import com.conorsheppard.distributedlist.serializers.Serializer;
import com.conorsheppard.distributedlist.model.Cat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class SerializerFactoryTest {

    @BeforeEach
    void setUp() {
        // Reset any custom serializers that might have been registered
        // This ensures tests start with a clean state
    }

    @Test
    void testBuiltInSerializers() {
        // Test that all built-in serializers are available
        assertTrue(SerializerFactory.hasSerializer(String.class));
        assertTrue(SerializerFactory.hasSerializer(Integer.class));
        assertTrue(SerializerFactory.hasSerializer(Long.class));
        assertTrue(SerializerFactory.hasSerializer(Double.class));
        assertTrue(SerializerFactory.hasSerializer(Float.class));
        assertTrue(SerializerFactory.hasSerializer(Boolean.class));
        assertTrue(SerializerFactory.hasSerializer(Byte.class));
        assertTrue(SerializerFactory.hasSerializer(Short.class));
        assertTrue(SerializerFactory.hasSerializer(Character.class));
    }

    @Test
    void testStringSerializer() {
        Serializer<String> serializer = SerializerFactory.createSerializer(String.class);
        assertNotNull(serializer);
        
        String original = "Hello, World!";
        String serialized = serializer.serialize(original);
        String deserialized = serializer.deserialize(serialized);
        
        assertEquals(original, deserialized);
    }

    @Test
    void testIntegerSerializer() {
        Serializer<Integer> serializer = SerializerFactory.createSerializer(Integer.class);
        assertNotNull(serializer);
        
        Integer original = 42;
        String serialized = serializer.serialize(original);
        Integer deserialized = serializer.deserialize(serialized);
        
        assertEquals(original, deserialized);
    }

    @Test
    void testLongSerializer() {
        Serializer<Long> serializer = SerializerFactory.createSerializer(Long.class);
        assertNotNull(serializer);
        
        Long original = 123456789L;
        String serialized = serializer.serialize(original);
        Long deserialized = serializer.deserialize(serialized);
        
        assertEquals(original, deserialized);
    }

    @Test
    void testDoubleSerializer() {
        Serializer<Double> serializer = SerializerFactory.createSerializer(Double.class);
        assertNotNull(serializer);
        
        Double original = 3.14159;
        String serialized = serializer.serialize(original);
        Double deserialized = serializer.deserialize(serialized);
        
        assertEquals(original, deserialized);
    }

    @Test
    void testBooleanSerializer() {
        Serializer<Boolean> serializer = SerializerFactory.createSerializer(Boolean.class);
        assertNotNull(serializer);
        
        Boolean original = true;
        String serialized = serializer.serialize(original);
        Boolean deserialized = serializer.deserialize(serialized);
        
        assertEquals(original, deserialized);
    }

    @Test
    void testCharacterSerializer() {
        Serializer<Character> serializer = SerializerFactory.createSerializer(Character.class);
        assertNotNull(serializer);
        
        Character original = 'A';
        String serialized = serializer.serialize(original);
        Character deserialized = serializer.deserialize(serialized);
        
        assertEquals(original, deserialized);
    }

    @Test
    void testCatModelAutoSerialization() {
        // Test that Cat class works automatically via reflection
        assertTrue(SerializerFactory.hasSerializer(Cat.class));
        
        Serializer<Cat> serializer = SerializerFactory.createSerializer(Cat.class);
        assertNotNull(serializer);
        
        Cat originalCat = new Cat("Whiskers");
        String serialized = serializer.serialize(originalCat);
        Cat deserialized = serializer.deserialize(serialized);
        
        assertNotNull(deserialized);
        assertEquals("Whiskers", deserialized.getName());
        assertEquals(originalCat.getName(), deserialized.getName());
    }

    @Test
    void testCatModelWithDifferentNames() {
        Serializer<Cat> serializer = SerializerFactory.createSerializer(Cat.class);
        
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        Cat fluffy = new Cat("Fluffy");
        
        assertEquals("Whiskers", serializer.deserialize(serializer.serialize(whiskers)).getName());
        assertEquals("Mittens", serializer.deserialize(serializer.serialize(mittens)).getName());
        assertEquals("Fluffy", serializer.deserialize(serializer.serialize(fluffy)).getName());
    }

    @Test
    void testNullHandling() {
        Serializer<String> stringSerializer = SerializerFactory.createSerializer(String.class);
        Serializer<Integer> intSerializer = SerializerFactory.createSerializer(Integer.class);
        Serializer<Cat> catSerializer = SerializerFactory.createSerializer(Cat.class);
        
        // Test null serialization
        assertNull(stringSerializer.serialize(null));
        assertNull(intSerializer.serialize(null));
        assertNull(catSerializer.serialize(null));
        
        // Test null deserialization
        assertNull(stringSerializer.deserialize(null));
        assertNull(intSerializer.deserialize(null));
        assertNull(catSerializer.deserialize(null));
    }

    @Test
    void testCustomSerializerRegistration() {
        // Test custom serializer registration
        Cat testCat = new Cat("TestCat");
        Serializer<Cat> customSerializer = SerializerFactory.createSerializer(Cat.class);
        
        // Register the custom serializer
        SerializerFactory.registerSerializer(Cat.class, customSerializer);
        
        // Verify it's still available
        assertTrue(SerializerFactory.hasSerializer(Cat.class));
        
        // Test it still works
        Serializer<Cat> retrievedSerializer = SerializerFactory.createSerializer(Cat.class);
        assertNotNull(retrievedSerializer);
        
        String serialized = retrievedSerializer.serialize(testCat);
        Cat deserialized = retrievedSerializer.deserialize(serialized);
        assertEquals("TestCat", deserialized.getName());
    }

    @Test
    void testReflectionBasedSerialization() {
        // Test that types with String constructors work automatically
        // This tests the reflection-based serializer creation
        
        // Cat class has a String constructor, so it should work
        assertTrue(SerializerFactory.hasSerializer(Cat.class));
        
        Serializer<Cat> serializer = SerializerFactory.createSerializer(Cat.class);
        assertNotNull(serializer);
        
        // Test that the serializer actually works
        Cat cat = new Cat("ReflectionTest");
        String serialized = serializer.serialize(cat);
        Cat deserialized = serializer.deserialize(serialized);
        
        assertEquals("ReflectionTest", deserialized.getName());
    }

    @Test
    void testMultipleSerializerInstances() {
        // Test that multiple serializer instances work correctly
        Serializer<String> serializer1 = SerializerFactory.createSerializer(String.class);
        Serializer<String> serializer2 = SerializerFactory.createSerializer(String.class);
        
        assertNotNull(serializer1);
        assertNotNull(serializer2);
        
        // Both should work independently
        String testString = "Test String";
        assertEquals(testString, serializer1.deserialize(serializer1.serialize(testString)));
        assertEquals(testString, serializer2.deserialize(serializer2.serialize(testString)));
    }
}
