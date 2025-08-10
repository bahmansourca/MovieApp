package com.example.movieapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.movieapp.R;
import com.example.movieapp.data.database.AppDatabase;
import com.example.movieapp.data.model.Movie;
import com.example.movieapp.ui.movie.MovieDetailActivity;
import com.example.movieapp.utils.StarRatingHelper;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    
    private List<Movie> movies;
    private Context context;
    private OnMovieClickListener listener;
    private AppDatabase database;
    private ExecutorService executor;
    
    public interface OnMovieClickListener {
        void onRentClick(Movie movie);
        void onTrailerClick(Movie movie);
    }
    
    public MovieAdapter(Context context, List<Movie> movies, OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newCachedThreadPool();
    }
    
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }
    
    @Override
    public int getItemCount() {
        return movies.size();
    }
    
    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }
    
    class MovieViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivMoviePoster;
        private TextView tvMovieTitle;
        private TextView tvMovieRating;
        private TextView tvMovieYear;
        private TextView tvMovieGenre;
        private TextView tvAverageRating;
        private MaterialButton btnRent;
        private MaterialButton btnTrailer;
        private LinearLayout playOverlay;
        private StarRatingHelper starRatingHelper;
        
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvMovieRating = itemView.findViewById(R.id.tv_movie_rating);
            tvMovieYear = itemView.findViewById(R.id.tv_movie_year);
            tvMovieGenre = itemView.findViewById(R.id.tv_movie_genre);
            tvAverageRating = itemView.findViewById(R.id.tv_average_rating);
            btnRent = itemView.findViewById(R.id.btn_rent);
            btnTrailer = itemView.findViewById(R.id.btn_trailer);
            playOverlay = itemView.findViewById(R.id.play_overlay);
            
            // Initialiser les étoiles de notation (non interactives dans la liste)
            View starRatingView = itemView.findViewById(R.id.star_rating);
            starRatingHelper = new StarRatingHelper(starRatingView, false);
            
            // Click sur la carte pour ouvrir les détails
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Movie movie = movies.get(position);
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("movie_id", movie.getId());
                    context.startActivity(intent);
                }
            });
        }
        
        public void bind(Movie movie) {
            // Titre du film
            tvMovieTitle.setText(movie.getTitle());
            
            // Note (sera gérée par les étoiles)
            tvMovieRating.setVisibility(View.GONE);
            
            // Année
            tvMovieYear.setText(String.valueOf(movie.getReleaseYear()));
            
            // Genre
            tvMovieGenre.setText(movie.getGenre());
            
            // Image du film - priorité aux images locales
            loadMovieImage(movie);
            
            // Gestion de l'overlay de lecture pour bande-annonce
            if (movie.getTrailerUrl() != null && !movie.getTrailerUrl().isEmpty()) {
                playOverlay.setVisibility(View.VISIBLE);
                ivMoviePoster.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onTrailerClick(movie);
                    }
                });
            } else {
                playOverlay.setVisibility(View.GONE);
            }
            
            // Bouton Louer
            btnRent.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRentClick(movie);
                }
            });
            
            // Bouton Bande-annonce
            btnTrailer.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTrailerClick(movie);
                }
            });
            
            // Masquer le bouton bande-annonce si pas de trailer
            if (movie.getTrailerUrl() == null || movie.getTrailerUrl().isEmpty()) {
                btnTrailer.setVisibility(View.GONE);
            } else {
                btnTrailer.setVisibility(View.VISIBLE);
            }
            
            // Charger les étoiles de notation
            loadMovieRating(movie.getId());
        }
        
        private void loadMovieRating(int movieId) {
            executor.execute(() -> {
                try {
                    // Charger la note moyenne et le nombre total
                    Float averageRating = database.ratingDao().getAverageRatingForMovie(movieId);
                    int ratingCount = database.ratingDao().getRatingCountForMovie(movieId);
                    
                    // Mettre à jour l'affichage sur le thread UI
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).runOnUiThread(() -> {
                            if (averageRating != null && ratingCount > 0) {
                                starRatingHelper.setRating(averageRating);
                                starRatingHelper.setRatingCount(ratingCount);
                                tvAverageRating.setText(String.format("%.1f", averageRating));
                            } else {
                                starRatingHelper.setRating(0f);
                                starRatingHelper.setRatingCount(0);
                                tvAverageRating.setText("0.0");
                            }
                        });
                    }
                    
                } catch (Exception e) {
                    // Ignorer les erreurs silencieusement pour ne pas affecter l'affichage
                }
            });
        }
        
        /**
         * Charge l'image du film en utilisant uniquement l'URL de l'affiche
         * @param movie Le film pour lequel charger l'image
         */
        private void loadMovieImage(Movie movie) {
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new RoundedCorners(16))
                    .placeholder(R.drawable.placeholder_movie)
                    .error(R.drawable.placeholder_movie)
                    .fallback(R.drawable.placeholder_movie)
                    .timeout(10000);

            String posterUrl = movie.getPosterUrl();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                Glide.with(context)
                        .load(posterUrl)
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (e != null) {
                                    e.logRootCauses("MovieAdapter");
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(ivMoviePoster);
            } else {
                ivMoviePoster.setImageResource(R.drawable.placeholder_movie);
            }
        }
    }
} 