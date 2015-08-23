package com.igorloborec.scad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igorloborec.scad.data.IScadProvider;
import com.igorloborec.scad.data.PersonalCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Bikonja on 19.8.2015..
 */
public class CalendarFragment {

    protected View mRootView;
    protected View mProgressView;
    protected GregorianCalendar mCalendar = new GregorianCalendar();
    protected IScadProvider mScadProvider;

    protected LinearLayout calendar_layout;
    protected TextView calendar_date_label;
    protected ListView calendar_item_list;

    public CalendarFragment(final View rootView, IScadProvider scadProvider, GregorianCalendar gregorianCalendar) {
        mRootView = rootView;
        mScadProvider = scadProvider;
        mCalendar = gregorianCalendar != null ? gregorianCalendar : new GregorianCalendar();

        getControls();

        // HACK: For testing
        //mCalendar.set(2015, Calendar.JUNE, 17);

        changeDate(mCalendar);

        calendar_date_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        changeDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE)).show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mRootView.getResources().getInteger(android.R.integer.config_shortAnimTime);

            calendar_layout.setVisibility(show ? View.GONE : View.VISIBLE);
            calendar_layout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    calendar_layout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            calendar_layout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    protected void getControls() {
        mProgressView = mRootView.findViewById(R.id.progress_bar);
        calendar_layout = ((LinearLayout)mRootView.findViewById(R.id.calendar_layout));
        calendar_date_label = ((TextView) mRootView.findViewById(R.id.calendar_date_label));
        calendar_item_list = ((ListView) mRootView.findViewById(R.id.calendar_item_list));
    }

    public void changeDate(GregorianCalendar calendar) {
        mCalendar = calendar;

        new ChangeDateAsyncTask().execute();
    }

    private class ChangeDateAsyncTask extends AsyncTask<Void, Void, PersonalCalendar> {

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected PersonalCalendar doInBackground(Void... params) {
            return mScadProvider.GetPersonalCalendar(mCalendar);
        }

        @Override
        protected void onPostExecute(PersonalCalendar personalCalendar) {
            GregorianCalendar firstDayOfWeek = (GregorianCalendar)mCalendar.clone();
            firstDayOfWeek.add(Calendar.DATE, -1);
            firstDayOfWeek.set(Calendar.DAY_OF_WEEK, mCalendar.getFirstDayOfWeek());
            firstDayOfWeek.add(Calendar.DATE, 1);

            GregorianCalendar lastDayOfWeek = (GregorianCalendar)firstDayOfWeek.clone();
            lastDayOfWeek.add(Calendar.DATE, 6);

            calendar_date_label.setText(String.format("%tY-%<tm-%<td  -  %tY-%<tm-%<td", firstDayOfWeek, lastDayOfWeek));

            CalendarEntryAdapter adapter = new CalendarEntryAdapter(mRootView.getContext(), personalCalendar.get_entries());
            calendar_item_list.setAdapter(adapter);

            showProgress(false);
        }
    }
}