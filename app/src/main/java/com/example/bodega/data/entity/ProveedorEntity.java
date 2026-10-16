package com.example.bodega.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "PROVEEDOR",
    indices = {@Index(value = {"rut_empresa"}, unique = true)}
)
public class ProveedorEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_proveedor")
    private int idProveedor;

    @NonNull
    @ColumnInfo(name = "rut_empresa")
    private String rutEmpresa;

    @NonNull
    @ColumnInfo(name = "razon_social")
    private String razonSocial;

    @ColumnInfo(name = "contacto")
    private String contacto;

    @ColumnInfo(name = "telefono")
    private String telefono;

    @ColumnInfo(name = "activo", defaultValue = "1")
    private int activo;

    public ProveedorEntity(@NonNull String rutEmpresa, @NonNull String razonSocial, String contacto, String telefono, int activo) {
        this.rutEmpresa = rutEmpresa;
        this.razonSocial = razonSocial;
        this.contacto = contacto;
        this.telefono = telefono;
        this.activo = activo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    @NonNull
    public String getRutEmpresa() {
        return rutEmpresa;
    }

    public void setRutEmpresa(@NonNull String rutEmpresa) {
        this.rutEmpresa = rutEmpresa;
    }

    @NonNull
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(@NonNull String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
