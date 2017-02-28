package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "EXTRA_DATE";
    @BindView(R.id.dialog_date_picker)
    DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_dialog_date, null);
        ButterKnife.bind(this,view);
        mDatePicker.init(mYear, mMonth, mDay, null);
        mDatePicker.setMinDate(calendar.getTimeInMillis());

        calendar.add(Calendar.DAY_OF_MONTH, QueryPreferences.getCountLoadedDays(getContext()));
        mDatePicker.setMaxDate(calendar.getTimeInMillis());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.select_date)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).
                                getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
