package com.example.ez_inventory.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "inventory_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int quantity;

    // Default constructor (required by Room)
    public Item() {
    }

    // Constructor with parameters
    @Ignore
    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void delete(Item item) {
    }
}
