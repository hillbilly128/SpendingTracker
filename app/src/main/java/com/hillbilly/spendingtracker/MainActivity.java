package com.hillbilly.spendingtracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity implements MainFragment.OnAddButtonClickListener, AddSpendingFragment.OnOkButtonClickListener, View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Main Activity";
    private FragmentManager fragmentManager;

    private MainFragment mainFragment;
    private AddSpendingFragment addSpendingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mainFragment = MainFragment.newInstance();
        addSpendingFragment = AddSpendingFragment.newInstance();

        fragmentTransaction.add(R.id.fragmentContainer, mainFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onAddButtonClick() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.replace(R.id.fragmentContainer, addSpendingFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onOkButtonClick(String date, String description, String amount) {
        Log.v(TAG, "Adding Spending clicked");
        ContentValues cv = new ContentValues();
        try {
            Log.v(TAG, DateFormat.getDateFormat(this).parse(date).toString());
            String dateValue = DateFormat.format("yyyy-MM-dd", DateFormat.getDateFormat(this).parse(date)).toString();
            Log.v(TAG, "Main Activity ok Button Click " + dateValue);
            cv.put(SpendingStoreOpenHelper.Schema.DATEFIELD, dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cv.put(SpendingStoreOpenHelper.Schema.DESCRIPTIONFIELD, description);

        //Clean away formatting and store
        cv.put(SpendingStoreOpenHelper.Schema.AMOUNTFIELD, Double.parseDouble(amount.replaceAll("[^\\d]", "")));


        long result = SpendingStoreOpenHelper.getWriteableSpending(this).insert(SpendingStoreOpenHelper.Schema.SPENDINGTABLE, null, cv);
        if (result > -1) {
            Toast.makeText(this, R.string.add_record_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.add_record_fail, Toast.LENGTH_SHORT).show();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.replace(R.id.fragmentContainer, mainFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {
        DialogFragment datePicker = new AddSpendingFragment.DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
