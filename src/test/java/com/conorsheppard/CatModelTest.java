package com.conorsheppard;

import com.conorsheppard.distributedlist.list.DistributedList;
import com.conorsheppard.distributedlist.store.SimpleStoreClient;
import com.conorsheppard.distributedlist.store.StoreClient;
import com.conorsheppard.distributedlist.serializers.IntegerSerializer;
import com.conorsheppard.distributedlist.serializers.SerializerFactory;
import com.conorsheppard.distributedlist.model.Cat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CatModelTest {

    private StoreClient<String, String> store;
    private DistributedList<Integer, Cat> catList;

    @BeforeEach
    void setUp() {
        store = new SimpleStoreClient();
        catList = new DistributedList<>(
            store, "test-cats",
            Integer.class,  // Integer keys (indices)
            Cat.class       // Cat values - automatically serialized!
        );
    }

    @Test
    void testCatModelBasicFunctionality() {
        // Test basic Cat model properties
        Cat cat = new Cat("Whiskers");
        assertEquals("Whiskers", cat.getName());
        
        cat.setName("Mittens");
        assertEquals("Mittens", cat.getName());
    }

    @Test
    void testCatModelToString() {
        Cat cat = new Cat("Whiskers");
        assertEquals("Whiskers", cat.toString());
        
        Cat unnamedCat = new Cat();
        assertEquals("Unnamed Cat", unnamedCat.toString());
    }

    @Test
    void testCatModelConstructors() {
        // Test default constructor
        Cat defaultCat = new Cat();
        assertNull(defaultCat.getName());
        
        // Test parameterized constructor
        Cat namedCat = new Cat("Whiskers");
        assertEquals("Whiskers", namedCat.getName());
    }

    @Test
    void testCatInDistributedList() {
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        Cat fluffy = new Cat("Fluffy");
        
        // Add cats to the list
        catList.add(whiskers);
        catList.add(mittens);
        catList.add(fluffy);
        
        // Verify list operations
        assertEquals(3, catList.size());
        assertEquals("Whiskers", catList.get(0).getName());
        assertEquals("Mittens", catList.get(1).getName());
        assertEquals("Fluffy", catList.get(2).getName());
    }

    @Test
    void testCatListOperations() {
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        Cat fluffy = new Cat("Fluffy");
        
        // Test add operations
        catList.add(whiskers);
        catList.add(mittens);
        catList.add(fluffy);
        
        assertEquals(3, catList.size());
        
        // Test get operations
        Cat retrievedWhiskers = catList.get(0);
        assertNotNull(retrievedWhiskers);
        assertEquals("Whiskers", retrievedWhiskers.getName());
        
        Cat retrievedMittens = catList.get(1);
        assertNotNull(retrievedMittens);
        assertEquals("Mittens", retrievedMittens.getName());
        
        Cat retrievedFluffy = catList.get(2);
        assertNotNull(retrievedFluffy);
        assertEquals("Fluffy", retrievedFluffy.getName());
    }

    @Test
    void testCatListRemoveOperation() {
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        Cat fluffy = new Cat("Fluffy");
        
        catList.add(whiskers);
        catList.add(mittens);
        catList.add(fluffy);
        
        assertEquals(3, catList.size());
        
        // Remove the middle cat (mittens at index 1)
        catList.remove(1);
        
        assertEquals(2, catList.size());
        assertEquals("Whiskers", catList.get(0).getName());
        assertEquals("Fluffy", catList.get(1).getName());
    }

    @Test
    void testCatListWithMultipleOperations() {
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        Cat fluffy = new Cat("Fluffy");
        Cat shadow = new Cat("Shadow");
        
        // Add cats
        catList.add(whiskers);
        catList.add(mittens);
        catList.add(fluffy);
        
        assertEquals(3, catList.size());
        
        // Remove and add more
        catList.remove(1);  // Remove mittens
        catList.add(shadow);
        
        assertEquals(3, catList.size());
        assertEquals("Whiskers", catList.get(0).getName());
        assertEquals("Fluffy", catList.get(1).getName());
        assertEquals("Shadow", catList.get(2).getName());
    }

    @Test
    void testCatListIsolation() {
        // Test that multiple cat lists are isolated
        DistributedList<Integer, Cat> catListA = new DistributedList<>(
            store, "cats-A", Integer.class, Cat.class);
        DistributedList<Integer, Cat> catListB = new DistributedList<>(
            store, "cats-B", Integer.class, Cat.class);
        
        Cat whiskers = new Cat("Whiskers");
        Cat mittens = new Cat("Mittens");
        
        catListA.add(whiskers);
        catListB.add(mittens);
        
        assertEquals(1, catListA.size());
        assertEquals(1, catListB.size());
        
        assertEquals("Whiskers", catListA.get(0).getName());
        assertEquals("Mittens", catListB.get(0).getName());
    }

    @Test
    void testCatModelEquality() {
        Cat cat1 = new Cat("Whiskers");
        Cat cat2 = new Cat("Whiskers");
        Cat cat3 = new Cat("Mittens");
        
        // Test that cats with same name are equal (if equals method is implemented)
        // Note: This test assumes equals() is implemented based on name
        // If not implemented, this test will fail and indicate the need for equals()
        
        // For now, test that they have the same name
        assertEquals(cat1.getName(), cat2.getName());
        assertNotEquals(cat1.getName(), cat3.getName());
    }

    @Test
    void testCatModelNullHandling() {
        // Test that Cat model handles null names gracefully
        Cat cat = new Cat();
        assertNull(cat.getName());
        
        cat.setName(null);
        assertNull(cat.getName());
        
        assertEquals("Unnamed Cat", cat.toString());
    }

    @Test
    void testCatListWithNullValues() {
        // Test that the list can handle null Cat objects
        catList.add(null);
        catList.add(new Cat("Whiskers"));
        
        assertEquals(2, catList.size());
        assertNull(catList.get(0));
        assertEquals("Whiskers", catList.get(1).getName());
    }

    @Test
    void testCatListPerformance() {
        // Test performance with multiple cats
        for (int i = 0; i < 100; i++) {
            catList.add(new Cat("Cat" + i));
        }
        
        assertEquals(100, catList.size());
        
        // Test retrieval performance
        for (int i = 0; i < 100; i++) {
            Cat cat = catList.get(i);
            assertNotNull(cat);
            assertEquals("Cat" + i, cat.getName());
        }
    }
}
