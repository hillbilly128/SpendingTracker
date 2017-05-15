package com.hillbilly.spendingtracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by craig on 08/05/17.
 */

public class SpendingStoreAdapter extends RecyclerView.Adapter {

    private SpendingStoreOpenHelper spendingData;

    public SpendingStoreAdapter(Context context) {
        spendingData = new SpendingStoreOpenHelper(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

        return new SpendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Cursor c = spendingData.getRecords("WHERE " + SpendingStoreOpenHelper.Schema._ID + "=" + position);
        ((SpendingViewHolder) holder).bindDate(c.getString(c.getColumnIndex(SpendingStoreOpenHelper.Schema.DATEFIELD)));
        ((SpendingViewHolder) holder).bindDescription(c.getString(c.getColumnIndex(SpendingStoreOpenHelper.Schema.DESCRIPTIONFIELD)));
        ((SpendingViewHolder) holder).bindAmount(c.getString(c.getColumnIndex(SpendingStoreOpenHelper.Schema.AMOUNTFIELD)));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SpendingViewHolder extends RecyclerView.ViewHolder {
        TextView mDateText;
        TextView mDescriptionText;
        TextView mAmountText;

        String mDate, mDescription, mAmount;


        public SpendingViewHolder(View itemView) {
            super(itemView);

            mDateText = (TextView) itemView.findViewById(R.id.dateView);
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
            mAmount = amount;
            mAmountText.setText(mAmount);
        }


    }
}
