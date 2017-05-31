package com.hillbilly.spendingtracker;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Created by craig on 08/05/17.
 */

public class SpendingStoreAdapter extends RecyclerView.Adapter {

    public static final String TAG = "SpendingAdapter";
    private SpendingStoreOpenHelper spendingData;
    private Cursor cursor;

    public SpendingStoreAdapter(Context context) {
        spendingData = new SpendingStoreOpenHelper(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

        return new SpendingViewHolder(view);
    }

    public void changeCursor(Cursor lCursor) {
        Log.v(TAG, "Cursor Changed too: " + lCursor.toString() + " with a count of " + lCursor.getCount());
        cursor = lCursor;
        notifyItemRangeChanged(0, getItemCount());

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.v(TAG, "Bind called on position " + position);

        if (cursor != null) {
            cursor.moveToPosition(position);
            Log.v(TAG, DatabaseUtils.dumpCurrentRowToString(cursor));
            Log.v(TAG, Arrays.toString(cursor.getColumnNames()));
            Log.v(TAG, cursor.getColumnName(cursor.getColumnIndex(SpendingStoreOpenHelper.Schema.DATEFIELD)));

            ((SpendingViewHolder) holder).bindDate(cursor.getString(cursor.getColumnIndex(SpendingStoreOpenHelper.Schema.DATEFIELD)));
            ((SpendingViewHolder) holder).bindDescription(cursor.getString(cursor.getColumnIndex(SpendingStoreOpenHelper.Schema.DESCRIPTIONFIELD)));
            ((SpendingViewHolder) holder).bindAmount(cursor.getString(cursor.getColumnIndex(SpendingStoreOpenHelper.Schema.AMOUNTFIELD)));
        }

    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public static class SpendingViewHolder extends RecyclerView.ViewHolder {
        TextView mDateText;
        TextView mDescriptionText;
        TextView mAmountText;

        String mDate, mDescription, mAmount;


        public SpendingViewHolder(View itemView) {
            super(itemView);

            mDateText = (TextView) itemView.findViewById(R.id.SpinnerWCView);
            mDescriptionText = (TextView) itemView.findViewById(R.id.DescriptionView);
            mAmountText = (TextView) itemView.findViewById(R.id.amountView);
        }

        public void bindDate(String date) {
            mDate = date;
            mDateText.setText(mDate);
        }

        public void bindDescription(String description) {
            mDescription = description;
            mDescriptionText.setText(mDescription);
        }

        public void bindAmount(String amount) {
            Log.v(TAG, "Bind Amount - " + amount);
            mAmount = amount;
            NumberFormat currencyFormater = NumberFormat.getCurrencyInstance();
            mAmountText.setText(currencyFormater.format(Double.parseDouble(mAmount)));
        }


    }
}
