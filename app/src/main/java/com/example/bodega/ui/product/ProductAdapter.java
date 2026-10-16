package com.example.bodega.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodega.data.model.ProductoWithCategoria;
import com.example.bodega.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductoWithCategoria> productList = new ArrayList<>();
    private OnProductActionListener listener;

    public interface OnProductActionListener {
        void onEdit(ProductoWithCategoria item);
        void onDisable(ProductoWithCategoria item);
    }

    public ProductAdapter(OnProductActionListener listener) {
        this.listener = listener;
    }

    public void setProductList(List<ProductoWithCategoria> products) {
        this.productList = products != null ? products : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductoWithCategoria item) {
            if (item == null || item.producto == null) return;

            binding.tvSku.setText(item.producto.getSkuCodigo());
            binding.tvNombre.setText(item.producto.getNombre());
            binding.tvDescripcion.setText(item.producto.getDescripcion());

            String categoryName = item.categoria != null ? item.categoria.getNombreCategoria() : "Sin Categoría";
            binding.tvCategoryBadge.setText(categoryName);

            int stock = item.producto.getStockActual();
            int minStock = item.producto.getStockMinimo();

            binding.tvStockActual.setText("Stock: " + stock);
            binding.tvStockMinimo.setText("Mín: " + minStock);

            if (stock <= minStock) {
                binding.tvLowStockWarning.setVisibility(View.VISIBLE);
                binding.tvStockActual.setTextColor(0xFFD32F2F);
            } else {
                binding.tvLowStockWarning.setVisibility(View.GONE);
                binding.tvStockActual.setTextColor(0xFF2E7D32);
            }

            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(item);
            });

            binding.btnDisable.setOnClickListener(v -> {
                if (listener != null) listener.onDisable(item);
            });
        }
    }
}
