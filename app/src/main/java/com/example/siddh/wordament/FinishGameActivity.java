package com.example.siddh.wordament;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FinishGameActivity extends AppCompatActivity {
    ListView wordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_game);
        wordList = (ListView) findViewById(R.id.wordList);
        Intent intent = getIntent();
        List<String> list = intent.getStringArrayListExtra("LIST_OF_WORDS");
        wordList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
    }
    public void newGame(View view) {
        Intent game = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(game);
        finish();
    }
}
