package com.example.bodega.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bodega.databinding.FragmentUsersBinding;

public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;
    private UserViewModel viewModel;
    private UserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        adapter = new UserAdapter();
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvUsers.setAdapter(adapter);

        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            if (users == null || users.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                adapter.setUserList(null);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.setUserList(users);
            }
        });

        viewModel.getOperationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        binding.fabAddUser.setOnClickListener(v -> {
            new AddUserDialogFragment().show(getChildFragmentManager(), "AddUserDialog");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
