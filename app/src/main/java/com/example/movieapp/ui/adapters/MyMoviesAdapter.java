package com.example.movieapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.data.model.Movie;
import com.example.movieapp.data.model.Rental;

import java.util.List;

public class MyMoviesAdapter extends RecyclerView.Adapter<MyMoviesAdapter.MyMoviesViewHolder> {
    
    private Context context;
    private List<RentalWithMovie> rentals;
    private OnReturnClickListener listener;
    
    public static class RentalWithMovie {
        public Rental rental;
        public Movie movie;
        
        public RentalWithMovie(Rental rental, Movie movie) {
            this.rental = rental;
            this.movie = movie;
        }
    }
    
    public interface OnReturnClickListener {
        void onReturnClick(Rental rental, Movie movie);
    }
    
    public MyMoviesAdapter(Context context, List<RentalWithMovie> rentals, OnReturnClickListener listener) {
        this.context = context;
        this.rentals = rentals;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public MyMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_movie, parent, false);
        return new MyMoviesViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyMoviesViewHolder holder, int position) {
        RentalWithMovie rentalWithMovie = rentals.get(position);
        holder.bind(rentalWithMovie);
    }
    
    @Override
    public int getItemCount() {
        return rentals.size();
    }
    
    public void updateRentals(List<RentalWithMovie> newRentals) {
        this.rentals = newRentals;
        notifyDataSetChanged();
    }
    
    class MyMoviesViewHolder extends RecyclerView.ViewHolder {
        
        private CardView cardView;
        private ImageView ivPoster;
        private TextView tvTitle, tvRentalDate, tvStatus;
        private Button btnReturn;
        
        public MyMoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.card_view);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvRentalDate = itemView.findViewById(R.id.tv_rental_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnReturn = itemView.findViewById(R.id.btn_return);
        }
        
        public void bind(RentalWithMovie rentalWithMovie) {
            Rental rental = rentalWithMovie.rental;
            Movie movie = rentalWithMovie.movie;
            
            tvTitle.setText(movie.getTitle());
            tvRentalDate.setText("Loué le: " + rental.getRentalDate());
            
            // Charger l'image - priorité aux images locales
            loadMoviePoster(movie);
            
            // Gérer le statut et le bouton de retour
            if ("ACTIVE".equals(rental.getStatus())) {
                tvStatus.setText("En cours");
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                btnReturn.setVisibility(View.VISIBLE);
                btnReturn.setEnabled(true);
                
                btnReturn.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onReturnClick(rental, movie);
                    }
                });
            } else {
                tvStatus.setText("Retourné le: " + rental.getReturnDate());
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                btnReturn.setVisibility(View.GONE);
            }
        }
        
        /**
         * Charge l'image du film en utilisant uniquement l'URL de l'affiche
         * @param movie Le film pour lequel charger l'image
         */
        private void loadMoviePoster(Movie movie) {
            String posterUrl = movie.getPosterUrl();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                Glide.with(context)
                        .load(posterUrl)
                        .placeholder(R.drawable.placeholder_movie)
                        .error(R.drawable.placeholder_movie)
                        .timeout(10000)
                        .into(ivPoster);
            } else {
                ivPoster.setImageResource(R.drawable.placeholder_movie);
            }
        }
    }
} 