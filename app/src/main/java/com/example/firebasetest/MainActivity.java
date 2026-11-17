package com.example.firebasetest;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private FirebaseAnalytics mAnalytics;

    private TextView tvAuthStatus;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnRegister;
    private Button btnSignIn;
    private Button btnAnonymous;
    private Button btnSignOut;

    private TextInputEditText etDocTitle;
    private TextInputEditText etDocContent;
    private Button btnSaveData;
    private Button btnReadData;
    private TextView tvFirestoreOutput;

    private Button btnLogAnalytics;
    private TextView tvConsoleLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Views
        tvAuthStatus = findViewById(R.id.tvAuthStatus);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnAnonymous = findViewById(R.id.btnAnonymous);
        btnSignOut = findViewById(R.id.btnSignOut);

        etDocTitle = findViewById(R.id.etDocTitle);
        etDocContent = findViewById(R.id.etDocContent);
        btnSaveData = findViewById(R.id.btnSaveData);
        btnReadData = findViewById(R.id.btnReadData);
        tvFirestoreOutput = findViewById(R.id.tvFirestoreOutput);

        btnLogAnalytics = findViewById(R.id.btnLogAnalytics);
        tvConsoleLog = findViewById(R.id.tvConsoleLog);

        setupAuthListeners();
        setupFirestore();
        setupAnalytics();
    }

    private void setupAuthListeners() {
        // Listen to Auth state changes
        mAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                String status = "Signed In\nEmail: " + (user.getEmail() != null ? user.getEmail() : "N/A")
                        + "\nIs Anonymous: " + user.isAnonymous()
                        + "\nUID: " + user.getUid();
                tvAuthStatus.setText(status);
            } else {
                tvAuthStatus.setText("Not signed in");
            }
        });

        // Register button
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            appendLog("Registering user: " + email + "...");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            appendLog("Registration Success! UID: " + (mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : ""));
                        } else {
                            appendLog("Registration Error: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                        }
                    });
        });

        // Sign In button
        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            appendLog("Signing in user: " + email + "...");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            appendLog("Sign In Success! UID: " + (mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : ""));
                        } else {
                            appendLog("Sign In Error: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                        }
                    });
        });

        // Anonymous Sign In button
        btnAnonymous.setOnClickListener(v -> {
            appendLog("Signing in anonymously...");
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            appendLog("Anonymous Sign In Success! UID: " + (mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : ""));
                        } else {
                            appendLog("Anonymous Sign In Error: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                        }
                    });
        });

        // Sign Out button
        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            appendLog("User signed out.");
        });
    }

    private void setupFirestore() {
        btnSaveData.setOnClickListener(v -> {
            String title = etDocTitle.getText() != null ? etDocTitle.getText().toString().trim() : "";
            String content = etDocContent.getText() != null ? etDocContent.getText().toString().trim() : "";

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Please enter both Title and Content", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser currentUser = mAuth.getCurrentUser();
            String uid = currentUser != null ? currentUser.getUid() : "anonymous_guest";

            Map<String, Object> note = new HashMap<>();
            note.put("title", title);
            note.put("content", content);
            note.put("timestamp", System.currentTimeMillis());
            note.put("authorUid", uid);

            appendLog("Saving note to Firestore...");
            mDb.collection("notes")
                    .add(note)
                    .addOnSuccessListener(documentReference -> {
                        appendLog("Note saved successfully with ID: " + documentReference.getId());
                        etDocTitle.setText("");
                        etDocContent.setText("");
                        fetchNotes();
                    })
                    .addOnFailureListener(e -> appendLog("Error saving note: " + e.getMessage()));
        });

        btnReadData.setOnClickListener(v -> fetchNotes());
    }

    private void fetchNotes() {
        appendLog("Fetching notes from Firestore...");
        mDb.collection("notes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        StringBuilder sb = new StringBuilder();
                        int count = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            count++;
                            String title = document.getString("title");
                            String content = document.getString("content");
                            sb.append(count).append(". ").append(title).append(": ").append(content).append("\n");
                        }
                        if (count == 0) {
                            tvFirestoreOutput.setText("No notes found in Firestore collection.");
                            appendLog("Fetch complete: 0 notes found.");
                        } else {
                            tvFirestoreOutput.setText(sb.toString().trim());
                            appendLog("Fetch complete: " + count + " notes retrieved.");
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        tvFirestoreOutput.setText("Failed to fetch notes.");
                        appendLog("Error fetching notes: " + errorMsg);
                    }
                });
    }

    private void setupAnalytics() {
        btnLogAnalytics.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "btn_log_analytics");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test Custom Event");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button_click");

            mAnalytics.logEvent("test_event_clicked", bundle);
            appendLog("Analytics Event 'test_event_clicked' logged successfully!");
            Toast.makeText(this, "Analytics event logged!", Toast.LENGTH_SHORT).show();
        });
    }

    private void appendLog(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String logLine = "[" + timestamp + "] " + message + "\n";
        tvConsoleLog.append(logLine);
    }
}
