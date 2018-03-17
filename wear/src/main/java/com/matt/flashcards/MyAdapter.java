package com.matt.flashcards;

import android.content.Context;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Matt on 3/17/2018.
 */

public class MyAdapter extends WearableRecyclerView.Adapter<MyAdapter.ViewHolder> {
    final private ListItemClickListener onClickListener;
    List deckList;

    public MyAdapter(List deckList, ListItemClickListener listener) {
        this.deckList = deckList;
        onClickListener = listener;
    }



    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int numberOfItems) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Deck deck = deckList.get(position);
//        holder.deckTitle.setText(deck.getTitle());
    }


    @Override
    public int getItemCount() {
        return deckList.size();
    }


    public class ViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {

        TextView deckTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            deckTitle = itemView.findViewById(R.id.deck_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }
    }
}
