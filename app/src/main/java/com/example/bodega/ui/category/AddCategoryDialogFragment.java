package com.example.bodega.ui.category;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.databinding.DialogAddCategoryBinding;

public class AddCategoryDialogFragment extends DialogFragment {

    private DialogAddCategoryBinding binding;
    private CategoryViewModel viewModel;
    private CategoriaEntity categoryToEdit;

    public static AddCategoryDialogFragment newInstance(@Nullable CategoriaEntity item) {
        AddCategoryDialogFragment fragment = new AddCategoryDialogFragment();
        fragment.categoryToEdit = item;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddCategoryBinding.inflate(LayoutInflater.from(getContext()));
        viewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        boolean isEdit = categoryToEdit != null;

        if (isEdit) {
            binding.etNombreCategoria.setText(categoryToEdit.getNombreCategoria());
            binding.etDescripcionCategoria.setText(categoryToEdit.getDescripcion());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
            .setView(binding.getRoot())
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
            .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String nombre = binding.etNombreCategoria.getText() != null ? binding.etNombreCategoria.getText().toString().trim() : "";
                String descripcion = binding.etDescripcionCategoria.getText() != null ? binding.etDescripcionCategoria.getText().toString().trim() : "";

                if (TextUtils.isEmpty(nombre)) {
                    binding.tilNombreCategoria.setError("Ingrese el nombre de la categoría");
                    return;
                }
                binding.tilNombreCategoria.setError(null);

                if (isEdit) {
                    categoryToEdit.setNombreCategoria(nombre);
                    categoryToEdit.setDescripcion(descripcion);
                    viewModel.updateCategory(categoryToEdit, new CategoryViewModel.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Categoría actualizada exitosamente", Toast.LENGTH_SHORT).show();
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
                    viewModel.registerCategory(nombre, descripcion);
                    dialog.dismiss();
                }
            });
        });

        return dialog;
    }
}
