package com.example.bodega.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.bodega.data.entity.UsuarioEntity;
import com.example.bodega.data.model.UsuarioWithRol;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    long insert(UsuarioEntity usuario);

    @Query("SELECT * FROM USUARIO WHERE username = :username LIMIT 1")
    UsuarioEntity getByUsername(String username);

    @Transaction
    @Query("SELECT * FROM USUARIO WHERE username = :username LIMIT 1")
    UsuarioWithRol getUsuarioWithRolByUsername(String username);

    @Transaction
    @Query("SELECT * FROM USUARIO ORDER BY id_usuario DESC")
    LiveData<List<UsuarioWithRol>> getAllUsuariosWithRol();

    @Query("SELECT COUNT(*) FROM USUARIO WHERE username = :username")
    int countByUsername(String username);
}
