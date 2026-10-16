package com.example.bodega.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bodega.data.entity.RolEntity;

import java.util.List;

@Dao
public interface RolDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(RolEntity rol);

    @Query("SELECT * FROM ROL ORDER BY nombre_rol ASC")
    LiveData<List<RolEntity>> getAllRoles();

    @Query("SELECT * FROM ROL WHERE id_rol = :idRol")
    RolEntity getRoleById(int idRol);

    @Query("SELECT COUNT(*) FROM ROL")
    int getRoleCount();
}
