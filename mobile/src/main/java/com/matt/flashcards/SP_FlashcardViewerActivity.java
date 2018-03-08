package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SP_FlashcardViewerActivity extends AppCompatActivity {

    private static int index = 0;
    private static boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_viewer);

        final Deck currentDeck = Settings.theDeckOfDecks.get(getIntent().getIntExtra("Index", 0));
        final TextView mainTextView = findViewById(R.id.txt_sp_flashcard_viewer);

        mainTextView.setText(currentDeck.get(index).getSideA());

        // Event for tapping on the card to flip it over
        View.OnClickListener flipCard = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flashcard currentCard = currentDeck.get(index);
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
                if (index < currentDeck.size() - 1) {
                    mainTextView.setText(currentDeck.get(++index).getSideA());
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
                if (index > 0) {
                    mainTextView.setText(currentDeck.get(--index).getSideA());
                    isFront = true;
                } else {
                    Toast.makeText(v.getContext(), "This is the first card", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
