package com.example.movieapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapp.data.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    
    @Insert
    long insertMovie(Movie movie);
    
    @Update
    void updateMovie(Movie movie);
    
    @Query("SELECT * FROM movies")
    List<Movie> getAllMovies();
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    Movie getMovieById(int movieId);
    
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :searchQuery || '%' OR genre LIKE '%' || :searchQuery || '%' OR director LIKE '%' || :searchQuery || '%' OR actors LIKE '%' || :searchQuery || '%'")
    List<Movie> searchMovies(String searchQuery);
    
    @Query("SELECT * FROM movies WHERE genre = :genre")
    List<Movie> getMoviesByGenre(String genre);
    
    @Query("UPDATE movies SET availableCopies = availableCopies - 1 WHERE id = :movieId AND availableCopies > 0")
    int decrementAvailableCopies(int movieId);
    
    @Query("UPDATE movies SET availableCopies = availableCopies + 1 WHERE id = :movieId AND availableCopies < totalCopies")
    int incrementAvailableCopies(int movieId);
    
    @Query("SELECT availableCopies FROM movies WHERE id = :movieId")
    int getAvailableCopies(int movieId);
} 