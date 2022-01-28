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
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.databinding.ActivityOrderBinding;
import com.atuvwapps.drawingsbyelle.model.OrderDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;
    private String errorMessage;
    private boolean formError;
    private int price;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.orderToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        SharedPreferences sharedPreferences = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        price = sharedPreferences.getInt("price", 0);
        binding.orderTitle.setText(getString(R.string.order) +"(€"+price+")");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.counties_array, R.layout.county_spinner_item);
        binding.orderCountySpinner.setAdapter(adapter);
        binding.orderRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == binding.orderDeliveryRadio.getId()){
                    binding.orderAddress1Label.setVisibility(View.VISIBLE);
                    binding.orderAddress1Text.setVisibility(View.VISIBLE);
                    binding.orderAddress2Label.setVisibility(View.VISIBLE);
                    binding.orderAddress2Text.setVisibility(View.VISIBLE);
                    binding.orderCountyLabel.setVisibility(View.VISIBLE);
                    binding.orderCountySpinner.setVisibility(View.VISIBLE);
                    binding.orderEircodeLabel.setVisibility(View.VISIBLE);
                    binding.orderEircodeText.setVisibility(View.VISIBLE);
                } else if(checkedId == binding.orderCollectionRadio.getId()){
                    binding.orderAddress1Label.setVisibility(View.INVISIBLE);
                    binding.orderAddress1Text.setVisibility(View.INVISIBLE);
                    binding.orderAddress2Label.setVisibility(View.INVISIBLE);
                    binding.orderAddress2Text.setVisibility(View.INVISIBLE);
                    binding.orderCountyLabel.setVisibility(View.INVISIBLE);
                    binding.orderCountySpinner.setVisibility(View.INVISIBLE);
                    binding.orderEircodeLabel.setVisibility(View.INVISIBLE);
                    binding.orderEircodeText.setVisibility(View.INVISIBLE);
                }
            }
        });
        binding.orderCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formError = checkForFormError();
                if(formError){
                    binding.orderFormError.setText(errorMessage);
                    binding.orderFormError.setVisibility(View.VISIBLE);
                }
                else{
                    saveOrderToFirebase();
                    binding.orderFormError.setVisibility(View.INVISIBLE);
                }
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

    private boolean checkForFormError(){
        if(binding.orderNameText.getText().toString().equals("")){
            errorMessage = "Please enter your name";
            return true;
        }

        if(binding.orderNumberText.getText().toString().equals("")){
            errorMessage = "Please enter your number";
            return true;
        }

        if(binding.orderEmailText.getText().toString().equals("")){
            errorMessage = "Please enter your email";
            return true;
        }

        if(binding.orderDeliveryRadio.isChecked() && binding.orderAddress1Text.getText().toString().equals("")){
            errorMessage = "Please enter your Address";
            return true;
        }

        if(binding.orderDeliveryRadio.isChecked() && binding.orderEircodeText.getText().toString().equals("")){
            errorMessage = "Please enter your Eircode";
            return true;
        }

        return false;
    }

    private void saveOrderToFirebase(){
        boolean collection;
        String address;
        String county;
        String eircode;
        if(binding.orderCollectionRadio.isChecked()){
            collection = true;
            address = "";
            county = "";
            eircode = "";
        } else{
            collection = false;
            address = binding.orderAddress1Text.getText().toString()+", "+binding.orderAddress2Text.getText().toString();
            county = binding.orderCountySpinner.getSelectedItem().toString();
            eircode = binding.orderEircodeText.getText().toString();
        }
        OrderDetails order = new OrderDetails(binding.orderNameText.getText().toString(), binding.orderNumberText.getText().toString(),
                binding.orderEmailText.getText().toString(), collection, address, county, eircode);
        db.collection("orders")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(OrderActivity.this, "Order added to database", Toast.LENGTH_SHORT).show();
                        startThanksActivity(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderActivity.this, "Error processing order please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startThanksActivity(String id){
        Intent intent = new Intent(OrderActivity.this, ThankYouActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}