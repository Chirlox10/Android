package com.example.bodega.ui.category;

import android.os.Bundle;
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

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.databinding.FragmentCategoriesBinding;

public class CategoriesFragment extends Fragment implements CategoryAdapter.OnCategoryActionListener {

    private FragmentCategoriesBinding binding;
    private CategoryViewModel viewModel;
    private CategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        adapter = new CategoryAdapter(this);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategories.setAdapter(adapter);

        viewModel.getActiveCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null || categories.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                adapter.setCategoryList(null);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setCategoryList(categories);
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

        binding.fabAddCategory.setOnClickListener(v -> {
            AddCategoryDialogFragment.newInstance(null)
                .show(getChildFragmentManager(), "AddCategoryDialog");
        });
    }

    @Override
    public void onEdit(CategoriaEntity item) {
        AddCategoryDialogFragment.newInstance(item)
            .show(getChildFragmentManager(), "EditCategoryDialog");
    }

    @Override
    public void onDisable(CategoriaEntity item) {
        if (item == null) return;

        new AlertDialog.Builder(requireContext())
            .setTitle("Deshabilitar Categoría")
            .setMessage("¿Está seguro que desea deshabilitar la categoría '" + item.getNombreCategoria() + "'?")
            .setPositiveButton("Deshabilitar", (dialog, which) -> {
                viewModel.disableCategory(item.getIdCategoria());
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
