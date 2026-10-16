package com.example.bodega.ui.product;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.data.entity.ProductoEntity;
import com.example.bodega.data.model.ProductoWithCategoria;
import com.example.bodega.databinding.DialogAddProductBinding;

import java.util.ArrayList;
import java.util.List;

public class AddEditProductDialogFragment extends DialogFragment {

    private DialogAddProductBinding binding;
    private ProductViewModel viewModel;
    private List<CategoriaEntity> categoryList = new ArrayList<>();
    private ProductoWithCategoria productToEdit;

    public static AddEditProductDialogFragment newInstance(@Nullable ProductoWithCategoria item) {
        AddEditProductDialogFragment fragment = new AddEditProductDialogFragment();
        fragment.productToEdit = item;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddProductBinding.inflate(LayoutInflater.from(getContext()));
        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        boolean isEdit = productToEdit != null && productToEdit.producto != null;
        binding.tvDialogTitle.setText(isEdit ? "Editar Producto" : "Registrar Producto");

        if (isEdit) {
            binding.etSku.setText(productToEdit.producto.getSkuCodigo());
            binding.etSku.setEnabled(false); // SKU cannot be changed
            binding.etNombre.setText(productToEdit.producto.getNombre());
            binding.etDescripcion.setText(productToEdit.producto.getDescripcion());
            binding.etStockActual.setText(String.valueOf(productToEdit.producto.getStockActual()));
            binding.etStockMinimo.setText(String.valueOf(productToEdit.producto.getStockMinimo()));
        }

        viewModel.getActiveCategories().observe(this, categories -> {
            categoryList = categories != null ? categories : new ArrayList<>();
            List<String> categoryNames = new ArrayList<>();
            int selectedIndex = 0;

            for (int i = 0; i < categoryList.size(); i++) {
                CategoriaEntity cat = categoryList.get(i);
                categoryNames.add(cat.getNombreCategoria());
                if (isEdit && cat.getIdCategoria() == productToEdit.producto.getIdCategoria()) {
                    selectedIndex = i;
                }
            }

            if (getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, categoryNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerCategory.setAdapter(adapter);
                if (categoryNames.size() > selectedIndex) {
                    binding.spinnerCategory.setSelection(selectedIndex);
                }
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
            .setView(binding.getRoot())
            .setPositiveButton("Guardar", null) // Overridden below to prevent auto-dismiss on error
            .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
            .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> saveProduct(isEdit, dialog));
        });

        return dialog;
    }

    private void saveProduct(boolean isEdit, AlertDialog dialog) {
        String sku = binding.etSku.getText() != null ? binding.etSku.getText().toString().trim() : "";
        String nombre = binding.etNombre.getText() != null ? binding.etNombre.getText().toString().trim() : "";
        String descripcion = binding.etDescripcion.getText() != null ? binding.etDescripcion.getText().toString().trim() : "";
        String stockActualStr = binding.etStockActual.getText() != null ? binding.etStockActual.getText().toString().trim() : "0";
        String stockMinimoStr = binding.etStockMinimo.getText() != null ? binding.etStockMinimo.getText().toString().trim() : "0";

        if (!isEdit && TextUtils.isEmpty(sku)) {
            binding.tilSku.setError("Ingrese el código SKU");
            return;
        } else {
            binding.tilSku.setError(null);
        }

        if (TextUtils.isEmpty(nombre)) {
            binding.tilNombre.setError("Ingrese el nombre del producto");
            return;
        } else {
            binding.tilNombre.setError(null);
        }

        if (categoryList.isEmpty()) {
            Toast.makeText(getContext(), "Debe registrar al menos una categoría antes", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPos = binding.spinnerCategory.getSelectedItemPosition();
        if (selectedPos < 0 || selectedPos >= categoryList.size()) {
            Toast.makeText(getContext(), "Seleccione una categoría válida", Toast.LENGTH_SHORT).show();
            return;
        }
        int idCategoria = categoryList.get(selectedPos).getIdCategoria();

        int stockActual = 0;
        int stockMinimo = 0;
        try {
            stockActual = Integer.parseInt(stockActualStr);
            stockMinimo = Integer.parseInt(stockMinimoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Los campos de stock deben ser números válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (stockActual < 0) {
            binding.tilStockActual.setError("El stock actual no puede ser negativo");
            return;
        } else {
            binding.tilStockActual.setError(null);
        }

        if (isEdit) {
            ProductoEntity p = productToEdit.producto;
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setStockActual(stockActual);
            p.setStockMinimo(stockMinimo);
            p.setIdCategoria(idCategoria);

            viewModel.updateProduct(p, new ProductViewModel.OperationCallback() {
                @Override
                public void onSuccess() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });
        } else {
            viewModel.registerProduct(sku, nombre, descripcion, stockActual, stockMinimo, idCategoria);
            dialog.dismiss();
        }
    }
}
