package com.example.iliasdissertation;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.iliasdissertation.SQLiteDatabase.DatabaseInstance;
import com.example.iliasdissertation.SQLiteDatabase.OrderEntity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer;

import java.util.List;

public class ItemChoiceActivity extends AppCompatActivity {

    String cName;
    String cDescription;
    String cNotes;
    double cPrice;
    String cImage;
    int cQuantity;
    TextView choiceName;
    TextView choiceDescription;
    TextView choicePrice;
    ImageView choiceImage;
    HorizontalQuantitizer hQ;
    EditText edit;
    double totalChoicePrice;
    MaterialButton materialButton;
    DatabaseInstance instance;
    private BottomNavigationView footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_choice);

        choiceName = findViewById(R.id.tvChoiceName);
        choiceDescription = findViewById(R.id.tvChoiceDescription);
        choicePrice = findViewById(R.id.tvChoicePrice);
        choiceImage = findViewById(R.id.ivChoiceImage);
        edit = findViewById(R.id.editText);

        hQ = findViewById(R.id.horizontalQuantitizer);

        Intent intent = getIntent();
        cName  = intent.getStringExtra("itemName");
        cDescription  = intent.getStringExtra("itemDescription");
        cPrice  = intent.getDoubleExtra("itemPrice", 0);
        cImage = intent.getStringExtra("itemImage");

        choiceName.setText(cName);
        choiceDescription.setText(cDescription);

        String sPrice = Double.toString(cPrice);
        String[] decimalNumbers = sPrice.split("\\.");
        if(Integer.parseInt(decimalNumbers[1]) < 10){
            choicePrice.setText(String.valueOf(cPrice) + "0" + "$");
        }
        else {
            choicePrice.setText(String.valueOf(cPrice) + "$");
        }

        Glide.with(this).load(cImage).into(choiceImage);

        //Get Additional item info for editing the cart item
        cNotes = intent.getStringExtra("itemNotes");
        cQuantity = intent.getIntExtra("itemQuantity", 0);
        double oldTotal = cPrice*cQuantity;

        //Set pre - existed notes and quantity from cart item
        edit.setText(cNotes);
        hQ.setValue(cQuantity);

        //colors of quantitizer
        hQ.setMinusIconColor("#D53C45");
        hQ.setPlusIconColor("#A2CAA6");
        hQ.setPlusIconBackgroundColor("#FDFDFD");
        hQ.setMinusIconBackgroundColor("#FDFDFD");

        //Get flag to signal update of Room database
        int flag = intent.getIntExtra("flag", 0);


        //Room Database
        //Insert or Update Data to Room Database when appropriate Button is clicked
        materialButton = findViewById(R.id.materialButton);

        instance = DatabaseInstance.getDatabase(getApplicationContext());

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItemsToCart(flag, oldTotal);
            }
        });

        //Change Order Button Text
        if(flag == 1){
            materialButton.setText("Update Order");
        }

        //Header Setup
        Toolbar toolbar = findViewById(R.id.itemChoiceToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //Make Toolbar Transparent
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setContentScrimColor(Color.TRANSPARENT);

        //Bottom Navigation Functionality
        footer = findViewById(R.id.bottom);
        footer.setSelectedItemId(R.id.hiddenItem);
        footer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu){
                    Intent home = new Intent(ItemChoiceActivity.this, Categories.class);
                    startActivity(home);
                } else if (item.getItemId() == R.id.cart) {
                    Intent cart = new Intent(ItemChoiceActivity.this, CartActivity.class);
                    startActivity(cart);
                }
                return false;
            }
        });

    }

    //Resetting the notes and quantity whenever returning to this screen
    @Override
    protected void onResume() {
        Intent intent = getIntent();
        //Get flag to signal update of Room database
        int flag = intent.getIntExtra("flag", 0);
        super.onResume();
        if(flag == 0){
            hQ.setValue(0);
            edit.getText().clear();
        }
    }

    //Header Arrow functionality, when clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    //Method for checking whether an item exists into the cart or not
    public boolean itemExists(List<OrderEntity> cartItems, OrderEntity orderedItem) {
        boolean value = false;
        for (OrderEntity item : cartItems ){
            if(item.getOrderName().equals(orderedItem.getOrderName()) && item.getDescription().equals(orderedItem.getDescription())
                    && item.getOrderNotes().equals(orderedItem.getOrderNotes())
                    && item.getOrderPrice() == orderedItem.getOrderPrice() && item.getImageUrl().equals(orderedItem.getImageUrl())){
                value = true;
            }
        }
        return  value;
    }

    //Method for getting the quantity of an item that already exists into the cart
    public int getExistingItemQuantity(List<OrderEntity> cartItems, OrderEntity orderedItem){
        OrderEntity existingItem = null;
        for (OrderEntity item : cartItems ){
            if(item.getOrderName().equals(orderedItem.getOrderName()) && item.getDescription().equals(orderedItem.getDescription())
                    && item.getOrderNotes().equals(orderedItem.getOrderNotes())
                    && item.getOrderPrice() == orderedItem.getOrderPrice() && item.getImageUrl().equals(orderedItem.getImageUrl())){
                existingItem = item;
            }
        }
        return existingItem.getOrderQuantity();
    }

    //Method for updating Room Database (Cart)
    public void updateCart(List<OrderEntity> cartItems, OrderEntity orderedItem, int newQuantity, double newTotal){
        for (OrderEntity item : cartItems ){
            if(item.getOrderName().equals(orderedItem.getOrderName()) && item.getDescription().equals(orderedItem.getDescription())
                    && item.getOrderNotes().equals(orderedItem.getOrderNotes())
                    && item.getOrderPrice() == orderedItem.getOrderPrice() && item.getImageUrl().equals(orderedItem.getImageUrl())){
                instance.orderDao().update(item.getOrderName(), item.getDescription(), item.getOrderNotes(), item.getOrderPrice(), item.getOrderQuantity(), item.getTotal(), newQuantity, item.getOrderNotes(), newTotal );
            }
        }
    }

    //Functionality for adding items in the cart or editing them. Covers various situations where item already exists in cart
    public void insertItemsToCart(int flag, double oldTotal){
        try{
            String notes = edit.getText().toString();
            int selectedValue = hQ.getValue();
            totalChoicePrice = cPrice*selectedValue;
            OrderEntity order = new OrderEntity(cName, cDescription, notes, cPrice ,selectedValue, totalChoicePrice, cImage);
            List<OrderEntity> cartItems = instance.orderDao().get();
            if(flag == 1 && selectedValue > 0 ){
                instance.orderDao().update(cName, cDescription, cNotes, cPrice, cQuantity, oldTotal, selectedValue, notes, totalChoicePrice );
                Intent intent = new Intent(ItemChoiceActivity.this, CartActivity.class);
                //Clear CartActivity and create a new instance due to bag while getting edited info
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Toast.makeText(ItemChoiceActivity.this, "Item has been updated.", Toast.LENGTH_SHORT).show();
            } else if (flag == 1 && selectedValue <= 0) {
                Toast.makeText(ItemChoiceActivity.this, "Please choose quantity.", Toast.LENGTH_SHORT).show();
            } else if (flag == 0 && selectedValue > 0 && itemExists(cartItems, order) == false) {
                instance.orderDao().insert(order);
                Toast.makeText(ItemChoiceActivity.this, "Item added into cart.", Toast.LENGTH_SHORT).show();
                hQ.setValue(0);
                edit.getText().clear();
            } else if (flag == 0 && selectedValue > 0 && itemExists(cartItems, order) == true) {
                getExistingItemQuantity(cartItems, order);
                int newQuantity = getExistingItemQuantity(cartItems, order) + selectedValue;
                double newTotalChoicePrice = newQuantity*cPrice;
                updateCart(cartItems, order, newQuantity, newTotalChoicePrice);
                Toast.makeText(ItemChoiceActivity.this, "Item already exists in cart. Quantity is now updated.", Toast.LENGTH_LONG).show();

            } else if (flag == 0 && selectedValue <= 0){
                Toast.makeText(ItemChoiceActivity.this, "Please choose quantity.", Toast.LENGTH_SHORT).show();
            }
        }catch(SQLiteException e){
            Toast.makeText(ItemChoiceActivity.this, "Item already into cart. Please update the existing one.", Toast.LENGTH_LONG).show();
        }
    }
}
