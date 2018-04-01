package com.example.mylibrary;

public class Flashcard {
    private String sideA;
    private String sideB;
    private long id;
    private boolean isFavorite;
    private static long counter = 0;

    public Flashcard(String sideA, String sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.id = counter++;
    }

    public Flashcard(String sideA, String sideB, boolean isFavorite) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.isFavorite = isFavorite;
        this.id = counter++;
    }

    public String getSideA() {
        return sideA;
    }

    public void setSideA(String sideA) {
        this.sideA = sideA;
    }

    public String getSideB() {
        return sideB;
    }

    public void setSideB(String sideB) {
        this.sideB = sideB;
    }

    public long getId() {
        return id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
