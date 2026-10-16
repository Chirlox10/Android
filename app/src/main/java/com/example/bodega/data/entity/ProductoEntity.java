package com.example.bodega.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "PRODUCTO",
    indices = {@Index(value = {"sku_codigo"}, unique = true)},
    foreignKeys = @ForeignKey(
        entity = CategoriaEntity.class,
        parentColumns = "id_categoria",
        childColumns = "id_categoria",
        onDelete = ForeignKey.RESTRICT
    )
)
public class ProductoEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_producto")
    private int idProducto;

    @NonNull
    @ColumnInfo(name = "sku_codigo")
    private String skuCodigo;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "stock_actual")
    private int stockActual;

    @ColumnInfo(name = "stock_minimo")
    private int stockMinimo;

    @ColumnInfo(name = "id_categoria", index = true)
    private int idCategoria;

    @ColumnInfo(name = "activo", defaultValue = "1")
    private int activo;

    public ProductoEntity(@NonNull String skuCodigo, @NonNull String nombre, String descripcion, int stockActual, int stockMinimo, int idCategoria, int activo) {
        this.skuCodigo = skuCodigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.idCategoria = idCategoria;
        this.activo = activo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    @NonNull
    public String getSkuCodigo() {
        return skuCodigo;
    }

    public void setSkuCodigo(@NonNull String skuCodigo) {
        this.skuCodigo = skuCodigo;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
