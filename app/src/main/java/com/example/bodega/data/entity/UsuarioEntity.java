package com.example.bodega.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "USUARIO",
    indices = {@Index(value = {"username"}, unique = true)},
    foreignKeys = @ForeignKey(
        entity = RolEntity.class,
        parentColumns = "id_rol",
        childColumns = "id_rol",
        onDelete = ForeignKey.RESTRICT
    )
)
public class UsuarioEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    private int idUsuario;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "nombre_completo")
    private String nombreCompleto;

    @NonNull
    @ColumnInfo(name = "password_hash")
    private String passwordHash;

    @ColumnInfo(name = "id_rol", index = true)
    private int idRol;

    @ColumnInfo(name = "activo", defaultValue = "1")
    private int activo;

    public UsuarioEntity(@NonNull String username, @NonNull String nombreCompleto, @NonNull String passwordHash, int idRol, int activo) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.passwordHash = passwordHash;
        this.idRol = idRol;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(@NonNull String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@NonNull String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
