package com.matt.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SP_FlashcardViewerActivity extends AppCompatActivity {

    private boolean isFullscreen = false;
    private boolean isFront = true;
    protected static Deck currentDeck;
    private TextView mainTextView;
    private TextView fullscreenTextView;
    private View fullscreenView;

    @Override
    protected void onResume() {
        super.onResume();
        // After editing a flashcard, make sure it has the updated information
        if (currentDeck != null && !currentDeck.isEmpty()) {
            if (currentDeck.currentCardIndex == currentDeck.size()) {
                currentDeck.getPrevCard();
            }
            setTextViews(currentDeck.getCurrentCard().getSideA());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_viewer);

        currentDeck = Settings.theDeckOfDecks.get(Deck.currentDeckIndex);
        setTitle(currentDeck.getTitle());

        fullscreenView = findViewById(R.id.fullscreen_frame);
        mainTextView = findViewById(R.id.txt_sp_flashcard_viewer);
        fullscreenTextView = fullscreenView.findViewById(R.id.fullscreen_textview);

        // Check if the deck is empty
        if (currentDeck.isEmpty()) {
            setTextViews("There are no flashcards in this deck");
        } else {
            setTextViews(currentDeck.getCurrentCard().getSideA());
        }

        // Add listeners to flip a card
        findViewById(R.id.clt_sp_flashcard_viewer_top).setOnClickListener(flipCard);
        mainTextView.setOnClickListener(flipCard);
        fullscreenTextView.setOnClickListener(flipCard);
        fullscreenView.findViewById(R.id.fullscreen_flip).setOnClickListener(flipCard);

        // Add listeners to go to the next card
        findViewById(R.id.btn_sp_flashcard_viewer_next).setOnClickListener(nextCard);
        fullscreenView.findViewById(R.id.fullscreen_next).setOnClickListener(nextCard);

        // Add listeners to go to the previous card
        findViewById(R.id.btn_sp_flashcard_viewer_prev).setOnClickListener(prevCard);
        fullscreenView.findViewById(R.id.fullscreen_prev).setOnClickListener(prevCard);
    }

    // Function to set the text for both text views
    public void setTextViews(String text) {
        mainTextView.setText(text);
        fullscreenTextView.setText(text);
    }

    // Event for tapping on the card to flip it over
    private View.OnClickListener flipCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!currentDeck.isEmpty()) {
                Flashcard currentCard = currentDeck.getCurrentCard();
                isFront = !isFront;
                setTextViews(isFront ? currentCard.getSideA() : currentCard.getSideB());
            }
        }
    };

    // Event for the next button
    private View.OnClickListener nextCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!currentDeck.isEmpty()) {
                if (currentDeck.currentCardIndex < currentDeck.size() - 1) {
                    setTextViews(currentDeck.getNextCard().getSideA());
                    isFront = true;
                } else if (currentDeck.size() == 1) {
                    Toast.makeText(getBaseContext(), "This is the only card", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "This is the last card", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "There are no flashcards in this deck", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // Event for the previous button
    private View.OnClickListener prevCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!currentDeck.isEmpty()) {
                if (currentDeck.currentCardIndex > 0) {
                    setTextViews(currentDeck.getPrevCard().getSideA());
                    isFront = true;
                } else if (currentDeck.size() == 1) {
                    Toast.makeText(getBaseContext(), "This is the only card", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "This is the first card", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "There are no flashcards in this deck", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            fullscreenView.setVisibility(View.GONE);
            getSupportActionBar().show();
            toggleFullscreen();
        } else {
            onNavigateUp();
        }
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
                fullscreenView.setVisibility(View.VISIBLE);
                return true;
            case R.id.action_new_card:
                newFlashCard();
                return true;
            case R.id.action_edit_card:
                if (currentDeck.isEmpty()) {
                    newFlashCard();
                } else {
                    editFlashCard();
                }
                return true;
            case R.id.action_delete_card:
                if (!currentDeck.isEmpty()) {
                    deleteFlashCard();
                }
                return true;
            case R.id.action_list_view:
                startActivity(new Intent(this, FlashcardListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newFlashCard() {
        startActivity(new Intent(this, AddEditActivity.class)
                .putExtra("EditMode", false)
                .putExtra("DeckIndex", Deck.currentDeckIndex));
    }

    private void editFlashCard() {
        startActivity(new Intent(this, AddEditActivity.class)
                .putExtra("EditMode", true)
                .putExtra("DeckIndex", Deck.currentDeckIndex)
                .putExtra("CardIndex", currentDeck.currentCardIndex));
    }

    private void deleteFlashCard() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentDeck.size() == 1) {
                            currentDeck.remove(currentDeck.currentCardIndex);
                            setTextViews("There are no flashcards in this deck");
                        } else if (currentDeck.size() - 1 == currentDeck.currentCardIndex) {
                            currentDeck.remove(currentDeck.currentCardIndex--);
                            setTextViews(currentDeck.getCurrentCard().getSideA());
                        } else {
                            currentDeck.remove(currentDeck.currentCardIndex);
                            setTextViews(currentDeck.getCurrentCard().getSideA());
                        }
                        Settings.saveData(getBaseContext());
                    }
                }).setNegativeButton("Cancel", null)
                .create().show();
    }

    public static class FlashcardListActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_flashcard_list);
            setTitle(currentDeck.getTitle());
            ((ListView) findViewById(R.id.flashcards_listview)).setAdapter(new FlashcardAdapter(this, currentDeck));
        }

        // Adds menu to the app bar
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_flashcard_listview, menu);
            return true;
        }

        // Events for the menu
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_new_card_list_view:
                    startActivity(new Intent(this, AddEditActivity.class)
                            .putExtra("EditMode", false)
                            .putExtra("DeckIndex", Deck.currentDeckIndex));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
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
}
