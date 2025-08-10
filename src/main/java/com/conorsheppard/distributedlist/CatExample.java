package com.conorsheppard.distributedlist;

/**
 * Example demonstrating how to use custom objects like Cat in DistributedList.
 */
public class CatExample {
    
    public static void main(String[] args) {
        StoreClient<String, String> store = new SimpleStoreClient();
        
        // Example 1: Cat as VALUE (Integer as key - this works naturally)
        DistributedList<Integer, Cat> catList = new DistributedList<>(
            store, "cat-values", 
            Integer.class,    // Integer keys
            Cat.class         // Cat values - automatically serialized!
        );
        
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        Cat fluffy = new Cat("Fluffy");
        
        catList.add(whiskers);  // Index 0
        catList.add(mittens);    // Index 1
        catList.add(fluffy);     // Index 2
        
        System.out.println("=== Cat as VALUES ===");
        System.out.println("List size: " + catList.size());
        System.out.println("Cat at index 0: " + catList.get(0).getName());
        System.out.println("Cat at index 1: " + catList.get(1).getName());
        System.out.println("Cat at index 2: " + catList.get(2).getName());
        
        // Example 2: Cat as KEY (String as value)
        // This requires a different approach since DistributedList expects integer-like keys
        // But we can demonstrate the serializer works
        System.out.println("\n=== Cat Serialization Test ===");
        Serializer<Cat> catSerializer = SerializerFactory.createSerializer(Cat.class);
        
        String serializedWhiskers = catSerializer.serialize(whiskers);
        String serializedMittens = catSerializer.serialize(mittens);
        
        System.out.println("Serialized Whiskers: " + serializedWhiskers);
        System.out.println("Serialized Mittens: " + serializedMittens);
        
        Cat deserializedWhiskers = catSerializer.deserialize(serializedWhiskers);
        Cat deserializedMittens = catSerializer.deserialize(serializedMittens);
        
        System.out.println("Deserialized Whiskers: " + deserializedWhiskers.getName());
        System.out.println("Deserialized Mittens: " + deserializedMittens.getName());
        
        // Example 3: Show that the factory automatically handles Cat
        System.out.println("\n=== Factory Capabilities ===");
        System.out.println("Can serialize Cat? " + SerializerFactory.hasSerializer(Cat.class));
        System.out.println("Can serialize String? " + SerializerFactory.hasSerializer(String.class));
        System.out.println("Can serialize Integer? " + SerializerFactory.hasSerializer(Integer.class));
        
        // Example 4: Demonstrate that Cat works with any other type
        DistributedList<Cat, Integer> catToAge = new DistributedList<>(
            store, "cat-ages",
            Cat.class,    // Cat keys
            Integer.class // Integer values
        );
        
        // Note: This won't work as expected with the current DistributedList implementation
        // because it's designed for integer-like keys, but it shows the serializer works
        System.out.println("\n=== Cat as Keys (Serializer Test) ===");
        System.out.println("Cat serializer created successfully: " + (catSerializer != null));
    }
}
