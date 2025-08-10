package com.example.movieapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rentals")
public class Rental {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int movieId;
    private String rentalDate;
    private String returnDate;
    private String status; // "ACTIVE" or "RETURNED"

    public Rental() {}

    public Rental(int userId, int movieId) {
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = java.time.LocalDate.now().toString();
        this.status = "ACTIVE";
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

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 