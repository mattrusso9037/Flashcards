package com.matt.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mylibrary.Deck;
import com.woxthebox.draglistview.DragItemAdapter;

import static com.matt.flashcards.Settings.theDeckOfDecks;

public class DeckDragItemAdapter extends DragItemAdapter<Deck, DeckDragItemAdapter.ViewHolder> {

    private Context context;

    public DeckDragItemAdapter(Context context) {
        this.context = context;
        setItemList(theDeckOfDecks);
    }

    @Override
    public long getUniqueItemId(int position) {
        return theDeckOfDecks.get(position).getId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item, parent, false),
                R.id.flashcard_item,
                true);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.deckTextView.setText(theDeckOfDecks.get(position).getTitle());

        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View inflater = LayoutInflater.from(context).inflate(R.layout.deck_dialog, null);
                final TextView dialogName = inflater.findViewById(R.id.deck_dialog_name);
                dialogName.setText(theDeckOfDecks.get(position).getTitle());
                new AlertDialog.Builder(context)
                        .setTitle(R.string.rename_deck)
                        .setView(inflater)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String deckTitle = dialogName.getText().toString();
                                if (deckTitle.isEmpty()) {
                                    new AlertDialog.Builder(context)
                                            .setTitle(R.string.error)
                                            .setMessage(R.string.error_decks_need_titles)
                                            .setPositiveButton(android.R.string.ok, null)
                                            .show();
                                } else {
                                    theDeckOfDecks.get(position).setTitle(dialogName.getText().toString());
                                    notifyDataSetChanged();
                                    Settings.saveData(context);
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.confirm_delete_deck)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                theDeckOfDecks.remove(position);
                                notifyDataSetChanged();
                                Settings.saveData(context);

                                // Show the deck tip when there are no decks
                                if (Settings.theDeckOfDecks.isEmpty()) {
                                    ((AppCompatActivity) context).findViewById(R.id.deck_tip)
                                            .setVisibility(View.VISIBLE);
                                }

                                SP_CategoryActivity.updateWear = true;
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {

        public TextView deckTextView;
        public ImageView editView;
        public ImageView deleteView;
        public LinearLayout editDeleteLayout;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            deckTextView = itemView.findViewById(R.id.flashcard_item_text);
            editView = itemView.findViewById(R.id.flashcard_item_edit);
            deleteView = itemView.findViewById(R.id.flashcard_item_delete);
            editDeleteLayout = itemView.findViewById(R.id.flashcard_item_actions);
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
            Deck.currentDeckIndex = getPositionForItemId(getItemId());
            context.startActivity(new Intent(context, FlashcardActivity.class));
        }
    }
}
