package com.example.iliasdissertation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.iliasdissertation.Models.CategoryModel;
import com.example.iliasdissertation.Models.ItemModel;
import com.example.iliasdissertation.RecyclerView.RecyclerAdapter;
import com.example.iliasdissertation.RecyclerView.SelectListener;
import com.example.iliasdissertation.Volley.MySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity implements SelectListener {
    RecyclerView recyclerView;
    List<CategoryModel> menuCategories = new ArrayList<>();
    String id;
    private BottomNavigationView footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);

        //Bottom Navigation Functionality
        footer = findViewById(R.id.bottom);
        footer.setSelectedItemId(R.id.menu);
        footer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu){
                    Intent home = new Intent(Categories.this, Categories.class);
                    startActivity(home);
                    return true;
                } else if (item.getItemId() == R.id.cart) {
                    Intent cart = new Intent(Categories.this, CartActivity.class);
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
        footer.getMenu().findItem(R.id.menu).setChecked(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        recyclerView = findViewById(R.id.categoryRecyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(menuCategories, this);
        recyclerView.setAdapter(adapter);
        fetchMenuCategories();
    }

    public void fetchMenuCategories() {

        //Get the id of the qr from the shared preferences
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        id = preferences.getString("restaurant", null);

        String url = "https://test2-9071d-default-rtdb.europe-west1.firebasedatabase.app/restaurants/" + id + ".json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray restaurantCategoriesArray = response.getJSONArray("categories");

                            for (int i = 0; i < restaurantCategoriesArray.length(); i++) {
                                JSONObject categoryObject = restaurantCategoriesArray.getJSONObject(i);

                                String name = categoryObject.getString("name");
                                String image = categoryObject.getString("image_url");

                                // Creating a list to store items for this category
                                List<ItemModel> items = new ArrayList<>();

                                JSONArray itemsArray = categoryObject.getJSONArray("items");

                                for (int k = 0; k < itemsArray.length(); k++) {
                                    JSONObject itemObject = itemsArray.getJSONObject(k);

                                    String iDescription = itemObject.getString("description");
                                    String iImageUrl = itemObject.getString("image_url");
                                    String iName = itemObject.getString("name");
                                    double iPrice = itemObject.getDouble("price");

                                    // Create an ItemModel instance
                                    ItemModel item = new ItemModel(iDescription, iImageUrl,iName, iPrice);

                                    // Add the created ItemModel instance to the items list
                                    items.add(item);
                                }

                                // Create a CategoryModel instance with the list of items
                                CategoryModel category = new CategoryModel(name, image, items);

                                // Add the created CategoryModel instance to the menuCategories list
                                menuCategories.add(category);
                            }

                            // Update the adapter with the new data
                            RecyclerAdapter adapter = (RecyclerAdapter) recyclerView.getAdapter();
                            adapter.updateData(menuCategories);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onItemClicked(CategoryModel category) {
        // Handle item click
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("name", category.getName());
        intent.putExtra("items", new ArrayList<>(category.getItems()));
        startActivity(intent);
    }
}

