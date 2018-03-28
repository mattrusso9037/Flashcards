package com.matt.flashcards;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mylibrary.Deck;

public class FlashcardActivity extends AppCompatActivity {

    private boolean isFullscreen = false;
    private ViewPager viewPager;
    private FlashcardFragmentPageAdapter pageAdapter;
    protected static Deck currentDeck;

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
                toggleFullscreen();
                getSupportActionBar().hide();
                return true;
            case R.id.action_new_card:
            case R.id.action_edit_card:
            case R.id.action_delete_card:
            case R.id.action_list_view:
                new DebugToast(this, Integer.toString(viewPager.getCurrentItem()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * https://developer.android.com/samples/ImmersiveMode/project.html
     */
    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            getSupportActionBar().show();
            toggleFullscreen();
            isFullscreen = false;
        } else {
            super.onBackPressed();
        }
    }
}
