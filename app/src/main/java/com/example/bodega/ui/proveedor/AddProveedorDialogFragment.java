package com.example.bodega.ui.proveedor;

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

import com.example.bodega.data.entity.ProveedorEntity;
import com.example.bodega.databinding.DialogAddProveedorBinding;

public class AddProveedorDialogFragment extends DialogFragment {

    private DialogAddProveedorBinding binding;
    private ProveedorViewModel viewModel;
    private ProveedorEntity proveedorToEdit;

    public static AddProveedorDialogFragment newInstance(@Nullable ProveedorEntity item) {
        AddProveedorDialogFragment fragment = new AddProveedorDialogFragment();
        fragment.proveedorToEdit = item;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddProveedorBinding.inflate(LayoutInflater.from(getContext()));
        viewModel = new ViewModelProvider(requireActivity()).get(ProveedorViewModel.class);

        boolean isEdit = proveedorToEdit != null;

        if (isEdit) {
            binding.etRutEmpresa.setText(proveedorToEdit.getRutEmpresa());
            binding.etRazonSocial.setText(proveedorToEdit.getRazonSocial());
            binding.etContacto.setText(proveedorToEdit.getContacto());
            binding.etTelefono.setText(proveedorToEdit.getTelefono());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
            .setView(binding.getRoot())
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
            .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String rut = binding.etRutEmpresa.getText() != null ? binding.etRutEmpresa.getText().toString().trim() : "";
                String razonSocial = binding.etRazonSocial.getText() != null ? binding.etRazonSocial.getText().toString().trim() : "";
                String contacto = binding.etContacto.getText() != null ? binding.etContacto.getText().toString().trim() : "";
                String telefono = binding.etTelefono.getText() != null ? binding.etTelefono.getText().toString().trim() : "";

                if (TextUtils.isEmpty(rut)) {
                    binding.tilRutEmpresa.setError("Ingrese el RUT de la empresa");
                    return;
                }
                binding.tilRutEmpresa.setError(null);

                if (TextUtils.isEmpty(razonSocial)) {
                    binding.tilRazonSocial.setError("Ingrese la razón social");
                    return;
                }
                binding.tilRazonSocial.setError(null);

                if (isEdit) {
                    proveedorToEdit.setRutEmpresa(rut);
                    proveedorToEdit.setRazonSocial(razonSocial);
                    proveedorToEdit.setContacto(contacto);
                    proveedorToEdit.setTelefono(telefono);

                    viewModel.updateProveedor(proveedorToEdit, new ProveedorViewModel.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Proveedor actualizado exitosamente", Toast.LENGTH_SHORT).show();
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
                    viewModel.registerProveedor(rut, razonSocial, contacto, telefono);
                    dialog.dismiss();
                }
            });
        });

        return dialog;
    }
}
