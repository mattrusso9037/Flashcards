package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;

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
}
