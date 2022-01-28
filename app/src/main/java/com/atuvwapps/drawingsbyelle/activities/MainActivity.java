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
import com.atuvwapps.drawingsbyelle.adapters.MainAdapter;
import com.atuvwapps.drawingsbyelle.databinding.ActivityMainBinding;
import com.atuvwapps.drawingsbyelle.model.Drawing;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.ItemClickListener {

    private ActivityMainBinding binding;
    private int count;
    private ArrayList<Drawing> categories;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.mainToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        binding.mainCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        shoppingCartItemCount();
        categories = new ArrayList<Drawing>();
        checkOnline();
        binding.mainErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOnline();
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

    public static int calculateNoOfColumns(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, SelectedCategoryActivity.class);
        intent.putExtra("Category", categories.get(position));
        startActivity(intent);
    }

    public void shoppingCartItemCount(){
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        count = sharedPreferences.getInt("count", 0);
        if(count > 0){
            binding.mainCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.carnation_pink)));
        } else{
            binding.mainCart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.jet)));
        }
    }

    public void getCategories(){
        db.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot:queryDocumentSnapshots) {
                            categories.add(snapshot.toObject(Drawing.class));
                        }
                        startRecycler();
                    }
                });
    }

    private void startRecycler(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        binding.mainRecycler.setLayoutManager(layoutManager);
        binding.mainRecycler.setAdapter(new MainAdapter(categories, this, this));
    }

    private void checkOnline(){
        if(isOnline()) {
            binding.mainRecycler.setVisibility(View.VISIBLE);
            binding.mainError.setVisibility(View.INVISIBLE);
            binding.mainErrorButton.setVisibility(View.INVISIBLE);
            getCategories();
        } else {
            binding.mainRecycler.setVisibility(View.INVISIBLE);
            binding.mainError.setVisibility(View.VISIBLE);
            binding.mainErrorButton.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}