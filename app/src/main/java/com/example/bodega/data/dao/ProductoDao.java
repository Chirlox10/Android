package com.example.bodega.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.bodega.data.entity.ProductoEntity;
import com.example.bodega.data.model.ProductoWithCategoria;

import java.util.List;

@Dao
public interface ProductoDao {

    @Insert
    long insert(ProductoEntity producto);

    @Update
    void update(ProductoEntity producto);

    @Transaction
    @Query("SELECT * FROM PRODUCTO WHERE activo = 1 ORDER BY nombre ASC")
    LiveData<List<ProductoWithCategoria>> getActiveProductsWithCategory();

    @Transaction
    @Query("SELECT * FROM PRODUCTO WHERE activo = 1 AND (LOWER(nombre) LIKE '%' || LOWER(:query) || '%' OR LOWER(sku_codigo) LIKE '%' || LOWER(:query) || '%') ORDER BY nombre ASC")
    LiveData<List<ProductoWithCategoria>> searchProducts(String query);

    @Query("SELECT COUNT(*) FROM PRODUCTO WHERE sku_codigo = :skuCodigo")
    int countBySku(String skuCodigo);

    @Query("UPDATE PRODUCTO SET activo = 0 WHERE id_producto = :idProducto")
    void disableProduct(int idProducto);
}
