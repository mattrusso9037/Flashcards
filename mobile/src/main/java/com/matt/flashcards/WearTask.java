package com.matt.flashcards;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.mylibrary.Deck;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

import static com.matt.flashcards.Settings.*;

/**
 * Created by Matt on 3/21/2018.
 */

public class WearTask extends AsyncTask<Void, Void, Void> {
    final String TRANSFER_PATH = "/dataTransferToWear";
    private GoogleApiClient googleClient;
    private Context context;
    private MenuItem syncItem;

    public WearTask(Context context, MenuItem syncItem) {
        this.context = context;
        this.syncItem = syncItem;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        createMessage(TRANSFER_PATH, theDeckOfDecks, googleClient);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        syncItem.setTitle(Html.fromHtml("<font color='#ffffff'>Sync with wear</font>"));
        syncItem.setEnabled(true);
        googleClient.disconnect();
        Log.i("wear", "synced");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        initGoogleApiClient();
        syncItem.setTitle(Html.fromHtml("<font color='#808080'>Sync with wear</font>"));
        syncItem.setEnabled(false);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        googleClient.disconnect();
    }

    private void createMessage(final String path, final ArrayList<Deck> deckList, GoogleApiClient googleClient) {

        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();

        if (nodes.getNodes().size() < 1 || nodes.getNodes() == null || !nodes.getNodes().get(0).isNearby()) {
            cancel(true);
        }

        ArrayList<DataMap> dataMapList = new ArrayList();
        DataMap dataMapTitleHolder = null;

        for (Node node : nodes.getNodes()) {
            dataMapTitleHolder = new DataMap();

            saveTitles(deckList, dataMapList);
            saveDecks(deckList, dataMapList);

            dataMapTitleHolder.putDataMapArrayList("deck title key", dataMapList);
            byte[] byteArray = dataMapTitleHolder.toByteArray();

            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleClient, node.getId(), path, byteArray).await();

        }
    }

    private void saveDecks(ArrayList<Deck> deckList, ArrayList<DataMap> dataMapList) {
        for (int deckKey = 0; deckKey < deckList.size(); deckKey++) {

            Deck currentDeck2 = deckList.get(deckKey);
            for (int j = 0; j < currentDeck2.size(); j++) {
                DataMap cardDataMap = new DataMap();

                cardDataMap.putString(deckKey + "side a", currentDeck2.get(j).getSideA());
                cardDataMap.putString(deckKey + "side b", currentDeck2.get(j).getSideB());

                dataMapList.add(cardDataMap);
            }

        }
    }

    private void saveTitles(ArrayList<Deck> deckList, ArrayList<DataMap> dataMapList) {
        for (int i = 0; i < deckList.size(); i++) {
            DataMap titleDataMap = new DataMap();
            titleDataMap.putString("deck title", deckList.get(i).getTitle());
            dataMapList.add(titleDataMap);
        }
    }

    private void initGoogleApiClient() {
        googleClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                .build();
        googleClient.connect();
    }

}
