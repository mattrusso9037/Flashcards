package com.matt.flashcards;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylibrary.Flashcard;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

public class FlashcardDragItemAdapter extends DragItemAdapter<Flashcard, FlashcardDragItemAdapter.ViewHolder> {

    private List<Flashcard> flashcardList;

    public FlashcardDragItemAdapter(List<Flashcard> flashcardList) {
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
    }
}
