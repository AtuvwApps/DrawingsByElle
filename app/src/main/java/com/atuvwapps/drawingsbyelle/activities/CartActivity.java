package com.atuvwapps.drawingsbyelle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.adapters.CartAdapter;
import com.atuvwapps.drawingsbyelle.databinding.ActivityCartBinding;
import com.atuvwapps.drawingsbyelle.model.OrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.ItemClickListener {
    private ActivityCartBinding binding;
    private ArrayList<OrderItem> order;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.cartToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        shoppingCartItemCount();
        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadData();
        binding.cartRecycler.setAdapter(new CartAdapter(order, this, this));
        binding.cartCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.size() != 0) {
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, "Your cart is now empty", Toast.LENGTH_SHORT).show();
                    binding.cartCompleteButton.setEnabled(false);
                    binding.cartRecycler.setVisibility(View.INVISIBLE);
                    binding.shoppingCartError.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cartBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Create options menu to display our about button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_me, menu);
        return true;
    }

    //Handle when our about button is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) { }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("item", null);
        Type type = new TypeToken<ArrayList<OrderItem>>() {}.getType();
        order = gson.fromJson(json, type);
        if(order == null){
            order = new ArrayList<OrderItem>();
        }
    }

    public void shoppingCartItemCount(){
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        count = sharedPreferences.getInt("count", 0);
        if(count > 0){
            binding.cartCompleteButton.setEnabled(true);
            binding.shoppingCartError.setVisibility(View.INVISIBLE);
            binding.cartRecycler.setVisibility(View.VISIBLE);
        } else{
            binding.cartCompleteButton.setEnabled(false);
            binding.cartRecycler.setVisibility(View.INVISIBLE);
            binding.shoppingCartError.setVisibility(View.VISIBLE);
        }
    }
}