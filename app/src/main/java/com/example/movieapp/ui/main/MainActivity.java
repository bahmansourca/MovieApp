package com.example.movieapp.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.movieapp.R;
import com.example.movieapp.ui.auth.LoginActivity;
import com.example.movieapp.ui.fragments.MovieListFragment;
import com.example.movieapp.ui.fragments.MyMoviesFragment;
import com.example.movieapp.ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigationView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Vérifier si l'utilisateur est connecté
        if (!isUserLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        initViews();
        setupNavigation();
        
        // Charger le fragment par défaut ou celui spécifié
        if (savedInstanceState == null) {
            String openTab = getIntent().getStringExtra("open_tab");
            if ("my_movies".equals(openTab)) {
                loadFragment(new MyMoviesFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_my_movies);
            } else {
                loadFragment(new MovieListFragment());
            }
        }
    }
    
    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
    
    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_movies) {
                fragment = new MovieListFragment();
            } else if (itemId == R.id.nav_my_movies) {
                fragment = new MyMoviesFragment();
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            
            return false;
        });
    }
    
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    
    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        return prefs.getInt("user_id", -1) != -1;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void logout() {
        // Effacer les données de session
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Retourner à l'écran de connexion
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 