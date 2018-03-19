package com.example.mylibrary;

public class Flashcard {
    private String sideA;
    private String sideB;

    public Flashcard(String sideA, String sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
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
}
