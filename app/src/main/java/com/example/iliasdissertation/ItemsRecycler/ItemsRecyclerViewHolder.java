package com.example.iliasdissertation.ItemsRecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iliasdissertation.Models.ItemModel;
import com.example.iliasdissertation.R;

public class ItemsRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tvItemName;
    TextView tvItemDescription;
    TextView tvItemPrice;
    ImageView ivItemImage;
    CardView cvItem;
    public ItemsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        tvItemName = itemView.findViewById(R.id.tvItemName);
        tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
        tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
        ivItemImage = itemView.findViewById(R.id.ivItemImage);
        cvItem = itemView.findViewById(R.id.cvItem);
    }

    public void bind(ItemModel item) {
        Glide.with(itemView.getContext()).load(item.getImage()).into(ivItemImage);
        tvItemName.setText(item.getName());
        tvItemDescription.setText(item.getDescription());

        String sPrice = Double.toString(item.getPrice());
        String[] decimalNumbers = sPrice.split("\\.");
        if(Integer.parseInt(decimalNumbers[1]) < 10){
            tvItemPrice.setText(String.valueOf(item.getPrice() + "0" + "$"));
        }
        else {
            tvItemPrice.setText(String.valueOf(item.getPrice() + "$"));
        }
    }
}
