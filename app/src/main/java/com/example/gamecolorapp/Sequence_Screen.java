package com.example.gamecolorapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//new imports
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class Sequence_Screen extends AppCompatActivity {
    //declare views for the colored circles
    private View viewRed, viewBlue, viewYellow, viewGreen;

    // List of all circle IDs
    private List<Integer> circleIds;

    // Random sequence of colors
    private List<Integer> randomSequence;

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

        // Generate a random sequence
        generateRandomSequence(4); // Generate a sequence of 4 colors

        // Start the sequence
        showSequence();

    }

    // Method to generate a random sequence of circle IDs
    private void generateRandomSequence(int length) {
        randomSequence = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(circleIds.size()); // Pick a random index
            randomSequence.add(circleIds.get(randomIndex));    // Add the corresponding ID to the sequence
        }
    }

    // Method to light up circles in the random sequence
    private void showSequence() {
        final int[] delay = {0}; // Initial delay
        for (int id : randomSequence) {
            handler.postDelayed(() -> lightUpCircle(id), delay[0]);
            delay[0] += 1000; // 1 second delay between each light-up
        }
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

}