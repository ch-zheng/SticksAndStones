package com.afrikappakorps.sticksandstones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract.PlayerEntry;

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
                new NewGameDialogFragment().show(getFragmentManager(), "new_game");
                break;
            case R.id.button_resumegame:
                int players = getContentResolver().query(PlayerEntry.CONTENT_URI,
                        new String[] {PlayerEntry._ID},
                        null, null, null).getCount();
                if (players < 3) {
                    Toast.makeText(getApplicationContext(), R.string.no_existing_game, Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(GameActivity.IS_NEW_GAME, false);
                startActivity(intent);
                break;
        }
    }

    public static class NewGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.create_game)
                    .setMessage(R.string.new_game_message)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getActivity(), NewGameActivity.class));
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            return builder.create();
        }
    }
}