package com.example.ez_inventory;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ez_inventory.dao.ItemDao;
import com.example.ez_inventory.data.Item;

public class AddEditItemActivity extends BaseActivity {
    private EditText itemNameEditText; // EditText for item name
    private EditText itemQuantityEditText; // EditText for item quantity
    private ItemDao inventoryItemDao; // DAO for inventory item operations

    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize views
        itemNameEditText = findViewById(R.id.itemName);
        itemQuantityEditText = findViewById(R.id.itemQuantity);
        // Button to save item details
        Button saveButton = findViewById(R.id.saveButton);

        // Initialize the database and DAO
        AppDatabase db = AppDatabase.getDatabase(this);
        inventoryItemDao = db.inventoryItemDao();

        // Check if we're editing an existing item
        itemId = getIntent().getIntExtra("itemId", -1);
        if (itemId != -1) {
            new Thread(() -> {
                Item itemToEdit = inventoryItemDao.getItemById(itemId);
                if (itemToEdit != null) {
                    runOnUiThread(() -> {
                        itemNameEditText.setText(itemToEdit.getName());
                        itemQuantityEditText.setText(String.valueOf(itemToEdit.getQuantity()));
                    });
                }
            }).start();
        }

        // Set click listener for save button
        saveButton.setOnClickListener(v -> saveItem());
    }

    // Save new or edited item
    private void saveItem() {
        String itemName = itemNameEditText.getText().toString();
        String quantityStr = itemQuantityEditText.getText().toString();

        if (itemName.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int itemQuantity = Integer.parseInt(quantityStr);

        // 🔧 Room insert MUST be off the main thread
        new Thread(() -> {
            try {
                if (itemId != -1) {
                    // Update existing item
                    Item updatedItem = new Item(itemName, itemQuantity);
                    updatedItem.setId(itemId);
                    inventoryItemDao.update(updatedItem);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Edit successful!", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // Insert new item
                    inventoryItemDao.insert(new Item(itemName, itemQuantity));
                    runOnUiThread(() ->
                            Toast.makeText(this, "Add successful!", Toast.LENGTH_SHORT).show()
                    );
                }

                runOnUiThread(this::finish);
            } catch (Exception e) {
                Log.e("SAVE_ITEM", "Error saving item: " + e.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}