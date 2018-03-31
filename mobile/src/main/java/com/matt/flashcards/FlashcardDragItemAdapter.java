package com.matt.flashcards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylibrary.Flashcard;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

import static com.matt.flashcards.FlashcardActivity.currentDeck;

public class FlashcardDragItemAdapter extends DragItemAdapter<Flashcard, FlashcardDragItemAdapter.ViewHolder> {

    private Context context;
    private List<Flashcard> flashcardList;

    public FlashcardDragItemAdapter(Context context, List<Flashcard> flashcardList) {
        this.context = context;
        this.flashcardList = flashcardList;
        setItemList(flashcardList);
    }

    @Override
    public long getUniqueItemId(int position) {
        return flashcardList.get(position).getId();
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
        holder.flashcardTextView.setText(flashcardList.get(position).getSideA());
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {

        public TextView flashcardTextView;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            flashcardTextView = itemView.findViewById(R.id.flashcard_item_text);
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
            currentDeck.currentCardIndex = getPositionForItemId(getItemId());
            ((AppCompatActivity) context).onNavigateUp();
        }
    }
}
