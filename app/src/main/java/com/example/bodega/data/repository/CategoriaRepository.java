package com.example.bodega.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.bodega.data.dao.CategoriaDao;
import com.example.bodega.data.database.AppDatabase;
import com.example.bodega.data.entity.CategoriaEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class CategoriaRepository {

    private final CategoriaDao categoriaDao;
    private final ExecutorService executor;

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public CategoriaRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.categoriaDao = db.categoriaDao();
        this.executor = AppDatabase.getDatabaseWriteExecutor();
    }

    public LiveData<List<CategoriaEntity>> getActiveCategories() {
        return categoriaDao.getActiveCategories();
    }

    public void registerCategory(String nombre, String descripcion, OperationCallback callback) {
        executor.execute(() -> {
            int count = categoriaDao.countByName(nombre);
            if (count > 0) {
                callback.onError("Ya existe una categoría registrada con ese nombre");
                return;
            }

            CategoriaEntity newCategory = new CategoriaEntity(nombre, descripcion, 1);
            long id = categoriaDao.insert(newCategory);
            if (id > 0) {
                callback.onSuccess();
            } else {
                callback.onError("Error al guardar la categoría");
            }
        });
    }

    public void updateCategory(CategoriaEntity categoria, OperationCallback callback) {
        executor.execute(() -> {
            int count = categoriaDao.countByNameExcludingId(categoria.getNombreCategoria(), categoria.getIdCategoria());
            if (count > 0) {
                callback.onError("Ya existe otra categoría registrada con ese nombre");
                return;
            }

            try {
                categoriaDao.update(categoria);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Error al actualizar la categoría: " + e.getMessage());
            }
        });
    }

    public void disableCategory(int idCategoria, OperationCallback callback) {
        executor.execute(() -> {
            try {
                categoriaDao.disableCategory(idCategoria);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Error al deshabilitar la categoría: " + e.getMessage());
            }
        });
    }
}
