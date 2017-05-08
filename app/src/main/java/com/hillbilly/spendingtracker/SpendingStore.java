package com.hillbilly.spendingtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by craig on 08/05/17.
 */

class SpendingStore extends SQLiteOpenHelper {

    public final static String SPENDINGTABLE = "spending";
    public final static String DATEFIELD = "date";
    public final static String DESCRIPTIONFIELD = "description";
    public final static String AMOUNTFIELD = "amount";
    private final static String SPENDING_CREATE_SQL = "CREATE TABLE " + SPENDINGTABLE + " (" + DATEFIELD + " DATE," + DESCRIPTIONFIELD + " TEXT," + AMOUNTFIELD + " NUMBER)";

    public SpendingStore(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SPENDING_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
