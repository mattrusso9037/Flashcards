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

    protected static int cardIndex;
    protected static int deckIndex;
    private boolean isFront = true;
    private static Deck currentDeck;
    private TextView mainTextView;
    protected static boolean keepCard;

    @Override
    protected void onResume() {
        super.onResume();
        // After editing a flashcard, make sure it has the updated information
        if (currentDeck != null && !currentDeck.isEmpty()) {
            if (cardIndex == currentDeck.size()) {
                cardIndex--;
            }
            mainTextView.setText(currentDeck.get(cardIndex).getSideA());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_viewer);

        if (!keepCard) {
            cardIndex = 0;
        } else {
            keepCard = !keepCard;
        }

        final Deck finalCurrentDeck = Settings.theDeckOfDecks.get(deckIndex = getIntent().getIntExtra("Index", deckIndex));
        currentDeck = finalCurrentDeck;
        final TextView mainTextView = findViewById(R.id.txt_sp_flashcard_viewer);
        this.mainTextView = mainTextView;

        setTitle(finalCurrentDeck.getTitle());

        if (finalCurrentDeck.isEmpty()) {
            mainTextView.setText("There are no flashcards in this deck");
        } else {
            mainTextView.setText(finalCurrentDeck.get(cardIndex).getSideA());
        }

        // Event for tapping on the card to flip it over
        View.OnClickListener flipCard = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalCurrentDeck.isEmpty()) {
                    Flashcard currentCard = finalCurrentDeck.get(cardIndex);
                    mainTextView.setText(isFront ? currentCard.getSideB() : currentCard.getSideA());
                    isFront = !isFront;
                }
            }
        };
        findViewById(R.id.clt_sp_flashcard_viewer_top).setOnClickListener(flipCard);
        mainTextView.setOnClickListener(flipCard);

        // Event for the next button
        findViewById(R.id.btn_sp_flashcard_viewer_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalCurrentDeck.isEmpty()) {
                    if (cardIndex < finalCurrentDeck.size() - 1) {
                        mainTextView.setText(finalCurrentDeck.get(++cardIndex).getSideA());
                        isFront = true;
                    } else if (finalCurrentDeck.size() == 1) {
                        Toast.makeText(getBaseContext(), "This is the only card", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "This is the last card", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "There are no flashcards in this deck", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Event for the previous button
        findViewById(R.id.btn_sp_flashcard_viewer_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalCurrentDeck.isEmpty()) {
                    if (cardIndex > 0) {
                        mainTextView.setText(finalCurrentDeck.get(--cardIndex).getSideA());
                        isFront = true;
                    } else if (finalCurrentDeck.size() == 1) {
                        Toast.makeText(getBaseContext(), "This is the only card", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "This is the first card", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "There are no flashcards in this deck", Toast.LENGTH_SHORT).show();
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
                newFlashCard();
                break;
            case R.id.action_edit_card:
                if (currentDeck.isEmpty()) {
                    newFlashCard();
                } else {
                    editFlashCard();
                }
                break;
            case R.id.action_delete_card:
                if (!currentDeck.isEmpty()) {
                    deleteFlashCard();
                }
                break;
            case R.id.action_list_view:
                keepCard = true;
                startActivity(new Intent(this, FlashcardListActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void newFlashCard() {
        startActivity(new Intent(this, AddEditActivity.class)
                .putExtra("EditMode", false)
                .putExtra("DeckIndex", deckIndex));
    }

    private void editFlashCard() {
        startActivity(new Intent(this, AddEditActivity.class)
                .putExtra("EditMode", true)
                .putExtra("DeckIndex", deckIndex)
                .putExtra("CardIndex", cardIndex));
    }

    private void deleteFlashCard() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentDeck.size() == 1) {
                            currentDeck.remove(cardIndex);
                            mainTextView.setText("There are no flashcards in this deck");
                        } else if (currentDeck.size() - 1 == cardIndex) {
                            currentDeck.remove(cardIndex--);
                            mainTextView.setText(currentDeck.get(cardIndex).getSideA());
                        } else {
                            currentDeck.remove(cardIndex);
                            mainTextView.setText(currentDeck.get(cardIndex).getSideA());
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
    }
}
