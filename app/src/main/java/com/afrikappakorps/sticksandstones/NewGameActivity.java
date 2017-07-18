package com.afrikappakorps.sticksandstones;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract;

public class NewGameActivity extends AppCompatActivity {

    private SticksAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_addplayers);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mAdapter = new SticksAdapter(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Setup toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_newgame));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.button_addplayer:
                ContentValues cv = new ContentValues();
                cv.put(SticksAndStonesContract.PlayerEntry.COLUMN_PLAYER_NAME, "bob");
                cv.put(SticksAndStonesContract.PlayerEntry.COLUMN_POINT_COUNT, 0);

                ContentResolver cr = getContentResolver();
                cr.insert(SticksAndStonesContract.PlayerEntry.CONTENT_URI, cv);
                mAdapter.setPlayerData();

                //TODO: Add player (to RecyclerView)
                break;
            case R.id.button_creategame:
                //TODO: Create new game
                break;
        }
    }
}
