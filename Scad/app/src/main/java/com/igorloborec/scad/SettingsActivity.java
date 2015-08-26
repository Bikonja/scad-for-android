package com.igorloborec.scad;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Bikonja on 26.8.2015..
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // add the xml resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
