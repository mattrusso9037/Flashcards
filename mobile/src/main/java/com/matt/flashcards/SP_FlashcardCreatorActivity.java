package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

public class SP_FlashcardCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_creator);

        // Dummy data
        final Deck currentDeck = Settings.theDeckOfDecks.get(0);
        // ListView Variable
        final ListView lv = findViewById(R.id.flashcards_listview);
        // Adapter variable
        final FlashcardAdapter adapter = new FlashcardAdapter(this, currentDeck);

        // Set the dummy data to the adapter
        lv.setAdapter(adapter);

        // Event to create a new flashcard
        findViewById(R.id.fab_flashcard_creator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText A = findViewById(R.id.edit_flashcard_creator_side_a),
                         B = findViewById(R.id.edit_flashcard_creator_side_b);
                currentDeck.add(new Flashcard(A.getText().toString(), B.getText().toString()));
                adapter.notifyDataSetChanged();
                A.getText().clear();
                B.getText().clear();
            }
        });
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
        switch (item.getItemId()) {
            case R.id.action_save:
                new DebugToast(getBaseContext(), "Nothing actually saved");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
