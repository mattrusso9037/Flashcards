package com.matt.flashcards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mylibrary.Deck;

public class FlashcardFragmentPageAdapter extends FragmentPagerAdapter {

    private Deck currentDeck;

    public FlashcardFragmentPageAdapter(FragmentManager fm, Deck currentDeck) {
        super(fm);
        this.currentDeck = currentDeck;
    }

    @Override
    public Fragment getItem(int position) {
        FlashcardFragment flashcardFragment = new FlashcardFragment(currentDeck.get(position));

        //

        return flashcardFragment;
    }

    @Override
    public int getCount() {
        return currentDeck.size();
    }
}
