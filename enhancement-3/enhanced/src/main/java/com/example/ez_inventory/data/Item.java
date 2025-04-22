package com.example.ez_inventory.data;

public class Item {
    private String id;       // Firestore document ID
    private String name;
    private int quantity;

    public Item() {
        // Needed for Firebase deserialization
    }

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
