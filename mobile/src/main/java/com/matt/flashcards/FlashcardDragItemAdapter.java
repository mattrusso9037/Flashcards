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

import com.example.mylibrary.Flashcard;
import com.woxthebox.draglistview.BoardView;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.flashcardTextView.setText(flashcardList.get(position).getSideA());

        if (context instanceof FlashcardDragListViewActivity) {

            holder.editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FlashcardDragListViewActivity) context).updateOnResume = true;
                    context.startActivity(
                            new Intent(context, AddEditActivity.class)
                            .putExtra("EditMode", true)
                            .putExtra("CardIndex", position));
                }
            });

            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.confirm_delete)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    flashcardList.remove(position);
                                    notifyDataSetChanged();
                                    ((FlashcardDragListViewActivity) context).changesMade = true;
                                }
                            }).setNegativeButton(R.string.cancel, null)
                            .show();
                }
            });
        } else if (context instanceof BoardActivity){
            final BoardView boardView = ((BoardActivity) context).boardView;

            holder.flashcardItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DebugToast(context, "Col: " + boardView.getFocusedColumn() + " | Row: " + position);
                }
            });
        }
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {

        public LinearLayout flashcardItem;
        public TextView flashcardTextView;
        public ImageView editView;
        public ImageView deleteView;
        public LinearLayout editDeleteLayout;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            flashcardItem = itemView.findViewById(R.id.flashcard_item);
            flashcardTextView = itemView.findViewById(R.id.flashcard_item_text);
            editView = itemView.findViewById(R.id.flashcard_item_edit);
            deleteView = itemView.findViewById(R.id.flashcard_item_delete);
            editDeleteLayout = itemView.findViewById(R.id.flashcard_item_actions);
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
            if (context instanceof FlashcardDragListViewActivity) {
                currentDeck.currentCardIndex = getPositionForItemId(getItemId());
                ((AppCompatActivity) context).onNavigateUp();
            }
        }
    }
}
