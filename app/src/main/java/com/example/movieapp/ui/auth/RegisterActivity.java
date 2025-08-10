package com.example.movieapp.ui.auth;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private ProgressBar progressBar;
    private AppDatabase database;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Initialiser la base de données
        database = AppDatabase.getInstance(this);
        
        // Initialiser les vues
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);
        progressBar = findViewById(R.id.progress_bar);
        
        // Gestion du bouton retour
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());
        
        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        
        // Validation
        if (TextUtils.isEmpty(name)) {
            etName.setError("Le nom est requis");
            etName.requestFocus();
            return;
        }
        
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
        
        if (!PasswordUtils.isPasswordStrong(password)) {
            etPassword.setError("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial");
            etPassword.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Les mots de passe ne correspondent pas");
            etConfirmPassword.requestFocus();
            return;
        }
        
        // Afficher le progress bar
        showProgress(true);
        
        // Effectuer l'inscription en arrière-plan
        new Thread(() -> {
            try {
                // Vérifier si l'email existe déjà
                int emailExists = database.userDao().checkEmailExists(email);
                
                if (emailExists > 0) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this, 
                            "Cet email est déjà utilisé", Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                
                // Créer le nouvel utilisateur
                String hashedPassword = PasswordUtils.hashPassword(password);
                User newUser = new User(name, email, hashedPassword);
                
                long userId = database.userDao().insertUser(newUser);
                
                if (userId > 0) {
                    // Inscription réussie
                    newUser.setId((int) userId);
                    
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this, 
                            "Inscription réussie !", Toast.LENGTH_LONG).show();
                        
                        // Sauvegarder la session et aller à l'activité principale
                        saveUserSession(newUser);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this, 
                            "Erreur lors de l'inscription", Toast.LENGTH_LONG).show();
                    });
                }
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(RegisterActivity.this, 
                        "Erreur d'inscription: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
    
    private void saveUserSession(User user) {
        getSharedPreferences("MovieAppPrefs", MODE_PRIVATE)
                .edit()
                .putInt("user_id", user.getId())
                .putString("user_name", user.getName())
                .putString("user_email", user.getEmail())
                .apply();
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
} 