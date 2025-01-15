package com.example.iliasdissertation.CartRecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iliasdissertation.R;
import com.example.iliasdissertation.SQLiteDatabase.OrderEntity;

import java.text.DecimalFormat;

public class CartRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView tvCartItemName;
    TextView tvCartItemDescription;
    TextView tvCartItemNotes;
    TextView tvCartItemPrice;
    TextView tvCartItemQuantity;
    TextView tvCartItemPriceTimesQuantity;
    ImageView ivDeleteButton;
    ImageView ivEditButton;
    CardView cvCartItem;


    public CartRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
        tvCartItemDescription = itemView.findViewById(R.id.tvCartItemDescription);
        tvCartItemNotes = itemView.findViewById(R.id.tvCartItemNotes);
        tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
        tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
        tvCartItemPriceTimesQuantity = itemView.findViewById(R.id.tvCartItemPriceTimesQuantity);
        ivDeleteButton = itemView.findViewById(R.id.ivDeleteButton);
        ivEditButton = itemView.findViewById(R.id.ivEditButton);
        cvCartItem = itemView.findViewById(R.id.cvCartItem);
    }

    public void bind (OrderEntity orderedItem){
        tvCartItemName.setText(orderedItem.getOrderName());
        tvCartItemDescription.setText(orderedItem.getDescription());
        tvCartItemNotes.setText(orderedItem.getOrderNotes());

        String sPrice = Double.toString(orderedItem.getOrderPrice());
        String[] decimalNumbers = sPrice.split("\\.");
        if(Integer.parseInt(decimalNumbers[1]) < 10){
            tvCartItemPrice.setText(String.valueOf(orderedItem.getOrderPrice() + "0"+ "$"));
        }
        else {
            tvCartItemPrice.setText(String.valueOf(orderedItem.getOrderPrice() + "$"));
        }

        tvCartItemQuantity.setText(String.valueOf(orderedItem.getOrderQuantity() + " x "));
        if (orderedItem.getOrderQuantity() == 1){
            tvCartItemPriceTimesQuantity.setVisibility(View.GONE);
        }
        else {
            DecimalFormat f = new DecimalFormat("##.00");
            double roundedOrderedItemTotal = Double.parseDouble(f.format(orderedItem.getTotal()));
            String sPrice2 = Double.toString(roundedOrderedItemTotal);
            String[] decimalNumbers2 = sPrice2.split("\\.");
            if(Integer.parseInt(decimalNumbers2[1]) < 10){
                tvCartItemPriceTimesQuantity.setText(String.valueOf(" = " + roundedOrderedItemTotal + "0" + "$"));
            }
            else {
                tvCartItemPriceTimesQuantity.setText(String.valueOf(" = " + roundedOrderedItemTotal + "$"));
            }
        }
    }
}
