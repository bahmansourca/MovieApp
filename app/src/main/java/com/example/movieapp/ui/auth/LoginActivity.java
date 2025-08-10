package com.example.movieapp.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.example.movieapp.data.database.AppDatabase;
import com.example.movieapp.data.model.User;
import com.example.movieapp.ui.main.MainActivity;
import com.example.movieapp.utils.PasswordUtils;

public class LoginActivity extends AppCompatActivity {
    
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;
    private ProgressBar progressBar;
    private AppDatabase database;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialiser la base de données
        database = AppDatabase.getInstance(this);
        
        // Initialiser les vues
        initViews();
        setupListeners();
        
        // Vérifier si l'utilisateur est déjà connecté
        checkIfUserLoggedIn();
    }
    
    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        
        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    
    private void checkIfUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        
        if (userId != -1) {
            // L'utilisateur est déjà connecté, aller à l'activité principale
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    
    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("L'email est requis");
            etEmail.requestFocus();
            return;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format d'email invalide");
            etEmail.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Le mot de passe est requis");
            etPassword.requestFocus();
            return;
        }
        
        // Afficher le progress bar
        showProgress(true);
        
        // Effectuer la connexion en arrière-plan
        new Thread(() -> {
            try {
                String hashedPassword = PasswordUtils.hashPassword(password);
                User user = database.userDao().authenticateUser(email, hashedPassword);
                
                runOnUiThread(() -> {
                    showProgress(false);
                    
                    if (user != null) {
                        // Connexion réussie
                        saveUserSession(user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Échec de connexion
                        Toast.makeText(LoginActivity.this, 
                            "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, 
                        "Erreur de connexion: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
    
    private void saveUserSession(User user) {
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("user_name", user.getName());
        editor.putString("user_email", user.getEmail());
        editor.apply();
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
} 