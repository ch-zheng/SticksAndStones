package com.afrikappakorps.sticksandstones;

import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private TextView mTimerView;
    public static final String IS_NEW_GAME = "isNewGame";

    private int match, round, player1, player2, judge;
    public static final String MATCH_KEY = "match", ROUND_KEY = "round",
        PLAYER1_KEY = "player1", PLAYER2_KEY = "player2", JUDGE_KEY = "judge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mTimerView = (TextView) findViewById(R.id.text_timer);

        //Variable setup
        if (getIntent().getExtras().getBoolean(IS_NEW_GAME)) {
            //TODO: Save shit in the database
        }

        //App bar setup
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_game));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Match " + match + ", Round " + round);

        //Timer setup
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long length) {
                mTimerView.setText(String.valueOf(length / 1000));
            }

            @Override
            public void onFinish() {
                String done = "Done!";
                mTimerView.setText(done);
            }

        }.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MATCH_KEY, match);
        outState.putInt(ROUND_KEY, round);
        outState.putInt(PLAYER1_KEY, player1);
        outState.putInt(PLAYER2_KEY, player2);
        outState.putInt(JUDGE_KEY, judge);
    }
}
