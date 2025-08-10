package com.example.movieapp.data.model;

public class Category {
    private String name;
    private String genre;
    private boolean isSelected;

    public Category(String name, String genre) {
        this.name = name;
        this.genre = genre;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
} 