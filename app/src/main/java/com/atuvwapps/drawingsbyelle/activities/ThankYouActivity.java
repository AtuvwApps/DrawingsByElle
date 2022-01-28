package com.atuvwapps.drawingsbyelle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.databinding.ActivityThankYouBinding;

public class ThankYouActivity extends AppCompatActivity {
    private ActivityThankYouBinding binding;
    private String orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThankYouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.thankyouToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        orderNumber = intent.getStringExtra("id");
        binding.thankyouOrderNumberText.setText(orderNumber);
        clearShoppingCart();
        binding.thankyouBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouActivity.this, MainActivity.class);
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

    //After the order is complete we empty the shopping cart
    private void clearShoppingCart(){
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", 0);
        editor.putString("item", "");
        editor.putInt("price", 0);
        editor.apply();
    }
}