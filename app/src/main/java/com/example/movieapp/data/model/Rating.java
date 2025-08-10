package com.example.movieapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "ratings",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"userId", "movieId"}, unique = true),
                @Index(value = "userId"),
                @Index(value = "movieId")
        })
public class Rating {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int userId;
    private int movieId;
    private float rating; // Note de 1.0 à 5.0
    private String comment; // Commentaire optionnel
    private long timestamp; // Date de l'évaluation
    
    public Rating() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Rating(int userId, int movieId, float rating, String comment) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getMovieId() {
        return movieId;
    }
    
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    
    public float getRating() {
        return rating;
    }
    
    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
} 