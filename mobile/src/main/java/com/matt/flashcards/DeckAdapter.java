package com.matt.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeckAdapter extends ArrayAdapter {

    private ArrayList<Deck> decks;

    public DeckAdapter(Context context, ArrayList<Deck> decks) {
        super(context, 0, decks);
        this.decks = decks;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.deck_item, parent, false);
        }

        final Deck currentDeck = (Deck) getItem(position);

        ((TextView) listItemView.findViewById(R.id.deck_title)).setText(currentDeck.getTitle());

        // Event for when a deck is clicked
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(
                        new Intent(getContext(), SP_FlashcardViewerActivity.class)
                                .putExtra("Index", position));
            }
        });

        // Event for when a deck is long clicked
        listItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                    .setTitle(decks.get(position).getTitle())
                    .setItems(new String[]{"Rename", "Delete"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    final View inflater = LayoutInflater.from(getContext())
                                            .inflate(R.layout.deck_dialog, null);
                                    final TextView dialogName = inflater.findViewById(R.id.deck_dialog_name);
                                    dialogName.setText(decks.get(position).getTitle());
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Rename deck:")
                                            .setView(inflater)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    decks.get(position).setTitle(dialogName.getText().toString());
                                                    notifyDataSetChanged();
                                                }
                                            }).setNegativeButton("Cancel", null)
                                            .create().show();
                                    break;
                                case 1:
                                    new AlertDialog.Builder(getContext())
                                        .setTitle("Are you sure you want to delete this deck?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                decks.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        }).setNegativeButton("Cancel", null)
                                        .create().show();
                            }
                        }
                    }).create().show();
                return true;
            }
        });

        return listItemView;
    }
}
