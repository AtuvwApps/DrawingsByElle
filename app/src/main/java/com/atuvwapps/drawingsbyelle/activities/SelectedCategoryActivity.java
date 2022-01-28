package com.atuvwapps.drawingsbyelle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.adapters.DrawingAdapter;
import com.atuvwapps.drawingsbyelle.adapters.MainAdapter;
import com.atuvwapps.drawingsbyelle.databinding.ActivitySelectedCategoryBinding;
import com.atuvwapps.drawingsbyelle.model.Drawing;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SelectedCategoryActivity extends AppCompatActivity implements MainAdapter.ItemClickListener {
    private ActivitySelectedCategoryBinding binding;
    private int count;
    private Drawing selectedCategory;
    private ArrayList<Drawing> drawings;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectedCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.selectedCategoryToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        drawings = new ArrayList<Drawing>();
        getDrawings();
        binding.selectedCategoryCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedCategoryActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        shoppingCartItemCount();
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

    public static int calculateNoOfColumns(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, SelectedDrawingActivity.class);
        intent.putExtra("Drawing", drawings.get(position));
        startActivity(intent);
    }

    private void shoppingCartItemCount(){
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        count = sharedPreferences.getInt("count", 0);
        if(count > 0){
            binding.selectedCategoryCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.carnation_pink)));
        } else{
            binding.selectedCategoryCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.jet)));
        }
    }

    private void getDrawings(){
        Intent intent = getIntent();
        //If no intent, get selectedCategory from sharedPrefs, this prevents null pointer on back nav.
        if(intent.hasExtra("Category")) {
            selectedCategory = intent.getParcelableExtra("Category");
            setSavedSelection();
        } else{
            getSavedSelection();
        }
        binding.selectedCategoryTitle.setText(selectedCategory.getName());
        if (selectedCategory.getName().equals("Line Art")){
            fetchDrawings("lineart");
        } else if (selectedCategory.getName().equals("Black & White")){
            fetchDrawings("blackandwhite");
        } else if (selectedCategory.getName().equals("Kids")){
            fetchDrawings("kids");
        } else if(selectedCategory.getName().equals("Colour")){
            fetchDrawings("colours");
        } else if(selectedCategory.getName().equals("Quotes")){
            fetchDrawings("quotes");
        }
    }

    private void fetchDrawings(String category){
        db.collection(category)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot:queryDocumentSnapshots) {
                            drawings.add(snapshot.toObject(Drawing.class));
                        }
                        startRecycler();
                    }
                });
    }

    private void startRecycler(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        binding.selectedCategoryRecycler.setLayoutManager(layoutManager);
        binding.selectedCategoryRecycler.setAdapter(new DrawingAdapter(drawings, this, this::onClick));
    }

    private void setSavedSelection(){
        SharedPreferences sharedPreferences = getSharedPreferences("chosen_category", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedCategory);
        editor.putString("Selection", json);
        editor.apply();
    }

    private void getSavedSelection(){
        SharedPreferences sharedPreferences = getSharedPreferences("chosen_category", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Selection", null);
        Type type = new TypeToken<Drawing>() {}.getType();
        selectedCategory = gson.fromJson(json, type);
    }
}