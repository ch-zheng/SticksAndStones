package com.afrikappakorps.sticksandstones;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log; //FIXME: DELETE ME
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract.PlayerEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GameActivity extends AppCompatActivity {
    public static final String IS_NEW_GAME = "isNewGame";
    //TODO make like a Next Round in: 3... 2... 1... sort of thing with CountDownTimer
    private int round, player1, player2, judge;
    private String prompt, entry1, entry2;
    private TextView promptText, nameText1, entryText1, nameText2, entryText2, judgeText;
    private Cursor mPlayers;

    /* PRE-RUN LIFECYCLE METHODS */
    //TODO: Determine which of this stuff belongs in onStart()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        refreshCursor();

        //Key-value setup
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (getIntent().getExtras().getBoolean(IS_NEW_GAME)) {
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.clear();
            prefEditor.apply(); //If no work, use .commit()
        }

        round = sharedPref.getInt("round", 1);
        player1 = sharedPref.getInt("player1", 0);
        player2 = sharedPref.getInt("player2", 1);
        judge = sharedPref.getInt("judge", 2);

        prompt = generatePrompt();
        entry1 = generateEntry();
        entry2 = generateEntry();

        //App bar setup
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_game));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set member TextViews
        promptText = (TextView) findViewById(R.id.text_prompt);
        nameText1 = (TextView) findViewById(R.id.text_name1);
        entryText1 = (TextView) findViewById(R.id.text_entry1);
        nameText2 = (TextView) findViewById(R.id.text_name2);
        entryText2 = (TextView) findViewById(R.id.text_entry2);
        judgeText = (TextView) findViewById(R.id.text_judge);

        refreshGameText();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Start timer
        Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.start();
    }

    /* APPLICATION RUNTIME METHODS */
    public void onButtonClick(View view) {
        //Voting buttons
        switch(view.getId()) {
            case R.id.button_vote1:
                //Give Player1 a point
                addPoint(player1);
                //Next player
                player2 = nextPlayer(player2);
                if (player2 == player1) player2 = nextPlayer(player2);
                break;
            case R.id.button_vote2:
                //Give Player2 a point
                addPoint(player2);
                //Next player
                player1 = player2;
                player2 = nextPlayer(player1);
                break;
        }

        //Setup next round
        if (view.getId() == R.id.button_vote1 || view.getId() == R.id.button_vote2) {
            prompt = generatePrompt();
            entry1 = generateEntry();
            entry2 = generateEntry();
            round++;
            refreshGameText();

            //FIXME: DELETE ME LATER
            //Prove that scoring works
            mPlayers.moveToFirst();
            int score1 = mPlayers.getInt(mPlayers.getColumnIndex(PlayerEntry.COLUMN_POINT_COUNT));
            mPlayers.moveToNext();
            int score2 = mPlayers.getInt(mPlayers.getColumnIndex(PlayerEntry.COLUMN_POINT_COUNT));
            mPlayers.moveToNext();
            int score3 = mPlayers.getInt(mPlayers.getColumnIndex(PlayerEntry.COLUMN_POINT_COUNT));
            Log.d("POTATO", "Charlie: " + score1 + ", Hang: " + score2 + ", George: " + score3);
        }
    }

    private void addPoint(int player) {
        mPlayers.moveToPosition(player);
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_POINT_COUNT, mPlayers.getInt(mPlayers.getColumnIndex(PlayerEntry.COLUMN_POINT_COUNT)) + 1);
        getContentResolver().update(
                PlayerEntry.CONTENT_URI,
                values,
                "_ID=?",
                new String[] {String.valueOf(player + 1)}
        );
        refreshCursor();
    }

    private int nextPlayer(int player) {
        player++;
        if (player >= mPlayers.getCount()) {
            player = 0;
        }
        return player;
    }

    private String generatePrompt() {
        final String[] prompt = new String[1];
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                prompt[0] = queryTextFile(R.raw.prompts);
                countDownLatch.countDown();
            }
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return prompt[0];
    }

    //Returns silly phrases (TODO by George)
    private String generateEntry() {
        //TODO query multiple text files on several threads to generate a full phrase
        final StringBuilder text = new StringBuilder();
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        final String[] phraseParts = new String[3]; //3 is just an arbitrary value here, is an array to store individual parts of the phrase
        phraseParts[1] = "";
        phraseParts[2] = "";
        Thread readWordOne = new Thread(new Runnable() {
            @Override
            public void run() {
                phraseParts[0] = queryTextFile(R.raw.phrases);
                countDownLatch.countDown();
            }
        });
        readWordOne.start();

        Thread readWord2 = new Thread(new Runnable() {
            @Override
            public void run() {
                phraseParts[1] = queryTextFile(R.raw.phrases2);
                countDownLatch.countDown();
            }
        });
        readWord2.start();

        Thread readWord3 = new Thread(new Runnable() {
            @Override
            public void run() {
                phraseParts[2] = queryTextFile(R.raw.phrases3);
                countDownLatch.countDown();
            }
        });
        readWord3.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return phraseParts[0] + " " + phraseParts[1] + " " + phraseParts[2];
    }

    private String queryTextFile(int id) {
        InputStream inputStream = getResources().openRawResource(id);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputStreamReader);
        Random random = new Random();

        int lineNum = random.nextInt(6) + 1; //Replace 6 with whatever the # of lines is
        String queriedWord = "";

        try {
            int x = 0;
            String line;

            while ((line = buffReader.readLine()) != null) {
                x++;
                if (x == lineNum) {
                    queriedWord = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "[FAILED TO QUERY WORD]";
        }
        return queriedWord;
    }


    //Refreshes all TextViews
    private void refreshGameText() {
        getSupportActionBar().setTitle(getResources().getString(R.string.round) + round);
        promptText.setText(prompt);
        entryText1.setText(entry1);
        entryText2.setText(entry2);

        mPlayers.moveToPosition(player1);
        nameText1.setText(
                mPlayers.getString(mPlayers.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME))
        );

        mPlayers.moveToPosition(player2);
        nameText2.setText(
                mPlayers.getString(mPlayers.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME))
        );

        mPlayers.moveToPosition(judge);
        judgeText.setText(
                getResources().getString(R.string.judge) + ": " +
                mPlayers.getString(mPlayers.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME))
        );
    }

    private void refreshCursor() {
        mPlayers = getContentResolver().query(
                PlayerEntry.CONTENT_URI,
                new String[] {PlayerEntry.COLUMN_PLAYER_NAME, PlayerEntry.COLUMN_POINT_COUNT},
                null, null,
                PlayerEntry._ID + " ASC"
        );
    }

    //APP BAR METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_scoreboard:
                //TODO: Show scoreboard (fullscreen dialog?)
                return true;
            case R.id.action_pause:
                //TODO: Show pause dialog, stop timer
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /* POST-RUN LIFECYCLE METHODS */
    @Override
    protected void onPause() {
        super.onPause();

        //Stop timer
        Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Save key-values
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("round", round);
        prefEditor.putInt("player1", player1);
        prefEditor.putInt("player2", player2);
        prefEditor.putInt("judge", judge);
        prefEditor.commit();
    }
}
