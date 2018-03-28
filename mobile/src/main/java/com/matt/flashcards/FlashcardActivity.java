package com.matt.flashcards;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mylibrary.Deck;

public class FlashcardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FlashcardFragmentPageAdapter pageAdapter;
    protected static Deck currentDeck;
    protected static int cardIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        currentDeck = Settings.theDeckOfDecks.get(Deck.currentDeckIndex);
        setTitle(currentDeck.getTitle());

        // Get the viewpager
        this.viewPager = findViewById(R.id.viewpager);

        // Set the adapter for the viewpager
        this.pageAdapter = new FlashcardFragmentPageAdapter(getSupportFragmentManager(), currentDeck);
        viewPager.setAdapter(pageAdapter);
    }

    // This adds menu items to the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_viewer, menu);
        return true;
    }

    // Events for the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fullscreen:
            case R.id.action_new_card:
            case R.id.action_edit_card:
            case R.id.action_delete_card:
            case R.id.action_list_view:
                new DebugToast(this, Integer.toString(cardIndex));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
