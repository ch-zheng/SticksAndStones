package com.afrikappakorps.sticksandstones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract.PlayerEntry;

public class ScoreboardDialogFragment extends DialogFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter mAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_playerscore, null, null, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        getLoaderManager().initLoader(0, null, this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.scoreboard)
                .setAdapter(mAdapter, null)
                .setNegativeButton(R.string.close, null);
        return builder.create();
    }

    //LOADER CALLBACK METHODS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                PlayerEntry.CONTENT_URI,
                new String[] {PlayerEntry.COLUMN_PLAYER_NAME, PlayerEntry.COLUMN_POINT_COUNT, PlayerEntry._ID},
                null, null,
                PlayerEntry.COLUMN_POINT_COUNT + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursorAndColumns(data,
                new String[] {PlayerEntry.COLUMN_PLAYER_NAME, PlayerEntry.COLUMN_POINT_COUNT},
                new int[] {R.id.text_name, R.id.text_score}
        );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
