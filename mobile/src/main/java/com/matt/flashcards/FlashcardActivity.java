package com.matt.flashcards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
        viewPager = findViewById(R.id.viewpager);

        // Updates the Decks current card index whenever the page is changed
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentDeck.currentCardIndex = position;
                new DebugToast(FlashcardActivity.this, position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Set the adapter for the viewpager
        pageAdapter = new FlashcardFragmentPageAdapter(getSupportFragmentManager(), currentDeck);
        viewPager.setAdapter(pageAdapter);
    }

    // This adds menu items to the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_viewer, menu);
        return super.onCreateOptionsMenu(menu);
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
                deleteFlashCard();
                return true;
            case R.id.action_list_view:
                new DebugToast(this, Integer.toString(currentDeck.currentCardIndex));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteFlashCard() {
        if (currentDeck.size() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to delete this?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentDeck.remove(viewPager.getCurrentItem());
                            pageAdapter.notifyDataSetChanged();
                            Settings.saveData(FlashcardActivity.this);
                        }
                    }).setNegativeButton("Cancel", null)
                    .create().show();
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
