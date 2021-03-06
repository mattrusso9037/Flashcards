package com.example.mylibrary;

import java.util.ArrayList;

public class Deck extends ArrayList<Flashcard> {

    private String title;
    public int currentCardIndex;
    public static int currentDeckIndex;
    private long id;
    private static long counter;

    public Deck(String title) {
        this.title = title;
        this.id = counter++;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Flashcard getNextCard() {
        if (currentCardIndex + 1 < size()) {
            return get(++this.currentCardIndex);
        }
        return null;
    }

    public Flashcard getPrevCard() {
        if (currentCardIndex > 0) {
            return get(--this.currentCardIndex);
        }
        return null;
    }

    public Flashcard getCurrentCard() {
        return get(currentCardIndex);
    }

    public long getId() {
        return id;
    }
}
