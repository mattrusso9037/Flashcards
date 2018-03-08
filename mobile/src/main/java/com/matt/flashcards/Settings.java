package com.matt.flashcards;

import java.util.ArrayList;

public final class Settings {

    private Settings() {} // Prevent instantiation of this class

    public final static ArrayList<Deck> theDeckOfDecks = new ArrayList<>();

    public static void loadData() {
        // Load in dummy data

        Deck android = new Deck("Android Development");

        android.add(new Flashcard("MVP", "Minimum Viable Product"));
        android.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        android.add(new Flashcard("Herp", "Derp"));
        android.add(new Flashcard("MVP", "Minimum Viable Product"));
        android.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        android.add(new Flashcard("Herp", "Derp"));
        android.add(new Flashcard("MVP", "Minimum Viable Product"));
        android.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        android.add(new Flashcard("Herp", "Derp"));
        android.add(new Flashcard("MVP", "Minimum Viable Product"));
        android.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        android.add(new Flashcard("Herp", "Derp"));
        android.add(new Flashcard("MVP", "Minimum Viable Product"));
        android.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        android.add(new Flashcard("Last", "Derp"));

        theDeckOfDecks.add(android);
        theDeckOfDecks.add(new Deck("aslddlfnsljgnfgjdnfkgjndfgkjdngkfjdngkjn"));
        theDeckOfDecks.add(new Deck("asdlkmdf asmdlkmasldk faslkdaslmd asdasd asdassdf dfsdf "));
        theDeckOfDecks.add(new Deck("Derp"));
        theDeckOfDecks.add(new Deck("Herp"));
        theDeckOfDecks.add(new Deck("Derp"));
        theDeckOfDecks.add(new Deck("Herp"));
        theDeckOfDecks.add(new Deck("Derp"));
        theDeckOfDecks.add(new Deck("Herp"));
        theDeckOfDecks.add(new Deck("Derp"));
        theDeckOfDecks.add(new Deck("Herp"));
        theDeckOfDecks.add(new Deck("Derp"));
        theDeckOfDecks.add(new Deck("Herp"));
        theDeckOfDecks.add(new Deck("Derp"));
    }

    public static void saveData() {
        //
    }
}
