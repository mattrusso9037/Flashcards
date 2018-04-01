package com.matt.flashcards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.deckTextView.setText(theDeckOfDecks.get(position).getTitle());
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {

        public TextView deckTextView;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            deckTextView = itemView.findViewById(R.id.flashcard_item_text);
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
//            currentDeck.currentCardIndex = getPositionForItemId(getItemId());
//            ((AppCompatActivity) context).onNavigateUp();
        }
    }
}
