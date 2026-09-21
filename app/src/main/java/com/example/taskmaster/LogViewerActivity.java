package com.example.taskmaster;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LogViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_viewer);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.log_viewer_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvActivityLog = findViewById(R.id.rv_activity_log);
        rvActivityLog.setLayoutManager(new LinearLayoutManager(this));
        
        ProductRepository repository = ProductRepository.getInstance();
        LogAdapter adapter = new LogAdapter(repository.getActivityLogs());
        rvActivityLog.setAdapter(adapter);
    }
}