package com.afrikappakorps.sticksandstones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class AddPlayerDialogFragment extends DialogFragment {
    AddUserDialogListener listener;
    public interface AddUserDialogListener {
        public void onAddUserDialogPositiveClick(DialogFragment dialog, EditText editor);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText nameEditor = new EditText(getActivity());
        nameEditor.setHint(R.string.name);
        //Builder options
        builder.setTitle(R.string.add_player)
                .setIcon(android.R.drawable.ic_input_add)
                .setView(nameEditor)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onAddUserDialogPositiveClick(AddPlayerDialogFragment.this, nameEditor);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddUserDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement AddUserDialogListener");
        }
    }
}
