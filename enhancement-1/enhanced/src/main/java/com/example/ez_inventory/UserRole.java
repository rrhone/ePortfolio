package com.example.ez_inventory;

public class UserRole {
    public String role;

    public UserRole() {}  // Firestore needs an empty constructor

    public UserRole(String role) {
        this.role = role;
    }
}
