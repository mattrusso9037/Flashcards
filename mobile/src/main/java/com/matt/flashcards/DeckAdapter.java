package com.matt.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mylibrary.Deck;

import java.util.ArrayList;

public class DeckAdapter extends ArrayAdapter {

    private ArrayList<Deck> decks;
    private MenuItem item;

    public DeckAdapter(Context context, ArrayList<Deck> decks, MenuItem item) {
        super(context, 0, decks);
        this.decks = decks;
        this.item = item;
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
                Deck.currentDeckIndex = position;
//                getContext().startActivity(new Intent(getContext(), SP_FlashcardViewerActivity.class));
                getContext().startActivity(new Intent(getContext(), FlashcardActivity.class));
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
                                            .setTitle(R.string.rename_deck)
                                            .setView(inflater)
                                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String deckTitle = dialogName.getText().toString();
                                                    if (deckTitle.isEmpty()) {
                                                        new AlertDialog.Builder(getContext())
                                                                .setTitle(R.string.error)
                                                                .setMessage(R.string.error_decks_need_titles)
                                                                .setPositiveButton(R.string.ok, null)
                                                                .show();
                                                    } else {
                                                        decks.get(position).setTitle(dialogName.getText().toString());
                                                        notifyDataSetChanged();
                                                        Settings.saveData(getContext());
                                                    }
                                                }
                                            }).setNegativeButton(R.string.cancel, null)
                                            .show();
                                    break;
                                case 1:
                                    new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.confirm_delete_deck)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                decks.remove(position);
                                                notifyDataSetChanged();
                                                Settings.saveData(getContext());

                                                // Show the deck tip when there are no decks
                                                if (Settings.theDeckOfDecks.isEmpty()) {
                                                    SP_CategoryActivity.deckTip.setVisibility(View.VISIBLE);
                                                }

                                                WearTask wearTask = new WearTask(getContext(), item);
                                                wearTask.execute();
                                            }
                                        }).setNegativeButton(R.string.cancel, null)
                                        .show();
                            }
                        }
                    }).show();
                return true;
            }
        });

        return listItemView;
    }
}
