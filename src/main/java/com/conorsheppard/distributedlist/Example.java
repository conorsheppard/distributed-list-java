package com.conorsheppard.distributedlist;

/**
 * Example demonstrating how the SerializerFactory pattern simplifies
 * DistributedList usage with different data types.
 */
public class Example {
    
    public static void main(String[] args) {
        // Create a simple in-memory store for demonstration
        StoreClient<String, String> store = new SimpleStoreClient();
        
        // OLD WAY - Required explicit serializers
        DistributedList<Integer, String> oldWay = new DistributedList<>(
            store, "old-list", 
            new IntegerSerializer(), 
            new StringSerializer()
        );
        
        // NEW WAY - Automatic serializer creation!
        DistributedList<Integer, String> newWay = new DistributedList<>(
            store, "new-list", 
            Integer.class, 
            String.class
        );
        
        // Even more types work automatically now
        DistributedList<Long, Double> numberList = new DistributedList<>(
            store, "numbers", 
            Long.class, 
            Double.class
        );
        
        DistributedList<Boolean, Character> boolCharList = new DistributedList<>(
            store, "bool-chars", 
            Boolean.class, 
            Character.class
        );
        
        // MAGIC: Cat class works automatically because it has a String constructor!
        DistributedList<Integer, Cat> catList = new DistributedList<>(
            store, "cats",
            Integer.class,
            Cat.class    // Factory automatically creates a serializer for Cat!
        );
        
        // Test the new functionality
        newWay.add("Hello");
        newWay.add("World");
        System.out.println("Size: " + newWay.size());
        System.out.println("First element: " + newWay.get(0));
        
        numberList.add(3.14);
        numberList.add(2.718);
        System.out.println("Number list size: " + numberList.size());
        System.out.println("First number: " + numberList.get(0L));
        
        boolCharList.add('A');
        boolCharList.add('B');
        System.out.println("Bool-char list size: " + boolCharList.size());
        System.out.println("First char: " + boolCharList.get(false));
        
        // Test Cat as key
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        
        catList.add(whiskers);
        catList.add(mittens);
        
        System.out.println("Cat list size: " + catList.size());
        System.out.println("Whiskers' info: " + catList.get(0));
        System.out.println("Mittens' info: " + catList.get(1));

        // Show that the factory can handle Cat automatically
        System.out.println("Can serialize Cat? " + SerializerFactory.hasSerializer(Cat.class));
    }
}
