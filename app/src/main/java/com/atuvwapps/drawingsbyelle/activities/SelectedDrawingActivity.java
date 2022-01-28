package com.atuvwapps.drawingsbyelle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.databinding.ActivitySelectedDrawingBinding;
import com.atuvwapps.drawingsbyelle.model.Drawing;
import com.atuvwapps.drawingsbyelle.model.OrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SelectedDrawingActivity extends AppCompatActivity {

    private ActivitySelectedDrawingBinding binding;
    private int itemPrice;
    private int price = 0;
    private int sizePrice = 15;
    private int quantity = 1;
    private int count;
    private Drawing selectedDrawing;
    private boolean isCartEmpty;
    private int cartPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectedDrawingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.selectedDrawingToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        selectedDrawing = intent.getParcelableExtra("Drawing");
        binding.selectedDrawingTitle.setText(selectedDrawing.getName());
        isCartEmpty = true;
        binding.selectedDrawingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedDrawingActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        shoppingCartItemCount();
        Picasso.get().load(selectedDrawing.getImageSource()).placeholder(R.drawable.placeholder).into(binding.selectedDrawingImage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.size_array, R.layout.selected_drawing_spinner_item);
        binding.selectedDrawingSizeSpinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.quantity_array, R.layout.selected_drawing_spinner_item);
        binding.selectedDrawingQuantitySpinner.setAdapter(adapter2);
        itemPrice = selectedDrawing.getPrice();
        binding.selectedDrawingSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("A1")){
                    sizePrice = 15;
                } else if(parent.getItemAtPosition(position).toString().equals("A2")){
                    sizePrice = 12;
                } else if(parent.getItemAtPosition(position).toString().equals("A3")){
                    sizePrice = 9;
                } else if(parent.getItemAtPosition(position).toString().equals("A4")){
                    sizePrice = 6;
                } else if(parent.getItemAtPosition(position).toString().equals("A5")){
                    sizePrice = 3;
                }
                price = (itemPrice + sizePrice) * quantity;
                binding.selectedDrawingPriceText.setText(Integer.toString(price));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.selectedDrawingQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("1")){
                    quantity = 1;
                }
                else if(parent.getItemAtPosition(position).toString().equals("2")){
                    quantity = 2;
                } else if(parent.getItemAtPosition(position).toString().equals("3")){
                    quantity = 3;
                } else if(parent.getItemAtPosition(position).toString().equals("4")){
                    quantity = 4;
                } else if(parent.getItemAtPosition(position).toString().equals("5")){
                    quantity = 5;
                }

                price = (itemPrice + sizePrice) * quantity;
                binding.selectedDrawingPriceText.setText(Integer.toString(price));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.selectedDrawingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void saveItem(){
        OrderItem item = new OrderItem(new Drawing(selectedDrawing.getName(), selectedDrawing.getImageSource(), price),
                binding.selectedDrawingSizeSpinner.getSelectedItem().toString(),
                binding.selectedDrawingQuantitySpinner.getSelectedItemPosition()+1);

        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<OrderItem> items = new ArrayList<>();
        Gson gson = new Gson();

        //If the cart is not empty then we need to get the cart and add on to it,
        // otherwise we would have replaced what is already there
        if(!isCartEmpty){
            String json = sharedPreferences.getString("item", null);
            Type type = new TypeToken<ArrayList<OrderItem>>() {}.getType();
            items = gson.fromJson(json, type);
        }
        items.add(item);
        String json = gson.toJson(items);
        count++;
        cartPrice = cartPrice + price;
        editor.putInt("count", count);
        editor.putInt("price", cartPrice);
        editor.putString("item", json);
        editor.apply();
        binding.selectedDrawingCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.carnation_pink)));
        Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show();
        binding.selectedDrawingAddButton.setEnabled(false);
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

    public void shoppingCartItemCount(){
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        count = sharedPreferences.getInt("count", 0);
        if(count > 0){
            isCartEmpty = false;
            binding.selectedDrawingCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.carnation_pink)));
        } else{
            binding.selectedDrawingCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.jet)));
        }
        cartPrice = sharedPreferences.getInt("price", 0);
    }
}