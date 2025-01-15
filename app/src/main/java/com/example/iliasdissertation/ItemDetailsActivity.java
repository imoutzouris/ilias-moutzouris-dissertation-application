package com.example.iliasdissertation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iliasdissertation.ItemsRecycler.ItemsRecyclerAdapter;
import com.example.iliasdissertation.ItemsRecycler.ItemsSelectListener;
import com.example.iliasdissertation.Models.ItemModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity implements ItemsSelectListener {
    TextView categoryName;
    RecyclerView recyclerView;
    private BottomNavigationView footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        //Header Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //Bottom Navigation Functionality
        footer = findViewById(R.id.bottom);
        footer.setSelectedItemId(R.id.hiddenItem);
        footer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu){
                    Intent home = new Intent(ItemDetailsActivity.this, Categories.class);
                    startActivity(home);
                } else if (item.getItemId() == R.id.cart) {
                    Intent cart = new Intent(ItemDetailsActivity.this, CartActivity.class);
                    startActivity(cart);
                }
                return false;
            }
        });
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        categoryName = findViewById(R.id.tvCategoryName);

        //Receive data from Categories activity through intent
        Intent intent = getIntent();
        String name  = intent.getStringExtra("name");
        ArrayList<ItemModel> items = intent.getParcelableArrayListExtra("items");

        categoryName.setText(name);

        //Recycler View for the items of the menu category(i.e salad items, etc)
        recyclerView = findViewById(R.id.itemsRecycler);
        ItemsRecyclerAdapter adapter = new ItemsRecyclerAdapter(items, this);
        recyclerView.setAdapter(adapter);

    }

    //Pass item information to ItemChoiceActivity and go to that screen
    @Override
    public void onItemClicked(ItemModel item) {
        Intent intent = new Intent(this, ItemChoiceActivity.class);
        intent.putExtra("itemName", item.getName());
        intent.putExtra("itemDescription", item.getDescription());
        intent.putExtra("itemPrice", item.getPrice());
        intent.putExtra("itemImage", item.getImage());
        startActivity(intent);
    }
}