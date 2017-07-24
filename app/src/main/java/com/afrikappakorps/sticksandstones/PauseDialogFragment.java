package com.afrikappakorps.sticksandstones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;

public class PauseDialogFragment extends DialogFragment {
    long stopTime = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pause)
                .setMessage(R.string.game_paused)
                .setNegativeButton(R.string.resume_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Chronometer timer = (Chronometer) getActivity().findViewById(R.id.timer);
                        timer.setBase(SystemClock.elapsedRealtime() + stopTime);
                        timer.start();
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
}
