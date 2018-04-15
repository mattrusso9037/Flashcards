package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static com.matt.flashcards.Settings.debugMode;

public class AboutActivity extends AppCompatActivity {

    private int clicks;
    private Toast currentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        currentToast = new Toast(AboutActivity.this);

        // TODO Add these strings to the strings.xml file
        // Clicking the TextView 10 times will toggle Debug Mode
        findViewById(R.id.txt_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks++;
                if (clicks > 4 && clicks < 10) {
                    currentToast.cancel();
                    currentToast = Toast.makeText(
                            AboutActivity.this,
                            "You are " + (10 - clicks) + " clicks away from toggling Debug mode",
                            Toast.LENGTH_SHORT
                    );
                    currentToast.show();
                } else if (clicks == 10) {
                    currentToast.cancel();
                    debugMode = !debugMode;
                    Toast.makeText(
                            AboutActivity.this,
                            "Debug mode " + (debugMode ? "enabled" : "disabled"),
                            Toast.LENGTH_SHORT
                    ).show();
                    clicks = 0;
                }
            }
        });
    }
}
