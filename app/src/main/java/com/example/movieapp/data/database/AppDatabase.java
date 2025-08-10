package com.example.movieapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movieapp.data.dao.MovieDao;
import com.example.movieapp.data.dao.RatingDao;
import com.example.movieapp.data.dao.RentalDao;
import com.example.movieapp.data.dao.UserDao;
import com.example.movieapp.data.model.Movie;
import com.example.movieapp.data.model.Rating;
import com.example.movieapp.data.model.Rental;
import com.example.movieapp.data.model.User;

@Database(entities = {User.class, Movie.class, Rental.class, Rating.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "movie_app_db";
    private static AppDatabase instance;
    
    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract RentalDao rentalDao();
    public abstract RatingDao ratingDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
} 