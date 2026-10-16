package com.example.bodega.ui.proveedor;

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

import com.example.bodega.data.entity.ProveedorEntity;
import com.example.bodega.databinding.FragmentProveedoresBinding;

public class ProveedoresFragment extends Fragment implements ProveedorAdapter.OnProveedorActionListener {

    private FragmentProveedoresBinding binding;
    private ProveedorViewModel viewModel;
    private ProveedorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProveedoresBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ProveedorViewModel.class);

        adapter = new ProveedorAdapter(this);
        binding.rvProveedores.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProveedores.setAdapter(adapter);

        viewModel.getActiveProveedores().observe(getViewLifecycleOwner(), proveedores -> {
            if (proveedores == null || proveedores.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                adapter.setProveedorList(null);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setProveedorList(proveedores);
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

        binding.fabAddProveedor.setOnClickListener(v -> {
            AddProveedorDialogFragment.newInstance(null)
                .show(getChildFragmentManager(), "AddProveedorDialog");
        });
    }

    @Override
    public void onEdit(ProveedorEntity item) {
        AddProveedorDialogFragment.newInstance(item)
            .show(getChildFragmentManager(), "EditProveedorDialog");
    }

    @Override
    public void onDisable(ProveedorEntity item) {
        if (item == null) return;

        new AlertDialog.Builder(requireContext())
            .setTitle("Deshabilitar Proveedor")
            .setMessage("¿Está seguro que desea deshabilitar el proveedor '" + item.getRazonSocial() + "'?")
            .setPositiveButton("Deshabilitar", (dialog, which) -> {
                viewModel.disableProveedor(item.getIdProveedor());
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
