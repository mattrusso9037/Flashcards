package com.matt.flashcards;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public final class Settings {

    private Settings() {} // Prevent instantiation of this class

    public final static ArrayList<Deck> theDeckOfDecks = new ArrayList<>();

    public static Deck shuffledDeck;
    public final static Deck favoritesDeck = new Deck("Favorites");

    public static boolean isFirstRun = false;
    public static boolean debugMode = false;
    private static boolean dataLoaded = false;

    private final static String SIDE_A_KEY = "SideA";
    private final static String SIDE_B_KEY = "SideB";
    private final static String FAVORITE_KEY = "Favorite";
    private final static String DECK_KEY = "Decks";
    private final static String DEBUG_KEY = "DebugMode";
    private final static String DECK_TITLE_KEY = "Title";
    private final static String DECK_FLASHCARDS_KEY = "Flashcards";
    private final static String FILE_NAME = "Settings.json";

    public static void loadDummyData() {
        Deck android = new Deck("Android Development");
        android.add(new Flashcard("DP", "Density-independent Pixels"));
        android.add(new Flashcard("SP", "Scale-independent Pixels"));
        android.add(new Flashcard("IDE", "Integrated Development Environment"));
        android.add(new Flashcard("Activity", "An activity is a single, focused thing that the user can do. Almost all activities interact with the user, so the Activity class takes care of creating a window for you"));
        android.add(new Flashcard("Key Mobile Challenges", "Low Processing Power\n" +
                "Limited RAM\n" +
                "Intermittent or Slow Internet\n" +
                "Limited Battery"));
        android.add(new Flashcard("MVP", "Minimum Viable Product"));
        android.add(new Flashcard("Intent", "An Intent is an abstract description of an operation to be performed.\n" +
                "An intent is the event to change one view to another"));
        android.add(new Flashcard("LinearLayout", "LinearLayout means you can align views one by one (vertically/horizontally)."));
        android.add(new Flashcard("RelativeLayout", "RelativeLayout means based on relation of views from its parents and other views."));
        android.add(new Flashcard("ConstraintLayout", "ConstraintLayout is similar to a RelativeLayout in that it uses relations to position and size widgets, but has additional flexibility and is easier to use in the Layout Editor."));
        android.add(new Flashcard("WebView", "WebView to load html, static or dynamic pages."));
        android.add(new Flashcard("FrameLayout", "FrameLayout to load child one above another, like cards inside a frame, we can place one above another or anywhere inside the frame."));
        android.add(new Flashcard("Model", "The Data"));
        android.add(new Flashcard("View", "The XML files in the layout folder"));
        android.add(new Flashcard("Controller", "The 'Activity'"));
        android.add(new Flashcard("AsyncTask", "Used for basic multithreading on Android"));
        android.add(new Flashcard("CRUD", "CREATE\n" +
                "READ\n" +
                "UPDATE\n" +
                "DELETE\n"));
        android.add(new Flashcard("Localization", "Modify strings.xml"));
        android.add(new Flashcard("Bob forks Bill’s ‘Project A’ – How can Bob get his changes into Bill’s master?", "Pull Request"));
        android.add(new Flashcard("Jen forks Bill’s ‘Project A’ to make ‘Project B’ what if it never gets merged with Project A?", "Nothing happens"));
        android.add(new Flashcard("Bill just deployed v1.0. Bill already started working on v2.0 and made changes to the code. Bill just found a bug in v1.0. What should he do to get this change into production with low risk?", "checkout v1.0, fix the bug, and merge into master"));
        android.add(new Flashcard("80/20 Rule", "80% of the work is done by 20% of the people"));
        android.add(new Flashcard("AndroidManifest.xml", "Contains information about app permissions, activities, ect."));

        Deck git = new Deck("Git");
        git.add(new Flashcard("Remote", "A remote is a cloud, in this case it is one that holds your code"));
        git.add(new Flashcard("Clone", "Cloning a repository is the act of downloading the repo and setting it up for use with the remote repository"));
        git.add(new Flashcard("Branching", "Branching is where you take the code in a different direction from a given point"));
        git.add(new Flashcard("Checkout", "Checkout is the act of switching between branches"));
        git.add(new Flashcard("Merging", "Merging is the act of taking two branches (or forks) and bringing them together"));
        git.add(new Flashcard("Merge conflict", "This is when the same area of code has been worked on and git cannot determine the correct action to merge"));
        git.add(new Flashcard("Pull request", "A pull request is a concept that does not exist in git alone, but exists in services like Github and bitbucket, this is the act of asking someone to review your changes so that they can be merged"));
        git.add(new Flashcard("Commit", " Committing is when you add your changes to git.  Initially this commit is only on your local machine but once you push the commit it will also be on the remote server"));
        git.add(new Flashcard("Forking", "Forking is the act of taking a copy of an existing project so that you can manage some future work, in the future you can choose to merge back into the master fork and you can pull updates whenever you want"));
        git.add(new Flashcard("Pulling", "Pulling is the act of pulling any recent changes in the code from another branch (local or remote)"));
        git.add(new Flashcard("Pushing", "This is the opposite of pulling, instead of grabbing changes this is the act of sending your changes up"));
        git.add(new Flashcard(".gitignore", "Git ignore is a concept of files to ignore when committing."));
        git.add(new Flashcard("Readme", "A readme is a file that displays styles and contents when you navigate to the containing folder. It is in the form of markdown and is called README.MD"));

        Deck famousPeople = new Deck("Famous Technologists in History");
        famousPeople.add(new Flashcard("I grew up wealthy – lawyer dad, banker mom\n" +
                "I learned to code @ 8\n" +
                "I went to ivy league school\n" +
                "I dropped out of college\n" +
                "Been arrested\n" +
                "“verbally combative, unreachable by phone”\n" +
                "Pitched my first product before it was ever built\n" +
                "The second richest man in the world", "Bill Gates"));
        famousPeople.add(new Flashcard("Born in white plains NY\n" +
                "Went to Mercy College, Harvard\n" +
                "My company went from Idea to launch in less than a month\n" +
                "The idea was not mine – I stole it from someone else and built it while they stood around twiddling their thumbs\n" +
                "Net worth $33 billion", "Mark Zuckerberg"));
        famousPeople.add(new Flashcard("Immigrant from Africa\n" +
                "Started over 5 companies\n" +
                "Owner or Advisor for 4 - $Billion companies\n" +
                "Degrees in economics & physics\n" +
                "Dropped out of Stanford", "Elon Musk"));
        famousPeople.add(new Flashcard("Born in NYC\n" +
                "World Cup Sailor\n" +
                "“Competitor”\n" +
                "Worked for CIA\n" +
                "Bought many of the world’s biggest software companies\n" +
                "Believes software itself can just be bought, great products and hard work are the key to success.", "Larry Ellison (Oracle)"));
        famousPeople.add(new Flashcard("Mother was a teenage parent\n" +
                "Worked on Wall St\n" +
                "Wrote his business plan on a road trip from NY to Seattle\n" +
                "Started company in garage\n" +
                "I believe that “Focusing on the customer is key”\n" +
                "Richest person in the world", "Jeff Bezos - Founder/CEO Amazon.com"));

        Deck java = new Deck("Java");
        java.add(new Flashcard("Interface", "An interface in the Java programming language is an abstract type that is used to specify a behaviour that classes must implement."));
        java.add(new Flashcard("Abstract Class", "Abstract classes cannot be instantiated, but they can be subclassed."));
        java.add(new Flashcard("Polymorphism", "Polymorphism is the capability of a method to do different things based on the object that it is acting upon."));
        java.add(new Flashcard("static", "In Java, a static member is a member of a class that isn’t associated with an instance of a class. Instead, the member belongs to the class itself."));

        theDeckOfDecks.clear();
        theDeckOfDecks.add(android);
        theDeckOfDecks.add(git);
        theDeckOfDecks.add(famousPeople);
        theDeckOfDecks.add(java);
    }

    public static void loadData(Context context) {
        // Make sure the data is only read in once from the file
        if (dataLoaded) return;
        dataLoaded = true;

        try {
            // Get ready to read in the JSON data
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(context.openFileInput(FILE_NAME), "UTF-8"));

            // Since all the JSON data is on 1 line, we only need to read in 1 line from the file
            JSONObject JSONSettings = new JSONObject(inputStream.readLine());
            inputStream.close();

            // Get the array of decks from the settings
            JSONArray JSONAllDecks = JSONSettings.getJSONArray(DECK_KEY);

            // Get the setting for Debug Mode
            try {
                debugMode = JSONSettings.getBoolean(DEBUG_KEY);
            } catch (JSONException e) {
                // Debug Mode is false by default, so no need to set it
            }

            for (int i = 0; i < JSONAllDecks.length(); i++) {
                // Get a deck from the array of decks
                JSONObject JSONDeck = (JSONObject) JSONAllDecks.get(i);

                // Create a new deck object using the title read in
                Deck deck = new Deck(JSONDeck.getString(DECK_TITLE_KEY));

                // Get an array of flashcards from the deck
                JSONArray JSONFlashcards = JSONDeck.getJSONArray(DECK_FLASHCARDS_KEY);

                for (int j = 0; j < JSONFlashcards.length(); j++) {
                    // Get a flashcard from the array of flashcards
                    JSONObject JSONFlashcard = JSONFlashcards.getJSONObject(j);

                    // Create a new Flashcard object and add it to the deck object
                    Flashcard f = new Flashcard(
                            JSONFlashcard.getString(SIDE_A_KEY),
                            JSONFlashcard.getString(SIDE_B_KEY)
                    );

                    try {
                        f.setFavorite(JSONFlashcard.getBoolean(FAVORITE_KEY));
                    } catch (JSONException e) {}

                    deck.add(f);

                    // If the flashcard is a favorite, add it to the favorites deck
                    if (f.isFavorite()) {
                        favoritesDeck.add(f);
                    }
                }
                // Add the new deck to the theDeckOfDecks
                theDeckOfDecks.add(deck);
            }
        } catch (FileNotFoundException e) {
            // It's likely the file was not found because the program ran for the first time
            isFirstRun = true;
        } catch (JSONException | IOException e) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(R.string.cant_load_data)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    public static void saveData(Context context) {
        JSONObject JSONSettings = new JSONObject();
        JSONArray JSONAllDecks = new JSONArray();

        try {
            for (Deck deck : theDeckOfDecks) {
                // Create a new JSON deck to hold all the flashcards
                JSONArray JSONDeck = new JSONArray();

                for (Flashcard flashcard : deck) {
                    // Adds a new JSON flashcard to the deck
                    JSONDeck.put(new JSONObject()
                            .put(SIDE_A_KEY, flashcard.getSideA())
                            .put(SIDE_B_KEY, flashcard.getSideB())
                            .put(FAVORITE_KEY, flashcard.isFavorite())
                    );
                }
                // Adds the JSON deck to the array of decks
                JSONAllDecks.put(new JSONObject()
                        .put(DECK_TITLE_KEY, deck.getTitle())
                        .put(DECK_FLASHCARDS_KEY, JSONDeck)
                );
            }

            // Adds the array of decks to the settings
            JSONSettings.put(DECK_KEY, JSONAllDecks);

            // Adds Debug Mode to the settings
            JSONSettings.put(DEBUG_KEY, debugMode);

            // Save the JSON data to the file
            FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(JSONSettings.toString().getBytes()); // Convert to binary and write
            outputStream.close();

        } catch (JSONException | IOException e) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(R.string.cant_save_data)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            return;
        }
        Toast.makeText(context, R.string.successful_save, Toast.LENGTH_SHORT).show();
    }

    public static String[] getAllDeckTitles() {
        String[] titles = new String[theDeckOfDecks.size()];
        for (int i = 0; i < titles.length; i++) {
            titles[i] = theDeckOfDecks.get(i).getTitle();
        }
        return titles;
    }

    public static void generateShuffledDeck(boolean[] decksChecked) {
        new DebugLog("generateShuffledDeck");

        // Make sure the shuffled deck is reset
        shuffledDeck = new Deck("Shuffle Mode");

        // Add all flashcards from checked off decks to the shuffled deck
        for (int i = 0; i < theDeckOfDecks.size(); i++) {
            new DebugLog(i + " " + decksChecked[i]);
            if (decksChecked[i]) {
                shuffledDeck.addAll(theDeckOfDecks.get(i));
                new DebugLog("Deck added: " + theDeckOfDecks.get(i).getTitle());
            }
        }

        // Shuffle the deck
        Collections.shuffle(shuffledDeck);
    }

    public static boolean areThereFlashcards() {
        for (Deck d : theDeckOfDecks) {
            if (!d.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
