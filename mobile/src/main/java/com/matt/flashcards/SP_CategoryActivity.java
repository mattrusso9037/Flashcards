package com.matt.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

public class SP_CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_category);

        findViewById(R.id.btn_mp_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SP_CategoryActivity.this, SP_FlashcardCreatorActivity.class));
            }
        });

        // Dummy Data
        ArrayList<Deck> dummyDecks = new ArrayList<>();
        dummyDecks.add(new Deck("Android Development"));
        dummyDecks.add(new Deck("aslddlfnsljgnfgjdnfkgjndfgkjdngkfjdngkjn"));
        dummyDecks.add(new Deck("asdlkmdf asmdlkmasldk faslkdaslmd asdasd asdassdf dfsdf "));
        dummyDecks.add(new Deck("Derp"));
        dummyDecks.add(new Deck("Herp"));
        dummyDecks.add(new Deck("Derp"));
        dummyDecks.add(new Deck("Herp"));
        dummyDecks.add(new Deck("Derp"));
        dummyDecks.add(new Deck("Herp"));
        dummyDecks.add(new Deck("Derp"));
        dummyDecks.add(new Deck("Herp"));
        dummyDecks.add(new Deck("Derp"));
        dummyDecks.add(new Deck("Herp"));
        dummyDecks.add(new Deck("Derp"));

        ((GridView) findViewById(R.id.grd_mp_category)).setAdapter(new DeckAdapter(this, dummyDecks));
    }
}
