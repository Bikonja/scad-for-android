/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igorloborec.scad.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * A login screen that offers login via username/password.
 */
public class ScadAuthenticatorActivity extends AccountAuthenticatorActivity {
    private static final String LOG_TAG = ScadAuthenticatorActivity.class.getSimpleName();

    private final int REQ_SIGNUP = 1;

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String PARAM_USER_PASS = "USER_PASS";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mPortalAddressView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scad_authenticator);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username_field);
        mPasswordView = (EditText) findViewById(R.id.password_field);
        mPortalAddressView = (EditText) findViewById(R.id.address_field);

        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null) {
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_DEFAULT;
        }

        if (accountName != null) {
            ((TextView)findViewById(R.id.username_field)).setText(accountName);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mPortalAddressView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String portalAddress = mPortalAddressView.getText().toString();
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid portalAddress, if the user entered one
        if (TextUtils.isEmpty(portalAddress)) {
            mPortalAddressView.setError(getString(R.string.error_field_required));
            focusView = mPortalAddressView;
            cancel = true;
        } else if (!isPortalAddressValid(portalAddress)) {
            mPortalAddressView.setError(getString(R.string.error_invalid_portal_address));
            focusView = mPortalAddressView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password, portalAddress, accountType);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        return true; // As a consumer of the portal we don't know the username requirements
    }

    private boolean isPasswordValid(String password) {
        return true; // As a consumer of the portal we don't know the password requirements
    }

    private boolean isPortalAddressValid(String portalAddress) {
        boolean isValid = false;

        // Do a simple check if we get a HTTP 200 for this address
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(portalAddress);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            isValid = response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void finishLogin(Intent intent) {
        Log.d(LOG_TAG, "finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        String accountPortalAddress = intent.getStringExtra(AccountGeneral.PORTAL_ADDRESS);
        String accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        final Account account = new Account(accountName, accountType);

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(LOG_TAG, "finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setUserData(account, AccountGeneral.PORTAL_ADDRESS, accountPortalAddress);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d(LOG_TAG, "finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Intent> {

        private final String mUsername;
        private final String mPassword;
        private final String mPortalUrl;
        private final String mAccountType;

        UserLoginTask(String username, String password, String portalUrl, String accountType) {
            mUsername = username;
            mPassword = password;
            mPortalUrl = portalUrl;
            mAccountType = accountType;
        }

        @Override
        protected Intent doInBackground(Void... params) {
            Log.d(LOG_TAG, "Start authentication");

            String authtoken = null;
            Bundle data = new Bundle();

            try {
                authtoken = AccountGeneral.sServerAuthenticate.userSignIn(mUsername, mPassword, mPortalUrl, mAuthTokenType);

                data.putString(AccountManager.KEY_ACCOUNT_NAME, mUsername);
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                data.putString(AccountGeneral.PORTAL_ADDRESS, mPortalUrl);
                data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                data.putString(PARAM_USER_PASS, mPassword);

            } catch (Exception e) {
                data.putString(KEY_ERROR_MESSAGE, e.getMessage());
            }

            final Intent res = new Intent();
            res.putExtras(data);
            return res;
        }

        @Override
        protected void onPostExecute(final Intent intent) {
            mAuthTask = null;
            showProgress(false);

            if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                mPasswordView.setError(getString(R.string.error_login_failed));
                mPasswordView.requestFocus();
            } else {
                finishLogin(intent);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



