package com.matt.flashcards;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.matt.flashcards.MainActivity.currentDeck;

public class SP_WearViewerActivity extends WearableActivity {

    private TextView cardText;
    private RelativeLayout cardLayout;
    private Button leftBtn;
    private Button rightBtn;
    private boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp__wear_viewer);

        cardText = (TextView) findViewById(R.id.cardText);
        cardLayout = findViewById(R.id.cardLayout);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);

        currentDeck.currentCardIndex = 0;

        cardText.setText(currentDeck.getCurrentCard().getSideA());

        Log.i("wear", "size viewer --- " + currentDeck.size());

        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wear", "index --- " + currentDeck.currentCardIndex);
                    cardText.setText(currentDeck.getCurrentCard().getSideB());
                    isFront = !isFront;

                if (!isFront) {
                    cardText.setText(currentDeck.getCurrentCard().getSideA());

                }

                }


        });


        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentDeck.getPrevCard() != null) {
                    currentDeck.currentCardIndex++;

                    cardText.setText(currentDeck.getPrevCard().getSideA());
                    Log.i("wear", "index --- " + currentDeck.currentCardIndex);

                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentDeck.getNextCard() != null) {
                    currentDeck.currentCardIndex--;
                    cardText.setText(currentDeck.getNextCard().getSideA());
                    Log.i("wear", "index --- " + currentDeck.currentCardIndex);

                }
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
}
