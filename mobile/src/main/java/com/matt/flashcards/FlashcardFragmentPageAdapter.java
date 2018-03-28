package com.matt.flashcards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

public class FlashcardFragmentPageAdapter extends FragmentPagerAdapter {

    private Deck currentDeck;

    public FlashcardFragmentPageAdapter(FragmentManager fm, Deck currentDeck) {
        super(fm);
        this.currentDeck = currentDeck;
    }

    @Override
    public Fragment getItem(int position) {
        Flashcard currentFlashcard = currentDeck.get(position);
        FlashcardFragment flashcardFragment = new FlashcardFragment();

        Bundle args = new Bundle();
        args.putString("Front", currentFlashcard.getSideA());
        args.putString("Back", currentFlashcard.getSideB());
        flashcardFragment.setArguments(args);

        return flashcardFragment;
    }

    @Override
    public int getCount() {
        return currentDeck.size();
    }
}
