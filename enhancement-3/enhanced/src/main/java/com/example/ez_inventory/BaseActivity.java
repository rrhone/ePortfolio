package com.example.ez_inventory;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // If we're on the MainActivity (login screen), skip inflating the menu entirely
        if (this instanceof MainActivity) {
            return false;
        }

        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inventory, menu);

        // Retrieve the saved role from SharedPreferences
        String role = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getString("userRole", "user"); // default to user

        // Hide "Inventory Menu" option for non-admins
        if (!"admin".equals(role)) {
            MenuItem inventoryItem = menu.findItem(R.id.action_inventory);
            if (inventoryItem != null) {
                inventoryItem.setVisible(false);
            }
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Optional: further ensure visibility is correct when menu is prepared
        MenuItem inventoryItem = menu.findItem(R.id.action_inventory);
        if (inventoryItem != null) {
            String role = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    .getString("userRole", "user");
            inventoryItem.setVisible("admin".equals(role));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_inventory) {
            // Navigate back to InventoryActivity
            Intent intent = new Intent(this, InventoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_logout) {
            // Clear session and return to login
            getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().clear().apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
