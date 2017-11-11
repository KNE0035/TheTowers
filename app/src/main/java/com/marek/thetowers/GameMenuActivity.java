package com.marek.thetowers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameMenuActivity extends AppCompatActivity {

    Button newGameButton;
    Button bestGameButton;
    Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        newGameButton = (Button) findViewById(R.id.newGameButton);
        bestGameButton = (Button) findViewById(R.id.bestGameButton);
        quitButton = (Button) findViewById(R.id.quitButton);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameStartIntent = new Intent(GameMenuActivity.this, GameViewActivity.class);
                startActivity(gameStartIntent);
            }
        });

        bestGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bestGameIntent = new Intent(GameMenuActivity.this, BestScoreActivity.class);
                startActivity(bestGameIntent);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                onDestroy();
            }
        });
    }
}
