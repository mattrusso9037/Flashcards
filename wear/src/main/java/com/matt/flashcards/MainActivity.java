package com.matt.flashcards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

public class MainActivity extends WearableActivity implements ListItemClickListener, MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private WearableRecyclerView recyclerView;
    private MyAdapter adapter;
    private WearableLinearLayoutManager layoutManager;
    private GoogleApiClient googleClient;
    static ArrayList list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        list.add("test2");
        list.add("test3");

        layoutManager = new WearableLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(list,this);
        recyclerView.setAdapter(adapter);

        // Enables Always-on
        setAmbientEnabled();

        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        googleClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
        googleClient.connect();
        Log.i("wear", "connected");
        Wearable.MessageApi.addListener(googleClient, this);
        Log.i("wear", "listener added");

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this,String.valueOf(clickedItemIndex), Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(googleClient, this);
        Log.i("wear", "on connected");

    }
    @Override
    protected void onStop() {
        if ( googleClient != null ) {
            Wearable.MessageApi.removeListener( googleClient, this );
            if ( googleClient.isConnected() ) {
                googleClient.disconnect();
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                if( messageEvent.getPath().equals( "/testPath" ) ) {
                   list.add(new String(messageEvent.getData()));
                   adapter.notifyDataSetChanged();
                    Log.i("wear", "received");
                    Log.i("wear", new String(messageEvent.getData()));


                }
            }
        });
    }
}
