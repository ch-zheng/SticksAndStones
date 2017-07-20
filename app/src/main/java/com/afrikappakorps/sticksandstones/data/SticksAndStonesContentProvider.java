package com.afrikappakorps.sticksandstones.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SticksAndStonesContentProvider extends ContentProvider {
    public static final int PLAYERS = 100;
    public static final int A_PLAYER = 101;

    private SticksAndStonesDbHelper mDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(SticksAndStonesContract.AUTHORITY, SticksAndStonesContract.PATH_PLAYERS, PLAYERS);
        uriMatcher.addURI(SticksAndStonesContract.AUTHORITY, SticksAndStonesContract.PATH_PLAYERS + "/#", A_PLAYER);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new SticksAndStonesDbHelper(context);
        return true;
    }

    /**
     * Queries the player database
     *
     * @param uri           uri of where to query, should be players
     * @param projection    columns which the data returned by the query will contain
     * @param selection     something which determines what will be queried (like: "_id=?")
     * @param selectionArgs an array of values which satisfy the selection
     * @param sortOrder     order in which results are sorted. ex: COLUMN_SCORE + " DESC" or COLUMN_SCORE + " ASC"
     * @return              Cursor containing the data specified
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case PLAYERS:
                retCursor = db.query(
                        SticksAndStonesContract.PlayerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("noob");
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * Inserts data into the database
     *
     * @param uri       should be the players one
     * @param values    ContentValues object containing data for the insert
     * @return          Uri of the table bcs. I'm lazy
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PLAYERS: {
                db.insert(SticksAndStonesContract.PlayerEntry.TABLE_NAME,
                        null,
                        values);
                break;
            }
            default:
                throw new UnsupportedOperationException("lul noob");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    /**
     * Deletes stuff from the database
     *
     * @param uri           Uri lul
     * @param selection     something which determines what will be deleted (like: "_id=?")
     * @param selectionArgs selectionArgs an array of values which satisfy the selection
     * @return              returns int representing how many rows were deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int numberOfRowsDeleted;

        switch (match) {
            case PLAYERS:
                numberOfRowsDeleted = db.delete(SticksAndStonesContract.PlayerEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("lul");
        }

        if (numberOfRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowIdChanged;

        switch (match) {
            case PLAYERS:
                rowIdChanged = db.update(SticksAndStonesContract.PlayerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("ffs");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowIdChanged;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
