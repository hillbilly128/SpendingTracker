package com.hillbilly.spendingtracker;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddButtonClickListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";
    private OnAddButtonClickListener mListener;
    private Spinner dateSelect;

    public MainFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Log.v(TAG, "View Inflated");

        Log.v(TAG, "Spinner created");
        dateSelect = ((Spinner) view.findViewById(R.id.dateSelect));

        final String[] spinnerFields = {SpendingStoreOpenHelper.Schema._ID, SpendingStoreOpenHelper.Schema.CALCWEEKCOMMFIELD, SpendingStoreOpenHelper.Schema.CALCTOTALSPENDING};
        final int[] spinnerIDs = {0, R.id.SpinnerWCView, R.id.SpinnerTotalView};

        final SimpleCursorAdapter adaptor = new SimpleCursorAdapter(this.getActivity(), R.layout.date_select_spinner_item, SpendingStoreOpenHelper.getOpenHelper(getActivity()).getSpinnerWeeks(spinnerFields), spinnerFields, spinnerIDs, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater mInflater = LayoutInflater.from(context);

                return mInflater.inflate(R.layout.date_select_spinner_item, null);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                Log.v(TAG, "Bind View - " + Arrays.toString(spinnerIDs));
                Log.v(TAG, "Bind View - " + view.findViewById(spinnerIDs[1]).toString());
                Log.v(TAG, "Bind View - " + view.findViewById(spinnerIDs[2]).toString());
                ((TextView) view.findViewById(spinnerIDs[1])).setText(cursor.getString(cursor.getColumnIndex(spinnerFields[1])));


                ((TextView) view.findViewById(spinnerIDs[2])).setText(NumberFormat.getCurrencyInstance().format(cursor.getDouble(cursor.getColumnIndex(spinnerFields[2]))));
            }
        };

        dateSelect.setAdapter(adaptor);

        Log.v(TAG, "Setting RecyclerView");
        final RecyclerView recyclerView = ((RecyclerView) view.findViewById(R.id.SpendingList));
        Log.v(TAG, recyclerView.toString());

        Log.v(TAG, "Setting layout manager");
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        Log.v(TAG, "setting Adapter");
        final SpendingStoreAdapter spendingStoreAdapter = new SpendingStoreAdapter(recyclerView.getContext());
        recyclerView.setAdapter(spendingStoreAdapter);

        Log.v(TAG, "setting ImageButton");
        Button addButton = ((Button) view.findViewById(R.id.addButton));
        Log.v(TAG, addButton.toString());
        addButton.setOnClickListener(new OnClickAddButton());

        dateSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adaptor.getCursor().moveToPosition(position);
                //Toast.makeText(getActivity(), "Week# - "+adaptor.getCursor().getString(adaptor.getCursor().getColumnIndex(spinnerFields[0]))+" Amount:"+adaptor.getCursor().getString(adaptor.getCursor().getColumnIndex(spinnerFields[2])), Toast.LENGTH_SHORT).show();
                String where = "strftime('%W'," + SpendingStoreOpenHelper.Schema.DATEFIELD + ") = '" + adaptor.getCursor().getString(adaptor.getCursor().getColumnIndex(spinnerFields[0])) + "'";
                Log.v(TAG, "Where Clause - " + where);
                spendingStoreAdapter.changeCursor(SpendingStoreOpenHelper.getOpenHelper(getActivity()).getRecords(where));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddButtonClickListener) {
            mListener = (OnAddButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOkButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddButtonClickListener {
        void onAddButtonClick();
    }

    private class OnClickAddButton implements View.OnClickListener {

        OnClickAddButton() {
            super();
            Log.v(TAG, "Add button click listener created");
        }

        @Override
        public void onClick(View v) {
            Log.v(TAG, "Add Button Clicked");

            if (mListener != null) {
                mListener.onAddButtonClick();
            }
        }
    }
}
