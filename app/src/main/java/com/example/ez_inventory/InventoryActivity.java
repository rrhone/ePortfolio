package com.example.ez_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_inventory.adapters.InventoryAdapter;
import com.example.ez_inventory.data.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private FloatingActionButton addButton;
    private Button editButton;
    private Button deleteButton;
    private String userRole;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        EditText searchEditText = findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inventoryAdapter != null) {
                    inventoryAdapter.filterList(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Log.d("INVENTORY", "InventoryActivity started");

        userRole = getIntent().getStringExtra("userRole");
        boolean isAdmin = "admin".equals(userRole);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // CRUD buttons
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE);

        //Log.d("USER_ROLE", "Logged in as: " + userRole);

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
                            db.collection("items").document(itemToDelete.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                                        reloadItems();
                                        editButton.setVisibility(View.GONE);
                                        addButton.setVisibility(View.VISIBLE); // optional if using FAB
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
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
        itemsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Item> itemList = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Item item = doc.toObject(Item.class);
                item.setId(doc.getId()); // Capture Firestore document ID
                itemList.add(item);
            }

            inventoryAdapter = new InventoryAdapter(itemList);
            recyclerView.setAdapter(inventoryAdapter);

            inventoryAdapter.setOnItemSelectedListener(selectedItem -> {
                if ("admin".equals(userRole)) {
                    editButton.setVisibility(selectedItem != null ? View.VISIBLE : View.GONE);
                }
            });

        }).addOnFailureListener(e -> {
            Log.e("INVENTORY", "Error loading items: " + e.getMessage());
            Toast.makeText(this, "Failed to load items", Toast.LENGTH_SHORT).show();
        });
    }

}
