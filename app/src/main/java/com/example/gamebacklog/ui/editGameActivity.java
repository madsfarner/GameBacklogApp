package com.example.gamebacklog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.Game;

public class editGameActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView textViewTitle, textViewPlatform;
    private boolean editingMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        textViewTitle = findViewById(R.id.editTextTitle);
        textViewPlatform = findViewById(R.id.editTextPlatform);

        String[] items = new String[]{"Want to play", "status 2", "status 3", "dropped"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        Intent inIntent = getIntent();
        editingMode = inIntent.getBooleanExtra(MainActivity.EDIT, false);
        Log.d("isInEditingMode", ""+editingMode);
        if(editingMode){
            final Game game = inIntent.getParcelableExtra(MainActivity.EXTRA_GAME);
            if(game == null) {
                Log.d("gameRef", "game ref is null");
            }
            if(game.getGameName() == null) {
                Log.d("NAMERef", "NAME ref is null");
            }
            Log.d("gameInfo", ""+game);
            textViewTitle.setText("gameTitle");
            textViewPlatform.setText("game platform");
            spinner.setSelection(adapter.getPosition(game.getGameStatus()));
            toolbar.setTitle(R.string.title_edit);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
