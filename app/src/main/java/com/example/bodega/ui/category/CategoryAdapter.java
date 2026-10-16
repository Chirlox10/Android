package com.example.bodega.ui.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoriaEntity> categoryList = new ArrayList<>();
    private OnCategoryActionListener listener;

    public interface OnCategoryActionListener {
        void onEdit(CategoriaEntity item);
        void onDisable(CategoriaEntity item);
    }

    public CategoryAdapter(OnCategoryActionListener listener) {
        this.listener = listener;
    }

    public void setCategoryList(List<CategoriaEntity> categories) {
        this.categoryList = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CategoriaEntity item) {
            if (item == null) return;
            binding.tvNombreCategoria.setText(item.getNombreCategoria());
            binding.tvDescripcionCategoria.setText(item.getDescripcion() != null ? item.getDescripcion() : "");

            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(item);
            });

            binding.btnDisable.setOnClickListener(v -> {
                if (listener != null) listener.onDisable(item);
            });
        }
    }
}
