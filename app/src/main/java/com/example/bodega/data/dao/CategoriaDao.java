package com.example.bodega.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bodega.data.entity.CategoriaEntity;

import java.util.List;

@Dao
public interface CategoriaDao {

    @Insert
    long insert(CategoriaEntity categoria);

    @Update
    void update(CategoriaEntity categoria);

    @Query("SELECT * FROM CATEGORIA WHERE activo = 1 ORDER BY nombre_categoria ASC")
    LiveData<List<CategoriaEntity>> getActiveCategories();

    @Query("SELECT * FROM CATEGORIA ORDER BY nombre_categoria ASC")
    LiveData<List<CategoriaEntity>> getAllCategories();

    @Query("SELECT COUNT(*) FROM CATEGORIA WHERE LOWER(nombre_categoria) = LOWER(:nombreCategoria)")
    int countByName(String nombreCategoria);

    @Query("SELECT COUNT(*) FROM CATEGORIA WHERE LOWER(nombre_categoria) = LOWER(:nombreCategoria) AND id_categoria != :idCategoria")
    int countByNameExcludingId(String nombreCategoria, int idCategoria);

    @Query("UPDATE CATEGORIA SET activo = 0 WHERE id_categoria = :idCategoria")
    void disableCategory(int idCategoria);
}
