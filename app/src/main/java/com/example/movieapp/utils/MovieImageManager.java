package com.example.movieapp.utils;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class MovieImageManager {
    
    private static final Map<String, Integer> movieImages = new HashMap<>();
    private static final Map<String, String> movieVideos = new HashMap<>();
    
    static {
        // ✅ Aucune image locale associée afin d'utiliser les liens distants (YouTube, URLs) comme les autres films
        // TODO: Si vous souhaitez réactiver une image locale pour un film, ajoutez-la ici
        
        // 🎬 Aucune vidéo locale associée afin d'utiliser les liens YouTube/streams distants
        // TODO: Si vous souhaitez réactiver une vidéo locale pour un film, ajoutez-la ici
        
        // TODO: Ajoutez d'autres vidéos quand vous en mettez plus :
        /*


        */
        
        // TODO: Ajoutez d'autres films quand vous mettez plus d'images :
        /*
        movieImages.put("Spider-Man: Across the Spider-Verse", R.drawable.spiderman_across);
        movieImages.put("Guardians of the Galaxy Vol. 3", R.drawable.guardians_vol3);
        movieImages.put("John Wick: Chapter 4", R.drawable.john_wick_4);
        movieImages.put("Avatar: The Way of Water", R.drawable.avatar_2);
        movieImages.put("Top Gun: Maverick", R.drawable.top_gun_maverick);
        movieImages.put("The Batman", R.drawable.the_batman);
        movieImages.put("Scream VI", R.drawable.scream_6);
        movieImages.put("Fast X", R.drawable.fast_x);
        movieImages.put("The Super Mario Bros. Movie", R.drawable.mario_bros);
        movieImages.put("Transformers: Rise of the Beasts", R.drawable.transformers_beasts);
        movieImages.put("Indiana Jones and the Dial of Destiny", R.drawable.indiana_jones_5);
        movieImages.put("M3GAN", R.drawable.megan);
        movieImages.put("Ant-Man and the Wasp: Quantumania", R.drawable.antman_3);
        movieImages.put("Cocaine Bear", R.drawable.cocaine_bear);
        movieImages.put("Evil Dead Rise", R.drawable.evil_dead_rise);
        movieImages.put("The Little Mermaid", R.drawable.little_mermaid);
        movieImages.put("Killers of the Flower Moon", R.drawable.killers_flower_moon);
        */
    }
    
    /**
     * Obtient l'ID de ressource pour un titre de film
     * @param movieTitle Le titre du film
     * @return L'ID de la ressource drawable ou 0 si non trouvé
     */
    public static int getMovieImageResource(String movieTitle) {
        Integer resourceId = movieImages.get(movieTitle);
        return resourceId != null ? resourceId : 0;
    }
    
    /**
     * Vérifie si une image locale existe pour ce film
     * @param movieTitle Le titre du film
     * @return true si l'image existe localement
     */
    public static boolean hasLocalImage(String movieTitle) {
        return movieImages.containsKey(movieTitle);
    }
    
    /**
     * Ajoute une nouvelle association film -> image
     * @param movieTitle Le titre du film
     * @param resourceId L'ID de la ressource drawable
     */
    public static void addMovieImage(String movieTitle, int resourceId) {
        movieImages.put(movieTitle, resourceId);
    }
    
    /**
     * Obtient le nom du fichier vidéo pour un titre de film
     * @param movieTitle Le titre du film
     * @return Le nom du fichier vidéo ou null si non trouvé
     */
    public static String getMovieVideoFile(String movieTitle) {
        return movieVideos.get(movieTitle);
    }
    
    /**
     * Vérifie si une vidéo locale existe pour ce film
     * @param movieTitle Le titre du film
     * @return true si la vidéo existe localement
     */
    public static boolean hasLocalVideo(String movieTitle) {
        return movieVideos.containsKey(movieTitle);
    }
    
    /**
     * Vérifie si un fichier vidéo existe dans les assets
     * @param context Le contexte Android
     * @param filename Le nom du fichier à vérifier
     * @return true si le fichier existe
     */
    public static boolean videoFileExists(Context context, String filename) {
        try {
            String[] files = context.getAssets().list("");
            if (files != null) {
                for (String file : files) {
                    if (file.equals(filename)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Obtient l'URI pour une vidéo locale dans assets
     * @param filename Le nom du fichier vidéo
     * @return L'URI de la vidéo
     */
    public static String getLocalVideoUri(String filename) {
        return "asset:///" + filename;
    }
    
    /**
     * Ajoute une nouvelle association film -> vidéo
     * @param movieTitle Le titre du film
     * @param filename Le nom du fichier vidéo
     */
    public static void addMovieVideo(String movieTitle, String filename) {
        movieVideos.put(movieTitle, filename);
    }
} 