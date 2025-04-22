package com.example.ez_inventory;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.example.ez_inventory.data.Item;

public class AddEditItemActivity extends BaseActivity {
    private EditText itemNameEditText;
    private EditText itemQuantityEditText;
    private FirebaseFirestore db;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI
        itemNameEditText = findViewById(R.id.itemName);
        itemQuantityEditText = findViewById(R.id.itemQuantity);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveItem()); // Hook up button

        // Check for itemId passed in intent
        itemId = getIntent().getStringExtra("itemId");
        if (itemId != null && !itemId.isEmpty()) {
            db.collection("items").document(itemId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Item itemToEdit = documentSnapshot.toObject(Item.class);
                            if (itemToEdit != null) {
                                itemNameEditText.setText(itemToEdit.getName());
                                itemQuantityEditText.setText(String.valueOf(itemToEdit.getQuantity()));
                            }
                        }
                    });
        }
    }

    // Save new or edited item
    private void saveItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String quantityStr = itemQuantityEditText.getText().toString().trim();

        if (itemName.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int itemQuantity;
        try {
            itemQuantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        Item item = new Item(itemName, itemQuantity);

        if (itemId != null && !itemId.isEmpty()) {
            // Update existing item
            db.collection("items").document(itemId)
                    .set(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            // Add new item
            db.collection("items")
                    .add(item)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Add failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }
}
