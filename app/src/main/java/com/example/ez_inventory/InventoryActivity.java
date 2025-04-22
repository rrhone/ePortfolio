package com.example.ez_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_inventory.adapters.InventoryAdapter;
import com.example.ez_inventory.dao.ItemDao;
import com.example.ez_inventory.data.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class InventoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private ItemDao inventoryItemDao;
    private FloatingActionButton addButton;
    private Button editButton;
    private Button deleteButton;

    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Log.d("INVENTORY", "InventoryActivity started");

        userRole = getIntent().getStringExtra("userRole");
        boolean isAdmin = "admin".equals(userRole);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up DAO
        inventoryItemDao = AppDatabase.getDatabase(this).inventoryItemDao();

        // Find buttons
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE);

        Log.d("USER_ROLE", "Logged in as: " + userRole);

        // Hide buttons for non-admins
        if (isAdmin) {
            addButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        } else {
            addButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            Toast.makeText(this, "Read-only access", Toast.LENGTH_SHORT).show();
        }

        // Admin-only: add button action
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditItemActivity.class);
            intent.putExtra("userRole", userRole);
            startActivity(intent);
        });

        // Admin-only: delete item
        deleteButton.setOnClickListener(v -> {
            Item itemToDelete = inventoryAdapter.getSelectedItem();
            if (itemToDelete != null) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete \"" + itemToDelete.getName() + "\"?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            new Thread(() -> {
                                inventoryItemDao.delete(itemToDelete);
                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                                    reloadItems();
                                    editButton.setVisibility(View.GONE);
                                });
                            }).start();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Admin-only: edit item
        editButton.setOnClickListener(v -> {
            Item selectedItem = inventoryAdapter.getSelectedItem();
            if (selectedItem != null) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Edit Item")
                        .setMessage("Are you sure you want to edit \"" + selectedItem.getName() + "\"?")
                        .setPositiveButton("Edit", (dialog, which) -> {
                            Intent intent = new Intent(this, AddEditItemActivity.class);
                            intent.putExtra("itemId", selectedItem.getId());
                            intent.putExtra("userRole", userRole);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
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
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().clear().apply();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadItems();
        editButton.setVisibility(View.GONE);
    }

    private void reloadItems() {
        new Thread(() -> {
            try {
                List<Item> itemList = inventoryItemDao.getAllItems();
                Log.d("INVENTORY", "Number of items inventory: " + itemList.size());

                runOnUiThread(() -> {
                    inventoryAdapter = new InventoryAdapter(itemList);

                    inventoryAdapter.setOnItemSelectedListener(selectedItem -> {
                        if ("admin".equals(userRole)) {
                            editButton.setVisibility(selectedItem != null ? View.VISIBLE : View.GONE);
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
