package com.afrikappakorps.sticksandstones;

import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity {

    private TextView mTimerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mTimerView = (TextView) findViewById(R.id.text_timer);

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                mTimerView.setText(String.valueOf(l / 1000));
            }

            @Override
            public void onFinish() {
                String done = "Done!";
                mTimerView.setText(done);
            }

        }.start();
    }
}
