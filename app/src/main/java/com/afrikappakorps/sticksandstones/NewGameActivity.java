package com.afrikappakorps.sticksandstones;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
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
import android.widget.Toast;

import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract.PlayerEntry;

public class NewGameActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>,
    AddPlayerDialogFragment.AddUserDialogListener {

    private AddPlayersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);
        getContentResolver().delete(PlayerEntry.CONTENT_URI, null, null);

        //Toolbar setup
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_newgame));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView setup
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_addplayers);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new AddPlayersAdapter(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        //Swipe-to-delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = (int) viewHolder.itemView.getTag();
                Uri uri = PlayerEntry.CONTENT_URI;
                String whereClause = PlayerEntry._ID + "=?";
                String[] whereArgs = {String.valueOf(id)};
                getContentResolver().delete(uri, whereClause, whereArgs);
            }
        }).attachToRecyclerView(recyclerView);

        //Loader
        getLoaderManager().initLoader(0, null, this);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.button_addplayer:
                new AddPlayerDialogFragment().show(getFragmentManager(), "adduser");
                break;
            case R.id.button_creategame:
                if (mAdapter.getItemCount() < 3) {
                    Toast.makeText(getApplicationContext(), R.string.must_have_players, Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(GameActivity.IS_NEW_GAME, true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAddUserDialogPositiveClick(DialogFragment dialog, EditText editor) {
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_NAME, editor.getText().toString());
        values.put(PlayerEntry.COLUMN_POINT_COUNT, 0);
        getContentResolver().insert(PlayerEntry.CONTENT_URI, values);
    }

    //LOADER CALLBACK METHODS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, PlayerEntry.CONTENT_URI,
                new String[] {PlayerEntry.COLUMN_PLAYER_NAME, PlayerEntry.COLUMN_POINT_COUNT, PlayerEntry._ID},
                null, null, null
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
