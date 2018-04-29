package com.matt.flashcards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mylibrary.Deck;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

public class DeckDragListViewActivity extends AppCompatActivity {

    private boolean changesMade;
    private boolean updateOnResume;
    private DragItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_drag_list_view);
        Settings.loadData(this);

        DragListView dragListView = findViewById(R.id.flashcard_drag_list_view);
        adapter = new DeckDragItemAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = dragListView.getRecyclerView();

        dragListView.setAdapter(adapter, true);
        dragListView.setLayoutManager(layoutManager);
        dragListView.setCanDragHorizontally(false);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation()));
        dragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {}

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {}

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    changesMade = true;
                }
            }
        });
    }

    // Save changes when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        if (changesMade) {
            Settings.saveData(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_listview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_card_list_view:
                final View inflater = getLayoutInflater().inflate(R.layout.deck_dialog, null);
                final TextView dialogName = inflater.findViewById(R.id.deck_dialog_name);
                new AlertDialog.Builder(DeckDragListViewActivity.this)
                        .setTitle(R.string.create_deck)
                        .setView(inflater)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String deckTitle = dialogName.getText().toString();
                                if (deckTitle.isEmpty()) {
                                    new AlertDialog.Builder(DeckDragListViewActivity.this)
                                            .setTitle(R.string.error)
                                            .setMessage(R.string.error_decks_need_titles)
                                            .setPositiveButton(android.R.string.ok, null)
                                            .show();
                                } else {
                                    Settings.theDeckOfDecks.add(new Deck(deckTitle));
                                    adapter.notifyDataSetChanged();
                                    Settings.saveData(DeckDragListViewActivity.this);

                                    // Hide the deck tip when decks are created
                                    DeckActivity.hideDeckTip = true;

                                    DeckActivity.updateWear = true;
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
