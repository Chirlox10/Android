package com.example.bodega.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.bodega.data.dao.RolDao;
import com.example.bodega.data.dao.UsuarioDao;
import com.example.bodega.data.database.AppDatabase;
import com.example.bodega.data.entity.RolEntity;
import com.example.bodega.data.entity.UsuarioEntity;
import com.example.bodega.data.model.UsuarioWithRol;
import com.example.bodega.util.PasswordUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class UsuarioRepository {

    private final UsuarioDao usuarioDao;
    private final RolDao rolDao;
    private final ExecutorService executor;

    public interface LoginCallback {
        void onSuccess(UsuarioWithRol usuarioWithRol);
        void onError(String message);
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public UsuarioRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.usuarioDao = db.usuarioDao();
        this.rolDao = db.rolDao();
        this.executor = AppDatabase.getDatabaseWriteExecutor();
    }

    public void login(String username, String password, LoginCallback callback) {
        executor.execute(() -> {
            UsuarioWithRol userWithRol = usuarioDao.getUsuarioWithRolByUsername(username);
            if (userWithRol == null || userWithRol.usuario == null) {
                callback.onError("Credenciales inválidas");
                return;
            }

            if (userWithRol.usuario.getActivo() != 1) {
                callback.onError("Usuario deshabilitado. Contacte al administrador");
                return;
            }

            boolean isPasswordCorrect = PasswordUtils.verifyPassword(password, userWithRol.usuario.getPasswordHash());
            if (isPasswordCorrect) {
                callback.onSuccess(userWithRol);
            } else {
                callback.onError("Credenciales inválidas");
            }
        });
    }

    public LiveData<List<UsuarioWithRol>> getAllUsuarios() {
        return usuarioDao.getAllUsuariosWithRol();
    }

    public LiveData<List<RolEntity>> getAllRoles() {
        return rolDao.getAllRoles();
    }

    public void registerUser(String username, String fullName, String password, int idRol, OperationCallback callback) {
        executor.execute(() -> {
            int count = usuarioDao.countByUsername(username);
            if (count > 0) {
                callback.onError("El nombre de usuario ya se encuentra registrado");
                return;
            }

            String passwordHash = PasswordUtils.hashPassword(password);
            UsuarioEntity newUser = new UsuarioEntity(username, fullName, passwordHash, idRol, 1);
            long id = usuarioDao.insert(newUser);
            if (id > 0) {
                callback.onSuccess();
            } else {
                callback.onError("Error al registrar el usuario");
            }
        });
    }
}
