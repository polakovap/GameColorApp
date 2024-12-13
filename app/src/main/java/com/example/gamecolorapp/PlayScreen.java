package com.example.gamecolorapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//new imports
import android.util.Log;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayScreen extends AppCompatActivity {
    private List<Integer> randomSequence;  // Sequence of colors to match
    private List<Integer> playerInput;     // Player's input
    private int currentStep = 0;           // Track the current step of the sequence
    private int score = 0;                 // Track score


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get intent
        Intent intent = getIntent();
        String playerName = intent.getStringExtra("playerName");
        score = intent.getIntExtra("score", 0);  // Get accumulated score from Intent

        // Log the current score for debugging
        Log.d("PlayScreen", "Current accumulated score: " + score);

        //initialize the sequences
        randomSequence = new ArrayList<>();
        playerInput = new ArrayList<>();

        //get the sequence from the intent
        int[] sequenceArray = getIntent().getIntArrayExtra("sequence");

        //convert the int array into a list
        if (sequenceArray != null) {
            for (int i : sequenceArray){
                randomSequence.add(i);
            }
        }

        //get buttons
        Button btnRed = findViewById(R.id.btnRed);
        Button btnBlue = findViewById(R.id.btnBlue);
        Button btnGreen = findViewById(R.id.btnGreen);
        Button btnYellow = findViewById(R.id.btnYellow);

        //set listeners for buttons
        btnRed.setOnClickListener(view -> onButtonPressed(R.id.viewRed));
        btnBlue.setOnClickListener(view -> onButtonPressed(R.id.viewBlue));
        btnGreen.setOnClickListener(view -> onButtonPressed(R.id.viewGreen));
        btnYellow.setOnClickListener(view -> onButtonPressed(R.id.viewYellow));
    }

    //method to handle button presses
    private void onButtonPressed(int colorId) {
        playerInput.add(colorId);  // Add the pressed color to player's input

        if (colorId != randomSequence.get(currentStep)) {
            // If the color doesn't match the sequence
            gameOver();
        } else {
            currentStep++;  // Move to the next step
            score += 10;    // Increment score

            // If the player has completed the sequence
            if (currentStep == randomSequence.size()) {
                // Player has matched the sequence, increase the sequence length
                generateNewSequence();
            }
        }
    }

    //method to show game over
    private void gameOver() {
        // Get the player name from the Intent
        String playerName = getIntent().getStringExtra("playerName");

        // Check if playerName is null or empty before proceeding
        if (playerName == null || playerName.trim().isEmpty()) {
            Log.e("PlayScreen", "Player name is null or empty!");
            return;  // Do not proceed if playerName is invalid
        }

        ScoresDataSource dataSource = new ScoresDataSource(this);
        dataSource.open();

        // Log to ensure playerName and score are valid
        Log.d("PlayScreen", "Saving score for " + playerName + ": " + score);

        // Add/update the player's score in the database
        dataSource.addOrUpdateScore(playerName, score);
        dataSource.close();

        //navigate to the GameOver screen
        Intent intent = new Intent(this, GameOverScreen.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("score", score);

        startActivity(intent);
        finish();
    }

    //method to get a new sequence
    private void generateNewSequence() {
        // Get the player name from the Intent
        String playerName = getIntent().getStringExtra("playerName");

        //increase the sequence length by 2
        int newLength = randomSequence.size() + 2;

        //generate a new sequence with the increased length
        // Generate a new sequence
        List<Integer> newSequence = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < newLength; i++) {
            newSequence.add(randomSequence.get(random.nextInt(randomSequence.size())));
        }

        // Pass the new sequence to Sequence_Screen along with the playerName and their score
        Intent intent = new Intent(this, Sequence_Screen.class);
        intent.putExtra("sequence", newSequence.stream().mapToInt(Integer::intValue).toArray());
        intent.putExtra("playerName", playerName);
        intent.putExtra("score", score);
        startActivity(intent);

        //go back to sequence screen
        finish();
    }
}