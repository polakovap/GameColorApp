package com.example.gamecolorapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//new imports
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class Sequence_Screen extends AppCompatActivity {
    //declare views for the colored circles
    private View viewRed, viewBlue, viewYellow, viewGreen;

    // List of all circle IDs
    private static List<Integer> circleIds;
    private static List<Integer> lightbulbIds;

    // Random sequence of colors
    private static List<Integer> randomSequence;

    // Handler for delays
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sequence_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        viewRed = findViewById(R.id.viewRed);
        viewBlue = findViewById(R.id.viewBlue);
        viewYellow = findViewById(R.id.viewYellow);
        viewGreen = findViewById(R.id.viewGreen);

        // Initialize the list of circle IDs
        circleIds = new ArrayList<>();
        circleIds.add(R.id.viewRed);
        circleIds.add(R.id.viewBlue);
        circleIds.add(R.id.viewYellow);
        circleIds.add(R.id.viewGreen);

        // Retrieve sequence data from the intent if available
        Intent intent = getIntent();
        String playerName = intent.getStringExtra("playerName");
        int[] sequenceArray = intent.getIntArrayExtra("sequence");
        int score = intent.getIntExtra("score", 0);

        //log for debugging
        Log.d("Sequence_Screen", "Player: " + playerName + ", Current Score: " + score);


        // If there's a new sequence passed from PlayScreen
        if (sequenceArray != null) {
            randomSequence = new ArrayList<>();
            for (int color : sequenceArray) {
                randomSequence.add(color);
            }
        } else {
            // Generate a random sequence if it's the first time (or add more colors based on game progression)
            generateRandomSequence(4);  // You can start with a sequence of 4 colors
        }

        // Start showing the sequence to the user
        showSequence(playerName, score);

    }

    // Method to generate a random sequence of circle IDs
    public static void generateRandomSequence(int length) {
        randomSequence = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(circleIds.size()); // Pick a random index
            randomSequence.add(circleIds.get(randomIndex));    // Add the corresponding ID to the sequence
        }
    }

    // Method to light up circles in the random sequence
    private void showSequence(String playerName, int score) {
        final int[] delay = {1000}; // Initial delay
        for (int id : randomSequence) {
            handler.postDelayed(() -> lightUpCircle(id), delay[0]);
            delay[0] += 1000; // 1 second delay between each light-up
        }

        // After the sequence finishes, navigate to the PlayScreen
        // Set the delay to be after the last circle in the sequence
        handler.postDelayed(() -> goToPlayScreen(playerName, score), delay[0] + 500); // Add an extra 500ms to give time for the last light-up
    }

    // Method to light up a single circle
    private void lightUpCircle(int circleId) {
        View circle = findViewById(circleId);
        if (circle != null) {
            if (circleId == R.id.viewRed) {
                // Change background to according color
                circle.setBackgroundColor(Color.parseColor("#FF0000"));

                // Revert to the original color after 500ms
                handler.postDelayed(() -> resetCircle(circle), 500);
            } else if (circleId == R.id.viewBlue) {
                // Change background to according color
                circle.setBackgroundColor(Color.parseColor("#0000FF"));

                // Revert to the original color after 500ms
                handler.postDelayed(() -> resetCircle(circle), 500);
            } else if (circleId == R.id.viewGreen) {
                // Change background to according color
                circle.setBackgroundColor(Color.parseColor("#008000"));

                // Revert to the original color after 500ms
                handler.postDelayed(() -> resetCircle(circle), 500);
            } else if (circleId == R.id.viewYellow) {
                // Change background to according color
                circle.setBackgroundColor(Color.parseColor("#Ffff00"));

                // Revert to the original color after 500ms
                handler.postDelayed(() -> resetCircle(circle), 500);
            }

        }
    }


    // Method to reset a circle's background color
    private void resetCircle(View circle) {
        circle.setBackgroundResource(R.drawable.circle_background); // Reset to original drawable
    }

    //a method to go to play screen
    private void goToPlayScreen(String playerName, int score) {
        // Convert the randomSequence list to an int array
        int[] sequenceArray = new int[randomSequence.size()];
        for (int i = 0; i < randomSequence.size(); i++) {
            sequenceArray[i] = randomSequence.get(i);
        }


        //intent to start sequence activity
        Intent intent = new Intent(Sequence_Screen.this, PlayScreen.class);

        // Pass the sequence, plyerName, and score to PlayScreen via Intent
        intent.putExtra("sequence", sequenceArray);
        intent.putExtra("playerName", playerName);
        intent.putExtra("score", score);

        // Start PlayScreen
        startActivity(intent);
    }

}