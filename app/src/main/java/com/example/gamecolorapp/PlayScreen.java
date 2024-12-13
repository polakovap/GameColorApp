package com.example.gamecolorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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

//imports for accelerometer
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class PlayScreen extends AppCompatActivity {
    private List<Integer> randomSequence;  // Sequence of colors to match
    private List<Integer> playerInput;     // Player's input
    private int currentStep = 0;           // Track the current step of the sequence
    private int score = 0;                 // Track score

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;
    private boolean isFlat = false;  // Tracks whether the phone is in a neutral position

    private Button btnRed, btnBlue, btnGreen, btnYellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_screen);

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
        btnRed = findViewById(R.id.btnRed);
        btnBlue = findViewById(R.id.btnBlue);
        btnGreen = findViewById(R.id.btnGreen);
        btnYellow = findViewById(R.id.btnYellow);

        //set listeners for buttons
        btnRed.setOnClickListener(view -> onButtonPressed(R.id.viewRed));
        btnBlue.setOnClickListener(view -> onButtonPressed(R.id.viewBlue));
        btnGreen.setOnClickListener(view -> onButtonPressed(R.id.viewGreen));
        btnYellow.setOnClickListener(view -> onButtonPressed(R.id.viewYellow));

        // Initialize accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            initializeAccelerometerListener();
        } else {
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeAccelerometerListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Example logic for tilt direction
                int tiltNegativeLimit = -3;
                int tiltPositiveLimit = 3;
                int tiltNeutralNegativeLimit = -1;
                int tiltNeutralPositiveLimit = 1;

                // Detect if the phone is in flat position
                if (x > tiltNeutralNegativeLimit && x < tiltNeutralPositiveLimit
                        && y > tiltNeutralNegativeLimit && y < tiltNeutralPositiveLimit) {
                    isFlat = true;
                }

                // Handle tilt only if phone is flat
                if (isFlat) {
                    if (y < tiltNegativeLimit) {
                        // Phone tilted to the right
                        handleTilt("right");
                    } else if (y > tiltPositiveLimit) {
                        // Phone tilted to the left
                        handleTilt("left");
                    } else if (x > tiltPositiveLimit) {
                        // Phone tilted forward
                        handleTilt("forward");
                    } else if (x < tiltNegativeLimit) {
                        // Phone tilted backward
                        handleTilt("backward");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Handle accuracy changes
            }
        };

        // Register the listener for accelerometer
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    // Handle tilt direction and map it to a button press
    private void handleTilt(String direction) {
        // Map direction to button press
        int buttonId = mapDirectionToButtonId(direction);
        if (buttonId != -1) {
            onButtonPressed(buttonId); // Simulate button press based on tilt
        } else {
            Toast.makeText(this, "Tilt again", Toast.LENGTH_SHORT).show();
        }
    }

    // Map tilt direction to button
    private int mapDirectionToButtonId(String direction) {
        switch (direction) {
            case "left":
                return R.id.viewBlue; // blue button
            case "forward":
                return R.id.viewRed; // red button
            case "right":
                return R.id.viewYellow; // yellow button
            case "backward":
                return R.id.viewGreen; // green button
            default:
                return -1; // Invalid direction
        }
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

    // Handle onResume and onPause for accelerometer listener
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
}