package com.example.bodega.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.data.repository.CategoriaRepository;
import com.example.bodega.util.SingleLiveEvent;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoriaRepository repository;
    private final LiveData<List<CategoriaEntity>> activeCategories;
    private final SingleLiveEvent<String> operationError = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> operationSuccess = new SingleLiveEvent<>();

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoriaRepository(application);
        activeCategories = repository.getActiveCategories();
    }

    public LiveData<List<CategoriaEntity>> getActiveCategories() {
        return activeCategories;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public void registerCategory(String nombre, String descripcion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            operationError.postValue("El nombre de la categoría es obligatorio");
            return;
        }

        repository.registerCategory(nombre.trim(), descripcion != null ? descripcion.trim() : "", new CategoriaRepository.OperationCallback() {
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

    public void updateCategory(CategoriaEntity categoria, OperationCallback callback) {
        repository.updateCategory(categoria, new CategoriaRepository.OperationCallback() {
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

    public void disableCategory(int idCategoria) {
        repository.disableCategory(idCategoria, new CategoriaRepository.OperationCallback() {
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
