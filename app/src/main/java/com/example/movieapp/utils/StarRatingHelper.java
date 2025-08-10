package com.example.movieapp.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;

public class StarRatingHelper {
    
    public interface OnRatingChangeListener {
        void onRatingChanged(float rating);
    }
    
    private ImageView[] stars;
    private TextView ratingCountText;
    private float currentRating = 0f;
    private boolean isInteractive = true;
    private OnRatingChangeListener listener;
    
    public StarRatingHelper(View rootView, boolean isInteractive) {
        this.isInteractive = isInteractive;
        initializeStars(rootView);
    }
    
    private void initializeStars(View rootView) {
        stars = new ImageView[5];
        stars[0] = rootView.findViewById(R.id.star1);
        stars[1] = rootView.findViewById(R.id.star2);
        stars[2] = rootView.findViewById(R.id.star3);
        stars[3] = rootView.findViewById(R.id.star4);
        stars[4] = rootView.findViewById(R.id.star5);
        ratingCountText = rootView.findViewById(R.id.tv_rating_count);
        
        if (isInteractive) {
            setupInteractiveStars();
        } else {
            // Désactiver les clics pour les étoiles non interactives
            for (ImageView star : stars) {
                if (star != null) {
                    star.setClickable(false);
                }
            }
        }
    }
    
    private void setupInteractiveStars() {
        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            if (stars[i] != null) {
                stars[i].setOnClickListener(v -> {
                    setRating(rating);
                    if (listener != null) {
                        listener.onRatingChanged(rating);
                    }
                });
            }
        }
    }
    
    public void setRating(float rating) {
        this.currentRating = rating;
        updateStarDisplay(rating);
    }
    
    private void updateStarDisplay(float rating) {
        for (int i = 0; i < stars.length; i++) {
            if (stars[i] != null) {
                if (i < Math.floor(rating)) {
                    // Étoile pleine
                    stars[i].setImageResource(R.drawable.ic_star_filled);
                } else if (i < rating && rating - i >= 0.5f) {
                    // Étoile demi-pleine
                    stars[i].setImageResource(R.drawable.ic_star_half);
                } else {
                    // Étoile vide
                    stars[i].setImageResource(R.drawable.ic_star_empty);
                }
            }
        }
    }
    
    public void setRatingCount(int count) {
        if (ratingCountText != null) {
            if (count > 0) {
                ratingCountText.setText("(" + count + ")");
                ratingCountText.setVisibility(View.VISIBLE);
            } else {
                ratingCountText.setVisibility(View.GONE);
            }
        }
    }
    
    public float getCurrentRating() {
        return currentRating;
    }
    
    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        this.listener = listener;
    }
    
    public void setEnabled(boolean enabled) {
        this.isInteractive = enabled;
        for (ImageView star : stars) {
            if (star != null) {
                star.setClickable(enabled);
            }
        }
    }
} 