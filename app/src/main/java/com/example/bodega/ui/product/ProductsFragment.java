package com.example.bodega.ui.product;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bodega.data.model.ProductoWithCategoria;
import com.example.bodega.databinding.FragmentProductsBinding;

public class ProductsFragment extends Fragment implements ProductAdapter.OnProductActionListener {

    private FragmentProductsBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        adapter = new ProductAdapter(this);
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProducts.setAdapter(adapter);

        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products == null || products.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                adapter.setProductList(null);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setProductList(products);
            }
        });

        viewModel.getOperationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Operación realizada con éxito", Toast.LENGTH_SHORT).show();
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.fabAddProduct.setOnClickListener(v -> {
            AddEditProductDialogFragment.newInstance(null)
                .show(getChildFragmentManager(), "AddProductDialog");
        });
    }

    @Override
    public void onEdit(ProductoWithCategoria item) {
        AddEditProductDialogFragment.newInstance(item)
            .show(getChildFragmentManager(), "EditProductDialog");
    }

    @Override
    public void onDisable(ProductoWithCategoria item) {
        if (item == null || item.producto == null) return;

        new AlertDialog.Builder(requireContext())
            .setTitle("Deshabilitar Producto")
            .setMessage("¿Está seguro que desea deshabilitar el producto '" + item.producto.getNombre() + "'?")
            .setPositiveButton("Deshabilitar", (dialog, which) -> {
                viewModel.disableProduct(item.producto.getIdProducto());
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
