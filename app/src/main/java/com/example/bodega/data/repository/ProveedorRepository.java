package com.example.bodega.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.bodega.data.dao.ProveedorDao;
import com.example.bodega.data.database.AppDatabase;
import com.example.bodega.data.entity.ProveedorEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ProveedorRepository {

    private final ProveedorDao proveedorDao;
    private final ExecutorService executor;

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public ProveedorRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.proveedorDao = db.proveedorDao();
        this.executor = AppDatabase.getDatabaseWriteExecutor();
    }

    public LiveData<List<ProveedorEntity>> getActiveProveedores() {
        return proveedorDao.getActiveProveedores();
    }

    public void registerProveedor(String rut, String razonSocial, String contacto, String telefono, OperationCallback callback) {
        executor.execute(() -> {
            int count = proveedorDao.countByRut(rut);
            if (count > 0) {
                callback.onError("Ya existe un proveedor registrado con ese RUT");
                return;
            }

            ProveedorEntity proveedor = new ProveedorEntity(rut, razonSocial, contacto, telefono, 1);
            long id = proveedorDao.insert(proveedor);
            if (id > 0) {
                callback.onSuccess();
            } else {
                callback.onError("Error al guardar el proveedor");
            }
        });
    }

    public void updateProveedor(ProveedorEntity proveedor, OperationCallback callback) {
        executor.execute(() -> {
            int count = proveedorDao.countByRutExcludingId(proveedor.getRutEmpresa(), proveedor.getIdProveedor());
            if (count > 0) {
                callback.onError("Ya existe otro proveedor registrado con ese RUT");
                return;
            }

            try {
                proveedorDao.update(proveedor);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Error al actualizar el proveedor: " + e.getMessage());
            }
        });
    }

    public void disableProveedor(int idProveedor, OperationCallback callback) {
        executor.execute(() -> {
            try {
                proveedorDao.disableProveedor(idProveedor);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Error al deshabilitar el proveedor: " + e.getMessage());
            }
        });
    }
}
