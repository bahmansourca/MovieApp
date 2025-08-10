package com.example.movieapp.api;

import com.example.movieapp.data.model.Movie;
import com.example.movieapp.data.model.Rental;
import com.example.movieapp.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    
    // User endpoints
    @POST("users/register")
    Call<User> registerUser(@Body User user);
    
    @POST("users/login")
    Call<User> loginUser(@Body User user);
    
    @PUT("users/{userId}/password")
    Call<User> updatePassword(@Path("userId") int userId, @Body User user);
    
    // Movie endpoints
    @GET("movies")
    Call<List<Movie>> getAllMovies();
    
    @GET("movies/{movieId}")
    Call<Movie> getMovieById(@Path("movieId") int movieId);
    
    @GET("movies/search")
    Call<List<Movie>> searchMovies(@Query("q") String searchQuery);
    
    @GET("movies/genre/{genre}")
    Call<List<Movie>> getMoviesByGenre(@Path("genre") String genre);
    
    // Rental endpoints
    @POST("rentals")
    Call<Rental> createRental(@Body Rental rental);
    
    @GET("rentals/user/{userId}")
    Call<List<Rental>> getUserRentals(@Path("userId") int userId);
    
    @PUT("rentals/{rentalId}/return")
    Call<Rental> returnRental(@Path("rentalId") int rentalId);
    
    @GET("rentals/user/{userId}/active")
    Call<List<Rental>> getActiveRentals(@Path("userId") int userId);
} 