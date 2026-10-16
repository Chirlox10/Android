package com.example.bodega.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.bodega.data.entity.RolEntity;
import com.example.bodega.data.entity.UsuarioEntity;

public class UsuarioWithRol {

    @Embedded
    public UsuarioEntity usuario;

    @Relation(
        parentColumn = "id_rol",
        entityColumn = "id_rol"
    )
    public RolEntity rol;
}
