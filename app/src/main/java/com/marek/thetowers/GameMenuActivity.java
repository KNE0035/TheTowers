package com.marek.thetowers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameMenuActivity extends AppCompatActivity {

    Button newGameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        newGameButton = (Button) findViewById(R.id.newGameButton);

        newGameButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameStartIntent = new Intent(GameMenuActivity.this, GameViewActivity.class);
                startActivity(gameStartIntent);
            }
        });
    }
}
