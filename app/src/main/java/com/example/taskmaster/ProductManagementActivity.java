package com.example.taskmaster;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductManagementActivity extends AppCompatActivity {

    private TextInputEditText etProductName;
    private TextInputEditText etProductPrice;
    private SimpleDateFormat dateFormat;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_management);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_management_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = ProductRepository.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        etProductName = findViewById(R.id.et_product_name);
        etProductPrice = findViewById(R.id.et_product_price);

        setupButtons();
    }

    private void setupButtons() {
        MaterialButton btnRegister = findViewById(R.id.btn_register);
        MaterialButton btnModify = findViewById(R.id.btn_modify);
        MaterialButton btnSearch = findViewById(R.id.btn_search);
        MaterialButton btnDelete = findViewById(R.id.btn_delete);

        btnRegister.setOnClickListener(v -> handleAction("Registered", true));
        btnModify.setOnClickListener(v -> handleAction("Modified", true));
        btnSearch.setOnClickListener(v -> handleAction("Searched", false));
        btnDelete.setOnClickListener(v -> handleAction("Deleted", false));
    }

    private void handleAction(String action, boolean requirePrice) {
        String name = etProductName.getText() != null ? etProductName.getText().toString().trim() : "";
        String price = etProductPrice.getText() != null ? etProductPrice.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            etProductName.setError(getString(R.string.error_name_required));
            return;
        }

        if (requirePrice && TextUtils.isEmpty(price)) {
            etProductPrice.setError(getString(R.string.error_price_required));
            return;
        }

        String currentTime = dateFormat.format(new Date());
        String logEntry;
        if (requirePrice || !TextUtils.isEmpty(price)) {
            logEntry = getString(R.string.log_format, action, name, price) + " [" + currentTime + "]";
        } else {
            logEntry = getString(R.string.log_search_format, action, name) + " [" + currentTime + "]";
        }

        repository.addLog(logEntry);
        
        if ("Registered".equals(action)) {
            try {
                double parsedPrice = Double.parseDouble(price);
                repository.addProduct(new Product(name, parsedPrice));
            } catch (NumberFormatException e) {
                // Ignore or handle
            }
        }
        
        Toast.makeText(this, action + " successful", Toast.LENGTH_SHORT).show();

        // Clear inputs
        etProductName.setText("");
        etProductPrice.setText("");
        etProductName.clearFocus();
        etProductPrice.clearFocus();
    }
}