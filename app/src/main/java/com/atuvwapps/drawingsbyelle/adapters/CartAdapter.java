package com.atuvwapps.drawingsbyelle.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.databinding.CartListItemBinding;
import com.atuvwapps.drawingsbyelle.model.OrderItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<OrderItem> orderItems;
    private Context context;
    private CartAdapter.ItemClickListener itemClickListener;
    private String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CartListItemBinding binding;
        CartAdapter.ItemClickListener itemClickListener;

        public CartViewHolder(CartListItemBinding b, CartAdapter.ItemClickListener itemClickListener) {
            super(b.getRoot());
            binding = b;
            this.itemClickListener = itemClickListener;
            binding.getRoot().setOnClickListener(this);

            binding.cartItemRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPrice = orderItems.get(getBindingAdapterPosition()).getDrawing().getPrice();
                    //Remove item from the cart
                    orderItems.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                    notifyItemRangeChanged(getBindingAdapterPosition(), orderItems.size());

                    //Save the new List of items for the cart to SharedPreferences
                    SharedPreferences sharedPreferences = CartAdapter.this.context
                            .getSharedPreferences("shopping_cart", CartAdapter.this.context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int cartPrice = sharedPreferences.getInt("price", 0) - itemPrice;
                    Gson gson = new Gson();
                    String json = gson.toJson(orderItems);
                    int count = orderItems.size();
                    editor.putInt("price", cartPrice);
                    editor.putInt("count", count);
                    editor.putString("item", json);
                    editor.apply();
                }
            });

        }

        @Override
        public void onClick(View view){
            itemClickListener.onClick(getAdapterPosition());
        }
    }

    public CartAdapter(List<OrderItem> orderItems, Context context, CartAdapter.ItemClickListener itemClickListener){
        this.orderItems = orderItems;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapter.CartViewHolder(CartListItemBinding.inflate(LayoutInflater.from(parent.getContext())), itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Picasso.get().load(orderItems.get(position).getDrawing().getImageSource())
                .placeholder(R.drawable.placeholder).into(holder.binding.cartItemImage);
        holder.binding.cartItemName.setText(orderItems.get(position).getDrawing().getName());
        holder.binding.cartItemSize.setText(orderItems.get(position).getSize());
        holder.binding.cartItemQuantity.setText("x"+Integer.toString(orderItems.get(position).getQuantity()));
        holder.binding.cartItemPrice.setText("€"+Integer.toString(orderItems.get(position).getDrawing().getPrice()));
    }

    @Override
    public int getItemCount() {
        if (orderItems != null) {
            return orderItems.size();
        } else {
            return 0;
        }
    }

    public interface ItemClickListener {
        void onClick(int position);
    }
}
