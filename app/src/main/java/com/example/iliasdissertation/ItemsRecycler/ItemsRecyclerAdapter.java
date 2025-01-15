package com.example.iliasdissertation.ItemsRecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iliasdissertation.Models.ItemModel;
import com.example.iliasdissertation.R;

import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerViewHolder> {

    private List<ItemModel> items;
    private ItemsSelectListener listener;

    public ItemsRecyclerAdapter(List<ItemModel> items, ItemsSelectListener listener){
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item_view, parent, false);
        return new ItemsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsRecyclerViewHolder holder, int position) {
        ItemModel item = items.get(position);
        holder.bind(item);

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<ItemModel> items){
        this.items = items;
        notifyDataSetChanged();
    }
}
