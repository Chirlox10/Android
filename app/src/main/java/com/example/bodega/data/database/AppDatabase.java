package com.example.bodega.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bodega.data.dao.CategoriaDao;
import com.example.bodega.data.dao.ProductoDao;
import com.example.bodega.data.dao.ProveedorDao;
import com.example.bodega.data.dao.RolDao;
import com.example.bodega.data.dao.UsuarioDao;
import com.example.bodega.data.entity.CategoriaEntity;
import com.example.bodega.data.entity.ProductoEntity;
import com.example.bodega.data.entity.ProveedorEntity;
import com.example.bodega.data.entity.RolEntity;
import com.example.bodega.data.entity.UsuarioEntity;
import com.example.bodega.util.PasswordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        RolEntity.class,
        UsuarioEntity.class,
        CategoriaEntity.class,
        ProveedorEntity.class,
        ProductoEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract RolDao rolDao();
    public abstract UsuarioDao usuarioDao();
    public abstract CategoriaDao categoriaDao();
    public abstract ProveedorDao proveedorDao();
    public abstract ProductoDao productoDao();

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "bodega_database"
                    )
                    .addCallback(sRoomDatabaseCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                AppDatabase database = INSTANCE;
                if (database == null) return;

                RolDao rolDao = database.rolDao();
                UsuarioDao usuarioDao = database.usuarioDao();
                CategoriaDao categoriaDao = database.categoriaDao();
                ProveedorDao proveedorDao = database.proveedorDao();
                ProductoDao productoDao = database.productoDao();

                // Seed Roles
                long adminRoleId = rolDao.insert(new RolEntity("Administrador", "Acceso total y gestión"));
                long opRoleId = rolDao.insert(new RolEntity("Operador", "Gestión operativa e inventario"));

                int idRolAdmin = (int) (adminRoleId > 0 ? adminRoleId : 1);
                int idRolOperador = (int) (opRoleId > 0 ? opRoleId : 2);

                // Seed Initial Users
                String adminHash = PasswordUtils.hashPassword("admin123");
                String opHash = PasswordUtils.hashPassword("123456");

                usuarioDao.insert(new UsuarioEntity("admin", "Administrador General", adminHash, idRolAdmin, 1));
                usuarioDao.insert(new UsuarioEntity("bodeguero", "Juan Bodeguero", opHash, idRolOperador, 1));

                // Seed Categories
                long cat1 = categoriaDao.insert(new CategoriaEntity("Bebidas", "Gaseosas, jugos y aguas", 1));
                long cat2 = categoriaDao.insert(new CategoriaEntity("Abarrotes", "Alimentos no perecibles", 1));
                long cat3 = categoriaDao.insert(new CategoriaEntity("Lácteos", "Leches, quesos y yogures", 1));

                // Seed Proveedores
                proveedorDao.insert(new ProveedorEntity("76.123.456-7", "Distribuidora Central S.A.", "Carlos Pérez", "+56912345678", 1));
                proveedorDao.insert(new ProveedorEntity("77.987.654-3", "Comercializadora del Sur SpA", "Ana Gómez", "+56987654321", 1));

                // Seed Products
                int idCat1 = (int) (cat1 > 0 ? cat1 : 1);
                int idCat2 = (int) (cat2 > 0 ? cat2 : 2);
                int idCat3 = (int) (cat3 > 0 ? cat3 : 3);

                productoDao.insert(new ProductoEntity("SKU-1001", "Coca-Cola 1.5L", "Bebida gaseosa desechable", 30, 10, idCat1, 1));
                productoDao.insert(new ProductoEntity("SKU-1002", "Arroz Grado 1 (1kg)", "Arroz grano largo ancho", 5, 15, idCat2, 1));
                productoDao.insert(new ProductoEntity("SKU-1003", "Leche Entera 1L", "Leche UHT caja", 40, 12, idCat3, 1));
            });
        }
    };
}
