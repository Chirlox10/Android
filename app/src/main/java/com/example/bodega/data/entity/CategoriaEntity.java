package com.example.bodega.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "CATEGORIA",
    indices = {@Index(value = {"nombre_categoria"}, unique = true)}
)
public class CategoriaEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_categoria")
    private int idCategoria;

    @NonNull
    @ColumnInfo(name = "nombre_categoria")
    private String nombreCategoria;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "activo", defaultValue = "1")
    private int activo;

    public CategoriaEntity(@NonNull String nombreCategoria, String descripcion, int activo) {
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @NonNull
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(@NonNull String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
