package com.example.ez_inventory.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_inventory.R;
import com.example.ez_inventory.data.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<Item> itemList; // List of inventory items
    private Item selectedItem;
    private List<Item> fullItemList;

    public interface OnItemSelectedListener {
        void onItemSelected(Item selectedItem);
    }
    private OnItemSelectedListener listener;
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
    public Item getSelectedItem() {
        return selectedItem;
    }

    public InventoryAdapter(List<Item> itemList) {
        this.itemList = itemList;
        this.fullItemList = new ArrayList<>(itemList); // Backup copy
    }

    public void filterList(String query) {
        if (query == null || query.trim().isEmpty()) {
            itemList = new ArrayList<>(fullItemList);
        } else {
            List<Item> filtered = new ArrayList<>();
            for (Item item : fullItemList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(item);
                }
            }
            itemList = filtered;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item view for RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to ViewHolder
        Item item = itemList.get(position);
        holder.itemName.setText(item.getName());

        int quantity = item.getQuantity();
        holder.itemQuantity.setText("Quantity: " + quantity);

        // Low-stock visual alert
        if (quantity < 5) {
            holder.itemQuantity.setTextColor(Color.RED);
            holder.itemQuantity.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            holder.itemQuantity.setTextColor(Color.BLACK);
            holder.itemQuantity.setTypeface(null, android.graphics.Typeface.NORMAL);
        }


        holder.itemView.setBackgroundColor(
                item.equals(selectedItem) ? Color.LTGRAY : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            if (item.equals(selectedItem)) {
                selectedItem = null;
            } else {
                selectedItem = item;
            }
            if (listener != null) {
                listener.onItemSelected(selectedItem);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName; // TextView for item name
        TextView itemQuantity; // TextView for item quantity

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}