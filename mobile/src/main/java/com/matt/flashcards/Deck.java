package com.matt.flashcards;

import java.util.ArrayList;

public class Deck extends ArrayList {
    //extends Arraylist makes the actual deck class ana array list

    private String title;

    public Deck(String title) {

        this.title = title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
