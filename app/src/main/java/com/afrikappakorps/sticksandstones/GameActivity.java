package com.afrikappakorps.sticksandstones;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract;

public class GameActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String IS_NEW_GAME = "isNewGame";
    private int round, player1, player2, judge;
    private Cursor mPlayers;

    /* PRE-RUN LIFECYCLE METHODS */
    //TODO: Determine which of this stuff belongs in onStart()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getLoaderManager().initLoader(0, null, this);

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

        //App bar setup
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_game));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Round " + round); //FIXME: Move to setGameText()
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
                //TODO: Give Player1 a point
                player2 = wrapIncrement(player2, mPlayers.getCount());
                if (player2 == player1) player2 = wrapIncrement(player2, mPlayers.getCount());
                break;
            case R.id.button_vote2:
                //TODO: Give Player2 a point
                player1 = player2;
                player2 = wrapIncrement(player1, mPlayers.getCount());
                break;
        }
        round++;
        //TODO: Reset the round
    }

    //TODO: Finish this
    private void setGameText() {
        getSupportActionBar().setTitle("Round " + round);
    }

    private int wrapIncrement(int input, int exclusiveLimit) {
        input++;
        if (input >= exclusiveLimit) {
            input = 0;
        }
        return input;
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

    //LOADER METHODS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //FIXME: More specific parameters?
        return new CursorLoader(this, SticksAndStonesContract.PlayerEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlayers = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlayers = null;
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
    }
}
