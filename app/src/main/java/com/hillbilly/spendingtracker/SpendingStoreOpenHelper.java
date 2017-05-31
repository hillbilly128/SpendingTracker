package com.hillbilly.spendingtracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by craig on 08/05/17.
 */

class SpendingStoreOpenHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "SpendingTracker.db";
    public final static int DATABASE_VERSION = 2;
    public static final String TAG = "OpenHelper";
    private static SpendingStoreOpenHelper openHelper;

    public SpendingStoreOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(TAG, "OpenHelper created Context Used - " + context.toString());
    }

    public static SQLiteDatabase getReadableSpending(Context context) {
        //Check static openHelper
        if (context == null) {
            throw new NullPointerException("Null Context");
        }

        if (openHelper == null) {
            openHelper = new SpendingStoreOpenHelper(context);
        }

        return openHelper.getReadableDatabase();
    }

    public static SQLiteDatabase getWriteableSpending(Context context) {
        //Check static openHelper
        if (context == null) {
            throw new NullPointerException("Null Context");
        }

        if (openHelper == null) {
            openHelper = new SpendingStoreOpenHelper(context);
        }

        return openHelper.getWritableDatabase();
    }

    public static SpendingStoreOpenHelper getOpenHelper(Context context) {
        //Check static openHelper
        if (context == null) {
            throw new NullPointerException("Null Context");
        }

        if (openHelper == null) {
            openHelper = new SpendingStoreOpenHelper(context);
        }

        return openHelper;
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
        String query = Schema.SPENDING_SELECT_ALL_SQL + " WHERE " + where;
        Log.v(TAG, query);
        SQLiteDatabase db = SpendingStoreOpenHelper.openHelper.getReadableDatabase();
        Log.v(TAG, db.toString());
        return db.rawQuery(query, null);



    }

    public Cursor getSpinnerWeeks(String[] columns) {
        String query = "SELECT strftime('%W', " + Schema.DATEFIELD + ") as " + columns[0] + ",";
        query = query + "strftime('%Y-%m-%d', (strftime('%J'," + Schema.DATEFIELD + ")-strftime('%w'," + Schema.DATEFIELD + ")+1)) as " + columns[1] + ",";
        query = query + "sum(" + Schema.AMOUNTFIELD + ") as " + columns[2] + " ";
        query = query + "FROM " + Schema.SPENDINGTABLE + " GROUP BY strftime('%W', " + Schema.DATEFIELD + ") ORDER BY strftime('%W', " + Schema.DATEFIELD + ")";
        Log.v(TAG, query);
        SQLiteDatabase db = SpendingStoreOpenHelper.openHelper.getReadableDatabase();
        Log.v(TAG, db.toString());
        return db.rawQuery(query, null);
    }

    public int getItemCount() {
        Cursor c = SpendingStoreOpenHelper.openHelper.getReadableDatabase().rawQuery(Schema.SPENDING_SELECT_ALL_SQL, null);
        Log.v(TAG, "Spending Item Count = " + c.getCount());
        return c.getCount();
    }

    static class Schema implements BaseColumns {

        public final static String SPENDINGTABLE = "Spending";

        public final static String DATEFIELD = "date";
        public final static String DESCRIPTIONFIELD = "description";
        public final static String AMOUNTFIELD = "amount";

        public final static String CALCWEEKNUMFIELD = "weeknum";
        public final static String CALCWEEKCOMMFIELD = "weekComm";
        public final static String CALCTOTALSPENDING = "totalSpending";
        public final static String ALL_DELETE_SQL = "DROP TABLE IF EXISTS " + SPENDINGTABLE;
        private final static String SPENDING_SELECT_ALL_SQL = "SELECT " + _ID + ", " + DATEFIELD + ", " + DESCRIPTIONFIELD + ", " + AMOUNTFIELD + " FROM " + SPENDINGTABLE;
        private final static String SPENDING_CREATE_SQL = "CREATE TABLE " + SPENDINGTABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATEFIELD + " TEXT," + DESCRIPTIONFIELD + " TEXT," + AMOUNTFIELD + " NUMBER)";

    }
}
