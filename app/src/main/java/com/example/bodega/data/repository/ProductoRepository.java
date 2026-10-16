package com.example.bodega.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.bodega.data.dao.ProductoDao;
import com.example.bodega.data.database.AppDatabase;
import com.example.bodega.data.entity.ProductoEntity;
import com.example.bodega.data.model.ProductoWithCategoria;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ProductoRepository {

    private final ProductoDao productoDao;
    private final ExecutorService executor;

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public ProductoRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.productoDao = db.productoDao();
        this.executor = AppDatabase.getDatabaseWriteExecutor();
    }

    public LiveData<List<ProductoWithCategoria>> getActiveProducts() {
        return productoDao.getActiveProductsWithCategory();
    }

    public LiveData<List<ProductoWithCategoria>> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getActiveProducts();
        }
        return productoDao.searchProducts(query.trim());
    }

    public void registerProduct(String sku, String nombre, String descripcion, int stockActual, int stockMinimo, int idCategoria, OperationCallback callback) {
        executor.execute(() -> {
            if (stockActual < 0) {
                callback.onError("El stock actual debe ser mayor o igual a 0");
                return;
            }

            int count = productoDao.countBySku(sku);
            if (count > 0) {
                callback.onError("El código SKU ya se encuentra registrado");
                return;
            }

            ProductoEntity newProduct = new ProductoEntity(sku, nombre, descripcion, stockActual, stockMinimo, idCategoria, 1);
            long id = productoDao.insert(newProduct);
            if (id > 0) {
                callback.onSuccess();
            } else {
                callback.onError("Error al registrar el producto");
            }
        });
    }

    public void updateProduct(ProductoEntity producto, OperationCallback callback) {
        executor.execute(() -> {
            if (producto.getStockActual() < 0) {
                callback.onError("El stock actual debe ser mayor o igual a 0");
                return;
            }

            try {
                productoDao.update(producto);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Error al actualizar el producto: " + e.getMessage());
            }
        });
    }

    public void disableProduct(int idProducto, OperationCallback callback) {
        executor.execute(() -> {
            try {
                productoDao.disableProduct(idProducto);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Error al deshabilitar el producto: " + e.getMessage());
            }
        });
    }
}
