package com.example.ez_inventory.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ez_inventory.data.Item;
import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM inventory_table WHERE id = :id LIMIT 1")
    Item getItemById(int id);

    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM inventory_table")
    List<Item> getAllItems();
}