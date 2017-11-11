package com.marek.thetowers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class BestScoreActivity extends AppCompatActivity {

    TextView bestScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_score);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int highScore = prefs.getInt(getString(R.string.high_score), 0);
        bestScoreText = (TextView) findViewById(R.id.bestScoreText);
        bestScoreText.setText("Your best score is: " + highScore);
    }
}
