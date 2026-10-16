package com.example.bodega.ui.user;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bodega.data.entity.RolEntity;
import com.example.bodega.databinding.DialogAddUserBinding;

import java.util.ArrayList;
import java.util.List;

public class AddUserDialogFragment extends DialogFragment {

    private DialogAddUserBinding binding;
    private UserViewModel viewModel;
    private List<RolEntity> roleList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddUserBinding.inflate(LayoutInflater.from(getContext()));
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        viewModel.getRoles().observe(this, roles -> {
            roleList = roles != null ? roles : new ArrayList<>();
            List<String> roleNames = new ArrayList<>();
            for (RolEntity role : roleList) {
                roleNames.add(role.getNombreRol());
            }

            if (getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, roleNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerRoles.setAdapter(adapter);
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
            .setView(binding.getRoot())
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
            .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
                String fullName = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
                String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

                if (TextUtils.isEmpty(username)) {
                    binding.tilUsername.setError("Ingrese el nombre de usuario");
                    return;
                }
                binding.tilUsername.setError(null);

                if (TextUtils.isEmpty(fullName)) {
                    binding.tilFullName.setError("Ingrese el nombre completo");
                    return;
                }
                binding.tilFullName.setError(null);

                if (TextUtils.isEmpty(password)) {
                    binding.tilPassword.setError("Ingrese la contraseña");
                    return;
                }
                binding.tilPassword.setError(null);

                if (roleList.isEmpty()) {
                    Toast.makeText(getContext(), "No hay roles disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedPos = binding.spinnerRoles.getSelectedItemPosition();
                if (selectedPos < 0 || selectedPos >= roleList.size()) {
                    Toast.makeText(getContext(), "Seleccione un rol válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                int idRol = roleList.get(selectedPos).getIdRol();
                viewModel.registerUser(username, fullName, password, idRol);
                dialog.dismiss();
            });
        });

        return dialog;
    }
}
