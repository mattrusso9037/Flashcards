package com.matt.flashcards;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mylibrary.Deck;

public class FlashcardActivity extends AppCompatActivity {

    protected static Deck currentDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        currentDeck = Settings.theDeckOfDecks.get(Deck.currentDeckIndex);
        setTitle(currentDeck.getTitle());

        // Set the adapter for the viewpager
        ((ViewPager) findViewById(R.id.viewpager)).setAdapter(
                new FlashcardFragmentPageAdapter(
                        getSupportFragmentManager(),
                        currentDeck));
    }
}
