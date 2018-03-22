package com.example.siddh.wordament;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FinishGameActivity extends AppCompatActivity {
    ListView wordList;
    TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_game);
        wordList = (ListView) findViewById(R.id.wordList);
        score = (TextView) findViewById(R.id.score);
        Intent intent = getIntent();
        List<String> list = intent.getStringArrayListExtra("LIST_OF_WORDS");
        int score1 = intent.getIntExtra("SCORE", 0);
        wordList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        score.setText("Score : " + score1);
    }
    public void newGame(View view) {
        Intent game = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(game);
        finish();
    }
}
