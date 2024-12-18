package com.example.gamecolorapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//new imports
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get the play button and editText
        Button btnPlay = findViewById(R.id.btnPlay);
        EditText etPlayerName = findViewById(R.id.etPlayerName);

        //set onclick listener to navigate to Sequence Activity
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = etPlayerName.getText().toString().trim();
                if (playerName.isEmpty() || playerName.equals("Name")) {
                    etPlayerName.setError("Please enter your name");
                    return;
                }

                //intent to start sequence activity
                Intent intent = new Intent(MainActivity.this, Sequence_Screen.class);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
            }
        });
    }
}