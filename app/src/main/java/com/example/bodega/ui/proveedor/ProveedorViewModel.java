package com.example.bodega.ui.proveedor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bodega.data.entity.ProveedorEntity;
import com.example.bodega.data.repository.ProveedorRepository;
import com.example.bodega.util.SingleLiveEvent;

import java.util.List;

public class ProveedorViewModel extends AndroidViewModel {

    private final ProveedorRepository repository;
    private final LiveData<List<ProveedorEntity>> activeProveedores;
    private final SingleLiveEvent<String> operationError = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> operationSuccess = new SingleLiveEvent<>();

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public ProveedorViewModel(@NonNull Application application) {
        super(application);
        repository = new ProveedorRepository(application);
        activeProveedores = repository.getActiveProveedores();
    }

    public LiveData<List<ProveedorEntity>> getActiveProveedores() {
        return activeProveedores;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public void registerProveedor(String rut, String razonSocial, String contacto, String telefono) {
        if (rut == null || rut.trim().isEmpty()) {
            operationError.postValue("El RUT de la empresa es obligatorio");
            return;
        }
        if (razonSocial == null || razonSocial.trim().isEmpty()) {
            operationError.postValue("La razón social es obligatoria");
            return;
        }

        repository.registerProveedor(rut.trim(), razonSocial.trim(), contacto != null ? contacto.trim() : "", telefono != null ? telefono.trim() : "", new ProveedorRepository.OperationCallback() {
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

    public void updateProveedor(ProveedorEntity proveedor, OperationCallback callback) {
        repository.updateProveedor(proveedor, new ProveedorRepository.OperationCallback() {
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

    public void disableProveedor(int idProveedor) {
        repository.disableProveedor(idProveedor, new ProveedorRepository.OperationCallback() {
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
