package com.example.bodega.ui.proveedor;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodega.data.entity.ProveedorEntity;
import com.example.bodega.databinding.ItemProveedorBinding;

import java.util.ArrayList;
import java.util.List;

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder> {

    private List<ProveedorEntity> proveedorList = new ArrayList<>();
    private OnProveedorActionListener listener;

    public interface OnProveedorActionListener {
        void onEdit(ProveedorEntity item);
        void onDisable(ProveedorEntity item);
    }

    public ProveedorAdapter(OnProveedorActionListener listener) {
        this.listener = listener;
    }

    public void setProveedorList(List<ProveedorEntity> proveedores) {
        this.proveedorList = proveedores != null ? proveedores : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProveedorBinding binding = ItemProveedorBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProveedorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        holder.bind(proveedorList.get(position));
    }

    @Override
    public int getItemCount() {
        return proveedorList.size();
    }

    class ProveedorViewHolder extends RecyclerView.ViewHolder {
        private final ItemProveedorBinding binding;

        ProveedorViewHolder(ItemProveedorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProveedorEntity item) {
            if (item == null) return;
            binding.tvRutEmpresa.setText("RUT: " + item.getRutEmpresa());
            binding.tvRazonSocial.setText(item.getRazonSocial());
            binding.tvContacto.setText("Contacto: " + (item.getContacto() != null ? item.getContacto() : "-"));
            binding.tvTelefono.setText("Teléfono: " + (item.getTelefono() != null ? item.getTelefono() : "-"));

            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(item);
            });

            binding.btnDisable.setOnClickListener(v -> {
                if (listener != null) listener.onDisable(item);
            });
        }
    }
}
