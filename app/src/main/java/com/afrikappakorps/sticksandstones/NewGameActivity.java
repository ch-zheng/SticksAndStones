package com.afrikappakorps.sticksandstones;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract;

public class NewGameActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>,
    AddUserDialogFragment.AddUserDialogListener {

    private SticksAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);

        //Setup toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_newgame));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recyclerview setup
        mRecyclerView = (RecyclerView) findViewById(R.id.list_addplayers);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new SticksAdapter(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                // Here is where you'll implement swipe to delete
                int id = (int) viewHolder.itemView.getTag();

                Uri uri = SticksAndStonesContract.PlayerEntry.CONTENT_URI;

                String whereClause = "_id=?";
                String[] whereArgs = {String.valueOf(id)};

                getContentResolver().delete(uri, whereClause, whereArgs);
                //getSupportLoaderManager().restartLoader(0, null, NewGameActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        //Loader
        getLoaderManager().initLoader(0, null, this);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.button_addplayer:
                new AddUserDialogFragment().show(getFragmentManager(), "adduser");
                break;
            case R.id.button_creategame:
                //TODO: Create new game
                break;
        }
    }

    //Dialog method
    public void onAddUserDialogPositiveClick(DialogFragment dialog, EditText editor) {
        ContentValues values = new ContentValues();
        values.put(SticksAndStonesContract.PlayerEntry.COLUMN_PLAYER_NAME, editor.getText().toString());
        values.put(SticksAndStonesContract.PlayerEntry.COLUMN_POINT_COUNT, 0);
        getContentResolver().insert(SticksAndStonesContract.PlayerEntry.CONTENT_URI, values);
    }

    //LOADER CALLBACK METHODS
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, SticksAndStonesContract.PlayerEntry.CONTENT_URI, null, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
