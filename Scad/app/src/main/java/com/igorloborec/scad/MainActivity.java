/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;

import com.igorloborec.scad.authentication.AccountGeneral;
import com.igorloborec.scad.data.IScadProvider;
import com.igorloborec.scad.data.WebScraperProvider.WebScraperProvider;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private AccountManager mAccountManager;
    private Account mAccount;
    private String mAccountToken = null;
    private String mPortalUrl;

    public String getmAccountToken() {
        return mAccountToken;
    }

    public String getmPortalUrl() {
        return mPortalUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountManager = AccountManager.get(this);
        accountLoginOrCreate(this);

        setupView(this);
    }

    private void setupView(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAccountToken != null && !mAccountToken.isEmpty()) {
                    activity.setContentView(R.layout.activity_main);

                    mNavigationDrawerFragment = (NavigationDrawerFragment)
                            getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
                    mTitle = activity.getTitle();

                    // Set up the drawer.
                    mNavigationDrawerFragment.setUp(
                            R.id.navigation_drawer,
                            (DrawerLayout) activity.findViewById(R.id.drawer_layout));
                } else {
                    activity.setContentView(R.layout.activity_main_not_logged_in);
                    Button login_button = (Button) activity.findViewById(R.id.login_button);
                    login_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            accountLoginOrCreate(activity);
                        }
                    });
                }
            }
        });
    }

    private void accountLoginOrCreate(final Activity activity) {
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        if (availableAccounts.length == 0) {
            addNewAccount(activity);
        } else {
            mAccount = availableAccounts[0];

            final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(mAccount, AccountGeneral.AUTHTOKEN_TYPE_DEFAULT, null, this, null, null);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bundle bnd = future.getResult();

                        mAccountToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                        mPortalUrl = mAccountManager.getUserData(mAccount, AccountGeneral.PORTAL_ADDRESS);

                        setupView(activity);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "Get token failed: " + e.getMessage());
                        e.printStackTrace();
                        setupView(activity);
                    }
                }
            }).start();
        }
    }

    private void addNewAccount(final Activity activity) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_DEFAULT, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();

                    accountLoginOrCreate(activity);
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Account create failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void logoutAccount() {
        if (mAccount != null) {
            mAccountManager.clearPassword(mAccount);
        }
        mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, mAccountToken);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        int normalizedPosition = position + 1;

        if (normalizedPosition == getResources().getInteger(R.integer.drawer_index_logout)) {
            logoutAccount();
            finish();
        }
            else {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(normalizedPosition))
                    .commitAllowingStateLoss();
        }
    }



    public void onSectionAttached(int number) {
        if (number == getResources().getInteger(R.integer.drawer_index_calendar)) {
            mTitle = getString(R.string.title_calendar);
        }
        else if (number == getResources().getInteger(R.integer.drawer_index_settings)) {
            mTitle = getString(R.string.title_settings);
        }
        else if (number == getResources().getInteger(R.integer.drawer_index_logout)) {
            mTitle = getString(R.string.title_logout);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            IScadProvider scadProvider = new WebScraperProvider((MainActivity)getActivity());

            View rootView = null;

            if (section == getResources().getInteger(R.integer.drawer_index_calendar)) {
                rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
                CalendarFragment.setUp(rootView, scadProvider);
            }
            else if (section == getResources().getInteger(R.integer.drawer_index_settings)) {
                rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            }
            else if (section == getResources().getInteger(R.integer.drawer_index_logout)) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
            } else {
                rootView = inflater.inflate(R.layout.fragment_error, container, false);
            }

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
