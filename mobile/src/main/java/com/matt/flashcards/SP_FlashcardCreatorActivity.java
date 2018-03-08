package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SP_FlashcardCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_flashcard_creator);

        // Dummy data
        final Deck dummyFlashcards = new Deck("Dummy Data");
        //ListView Variable
        final ListView lv = ((ListView) findViewById(R.id.flashcards_listview));
        //Adapter variable
        final FlashcardAdapter adapter = new FlashcardAdapter(this, dummyFlashcards);


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

        //set the dummy data to the adapter
        lv.setAdapter(adapter);

        //Event to add another flashcard to the adapter
        findViewById(R.id.fab_flashcard_creator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sideA = null;
                String sideB = null;
                EditText A = findViewById(R.id.edit_flashcard_creator_side_a);
                EditText B = findViewById(R.id.edit_flashcard_creator_side_b);
                sideA = A.getText().toString();
                sideB = B.getText().toString();
                dummyFlashcards.add(new Flashcard(sideA,sideB));
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
                Toast.makeText(getBaseContext(), "Nothing actually saved", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
