package com.afrikappakorps.sticksandstones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);

        //Setup toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_newgame));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.button_addplayer:
                //TODO: Add player (to RecyclerView)
                break;
            case R.id.button_creategame:
                //TODO: Create new game
                break;
        }
    }
}
