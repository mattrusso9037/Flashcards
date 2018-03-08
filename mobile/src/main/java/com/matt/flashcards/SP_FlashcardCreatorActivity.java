package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class SP_FlashcardCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_creator);

        // Dummy data
        Deck dummyFlashcards = new Deck("Dummy Data");
        dummyFlashcards.add(new Flashcard("MVP", "Minimum Viable Product"));
        dummyFlashcards.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        dummyFlashcards.add(new Flashcard("Herp", "Derp"));
        dummyFlashcards.add(new Flashcard("MVP", "Minimum Viable Product"));
        dummyFlashcards.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        dummyFlashcards.add(new Flashcard("Herp", "Derp"));
        dummyFlashcards.add(new Flashcard("MVP", "Minimum Viable Product"));
        dummyFlashcards.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        dummyFlashcards.add(new Flashcard("Herp", "Derp"));
        dummyFlashcards.add(new Flashcard("MVP", "Minimum Viable Product"));
        dummyFlashcards.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        dummyFlashcards.add(new Flashcard("Herp", "Derp"));
        dummyFlashcards.add(new Flashcard("MVP", "Minimum Viable Product"));
        dummyFlashcards.add(new Flashcard("Lorem Ipsum", "dolor sit amet"));
        dummyFlashcards.add(new Flashcard("Herp", "Derp"));

        ((ListView) findViewById(R.id.flashcards_listview)).setAdapter(new FlashcardAdapter(this, dummyFlashcards));
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
                Toast.makeText(getBaseContext(), "Nothing actually saved", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
