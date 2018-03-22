package com.example.siddh.wordament;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static boolean[][] visited;
    private static final int MIN_WORD_LENGTH = 3;
//    private static final int MAX_WORD_LENGTH = 5;
    private static ArrayList<String> words5letters = new ArrayList<>();
    private static ArrayList<String> words4letters = new ArrayList<>();
    private static ArrayList<String> words3letters = new ArrayList<>();
    private static HashSet<String> dict = new HashSet<>();
    private static Stack<LetterTile> selected_word = new Stack<>();
    public static ArrayList<String> wordsInGrid = new ArrayList<>();
    public static int count=0;
    private static Character[][] grid = new Character[4][4];
    private static Random random = new Random();
    public static int score;
    public static TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordsInGrid.clear();
        score = 0;
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length() >= MIN_WORD_LENGTH)
                    dict.add(word);
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
        final TextView timer = (TextView) findViewById(R.id.timer);
        status = (TextView) findViewById(R.id.gameStatus);
        status.setText("Game Started");
        final GridView letterGrid = (GridView) findViewById(R.id.letterGrid);
        //letterGrid.seListener();
        ArrayList<LetterTile> letters = new ArrayList<>();
        String[] wordsToPutInGrid = new String[4];
        for(int i=0; i<2; i++) {
            wordsToPutInGrid[i] = words4letters.get(random.nextInt(words4letters.size()));
            for(int j=0; j<4; j++) {
                letters.add(new LetterTile(this, wordsToPutInGrid[i].charAt(j)));
            }
        }
        for(int i=2; i<3; i++){
            wordsToPutInGrid[i] = words3letters.get(random.nextInt(words3letters.size()));
            for(int j=0; j<3; j++){
                letters.add(new LetterTile(this, wordsToPutInGrid[i].charAt(j)));
            }
        }
        wordsToPutInGrid[3] = words5letters.get(random.nextInt(words5letters.size()));
        for(int j=0; j<5; j++){
            letters.add(new LetterTile(this, wordsToPutInGrid[3].charAt(j)));
        }
        Collections.shuffle(letters);
        int s = letters.size();
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                grid[i][j] = letters.get(s-1).letter;
                s--;
            }
        }

        letterGrid.setAdapter(new GridAdapter(this, letters));

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Time left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.i("HI", "Done");
                timer.setText("done!");
                Intent intent = new Intent(getApplicationContext(), FinishGameActivity.class);
                findAllWords();
                Log.i("MyInfo", "Finished");
                intent.putExtra("LIST_OF_WORDS", wordsInGrid);
                intent.putExtra("SCORE", score);
                startActivity(intent);
                finish();
            }
        }.start();

        return true;
    }

    public final class GridAdapter extends BaseAdapter {
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
            view.setOnDragListener(new DragListener());

            if (view == null) {
                view = (LetterTile) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            return view;
        }
    }
    private class DragListener implements View.OnDragListener {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    LetterTile tile = (LetterTile) view;
                    switch (dragEvent.getAction()){
                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.i("MyInfoMain", "Entered " + tile.letter);
                            tile.setBackgroundColor(Color.GREEN);
                            selected_word.push(tile);
                            tile.invalidate();
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.i("MyInfo", "Exited " + tile.letter);
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;
                        case DragEvent.ACTION_DRAG_STARTED:
                            selected_word.clear();
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.i("MyInfo", "Ended");
                            tile.setBackgroundColor(Color.rgb(255, 255, 200));
                            tile.invalidate();
                            if(isGoodWord(selected_word)) {
                                score++;
                                status.setText("Score : "+score);
                                Log.d("Score seww", "good word");
                            }
                            return true;
                        case DragEvent.ACTION_DROP:
                            return true;
                    }
                    return false;
                }

    }


    public static boolean isGoodWord(Stack<LetterTile> selected_word) {
        if(selected_word.isEmpty()){
            return false;
        } else {
            StringBuffer buffer = new StringBuffer();
            while(!selected_word.isEmpty()) {
                buffer.append(selected_word.pop().letter);
            }
            if(dict.contains(buffer.reverse().toString())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void findAllWords() {
        visited = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                StringBuffer buffer = new StringBuffer();
                searchword(i, j, buffer);
            }
        }
    }

    private static void searchword(int i, int j, StringBuffer buffer) {
        if (i < 0 || j < 0 || i > grid.length - 1 || j > grid[i].length - 1) {
            return;
        }
        if (visited[i][j] == true) {
            return;
        }
        if(buffer.length() >= 5){
            return;
        }
        visited[i][j] = true;
        buffer.append(grid[i][j]);
        System.out.println(buffer.toString());
        if (dict.contains(buffer.toString())) {
            Log.i("Found :", buffer.toString());
            wordsInGrid.add(buffer.toString());
        }
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                searchword(k, l, buffer);
            }
        }
        buffer.deleteCharAt(buffer.length() - 1);
        visited[i][j] = false;
    }
}
