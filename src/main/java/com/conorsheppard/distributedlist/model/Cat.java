package com.conorsheppard.distributedlist.model;

public class Cat {
    private String name;
    
    public Cat() {
        // Default constructor
    }
    
    public Cat(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name != null ? name : "Unnamed Cat";
    }
}
