package com.matt.flashcards;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary.Flashcard;

import static com.matt.flashcards.MainActivity.currentDeck;

public class SP_WearViewerActivity extends WearableActivity {

    private TextView cardText;
    private boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp__wear_viewer);

        cardText = findViewById(R.id.cardText);
        RelativeLayout cardLayout = findViewById(R.id.cardLayout);
        Button leftBtn = findViewById(R.id.leftBtn);
        Button rightBtn = findViewById(R.id.rightBtn);

        currentDeck.currentCardIndex = 0;

        cardText.setText(currentDeck.getCurrentCard().getSideA());

        Log.i("wear", "size viewer --- " + currentDeck.size());

        View.OnClickListener onFlip = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wear/flip", "index --- " + currentDeck.currentCardIndex);
                Flashcard currentCard = currentDeck.getCurrentCard();
                isFront = !isFront;
                cardText.setText(isFront ? currentCard.getSideA() : currentCard.getSideB());
            }
        };
        cardLayout.setOnClickListener(onFlip);
        cardText.setOnClickListener(onFlip);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDeck.getPrevCard() != null) {
                    cardText.setText(currentDeck.getCurrentCard().getSideA());
                    isFront = true;
                }
                Log.i("wear/prev", "index --- " + currentDeck.currentCardIndex);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDeck.getNextCard() != null) {
                    cardText.setText(currentDeck.getCurrentCard().getSideA());
                    isFront = true;
                }
                Log.i("wear/next", "index --- " + currentDeck.currentCardIndex);
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
}
