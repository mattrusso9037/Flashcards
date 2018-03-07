package com.matt.flashcards;

import java.util.ArrayList;

public class Deck extends ArrayList<Flashcard> {

    private String title;

    public Deck(String title) {
        this.title = title;
    }
}
