package com.marek.thetowers;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActiveObjectDetailActivity extends AppCompatActivity {

    ImageView image;
    TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_active_object_detail);

        int imageValue = 0;
        String description = "";

        image = (ImageView) findViewById(R.id.activeObjectDetailImage);
        txtDescription = (TextView) findViewById(R.id.txtDetailDesc);

        Intent intent = getIntent();
        imageValue = Integer.valueOf(intent.getExtras().getInt("IMAGE_VALUE"));
        description = intent.getStringExtra("DESCRIPTION");

        image.setImageResource(imageValue);
        txtDescription.setText(description);
    }
}
