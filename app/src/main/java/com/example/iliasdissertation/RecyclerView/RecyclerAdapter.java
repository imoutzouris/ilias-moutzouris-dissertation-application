package com.example.iliasdissertation.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iliasdissertation.Models.CategoryModel;
import com.example.iliasdissertation.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<CategoryModel> categoryItems;
    private SelectListener listener;
    public RecyclerAdapter(List<CategoryModel> categoryItems, SelectListener listener) {
        this.categoryItems = categoryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        CategoryModel categoryItem = categoryItems.get(position);
        holder.bind(categoryItem);

        holder.cvMenuCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(categoryItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public void updateData(List<CategoryModel> categoryItems){
        this.categoryItems = categoryItems;
        notifyDataSetChanged();
    }
}
