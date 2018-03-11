package com.matt.flashcards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class SP_CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_category);

        final DeckAdapter adapter = new DeckAdapter(this, Settings.theDeckOfDecks);
        ((GridView) findViewById(R.id.grd_mp_category)).setAdapter(adapter);

        findViewById(R.id.btn_mp_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final View inflater = getLayoutInflater().inflate(R.layout.deck_dialog, null);
                final TextView dialogName = inflater.findViewById(R.id.deck_dialog_name);
                new AlertDialog.Builder(SP_CategoryActivity.this)
                        .setTitle("Create deck:")
                        .setView(inflater)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.theDeckOfDecks.add(
                                        new Deck(dialogName.getText().toString()));
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", null)
                        .create().show();
            }
        });
    }
}
