package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private boolean isEnteredState = false; // Alternates between Entrance and Exit tracking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton btnNavProductMgmt = findViewById(R.id.btn_nav_product_mgmt);
        MaterialButton btnNavActivityLog = findViewById(R.id.btn_nav_activity_log);
        MaterialButton btnNavStock = findViewById(R.id.btn_nav_stock);
        MaterialButton btnLogAccess = findViewById(R.id.btn_log_access);

        btnNavProductMgmt.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProductManagementActivity.class);
            startActivity(intent);
        });

        btnNavActivityLog.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LogViewerActivity.class);
            startActivity(intent);
        });

        btnNavStock.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, StockActivity.class);
            startActivity(intent);
        });

        btnLogAccess.setOnClickListener(v -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = dateFormat.format(new Date());
            String logMessage;

            if (!isEnteredState) {
                logMessage = getString(R.string.log_app_entrance) + " [" + currentTime + "]";
                btnLogAccess.setText(R.string.btn_log_access_exit);
                btnLogAccess.setIconResource(android.R.drawable.ic_lock_power_off);
                Toast.makeText(this, R.string.log_app_entrance, Toast.LENGTH_SHORT).show();
            } else {
                logMessage = getString(R.string.log_app_exit) + " [" + currentTime + "]";
                btnLogAccess.setText(R.string.btn_log_access_entrance);
                btnLogAccess.setIconResource(android.R.drawable.ic_lock_lock);
                Toast.makeText(this, R.string.log_app_exit, Toast.LENGTH_SHORT).show();
            }

            // Save the log entry into our repository
            ProductRepository.getInstance().addLog(logMessage);
            isEnteredState = !isEnteredState;
        });
    }
}