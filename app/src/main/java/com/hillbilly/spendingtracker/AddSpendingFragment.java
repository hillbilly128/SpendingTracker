package com.hillbilly.spendingtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.NumberFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.FieldPosition;

import static android.content.ContentValues.TAG;

public class AddSpendingFragment extends Fragment {

    private OnOkButtonClickListener mListener;
    private EditText editDate;
    private EditText editDesc;
    private EditText editCost;

    public AddSpendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static AddSpendingFragment newInstance() {
        AddSpendingFragment fragment = new AddSpendingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_spending, container, false);

        editDate = ((EditText) view.findViewById(R.id.editDate));
        editDesc = ((EditText) view.findViewById(R.id.editDescription));
        editCost = ((EditText) view.findViewById(R.id.editCost));

        editCost.addTextChangedListener(new TextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length() - editCost.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the digits beneath the mask, so we always work with a raw string with only digits
                double value = Double.parseDouble(string.replaceAll("[^\\d]", ""));

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag && !backspacingFlag) {
                    //we will edit. next call on this textWatcher will be ignored
                    editedFlag = true;
                    //here is the core. we substring the raw digits and add the mask as convenient
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                    String ans = currencyFormat.format(value);
                    editCost.setText(ans);
                    //we deliver the cursor to its original position relative to the end of the string
                    editCost.setSelection(editCost.getText().length() - cursorComplement);
                }
            }
        });

        Button dateButton = ((Button) view.findViewById(R.id.dateButton));
        Button okButton = ((Button) view.findViewById(R.id.okButton));

        dateButton.setOnClickListener((View.OnClickListener) getActivity());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onOkButtonClick(editDate.getText().toString(), editDesc.getText().toString(), editCost.getText().toString());
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOkButtonClickListener) {
            mListener = (OnOkButtonClickListener) context;
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
    public interface OnOkButtonClickListener {
        void onOkButtonClick(String date, String description, String amount);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            EditText editDate = ((EditText) getActivity().findViewById(R.id.editDate));

            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);

            StringBuffer date = DateFormat.getDateInstance(DateFormat.SHORT).format(c, new StringBuffer(""), new FieldPosition(DateFormat.DATE_FIELD));
            Log.v(TAG, date.toString());
            editDate.setText(date.toString());
        }
    }

}
