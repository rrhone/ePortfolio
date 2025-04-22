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

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<Item> itemList; // List of inventory items
    private Item selectedItem;

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
        Log.d("ADAPTER", "Binding item: " + item.getName() + " (" + item.getQuantity() + ")");
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText("Quantity: " + item.getQuantity());

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

        /*holder.deleteButton.setOnClickListener(v -> {
            item.delete(item); // Delete item from the database
            itemList.remove(position); // Remove item from the list
            notifyItemRemoved(position); // Notify adapter about item removal
        });*/
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName; // TextView for item name
        TextView itemQuantity; // TextView for item quantity
        //Button deleteButton; // Button to delete item

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            //deleteButton = itemView.findViewById(R.id.deleteButton); // Initialize delete button
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}