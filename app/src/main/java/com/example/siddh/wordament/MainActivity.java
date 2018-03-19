package com.example.siddh.wordament;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

//    private static final int MIN_WORD_LENGTH = 3;
//    private static final int MAX_WORD_LENGTH = 5;
    private ArrayList<String> words5letters = new ArrayList<>();
    private ArrayList<String> words4letters = new ArrayList<>();
    private ArrayList<String> words3letters = new ArrayList<>();
    ArrayList<LetterTile> letters = new ArrayList<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GridView grid = (GridView) findViewById(R.id.letterGrid);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length() == 3) {
                    words3letters.add(word);
                }
                if(word.length() == 4) {
                    words4letters.add(word);
                }
                if(word.length() == 5) {
                    words5letters.add(word);
                }
            }
        } catch(IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean onStartGame(View view) {
        TextView status = (TextView) findViewById(R.id.gameStatus);
        status.setText("Game Started");
        GridView letterGrid = (GridView) findViewById(R.id.letterGrid);
        String[] wordsToPutInGrid = new String[6];
        for(int i=0; i<3; i++) {
            wordsToPutInGrid[i] = words5letters.get(random.nextInt(words5letters.size()));
            for(int j=0; j<5; j++) {
                letters.add(new LetterTile(this, wordsToPutInGrid[i].charAt(j)));
            }
        }
        for(int i=3; i<5; i++){
            wordsToPutInGrid[i] = words3letters.get(random.nextInt(words3letters.size()));
            for(int j=0; j<3; j++){
                letters.add(new LetterTile(this, wordsToPutInGrid[i].charAt(j)));
            }
        }
        wordsToPutInGrid[5] = words4letters.get(random.nextInt(words4letters.size()));
        for(int j=0; j<4; j++){
            letters.add(new LetterTile(this, wordsToPutInGrid[5].charAt(j)));
        }
        Collections.shuffle(letters);
        letterGrid.setAdapter(new GridAdapter(this, letters));
        return true;
    }

    public static final class GridAdapter extends BaseAdapter {
        private ArrayList<LetterTile> mLetters;

        private Context mContext;

        public GridAdapter(Context c, ArrayList<LetterTile> letters) {
            mContext = c;
            mLetters = letters;
        }

        public int getCount() {
            return mLetters.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new LetterTile for each item referenced by the Adapter
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            LetterTile view = mLetters.get(position);

            if (view == null) {
                view = (LetterTile) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            return view;
        }
    }
}
