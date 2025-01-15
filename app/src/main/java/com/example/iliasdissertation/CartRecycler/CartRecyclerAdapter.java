package com.example.iliasdissertation.CartRecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iliasdissertation.R;
import com.example.iliasdissertation.SQLiteDatabase.OrderEntity;

import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerViewHolder> {

    private List<OrderEntity> orderedItems;
    private CartEditItemSelectListener listener;
    private CartDeleteItemSelectListener deleteListener;

    public CartRecyclerAdapter(List<OrderEntity> orderedItems, CartEditItemSelectListener listener, CartDeleteItemSelectListener deleteListener) {
        this.orderedItems = orderedItems;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CartRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_view, parent, false);
        return new CartRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewHolder holder, int position) {
        OrderEntity orderedItem = orderedItems.get(position);
        holder.bind(orderedItem);

        holder.ivEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditIconClicked(orderedItem);
            }
        });

        holder.ivDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onDeleteIconClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderedItems.size();
    }

    public void updateData(List<OrderEntity> orderedItems){
        this.orderedItems = orderedItems;
        notifyDataSetChanged();
    }
}
