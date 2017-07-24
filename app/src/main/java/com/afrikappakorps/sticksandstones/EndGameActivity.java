package com.afrikappakorps.sticksandstones;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract.PlayerEntry;

public class EndGameActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        //Toolbar setup
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_endgame));

        //ListView setup
        ListView playerList = (ListView) findViewById(R.id.list_endgame);
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.item_playerscore_final,
                null,
                new String[] {PlayerEntry.COLUMN_PLAYER_NAME, PlayerEntry.COLUMN_POINT_COUNT},
                new int[] {R.id.text_name, R.id.text_score},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        playerList.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getContentResolver().delete(PlayerEntry.CONTENT_URI, null, null);
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.button_endgame:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    //LOADER CALLBACK METHODS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, PlayerEntry.CONTENT_URI,
                new String[] {PlayerEntry.COLUMN_PLAYER_NAME, PlayerEntry.COLUMN_POINT_COUNT, PlayerEntry._ID},
                null, null,
                PlayerEntry.COLUMN_POINT_COUNT + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
