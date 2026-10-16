package com.example.bodega.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bodega.data.entity.ProveedorEntity;

import java.util.List;

@Dao
public interface ProveedorDao {

    @Insert
    long insert(ProveedorEntity proveedor);

    @Update
    void update(ProveedorEntity proveedor);

    @Query("SELECT * FROM PROVEEDOR WHERE activo = 1 ORDER BY razon_social ASC")
    LiveData<List<ProveedorEntity>> getActiveProveedores();

    @Query("SELECT COUNT(*) FROM PROVEEDOR WHERE rut_empresa = :rutEmpresa")
    int countByRut(String rutEmpresa);

    @Query("SELECT COUNT(*) FROM PROVEEDOR WHERE rut_empresa = :rutEmpresa AND id_proveedor != :idProveedor")
    int countByRutExcludingId(String rutEmpresa, int idProveedor);

    @Query("UPDATE PROVEEDOR SET activo = 0 WHERE id_proveedor = :idProveedor")
    void disableProveedor(int idProveedor);
}
