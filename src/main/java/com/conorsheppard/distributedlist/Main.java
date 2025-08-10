package com.conorsheppard.distributedlist;

public class Main {
    public static void main(String[] args) {
        // Create store clients
        StoreClient<String, String> stringStore = new SimpleStoreClient();
        StoreClient<String, String> intStore = new SimpleStoreClient();
        StoreClient<String, String> mixedStore = new SimpleStoreClient();
        
        // Create serializers
        StringSerializer stringSerializer = new StringSerializer();
        IntegerSerializer intSerializer = new IntegerSerializer();
        
        // Example 1: Integer keys, String values
        DistributedList<Integer, String> stringList = new DistributedList<>(
            stringStore, "strings", intSerializer, stringSerializer);
        
        // Example 2: Integer keys, Integer values  
        DistributedList<Integer, Integer> intList = new DistributedList<>(
            intStore, "integers", intSerializer, intSerializer);
        
        // Example 3: String keys, Integer values (mixed types)
        DistributedList<String, Integer> mixedList = new DistributedList<>(
            mixedStore, "mixed", stringSerializer, intSerializer);
        
        // Add to string list
        stringList.add("Hello");
        stringList.add("World");
        stringList.add("Generic");
        stringList.add("List");
        
        // Add to integer list
        intList.add(42);
        intList.add(100);
        intList.add(999);
        intList.add(12345);
        
        // Add to mixed list
        mixedList.add(10);
        mixedList.add(20);
        mixedList.add(30);
        
        // Print results
        System.out.println("String List (Integer keys, String values):");
        for (int i = 0; i < stringList.size(); i++) {
            System.out.println("  [" + i + "] = " + stringList.get(i));
        }
        
        System.out.println("\nInteger List (Integer keys, Integer values):");
        for (int i = 0; i < intList.size(); i++) {
            System.out.println("  [" + i + "] = " + intList.get(i));
        }
        
        System.out.println("\nMixed List (String keys, Integer values):");
        for (int i = 0; i < mixedList.size(); i++) {
            String key = String.valueOf(i);
            System.out.println("  [" + key + "] = " + mixedList.get(key));
        }
        
        // Demonstrate removal
        System.out.println("\nAfter removing index 1 from string list:");
        stringList.remove(1);
        for (int i = 0; i < stringList.size(); i++) {
            System.out.println("  [" + i + "] = " + stringList.get(i));
        }
        
        System.out.println("\nAfter removing key '1' from mixed list:");
        mixedList.remove("1");
        for (int i = 0; i < mixedList.size(); i++) {
            String key = String.valueOf(i);
            Integer value = mixedList.get(key);
            if (value != null) {
                System.out.println("  [" + key + "] = " + value);
            }
        }
    }
} 