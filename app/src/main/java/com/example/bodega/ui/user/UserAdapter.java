package com.example.bodega.ui.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodega.data.model.UsuarioWithRol;
import com.example.bodega.databinding.ItemUserBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UsuarioWithRol> userList = new ArrayList<>();

    public void setUserList(List<UsuarioWithRol> users) {
        this.userList = users != null ? users : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UsuarioWithRol item) {
            if (item == null || item.usuario == null) return;
            binding.tvNombreCompleto.setText(item.usuario.getNombreCompleto());
            binding.tvUsername.setText("@" + item.usuario.getUsername());

            String roleName = item.rol != null ? item.rol.getNombreRol() : "Sin Rol";
            binding.tvRoleBadge.setText(roleName);
        }
    }
}
