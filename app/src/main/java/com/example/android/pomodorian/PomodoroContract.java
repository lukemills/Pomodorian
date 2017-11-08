package com.example.android.pomodorian;

import android.provider.BaseColumns;

/**
 * The PomodoroContract class stores the constants used in database operations
 */
public class PomodoroContract {

    // Empty constructor in case someone accidentally attempts to instantiate a InventoryContract object
    public PomodoroContract() {
    }

    public static final class PomodoroEntity implements BaseColumns {

        public static final String SESSIONS_TABLE_NAME = "inStock";

        // Columns containing Strings in the database
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_WORK_DURATION = "work_duration";
        public static final String COLUMN_BREAK_DURATION = "break_duration";
        public static final String COLUMN_STREAKS = "streaks";

    }
}

