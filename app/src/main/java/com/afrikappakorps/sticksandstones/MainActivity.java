package com.afrikappakorps.sticksandstones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Button click
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.button_newgame:
                startActivity(new Intent(this, NewGameActivity.class));
                break;
            case R.id.button_resumegame:
                //TODO: Resume existing game
                break;
            case R.id.button_settings:
                //TODO: Goto 'settings menu'
                break;
        }
    }
}
