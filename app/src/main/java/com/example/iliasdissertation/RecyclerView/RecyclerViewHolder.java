package com.example.iliasdissertation.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iliasdissertation.Models.CategoryModel;
import com.example.iliasdissertation.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    ImageView ivCategoryImage;
    TextView tvCategoryName;

    CardView cvMenuCategory;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
        tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        cvMenuCategory = itemView.findViewById(R.id.cvMenuCategory);
    }

    public void bind(CategoryModel item){
        Glide.with(itemView.getContext()).load(item.getImage()).into(ivCategoryImage);
        tvCategoryName.setText(item.getName());
    }
}
