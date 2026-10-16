package com.example.bodega.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bodega.data.model.UsuarioWithRol;
import com.example.bodega.data.repository.UsuarioRepository;
import com.example.bodega.util.SingleLiveEvent;

public class AuthViewModel extends AndroidViewModel {

    private final UsuarioRepository repository;
    private final SingleLiveEvent<String> errorMessage = new SingleLiveEvent<>();
    private final SingleLiveEvent<UsuarioWithRol> loginSuccess = new SingleLiveEvent<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new UsuarioRepository(application);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<UsuarioWithRol> getLoginSuccess() {
        return loginSuccess;
    }

    public void login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.postValue("Ingrese su nombre de usuario");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.postValue("Ingrese su contraseña");
            return;
        }

        repository.login(username.trim(), password.trim(), new UsuarioRepository.LoginCallback() {
            @Override
            public void onSuccess(UsuarioWithRol usuarioWithRol) {
                loginSuccess.postValue(usuarioWithRol);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
            }
        });
    }
}
