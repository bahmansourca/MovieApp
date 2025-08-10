package com.example.movieapp.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.example.movieapp.data.database.AppDatabase;
import com.example.movieapp.data.model.User;
import com.example.movieapp.utils.PasswordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvName, tvEmail, tvRegistrationDate;
    private TextView tvActiveRentals, tvTotalRentals;
    private LinearLayout btnChangePassword, btnRentalHistory;
    private ProgressBar progressBar;
    
    private AppDatabase database;
    private ExecutorService executor;
    private User currentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        database = AppDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        
        initViews();
        loadUserProfile();
    }
    
    private void initViews() {
        try {
            tvName = findViewById(R.id.tv_user_name);
            tvEmail = findViewById(R.id.tv_user_email);
            tvActiveRentals = findViewById(R.id.tv_active_rentals);
            tvTotalRentals = findViewById(R.id.tv_total_rentals);
            btnChangePassword = findViewById(R.id.btn_change_password);
            btnRentalHistory = findViewById(R.id.btn_rental_history);
            progressBar = findViewById(R.id.progress_bar);
            
            // Gestion du bouton retour
            findViewById(R.id.btn_back).setOnClickListener(v -> finish());
            
            if (btnChangePassword != null) {
                btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
            }
            
            // Gestion du bouton de l'historique des locations
            if (btnRentalHistory != null) {
                btnRentalHistory.setOnClickListener(v -> openRentalHistory());
            }
            
            // Gestion du bouton de déconnexion
            findViewById(R.id.btn_logout).setOnClickListener(v -> showLogoutDialog());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'initialisation: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void loadUserProfile() {
        showProgress(true);
        
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        
        if (userId == -1) {
            Toast.makeText(this, "Erreur: Utilisateur non connecté", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        executor.execute(() -> {
            try {
                User user = database.userDao().getUserById(userId);
                
                if (user != null) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        displayUserProfile(user);
                        currentUser = user;
                        
                        // Charger les statistiques de location
                        loadRentalStatistics(userId);
                    });
                } else {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Erreur: Utilisateur non trouvé", Toast.LENGTH_LONG).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(this, "Erreur de chargement: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void displayUserProfile(User user) {
        if (tvName != null) {
            tvName.setText(user.getName());
        }
        if (tvEmail != null) {
            tvEmail.setText(user.getEmail());
        }
        // tvRegistrationDate pas encore implémenté dans le layout
        // if (tvRegistrationDate != null) {
        //     tvRegistrationDate.setText("Inscrit le: " + user.getRegistrationDate());
        // }
    }
    
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        
        EditText etCurrentPassword = dialogView.findViewById(R.id.et_current_password);
        EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        EditText etConfirmNewPassword = dialogView.findViewById(R.id.et_confirm_new_password);
        
        builder.setView(dialogView)
                .setTitle("Changer le mot de passe")
                .setPositiveButton("Changer", null)
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        
        AlertDialog dialog = builder.create();
        
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String currentPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmNewPassword = etConfirmNewPassword.getText().toString();
                
                if (validatePasswordChange(currentPassword, newPassword, confirmNewPassword)) {
                    changePassword(currentPassword, newPassword);
                    dialog.dismiss();
                }
            });
        });
        
        dialog.show();
    }
    
    private boolean validatePasswordChange(String currentPassword, String newPassword, String confirmNewPassword) {
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, "Veuillez entrer votre mot de passe actuel", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Veuillez entrer un nouveau mot de passe", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!PasswordUtils.isPasswordStrong(newPassword)) {
            Toast.makeText(this, "Le nouveau mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "Les nouveaux mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    private void changePassword(String currentPassword, String newPassword) {
        showProgress(true);
        
        executor.execute(() -> {
            try {
                // Vérifier le mot de passe actuel
                String currentPasswordHash = PasswordUtils.hashPassword(currentPassword);
                if (!currentPasswordHash.equals(currentUser.getPasswordHash())) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Mot de passe actuel incorrect", Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                
                // Mettre à jour le mot de passe
                String newPasswordHash = PasswordUtils.hashPassword(newPassword);
                currentUser.setPasswordHash(newPasswordHash);
                database.userDao().updateUser(currentUser);
                
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(this, "Mot de passe changé avec succès !", Toast.LENGTH_LONG).show();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(this, "Erreur lors du changement: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Déconnexion");
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?");
        builder.setIcon(R.drawable.ic_logout_modern);
        
        builder.setPositiveButton("Déconnexion", (dialog, which) -> {
            logout();
        });
        
        builder.setNegativeButton("Annuler", (dialog, which) -> {
            dialog.dismiss();
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        // Personnaliser les couleurs des boutons
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.accent_red));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.text_secondary));
    }
    
    private void logout() {
        // Effacer les données de session
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        // Rediriger vers l'écran de connexion
        Intent intent = new Intent(this, com.example.movieapp.ui.auth.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        
        Toast.makeText(this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show();
    }
    
    private void loadRentalStatistics(int userId) {
        executor.execute(() -> {
            try {
                // Compter les locations actives
                int activeRentals = database.rentalDao().getActiveRentalCount(userId);
                
                // Compter le total des locations
                int totalRentals = database.rentalDao().getTotalRentalCount(userId);
                
                runOnUiThread(() -> {
                    if (tvActiveRentals != null) {
                        tvActiveRentals.setText(String.valueOf(activeRentals));
                    }
                    if (tvTotalRentals != null) {
                        tvTotalRentals.setText(String.valueOf(totalRentals));
                    }
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erreur lors du chargement des statistiques: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void openRentalHistory() {
        // Ouvrir l'activité MyMoviesFragment via MainActivity
        Intent intent = new Intent(this, com.example.movieapp.ui.main.MainActivity.class);
        intent.putExtra("open_tab", "my_movies");
        startActivity(intent);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnChangePassword.setEnabled(!show);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
} 