package com.example.bodega.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "ROL",
    indices = {@Index(value = {"nombre_rol"}, unique = true)}
)
public class RolEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_rol")
    private int idRol;

    @NonNull
    @ColumnInfo(name = "nombre_rol")
    private String nombreRol;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    public RolEntity(@NonNull String nombreRol, String descripcion) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    @NonNull
    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(@NonNull String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
