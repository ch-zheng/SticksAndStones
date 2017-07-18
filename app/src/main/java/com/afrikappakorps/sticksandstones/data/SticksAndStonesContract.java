package com.afrikappakorps.sticksandstones.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SticksAndStonesContract {
    public static final String AUTHORITY = "com.afrikappakorps.sticksandstones";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_PLAYERS = "players";

    public static final class PlayerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYERS).build();
        public static final String TABLE_NAME = "players";
        public static final String COLUMN_PLAYER_NAME = "name";
        public static final String COLUMN_POINT_COUNT = "points";
    }
}
