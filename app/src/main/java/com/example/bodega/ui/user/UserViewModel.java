package com.example.bodega.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bodega.data.entity.RolEntity;
import com.example.bodega.data.model.UsuarioWithRol;
import com.example.bodega.data.repository.UsuarioRepository;
import com.example.bodega.util.SingleLiveEvent;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UsuarioRepository repository;
    private final LiveData<List<UsuarioWithRol>> users;
    private final LiveData<List<RolEntity>> roles;

    private final SingleLiveEvent<String> operationError = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> operationSuccess = new SingleLiveEvent<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UsuarioRepository(application);
        users = repository.getAllUsuarios();
        roles = repository.getAllRoles();
    }

    public LiveData<List<UsuarioWithRol>> getUsers() {
        return users;
    }

    public LiveData<List<RolEntity>> getRoles() {
        return roles;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public void registerUser(String username, String fullName, String password, int idRol) {
        if (username == null || username.trim().isEmpty()) {
            operationError.postValue("El nombre de usuario es obligatorio");
            return;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            operationError.postValue("El nombre completo es obligatorio");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            operationError.postValue("La contraseña es obligatoria");
            return;
        }
        if (idRol <= 0) {
            operationError.postValue("Seleccione un rol válido");
            return;
        }

        repository.registerUser(username.trim(), fullName.trim(), password.trim(), idRol, new UsuarioRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                operationSuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                operationError.postValue(message);
            }
        });
    }
}
