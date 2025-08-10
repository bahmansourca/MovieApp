package com.example.movieapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapp.data.model.Rental;

import java.util.List;

@Dao
public interface RentalDao {
    
    @Insert
    long insertRental(Rental rental);
    
    @Update
    void updateRental(Rental rental);
    
    @Query("SELECT * FROM rentals WHERE userId = :userId")
    List<Rental> getRentalsByUserId(int userId);
    
    @Query("SELECT * FROM rentals WHERE userId = :userId AND status = 'ACTIVE'")
    List<Rental> getActiveRentalsByUserId(int userId);
    
    @Query("SELECT COUNT(*) FROM rentals WHERE userId = :userId AND status = 'ACTIVE'")
    int getActiveRentalCount(int userId);
    
    @Query("SELECT COUNT(*) FROM rentals WHERE userId = :userId")
    int getTotalRentalCount(int userId);
    
    @Query("SELECT COUNT(*) FROM rentals WHERE userId = :userId AND movieId = :movieId AND status = 'ACTIVE'")
    int checkIfUserHasActiveRental(int userId, int movieId);
    
    @Query("UPDATE rentals SET returnDate = :returnDate, status = 'RETURNED' WHERE id = :rentalId")
    void returnRental(int rentalId, String returnDate);
    
    @Query("SELECT * FROM rentals WHERE id = :rentalId")
    Rental getRentalById(int rentalId);
} 