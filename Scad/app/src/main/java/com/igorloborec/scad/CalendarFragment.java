package com.igorloborec.scad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igorloborec.scad.data.IScadProvider;
import com.igorloborec.scad.data.PersonalCalendar;
import com.igorloborec.scad.data.WebScraperProvider.Helper;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Bikonja on 19.8.2015..
 */
public class CalendarFragment extends Fragment {

    public MainActivity getmActivity() {
        return mActivity;
    }

    public void setmActivity(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    protected MainActivity mActivity;

    protected View mRootView;
    protected View mProgressView;
    protected LinearLayout calendar_layout;
    protected TextView calendar_date_label;
    protected ListView calendar_item_list;

    protected GregorianCalendar mCalendar;

    public CalendarFragment() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mActivity.getResources().getInteger(android.R.integer.config_shortAnimTime);

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

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }

        setUp(rootView, args);
        return rootView;
    }

    protected void getControls() {
        mProgressView = mRootView.findViewById(R.id.progress_bar);
        calendar_layout = ((LinearLayout)mRootView.findViewById(R.id.calendar_layout));
        calendar_date_label = ((TextView)mRootView.findViewById(R.id.calendar_date_label));
        calendar_item_list = ((ListView)mRootView.findViewById(R.id.calendar_item_list));
    }

    public void setUp(View rootView, Bundle args) {
        mActivity = (MainActivity)getActivity();
        mRootView = rootView;

        getControls();
        calendar_date_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mActivity.getmWorkingDate().set(year, monthOfYear, dayOfMonth);
                        mActivity.getmCalendarFragmentCollectionPagerAdapter().notifyDataSetChanged();
                        mActivity.getmCalendarViewPager().setCurrentItem(CalendarFragmentCollectionPagerAdapter.FIRST_ITEM);
                        changeDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));
                    }
                }, mActivity.getmWorkingDate().get(Calendar.YEAR), mActivity.getmWorkingDate().get(Calendar.MONTH), mActivity.getmWorkingDate().get(Calendar.DATE)).show();
            }
        });

        Integer add = args.getInt("ADD");
        mCalendar = (GregorianCalendar)mActivity.getmWorkingDate().clone();
        mCalendar.add(Calendar.DATE, add);

        changeDate(mCalendar);
    }

    public void changeDate(final GregorianCalendar calendar) {
        mCalendar = (GregorianCalendar)calendar.clone();
        calendar_date_label.setText(String.format(Helper.Preferences.GetDateFormatString(mActivity), mCalendar));
        new ChangeDateAsyncTask().execute();
    }

    private class ChangeDateAsyncTask extends AsyncTask<Void, Void, PersonalCalendar> {

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected PersonalCalendar doInBackground(Void... params) {
            PersonalCalendar personalCalendar = null;

            try {
                personalCalendar = mActivity.getmScadProvider().GetPersonalCalendar(mCalendar);
            } catch (AccessDeniedException e) {
                e.printStackTrace();

                mActivity.RefreshToken();
            }

            return personalCalendar;
        }

        @Override
        protected void onPostExecute(PersonalCalendar personalCalendar) {
            /*GregorianCalendar firstDayOfWeek = (GregorianCalendar)mCalendar.clone();
            firstDayOfWeek.add(Calendar.DATE, -1);
            firstDayOfWeek.set(Calendar.DAY_OF_WEEK, mCalendar.getFirstDayOfWeek());
            firstDayOfWeek.add(Calendar.DATE, 1);

            GregorianCalendar lastDayOfWeek = (GregorianCalendar)firstDayOfWeek.clone();
            lastDayOfWeek.add(Calendar.DATE, 6);

            //calendar_date_label.setText(String.format("%tY-%<tm-%<td  -  %tY-%<tm-%<td", firstDayOfWeek, lastDayOfWeek));*/
            if (personalCalendar != null) {
                calendar_date_label.setText(String.format(Helper.Preferences.GetDateFormatString(mActivity), mCalendar));

                CalendarEntryAdapter adapter = new CalendarEntryAdapter(mActivity, personalCalendar.get_entries(mCalendar));
                calendar_item_list.setEmptyView(mRootView.findViewById(R.id.calendar_empy));
                calendar_item_list.setAdapter(adapter);
            }

            showProgress(false);
        }
    }
}