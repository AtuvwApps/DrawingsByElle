package com.atuvwapps.drawingsbyelle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.atuvwapps.drawingsbyelle.R;
import com.atuvwapps.drawingsbyelle.databinding.MainListItemBinding;
import com.atuvwapps.drawingsbyelle.model.Drawing;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{
    private List<Drawing> drawings;
    private Context context;
    private ItemClickListener itemClickListener;
    private String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";

    public static class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MainListItemBinding binding;
        ItemClickListener itemClickListener;

        public MainViewHolder(MainListItemBinding b, ItemClickListener itemClickListener) {
            super(b.getRoot());
            binding = b;
            this.itemClickListener = itemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            itemClickListener.onClick(getBindingAdapterPosition());
        }
    }

    public MainAdapter(List<Drawing> drawings, Context context, ItemClickListener itemClickListener){
        this.drawings = drawings;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(MainListItemBinding.inflate(LayoutInflater.from(parent.getContext())), itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Picasso.get().load(drawings.get(position).getImageSource()).placeholder(R.drawable.placeholder).into(holder.binding.mainListItemImage);
        holder.binding.mainListItemText.setText(drawings.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (drawings != null) {
            return drawings.size();
        } else {
            return 0;
        }
    }

    public interface ItemClickListener {
        void onClick(int position);
    }
}
