package com.example.ez_inventory;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ez_inventory.dao.ItemDao;
import com.example.ez_inventory.dao.UserDao;
import com.example.ez_inventory.data.Item;
import com.example.ez_inventory.data.User;

@Database(entities = {User.class, Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ItemDao inventoryItemDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "app_database")
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }
}