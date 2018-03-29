package com.matt.flashcards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

public class FlashcardFragmentPageAdapter extends FragmentStatePagerAdapter {

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

    /**
     * https://stackoverflow.com/a/27422029
     *
     * Needed to override this method and change this class to extend FragmentStatePagerAdapter
     * in order to update the viewpager properly when calling notifyDataSetChanged()
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return currentDeck.size();
    }
}
