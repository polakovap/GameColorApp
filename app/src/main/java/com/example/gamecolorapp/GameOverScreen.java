package com.example.gamecolorapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

public class GameOverScreen extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView tvScore = findViewById(R.id.tvScore);
        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);
        Button btnNewGame = findViewById(R.id.btnNewGame);

        // Retrieve the score and name passed from PlayScreen
        int score = getIntent().getIntExtra("score", 0);
        tvScore.setText("Your Score: " + score);

        btnLeaderboard.setOnClickListener(view -> {
            Intent intent = new Intent(this, LeaderboardScreen.class);
            intent.putExtra("score", score);
            startActivity(intent);
        });

        btnNewGame.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}