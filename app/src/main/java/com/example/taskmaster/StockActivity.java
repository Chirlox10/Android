package com.example.taskmaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.stock_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvProducts = findViewById(R.id.rv_products);
        MaterialButton btnUpdateStock = findViewById(R.id.btn_update_stock);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        btnUpdateStock.setOnClickListener(v -> {
            // Show all registered products from repository
            List<Product> registeredProducts = ProductRepository.getInstance().getProducts();
            productList.clear();
            productList.addAll(registeredProducts);
            adapter.notifyDataSetChanged();
            
            if (registeredProducts.isEmpty()) {
                Toast.makeText(StockActivity.this, "No hay productos registrados", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StockActivity.this, "Stock actualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        private final List<Product> list;

        public ProductAdapter(List<Product> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Product product = list.get(position);
            holder.textView.setText(product.toString());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView textView;

            public ViewHolder(View view) {
                super(view);
                this.textView = view.findViewById(android.R.id.text1);
            }
        }
    }
}