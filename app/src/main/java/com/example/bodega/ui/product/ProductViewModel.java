package com.example.bodega.ui.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.data.entity.ProductoEntity;
import com.example.bodega.data.model.ProductoWithCategoria;
import com.example.bodega.data.repository.CategoriaRepository;
import com.example.bodega.data.repository.ProductoRepository;
import com.example.bodega.util.SingleLiveEvent;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final LiveData<List<ProductoWithCategoria>> products;

    private final SingleLiveEvent<String> operationError = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> operationSuccess = new SingleLiveEvent<>();

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductoRepository(application);
        categoriaRepository = new CategoriaRepository(application);
        products = Transformations.switchMap(searchQuery, repository::searchProducts);
    }

    public LiveData<List<ProductoWithCategoria>> getProducts() {
        return products;
    }

    public LiveData<List<CategoriaEntity>> getActiveCategories() {
        return categoriaRepository.getActiveCategories();
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void registerProduct(String sku, String nombre, String descripcion, int stockActual, int stockMinimo, int idCategoria) {
        repository.registerProduct(sku, nombre, descripcion, stockActual, stockMinimo, idCategoria, new ProductoRepository.OperationCallback() {
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

    public void updateProduct(ProductoEntity producto, OperationCallback callback) {
        repository.updateProduct(producto, new ProductoRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                if (callback != null) callback.onSuccess();
                operationSuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                if (callback != null) callback.onError(message);
                operationError.postValue(message);
            }
        });
    }

    public void disableProduct(int idProducto) {
        repository.disableProduct(idProducto, new ProductoRepository.OperationCallback() {
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

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }
}
