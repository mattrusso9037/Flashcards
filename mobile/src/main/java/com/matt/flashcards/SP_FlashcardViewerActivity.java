package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SP_FlashcardViewerActivity extends AppCompatActivity {

    protected static int cardIndex;
    protected static int deckIndex;
    protected static boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_viewer);
        cardIndex = 0;

        final Deck currentDeck = Settings.theDeckOfDecks.get(deckIndex = getIntent().getIntExtra("Index", 0));
        final TextView mainTextView = findViewById(R.id.txt_sp_flashcard_viewer);

        mainTextView.setText(currentDeck.get(cardIndex).getSideA());

        // Event for tapping on the card to flip it over
        View.OnClickListener flipCard = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flashcard currentCard = currentDeck.get(cardIndex);
                mainTextView.setText(isFront ? currentCard.getSideB() : currentCard.getSideA());
                isFront = !isFront;
            }
        };
        findViewById(R.id.clt_sp_flashcard_viewer_top).setOnClickListener(flipCard);
        mainTextView.setOnClickListener(flipCard);

        // Event for the next button
        findViewById(R.id.btn_sp_flashcard_viewer_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardIndex < currentDeck.size() - 1) {
                    mainTextView.setText(currentDeck.get(++cardIndex).getSideA());
                    isFront = true;
                } else {
                    Toast.makeText(v.getContext(), "This is the last card", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Event for the previous button
        findViewById(R.id.btn_sp_flashcard_viewer_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardIndex > 0) {
                    mainTextView.setText(currentDeck.get(--cardIndex).getSideA());
                    isFront = true;
                } else {
                    Toast.makeText(v.getContext(), "This is the first card", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                new DebugToast(getBaseContext(), "Fullscreen Mode");
                break;
            case R.id.action_new_card:
                new DebugToast(getBaseContext(), "New Flashcard");
                break;
            case R.id.action_edit_card:
                new DebugToast(getBaseContext(), "Edit Flashcard");
                break;
            case R.id.action_delete_card:
                new DebugToast(getBaseContext(), "Delete Flashcard");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
