package com.matt.flashcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;


public class MainActivity extends WearableActivity implements ListItemClickListener, MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private WearableRecyclerView recyclerView;
    private MyAdapter adapter;
    private WearableLinearLayoutManager layoutManager;
    private GoogleApiClient googleClient;
    private TextView defaultText;
    private ArrayList titleList = new ArrayList();
    private ArrayList<Deck> deckList;
    protected static Deck currentDeck;

    final String TRANSFER_PATH = "/dataTransferToWear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        defaultText = findViewById(R.id.defaultText);

        recyclerView.setHasFixedSize(true);

        layoutManager = new WearableLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(titleList, this);
        recyclerView.setAdapter(adapter);
        deckList = new ArrayList<>();

        // Enables Always-on
        setAmbientEnabled();
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        googleClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
        googleClient.connect();
        Wearable.MessageApi.addListener(googleClient, this);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(this, SP_WearViewerActivity.class);
        currentDeck = deckList.get(clickedItemIndex);
        Toast toast = Toast.makeText(this, String.valueOf("empty"), Toast.LENGTH_SHORT);

        if (!currentDeck.isEmpty()) {
            startActivity(intent);
            toast.cancel();
        }
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initGoogleApiClient();

    }


    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(googleClient, this);
        Log.i("wear", "connected");

    }

    @Override
    protected void onStop() {
        if (googleClient != null) {
            Wearable.MessageApi.removeListener(googleClient, this);
            if (googleClient.isConnected()) {
                googleClient.disconnect();
                Log.i("wear", "disconnected");

            }
        }
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("wear", "suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("wear", "failed");
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageEvent.getPath().equals(TRANSFER_PATH)) {
                    Log.i("wear", String.valueOf(adapter.getItemCount()));

                    adapter.clear();
                    Log.i("wear", String.valueOf(adapter.getItemCount()));
                    DataMap dataMap = DataMap.fromByteArray(messageEvent.getData());
                    ArrayList<DataMap> dataMapList = new ArrayList<>();

                    getTitles(dataMapList, dataMap);
                    makeDecks(dataMapList);
                    makeFlashCards(dataMapList);
                    adapter.notifyDataSetChanged();
                    dataMapList.clear();
                    if (!deckList.isEmpty()) {
                        defaultText.setVisibility(View.INVISIBLE);
                    }
                    Log.i("wear", "received");

                }
            }
        });
    }

    private void getTitles(ArrayList<DataMap> dataMapList, DataMap dataMap) {
        titleList.clear();
        int size = dataMap.getDataMapArrayList("deck title key").size();
        //get titles and cards
        for (int i = 0; i < size; i++) {
            DataMap incomingTitle;
            incomingTitle = dataMap.getDataMapArrayList("deck title key").get(i);
            dataMapList.add(incomingTitle);
        }
    }

    private void makeDecks(ArrayList<DataMap> dataMapList) {
        deckList.clear();
        for (int i = 0; i < dataMapList.size(); i++) {
            String title = dataMapList.get(i).getString("deck title");
            if (title != null) {
                Deck deck = new Deck(title);
                deckList.add(deck);
                titleList.add(title);
            }

        }
    }

    private void makeFlashCards(ArrayList<DataMap> dataMapList) {
        for (int deckKey = 0; deckKey < deckList.size(); deckKey++) {
            for (int i = 0; i < dataMapList.size(); i++) {
                String sideA = dataMapList.get(i).getString(deckKey + "side a");
                String sideB = dataMapList.get(i).getString(deckKey + "side b");
                if (sideA != null) {
                    Flashcard card = new Flashcard(sideA, sideB);
                    deckList.get(deckKey).add(card);
                }
            }

        }

    }
}
