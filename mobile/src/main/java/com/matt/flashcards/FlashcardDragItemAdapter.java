package com.matt.flashcards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mylibrary.Flashcard;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

import static com.matt.flashcards.FlashcardActivity.currentDeck;

public class FlashcardDragItemAdapter extends DragItemAdapter<Flashcard, FlashcardDragItemAdapter.ViewHolder> {

    private Context context;
    private List<Flashcard> flashcardList;
    private boolean showEditDeleteButtons;

    public FlashcardDragItemAdapter(Context context, List<Flashcard> flashcardList) {
        this.context = context;
        this.flashcardList = flashcardList;
        setItemList(flashcardList);
    }

    public FlashcardDragItemAdapter(Context context, List<Flashcard> flashcardList, boolean showEditDeleteButtons) {
        this.context = context;
        this.flashcardList = flashcardList;
        this.showEditDeleteButtons = showEditDeleteButtons;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.flashcardTextView.setText(flashcardList.get(position).getSideA());

        if (showEditDeleteButtons) {

            holder.editDeleteLayout.setVisibility(View.VISIBLE);

            holder.editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DebugToast(context, "Edit Clicked: " + position);
                }
            });

            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DebugToast(context, "Delete Clicked: " + position);
                }
            });
        }
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {

        public TextView flashcardTextView;
        public ImageView editView;
        public ImageView deleteView;
        public LinearLayout editDeleteLayout;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            flashcardTextView = itemView.findViewById(R.id.flashcard_item_text);
            editView = itemView.findViewById(R.id.flashcard_item_edit);
            deleteView = itemView.findViewById(R.id.flashcard_item_delete);
            editDeleteLayout = itemView.findViewById(R.id.flashcard_item_actions);
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
            currentDeck.currentCardIndex = getPositionForItemId(getItemId());
            ((AppCompatActivity) context).onNavigateUp();
        }
    }
}
