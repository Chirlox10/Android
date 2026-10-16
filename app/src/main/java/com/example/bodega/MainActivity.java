package com.example.bodega;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bodega.databinding.ActivityMainBinding;
import com.example.bodega.ui.auth.LoginActivity;
import com.example.bodega.ui.category.AddCategoryDialogFragment;
import com.example.bodega.ui.category.CategoriesFragment;
import com.example.bodega.ui.product.AddEditProductDialogFragment;
import com.example.bodega.ui.product.ProductsFragment;
import com.example.bodega.ui.proveedor.AddProveedorDialogFragment;
import com.example.bodega.ui.proveedor.ProveedoresFragment;
import com.example.bodega.ui.user.AddUserDialogFragment;
import com.example.bodega.ui.user.UsersFragment;
import com.example.bodega.util.SessionManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bodega");
            getSupportActionBar().setSubtitle(sessionManager.getFullName() + " (" + sessionManager.getRoleName() + ")");
        }

        if (!sessionManager.isAdmin()) {
            Menu menu = binding.bottomNavigation.getMenu();
            MenuItem userItem = menu.findItem(R.id.nav_users);
            if (userItem != null) {
                userItem.setVisible(false);
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_products) {
                loadFragment(new ProductsFragment());
                return true;
            } else if (itemId == R.id.nav_categories) {
                loadFragment(new CategoriesFragment());
                return true;
            } else if (itemId == R.id.nav_suppliers) {
                loadFragment(new ProveedoresFragment());
                return true;
            } else if (itemId == R.id.nav_users) {
                if (sessionManager.isAdmin()) {
                    loadFragment(new UsersFragment());
                    return true;
                }
            }
            return false;
        });

        if (savedInstanceState == null) {
            binding.bottomNavigation.setSelectedItemId(R.id.nav_products);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add) {
            triggerRegistrationForCurrentTab();
            return true;
        } else if (itemId == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void triggerRegistrationForCurrentTab() {
        int currentTabId = binding.bottomNavigation.getSelectedItemId();
        if (currentTabId == R.id.nav_products) {
            AddEditProductDialogFragment.newInstance(null)
                .show(getSupportFragmentManager(), "AddProductDialog");
        } else if (currentTabId == R.id.nav_categories) {
            new AddCategoryDialogFragment()
                .show(getSupportFragmentManager(), "AddCategoryDialog");
        } else if (currentTabId == R.id.nav_suppliers) {
            new AddProveedorDialogFragment()
                .show(getSupportFragmentManager(), "AddProveedorDialog");
        } else if (currentTabId == R.id.nav_users) {
            if (sessionManager.isAdmin()) {
                new AddUserDialogFragment()
                    .show(getSupportFragmentManager(), "AddUserDialog");
            }
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Está seguro que desea cerrar su sesión actual?")
            .setPositiveButton("Cerrar Sesión", (dialog, which) -> {
                sessionManager.logoutUser();
                navigateToLogin();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
