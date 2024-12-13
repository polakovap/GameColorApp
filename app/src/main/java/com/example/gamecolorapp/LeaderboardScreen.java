package com.example.gamecolorapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.content.Intent;

import java.util.List;

public class LeaderboardScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve sequence data from the intent if available
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        //get the back button and listview
        Button btnBack = findViewById(R.id.btnBack);
        ListView lvLeaderboard = findViewById(R.id.lvLeaderboard);

        //add the data source into the listview
        ScoresDataSource dataSource = new ScoresDataSource(this);
        dataSource.open();
        List<String> topPlayers = dataSource.getTopPlayers(5);
        dataSource.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, topPlayers);
        lvLeaderboard.setAdapter(adapter);

        //set onclick listener to navigate back to game over
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to start sequence activity
                Intent intent = new Intent(LeaderboardScreen.this, GameOverScreen.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
        });
    }
}