package com.example.movieapp.ui.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.data.database.AppDatabase;
import com.example.movieapp.data.model.Movie;
import com.example.movieapp.data.model.Rating;
import com.example.movieapp.data.model.Rental;
import com.example.movieapp.utils.StarRatingHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieDetailActivity extends AppCompatActivity {
    
    private ImageView ivPoster;
    private TextView tvTitle, tvYear, tvGenre, tvLanguage, tvCountry, tvDirector, tvActors, tvDescription, tvAvailable;
    private TextView tvAverageRatingDetail, tvRatingCountDetail;
    private Button btnRent;
    private ProgressBar progressBar;
    private PlayerView playerView;
    private ExoPlayer player;
    private StarRatingHelper userRatingHelper;
    
    private AppDatabase database;
    private ExecutorService executor;
    private Movie currentMovie;
    private int currentUserId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        
        database = AppDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        
        // Récupérer l'ID de l'utilisateur connecté
        SharedPreferences prefs = getSharedPreferences("MovieAppPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);
        
        initViews();
        setupPlayer();
        
        // Récupérer l'ID du film depuis l'intent
        int movieId = getIntent().getIntExtra("movie_id", -1);
        if (movieId != -1) {
            loadMovieDetails(movieId);
        } else {
            Toast.makeText(this, "Erreur: Film non trouvé", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initViews() {
        ivPoster = findViewById(R.id.iv_poster);
        tvTitle = findViewById(R.id.tv_title);
        tvYear = findViewById(R.id.tv_year);
        tvGenre = findViewById(R.id.tv_genre);
        tvLanguage = findViewById(R.id.tv_language);
        tvCountry = findViewById(R.id.tv_country);
        tvDirector = findViewById(R.id.tv_director);
        tvActors = findViewById(R.id.tv_actors);
        tvDescription = findViewById(R.id.tv_description);
        tvAvailable = findViewById(R.id.tv_available);
        btnRent = findViewById(R.id.btn_rent);
        progressBar = findViewById(R.id.progress_bar);
        playerView = findViewById(R.id.player_view);
        tvAverageRatingDetail = findViewById(R.id.tv_average_rating_detail);
        tvRatingCountDetail = findViewById(R.id.tv_rating_count_detail);
        
        // Initialiser les étoiles de notation
        View userRatingView = findViewById(R.id.user_rating);
        userRatingHelper = new StarRatingHelper(userRatingView, true);
        userRatingHelper.setOnRatingChangeListener(this::onUserRatingChanged);
        
        // Gestion du bouton retour
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        btnRent.setOnClickListener(v -> attemptRent());
    }
    
    private void setupPlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
    }
    
    private void loadMovieDetails(int movieId) {
        showProgress(true);
        
        executor.execute(() -> {
            try {
                Movie movie = database.movieDao().getMovieById(movieId);
                
                if (movie != null) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        displayMovieDetails(movie);
                        currentMovie = movie;
                    });
                } else {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Film non trouvé", Toast.LENGTH_LONG).show();
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
    
    private void displayMovieDetails(Movie movie) {
        tvTitle.setText(movie.getTitle());
        tvYear.setText(String.valueOf(movie.getReleaseYear()));
        tvGenre.setText(movie.getGenre());
        tvLanguage.setText("Langue: " + movie.getOriginalLanguage());
        tvCountry.setText("Pays: " + movie.getCountry());
        tvDirector.setText("Réalisateur: " + movie.getDirector());
        tvActors.setText("Acteurs: " + movie.getActors());
        tvDescription.setText(movie.getDescription());
        tvAvailable.setText(movie.getAvailableCopies() + " copies disponibles");
        
        // Charger l'affiche - priorité aux images locales
        loadMoviePoster(movie);
        
        // Charger la bande-annonce - priorité aux vidéos locales
        loadMovieTrailer(movie);
        
        // Vérifier si l'utilisateur peut louer ce film
        checkRentalEligibility(movie);
        
        // Charger les évaluations
        loadRatings(movie.getId());
    }
    
    /**
     * Charge l'image du film en utilisant uniquement l'URL de l'affiche
     * @param movie Le film pour lequel charger l'image
     */
    private void loadMoviePoster(Movie movie) {
        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.placeholder_movie)
                    .error(R.drawable.placeholder_movie)
                    .timeout(10000)
                    .into(ivPoster);
        } else {
            ivPoster.setImageResource(R.drawable.placeholder_movie);
        }
    }

    /**
     * Charge la bande-annonce à partir de l'URL (YouTube ou autre)
     * @param movie Le film pour lequel charger la bande-annonce
     */
    private void loadMovieTrailer(Movie movie) {
        if (movie.getTrailerUrl() != null && !movie.getTrailerUrl().isEmpty()) {
            loadTrailer(movie.getTrailerUrl());
        }
    }
    

    private void loadTrailer(String trailerUrl) {
        try {
            // Pour les URLs YouTube, créer un player intégré
            if (trailerUrl.contains("youtube.com") || trailerUrl.contains("youtu.be")) {
                // Masquer l'ExoPlayer et créer un WebView YouTube
                if (playerView != null) {
                    playerView.setVisibility(View.GONE);
                }
                
                setupYouTubeWebView(trailerUrl);
            } else {
                // Pour d'autres URLs vidéo, essayer de les lire directement avec ExoPlayer
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(trailerUrl));
                player.setMediaItem(mediaItem);
                player.prepare();
            }
        } catch (Exception e) {
            // Ignorer les erreurs de chargement de vidéo
        }
    }
    
    private void setupYouTubeWebView(String youtubeUrl) {
        // Créer une WebView pour lire YouTube directement dans l'app
        WebView webView = findViewById(R.id.youtube_webview);
        if (webView != null) {
            webView.setVisibility(View.VISIBLE);
            
            // Configuration de la WebView
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false; // Laisser la WebView gérer l'URL
                }
            });
            
            // Convertir l'URL YouTube en URL embed
            String embedUrl = convertToEmbedUrl(youtubeUrl);
            if (embedUrl != null) {
                // Créer le HTML pour le player YouTube intégré
                String html = "<!DOCTYPE html><html><body style='margin:0;padding:0;background:#000;'>" +
                        "<iframe width='100%' height='100%' src='" + embedUrl + "' " +
                        "frameborder='0' allowfullscreen allow='autoplay; encrypted-media'>" +
                        "</iframe></body></html>";
                
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            }
        } else {
            // Fallback : bouton pour ouvrir YouTube externe
            setupYouTubeButton(youtubeUrl);
        }
    }
    
    private void setupYouTubeButton(String youtubeUrl) {
        // Bouton fallback pour ouvrir YouTube externe
        View playButton = findViewById(R.id.play_overlay);
        if (playButton != null) {
            playButton.setVisibility(View.VISIBLE);
            playButton.setOnClickListener(v -> openYouTube(youtubeUrl));
        }
    }
    
    private String convertToEmbedUrl(String youtubeUrl) {
        try {
            String videoId = null;
            
            // Extraire l'ID de la vidéo depuis différents formats d'URL YouTube
            if (youtubeUrl.contains("youtube.com/watch")) {
                String[] parts = youtubeUrl.split("v=");
                if (parts.length > 1) {
                    videoId = parts[1].split("&")[0];
                }
            } else if (youtubeUrl.contains("youtu.be/")) {
                String[] parts = youtubeUrl.split("youtu.be/");
                if (parts.length > 1) {
                    videoId = parts[1].split("\\?")[0];
                }
            }
            
            if (videoId != null) {
                return "https://www.youtube.com/embed/" + videoId + "?autoplay=1&modestbranding=1&showinfo=0&rel=0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void openYouTube(String youtubeUrl) {
        try {
            // Essayer d'ouvrir dans l'app YouTube d'abord
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            youtubeIntent.setPackage("com.google.android.youtube");
            startActivity(youtubeIntent);
        } catch (Exception e) {
            try {
                // Si YouTube n'est pas installé, ouvrir dans le navigateur
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
                startActivity(browserIntent);
            } catch (Exception ex) {
                Toast.makeText(this, "Impossible d'ouvrir la bande-annonce", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void checkRentalEligibility(Movie movie) {
        executor.execute(() -> {
            try {
                // Vérifier le nombre de locations actives
                int activeRentals = database.rentalDao().getActiveRentalCount(currentUserId);
                
                // Vérifier si l'utilisateur a déjà loué ce film
                int hasRented = database.rentalDao().checkIfUserHasActiveRental(currentUserId, movie.getId());
                
                runOnUiThread(() -> {
                    if (activeRentals >= 5) {
                        btnRent.setText("Limite de 5 films atteinte");
                        btnRent.setEnabled(false);
                    } else if (hasRented > 0) {
                        btnRent.setText("Déjà loué");
                        btnRent.setEnabled(false);
                    } else if (movie.getAvailableCopies() <= 0) {
                        btnRent.setText("Indisponible");
                        btnRent.setEnabled(false);
                    } else {
                        btnRent.setText("Louer");
                        btnRent.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnRent.setText("Erreur");
                    btnRent.setEnabled(false);
                });
            }
        });
    }
    
    private void attemptRent() {
        if (currentMovie == null) return;
        
        showProgress(true);
        
        executor.execute(() -> {
            try {
                // Vérifier à nouveau la disponibilité
                int availableCopies = database.movieDao().getAvailableCopies(currentMovie.getId());
                int activeRentals = database.rentalDao().getActiveRentalCount(currentUserId);
                int hasRented = database.rentalDao().checkIfUserHasActiveRental(currentUserId, currentMovie.getId());
                
                if (availableCopies <= 0) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Ce film n'est plus disponible", Toast.LENGTH_LONG).show();
                        checkRentalEligibility(currentMovie);
                    });
                    return;
                }
                
                if (activeRentals >= 5) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Vous avez atteint la limite de 5 films", Toast.LENGTH_LONG).show();
                        checkRentalEligibility(currentMovie);
                    });
                    return;
                }
                
                if (hasRented > 0) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Vous avez déjà loué ce film", Toast.LENGTH_LONG).show();
                        checkRentalEligibility(currentMovie);
                    });
                    return;
                }
                
                // Créer la location
                Rental rental = new Rental(currentUserId, currentMovie.getId());
                long rentalId = database.rentalDao().insertRental(rental);
                
                if (rentalId > 0) {
                    // Décrémenter le nombre de copies disponibles
                    database.movieDao().decrementAvailableCopies(currentMovie.getId());
                    
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Film loué avec succès !", Toast.LENGTH_LONG).show();
                        checkRentalEligibility(currentMovie);
                    });
                } else {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(this, "Erreur lors de la location", Toast.LENGTH_LONG).show();
                    });
                }
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRent.setEnabled(!show);
    }
    
    private void onUserRatingChanged(float rating) {
        if (currentMovie != null) {
            saveUserRating(currentMovie.getId(), rating);
        }
    }
    
    private void saveUserRating(int movieId, float rating) {
        executor.execute(() -> {
            try {
                // Vérifier si l'utilisateur a déjà noté ce film
                Rating existingRating = database.ratingDao().getRatingByUserAndMovie(currentUserId, movieId);
                
                if (existingRating != null) {
                    // Mettre à jour la note existante
                    existingRating.setRating(rating);
                    existingRating.setTimestamp(System.currentTimeMillis());
                    database.ratingDao().updateRating(existingRating);
                } else {
                    // Créer une nouvelle note
                    Rating newRating = new Rating(currentUserId, movieId, rating, null);
                    database.ratingDao().insertRating(newRating);
                }
                
                // Recharger les évaluations pour mettre à jour l'affichage
                runOnUiThread(() -> loadRatings(movieId));
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erreur lors de la sauvegarde de la note", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void loadRatings(int movieId) {
        executor.execute(() -> {
            try {
                // Charger la note de l'utilisateur
                Rating userRating = database.ratingDao().getRatingByUserAndMovie(currentUserId, movieId);
                
                // Charger la note moyenne et le nombre total
                Float averageRating = database.ratingDao().getAverageRatingForMovie(movieId);
                int ratingCount = database.ratingDao().getRatingCountForMovie(movieId);
                
                runOnUiThread(() -> {
                    // Mettre à jour l'affichage des étoiles de l'utilisateur
                    if (userRating != null) {
                        userRatingHelper.setRating(userRating.getRating());
                    } else {
                        userRatingHelper.setRating(0f);
                    }
                    
                    // Mettre à jour l'affichage de la note moyenne
                    if (averageRating != null && ratingCount > 0) {
                        tvAverageRatingDetail.setText(String.format("%.1f", averageRating));
                        tvRatingCountDetail.setText("(" + ratingCount + " vote" + (ratingCount > 1 ? "s" : "") + ")");
                    } else {
                        tvAverageRatingDetail.setText("0.0");
                        tvRatingCountDetail.setText("(0 votes)");
                    }
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erreur lors du chargement des notes", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
} 