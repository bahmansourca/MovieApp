package com.example.movieapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapp.data.model.Rating;

import java.util.List;

@Dao
public interface RatingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRating(Rating rating);
    
    @Update
    void updateRating(Rating rating);
    
    @Delete
    void deleteRating(Rating rating);
    
    @Query("SELECT * FROM ratings WHERE userId = :userId AND movieId = :movieId")
    Rating getRatingByUserAndMovie(int userId, int movieId);
    
    @Query("SELECT * FROM ratings WHERE movieId = :movieId")
    List<Rating> getRatingsByMovie(int movieId);
    
    @Query("SELECT * FROM ratings WHERE userId = :userId")
    List<Rating> getRatingsByUser(int userId);
    
    @Query("SELECT AVG(rating) FROM ratings WHERE movieId = :movieId")
    Float getAverageRatingForMovie(int movieId);
    
    @Query("SELECT COUNT(*) FROM ratings WHERE movieId = :movieId")
    int getRatingCountForMovie(int movieId);
    
    @Query("SELECT * FROM ratings ORDER BY timestamp DESC")
    List<Rating> getAllRatings();
    
    @Query("SELECT * FROM ratings WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    List<Rating> getRecentRatingsByUser(int userId, int limit);
    
    @Query("DELETE FROM ratings WHERE userId = :userId AND movieId = :movieId")
    void deleteRatingByUserAndMovie(int userId, int movieId);
    
    @Query("SELECT * FROM ratings WHERE rating >= :minRating ORDER BY rating DESC")
    List<Rating> getHighRatings(float minRating);
    
    @Query("SELECT movieId, AVG(rating) as avgRating FROM ratings GROUP BY movieId HAVING COUNT(*) >= :minCount ORDER BY avgRating DESC")
    List<MovieRatingAverage> getTopRatedMovies(int minCount);
    
    // Classe pour les résultats d'agrégation
    public static class MovieRatingAverage {
        public int movieId;
        public float avgRating;
        
        public MovieRatingAverage() {}
        
        public MovieRatingAverage(int movieId, float avgRating) {
            this.movieId = movieId;
            this.avgRating = avgRating;
        }
    }
} 