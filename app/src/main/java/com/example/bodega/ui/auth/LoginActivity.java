package com.example.bodega.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bodega.MainActivity;
import com.example.bodega.data.model.UsuarioWithRol;
import com.example.bodega.databinding.ActivityLoginBinding;
import com.example.bodega.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getLoginSuccess().observe(this, this::onLoginSuccess);
        viewModel.getErrorMessage().observe(this, this::onLoginError);

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";

            binding.tilUsername.setError(null);
            binding.tilPassword.setError(null);

            viewModel.login(username, password);
        });
    }

    private void onLoginSuccess(UsuarioWithRol userWithRol) {
        if (userWithRol == null || userWithRol.usuario == null) return;

        int userId = userWithRol.usuario.getIdUsuario();
        String username = userWithRol.usuario.getUsername();
        String fullName = userWithRol.usuario.getNombreCompleto();
        int roleId = userWithRol.usuario.getIdRol();
        String roleName = userWithRol.rol != null ? userWithRol.rol.getNombreRol() : "Operador";

        sessionManager.createLoginSession(userId, username, fullName, roleId, roleName);

        Toast.makeText(this, "Bienvenido " + fullName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void onLoginError(String error) {
        if (error == null || error.isEmpty()) return;

        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        binding.etPassword.setText("");
        binding.etUsername.requestFocus();
    }
}
