package com.example.movieapp.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.data.database.AppDatabase;
import com.example.movieapp.data.model.Movie;
import com.example.movieapp.data.model.Rental;
import com.example.movieapp.ui.adapters.MyMoviesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyMoviesFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private MyMoviesAdapter adapter;
    private AppDatabase database;
    private ExecutorService executor;
    private int currentUserId;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_movies, container, false);
        
        database = AppDatabase.getInstance(requireContext());
        executor = Executors.newSingleThreadExecutor();
        
        // Récupérer l'ID de l'utilisateur connecté
        SharedPreferences prefs = requireContext().getSharedPreferences("MovieAppPrefs", 0);
        currentUserId = prefs.getInt("user_id", -1);
        
        initViews(view);
        setupRecyclerView();
        loadMyMovies();
        
        return view;
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
    }
    
    private void setupRecyclerView() {
        adapter = new MyMoviesAdapter(requireContext(), new ArrayList<>(), (rental, movie) -> {
            // Retourner le film
            returnMovie(rental, movie);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void loadMyMovies() {
        showProgress(true);
        
        executor.execute(() -> {
            try {
                List<Rental> rentals = database.rentalDao().getRentalsByUserId(currentUserId);
                List<MyMoviesAdapter.RentalWithMovie> rentalWithMovies = new ArrayList<>();
                
                for (Rental rental : rentals) {
                    Movie movie = database.movieDao().getMovieById(rental.getMovieId());
                    if (movie != null) {
                        rentalWithMovies.add(new MyMoviesAdapter.RentalWithMovie(rental, movie));
                    }
                }
                
                requireActivity().runOnUiThread(() -> {
                    showProgress(false);
                    adapter.updateRentals(rentalWithMovies);
                    updateEmptyState(rentalWithMovies.isEmpty());
                });
                
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(requireContext(), 
                        "Erreur de chargement: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void returnMovie(Rental rental, Movie movie) {
        if (!"ACTIVE".equals(rental.getStatus())) {
            Toast.makeText(requireContext(), "Ce film a déjà été retourné", Toast.LENGTH_SHORT).show();
            return;
        }
        
        executor.execute(() -> {
            try {
                // Mettre à jour la location
                String returnDate = java.time.LocalDate.now().toString();
                database.rentalDao().returnRental(rental.getId(), returnDate);
                
                // Incrémenter le nombre de copies disponibles
                database.movieDao().incrementAvailableCopies(movie.getId());
                
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), 
                        "Film retourné avec succès !", Toast.LENGTH_LONG).show();
                    
                    // Recharger la liste
                    loadMyMovies();
                });
                
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), 
                        "Erreur lors du retour: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recharger les données quand on revient sur ce fragment
        loadMyMovies();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
} 