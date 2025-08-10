package com.example.movieapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.data.model.Category;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateSelection(int selectedPosition) {
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).setSelected(i == selectedPosition);
        }
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }

        public void bind(Category category) {
            tvCategoryName.setText(category.getName());
            
            // Styling based on selection
            if (category.isSelected()) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.primary));
                tvCategoryName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.on_primary));
                cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.primary));
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.surface));
                tvCategoryName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_primary));
                cardView.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.outline));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
} 