package com.matt.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mylibrary.Flashcard;
import com.woxthebox.draglistview.BoardView;

import static com.matt.flashcards.Settings.theDeckOfDecks;

public class BoardActivity extends AppCompatActivity {

    protected BoardView boardView;
    protected static boolean changesMade;
    protected static Bundle addEditBundle;

    @Override
    protected void onResume() {
        super.onResume();
        if (addEditBundle != null) {
            int editCol = addEditBundle.getInt("editCol");
            int editRow;
            String sideA = addEditBundle.getString("sideA");
            String sideB = addEditBundle.getString("sideB");

            if (addEditBundle.getBoolean("editMode")) {
                editRow = addEditBundle.getInt("editRow");
                Flashcard f = (Flashcard) boardView.getAdapter(editCol).getItemList().get(editRow);
                f.setSideA(sideA);
                f.setSideB(sideB);
            } else {
                editRow = boardView.getItemCount(editCol);
                boardView.addItem(editCol, editRow, new Flashcard(sideA, sideB), false);
            }

            boardView.getAdapter(editCol).notifyDataSetChanged();
            boardView.scrollToItem(editCol, editRow, true);
            changesMade = true;
            addEditBundle = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Settings.loadData(this);

        boardView = findViewById(R.id.board_view);
        boardView.setSnapToColumnsWhenScrolling(true);
        boardView.setSnapToColumnWhenDragging(true);
        boardView.setSnapDragItemToTouch(true);
        boardView.setSnapToColumnInLandscape(false);
        boardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        boardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {}

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    changesMade = true;
                }
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {}

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {}

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {
                boardView.getHeaderView(oldColumn).findViewById(R.id.column_add).setVisibility(View.INVISIBLE);
                boardView.getHeaderView(newColumn).findViewById(R.id.column_add).setVisibility(View.VISIBLE);
            }
        });

        FlashcardDragItemAdapter adapter;
        View header;
        RecyclerView recyclerView;

        for (int i = 0; i < theDeckOfDecks.size(); i++) {
            adapter = new FlashcardDragItemAdapter(this, theDeckOfDecks.get(i));
            header = View.inflate(this, R.layout.column_header, null);

            ((TextView) header.findViewById(R.id.column_text)).setText(theDeckOfDecks.get(i).getTitle());

            header.findViewById(R.id.column_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEditBundle = new Bundle();
                    addEditBundle.putBoolean("editMode", false);
                    addEditBundle.putInt("editCol", boardView.getFocusedColumn());
                    BoardActivity.this.startActivity(new Intent(BoardActivity.this, AddEditActivity.class)
                            .putExtra("EditMode", false)
                            .putExtra("BoardMode", true));
                }
            });

            boardView.addColumnList(adapter, header, false);
            recyclerView = boardView.getRecyclerView(i);
            recyclerView.setVerticalScrollBarEnabled(true);
            recyclerView.setScrollbarFadingEnabled(false);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(this,
                            ((LinearLayoutManager)
                                    recyclerView.getLayoutManager()).getOrientation()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (changesMade) {
            Settings.saveData(this);
            changesMade = false;
        }
    }
}
