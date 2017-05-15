package com.hillbilly.spendingtracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by craig on 08/05/17.
 */

class SpendingStoreOpenHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "SpendingTracker.db";
    public final static int DATABASE_VERSION = 2;

    public SpendingStoreOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Schema.SPENDING_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Schema.ALL_DELETE_SQL);
        onCreate(db);
    }

    public Cursor getAllRecords() {
        return getRecords(null);
    }

    public Cursor getRecords(String where) {
        return getReadableDatabase().rawQuery(Schema.SPENDING_SELECT_ALL_SQL + " " + where, null);
    }

    public int getItemCount() {
        return getReadableDatabase().rawQuery(Schema.SPENDING_COUNT, null).getInt(0);
    }

    static class Schema implements BaseColumns {

        public final static String SPENDINGTABLE = "Spending";
        public final static String ALL_DELETE_SQL = "DROP TABLE IF EXISTS " + SPENDINGTABLE;
        public final static String DATEFIELD = "date";
        public final static String DESCRIPTIONFIELD = "description";
        public final static String AMOUNTFIELD = "amount";
        private final static String SPENDING_COUNT = "SELECT COUNT(*) FROM " + SPENDINGTABLE;
        private final static String SPENDING_SELECT_ALL_SQL = "SELECT * FROM " + SPENDINGTABLE;
        private final static String SPENDING_CREATE_SQL = "CREATE TABLE " + SPENDINGTABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATEFIELD + " DATE," + DESCRIPTIONFIELD + " TEXT," + AMOUNTFIELD + " NUMBER)";
    }

}
