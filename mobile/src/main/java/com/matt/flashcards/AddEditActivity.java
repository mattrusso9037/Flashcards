package com.matt.flashcards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

public class AddEditActivity extends AppCompatActivity {

    private Deck currentDeck;
    private Flashcard currentCard;
    private boolean editMode;
    private TextView sideA;
    private TextView sideB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Settings.loadData(this);

        // Add an Up button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        currentDeck = Settings.theDeckOfDecks.get(extras.getInt("DeckIndex"));
        editMode = extras.getBoolean("EditMode");

        // Get the textviews for both sides
        sideA = findViewById(R.id.add_edit_side_a);
        sideB = findViewById(R.id.add_edit_side_b);

        if (editMode) {
            currentCard = currentDeck.get(extras.getInt("CardIndex"));
            sideA.setText(currentCard.getSideA());
            sideB.setText(currentCard.getSideB());
            setTitle(R.string.edit_flashcard);
        } else {
            setTitle(R.string.add_flashcard);
        }
    }

    // This adds menu items to the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_creator, menu);
        return true;
    }

    // Event for the save button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String a = sideA.getText().toString(), b = sideB.getText().toString();
        switch (item.getItemId()) {
            case R.id.action_save:
                // Prevent blank flashcards from being created
                if (a.isEmpty() && b.isEmpty()){
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.error)
                            .setMessage(R.string.error_cant_save_blank_flashcard)
                            .setPositiveButton(R.string.ok, null)
                            .show();
                    return true;
                }

                if (editMode) { // Edit the current flashcard
                    currentCard.setSideA(a);
                    currentCard.setSideB(b);
                } else { // Add a new flashcard
                    currentDeck.add(new Flashcard(
                            sideA.getText().toString(),
                            sideB.getText().toString()
                    ));

                    // Set the current flashcard to the new flashcard
                    FlashcardActivity.currentDeck.currentCardIndex = currentDeck.size() - 1;
                }

                FlashcardActivity.updateOnResume();
                Settings.saveData(this);
                super.onBackPressed();
                return true;
            // Event for the Up button
            case android.R.id.home: // https://stackoverflow.com/a/8887556
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        String a = sideA.getText().toString(), b = sideB.getText().toString();
        if (editMode && currentCard.getSideA().equals(a) && currentCard.getSideB().equals(b) ||
                !editMode && a.isEmpty() && b.isEmpty()) {
            super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirm_lose_changes)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddEditActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }
}
