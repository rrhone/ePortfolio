package com.example.ez_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_inventory.adapters.InventoryAdapter;
import com.example.ez_inventory.dao.ItemDao;
import com.example.ez_inventory.data.Item;

import java.util.List;

public class InventoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private ItemDao inventoryItemDao;
    private Button addItemButton;
    private Button deleteItemButton;
    private Button editItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Log.d("INVENTORY", "InventoryActivity started");

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up DAO
        inventoryItemDao = AppDatabase.getDatabase(this).inventoryItemDao();

        addItemButton = findViewById(R.id.addItemButton);
        deleteItemButton = findViewById(R.id.deleteItemButton);
        editItemButton = findViewById(R.id.editItemButton);
        editItemButton.setVisibility(View.GONE);

        // Add item button
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditItemActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Delete item button
        deleteItemButton.setOnClickListener(v -> {
            Item itemToDelete = inventoryAdapter.getSelectedItem();
            if (itemToDelete != null) {
                new Thread(() -> {
                    inventoryItemDao.delete(itemToDelete);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                        reloadItems();
                        addItemButton.setVisibility(View.VISIBLE);
                        editItemButton.setVisibility(View.GONE);
                    });
                }).start();
            } else {
                Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit item button
        editItemButton.setOnClickListener(v -> {
            Item selectedItem = inventoryAdapter.getSelectedItem();
            if (selectedItem != null) {
                Intent intent = new Intent(this, AddEditItemActivity.class);
                intent.putExtra("itemId", selectedItem.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadItems(); // Refresh items when returning to screen
        editItemButton.setVisibility(View.GONE);
        addItemButton.setVisibility(View.VISIBLE);
    }

    private void reloadItems() {
        new Thread(() -> {
            try {
                List<Item> itemList = inventoryItemDao.getAllItems();
                Log.d("INVENTORY", "Number of items inventory: " + itemList.size());

                runOnUiThread(() -> {
                    inventoryAdapter = new InventoryAdapter(itemList);

                    // Respond to item selection changes
                    inventoryAdapter.setOnItemSelectedListener(selectedItem -> {
                        if (selectedItem != null) {
                            addItemButton.setVisibility(View.GONE);
                            editItemButton.setVisibility(View.VISIBLE);
                        } else {
                            addItemButton.setVisibility(View.VISIBLE);
                            editItemButton.setVisibility(View.GONE);
                        }
                    });
                    recyclerView.setAdapter(inventoryAdapter);
                });
            } catch (Exception e) {
                Log.e("INVENTORY", "Error loading items: " + e.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(this, "Failed to load items", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
