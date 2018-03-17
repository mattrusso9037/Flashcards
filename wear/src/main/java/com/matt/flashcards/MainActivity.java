package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends WearableActivity implements ListItemClickListener{

    private WearableRecyclerView recyclerView;
    private MyAdapter adapter;
    private WearableLinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ArrayList list = new ArrayList();
        list.add("test2");
        list.add("test3");

        layoutManager = new WearableLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(list,this);
        recyclerView.setAdapter(adapter);

        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this,String.valueOf(clickedItemIndex), Toast.LENGTH_SHORT).show();
    }
}
