package com.example.movieapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapp.data.model.User;

@Dao
public interface UserDao {
    
    @Insert
    long insertUser(User user);
    
    @Update
    void updateUser(User user);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);
    
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);
    
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash LIMIT 1")
    User authenticateUser(String email, String passwordHash);
} 