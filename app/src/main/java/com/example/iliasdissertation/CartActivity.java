package com.example.iliasdissertation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iliasdissertation.CartRecycler.CartDeleteItemSelectListener;
import com.example.iliasdissertation.CartRecycler.CartEditItemSelectListener;
import com.example.iliasdissertation.CartRecycler.CartRecyclerAdapter;
import com.example.iliasdissertation.Models.CartModel;
import com.example.iliasdissertation.Models.FinalOrderItemModel;
import com.example.iliasdissertation.Models.FinalOrderModel;
import com.example.iliasdissertation.SQLiteDatabase.DatabaseInstance;
import com.example.iliasdissertation.SQLiteDatabase.OrderEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartEditItemSelectListener, CartDeleteItemSelectListener {

    List<OrderEntity> orderedItems = new ArrayList<>();
    List<CartModel>  cartItems = new ArrayList<>();
    List<FinalOrderItemModel> finalOrderItems = new ArrayList<>();
    FinalOrderModel finalOrder;
    RecyclerView recycler;
    MaterialButton confirmOrderButton;
    TextView tvTotalAmount;
    double total;
    private BottomNavigationView footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Header Functionality
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //Bottom Navigation Functionality
        footer = findViewById(R.id.bottom);
        footer.setSelectedItemId(R.id.cart);
        footer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu){
                    Intent home = new Intent(CartActivity.this, Categories.class);
                    startActivity(home);
                    return true;
                } else if (item.getItemId() == R.id.cart) {
                    Intent cart = new Intent(CartActivity.this, CartActivity.class);
                    startActivity(cart);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Make the menu icon highlighted again when returning to activity using up navigation arrow
        footer.getMenu().findItem(R.id.cart).setChecked(true);

        orderedItems.clear();
        fetchOrder();
        tvTotalAmount = findViewById(R.id.tvTotalAmount1);
        tvTotalAmount.setText(String.format("%.2f$",total));
    }

    //Header Arrow functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        recycler = findViewById(R.id.cartRecycler);
        CartRecyclerAdapter adapter = new CartRecyclerAdapter(orderedItems, this::onEditIconClicked, this::onDeleteIconClicked);
        recycler.setAdapter(adapter);
        fetchOrder();

        confirmOrderButton = findViewById(R.id.confirmOrderButton);
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request table Number and send order to the database
                requestTableNumber();
            }
        });
    }

    //Method for sending order to the database
    public void sendFinalOrderToDatabase(String tableNumber){
        DatabaseInstance instance = DatabaseInstance.getDatabase(getApplicationContext());
        List<OrderEntity> array = instance.orderDao().get();
        for(OrderEntity item : array){
            String orderedItemName = item.getOrderName();
            String orderedItemNotes = item.getOrderNotes();
            int orderedItemQuantity = item.getOrderQuantity();
            FinalOrderItemModel finalOrderItem = new FinalOrderItemModel(orderedItemName, orderedItemNotes, orderedItemQuantity);
            finalOrderItems.add((finalOrderItem));
        }
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        finalOrder = new FinalOrderModel(finalOrderItems, tableNumber);
        FirebaseDatabase.getInstance().getReference("Orders").child(String.valueOf(currentDate))
                .setValue(finalOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            instance.orderDao().deleteAll();
                            successfulOrder();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Alert dialog box for requesting table number and sending order to the database
    public void requestTableNumber(){
        //Create dialog for inserting table number
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this, R.style.AlertDialogShape);
        View dialog_layout = LayoutInflater.from(CartActivity.this).inflate(R.layout.table_number, null);

        //Implement Cancel and Okay buttons
        builder.setView(dialog_layout).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText tNumber = dialog_layout.findViewById(R.id.alertDialogText);
                String tableNumber = tNumber.getText().toString();
                if(tableNumber.isEmpty()){
                    Toast.makeText(CartActivity.this, "Order was not sent! Please provide your table number.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(orderedItems.isEmpty()){
                        Toast.makeText(CartActivity.this, "Order was not sent! Cart is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //Send order to the database
                        sendFinalOrderToDatabase(tableNumber);
                        dialog.dismiss();
                    }
                }
            }
        });

        //Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //Center the dialog on the screen
        dialog.getWindow().setGravity(Gravity.CENTER);

        //Change cancel and ok buttons colors
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#A3A3A3"));
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#6dac73"));
    }

    public void successfulOrder(){
        //Create dialog for confirming successful order
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        View dialog_layout = LayoutInflater.from(CartActivity.this).inflate(R.layout.successful_order, null);
        builder.setView(dialog_layout);

        //Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        //Center the dialog on the screen
        dialog.getWindow().setGravity(Gravity.CENTER);

        //Make background transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Implement functionality for the button of alert dialog box
        Button successfulOrderButton = dialog_layout.findViewById(R.id.successfulOrderButton);
        successfulOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, Categories.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    public void fetchOrder() {
        total = 0.0;
        DatabaseInstance instance = DatabaseInstance.getDatabase(getApplicationContext());
        List<OrderEntity> array = instance.orderDao().get();
        for(OrderEntity item : array){
            String orderedItemName = item.getOrderName();
            String orderedItemDescription = item.getDescription();
            String orderedItemNotes = item.getOrderNotes();
            double orderedItemPrice = item.getOrderPrice();
            int orderedItemQuantity = item.getOrderQuantity();
            double orderedItemPriceTimesQuantity = orderedItemPrice*orderedItemQuantity;
            total = total + orderedItemPriceTimesQuantity;
            String orderedItemImage = item.getImageUrl();
            OrderEntity orderedItem = new OrderEntity(orderedItemName, orderedItemDescription, orderedItemNotes, orderedItemPrice, orderedItemQuantity,orderedItemPriceTimesQuantity, orderedItemImage);
            orderedItems.add(orderedItem);
            //Add the same items into cartItem which is parceable to transfer it to other screens
            CartModel cartItem = new CartModel(orderedItemName, orderedItemDescription, orderedItemNotes, orderedItemPrice, orderedItemQuantity,orderedItemPriceTimesQuantity, orderedItemImage);
            cartItems.add(cartItem);
            tvTotalAmount = findViewById(R.id.tvTotalAmount1);
            tvTotalAmount.setText(String.format("%.2f$",total));
        }
        CartRecyclerAdapter adapter =(CartRecyclerAdapter) recycler.getAdapter();
        adapter.updateData(orderedItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditIconClicked(OrderEntity orderedItem) {
        Intent intent = new Intent(CartActivity.this, ItemChoiceActivity.class);
        intent.putExtra("itemName", orderedItem.getOrderName());
        intent.putExtra("itemNotes", orderedItem.getOrderNotes());
        intent.putExtra("itemDescription", orderedItem.getDescription());
        intent.putExtra("itemPrice", orderedItem.getOrderPrice());
        intent.putExtra("itemQuantity", orderedItem.getOrderQuantity());
        intent.putExtra("itemImage", orderedItem.getImageUrl());
        intent.putExtra("flag", 1);
        intent.putExtra("cartItems", new ArrayList<>(cartItems));
        startActivity(intent);
    }

    @Override
    public void onDeleteIconClicked(int position) {
        OrderEntity item = orderedItems.get(position);
        DatabaseInstance instance = DatabaseInstance.getDatabase((getApplicationContext()));

        //Delete item from Room Database
        instance.orderDao().delete(item.getOrderName(), item.getDescription(), item.getOrderNotes(), item.getOrderPrice(), item.getOrderQuantity(), item.getTotal());

        //subtract item price
        double deletedTotal = item.getTotal();
        total = Math.abs(total-deletedTotal);

        //Remove item from the adapter list
        orderedItems.remove(item);

        //Notify adapter for the change
        CartRecyclerAdapter adapter =(CartRecyclerAdapter) recycler.getAdapter();
        adapter.notifyDataSetChanged();
        Toast.makeText(CartActivity.this, "Item has been deleted from cart", Toast.LENGTH_SHORT).show();

        //update total amount
        tvTotalAmount.setText(String.format("Total: %.2f$",total));

    }
}