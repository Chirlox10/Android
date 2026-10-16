package com.example.bodega.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.data.entity.ProductoEntity;

public class ProductoWithCategoria {

    @Embedded
    public ProductoEntity producto;

    @Relation(
        parentColumn = "id_categoria",
        entityColumn = "id_categoria"
    )
    public CategoriaEntity categoria;
}
