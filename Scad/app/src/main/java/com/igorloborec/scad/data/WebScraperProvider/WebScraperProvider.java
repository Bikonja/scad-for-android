package com.igorloborec.scad.data.WebScraperProvider;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.igorloborec.scad.MainActivity;
import com.igorloborec.scad.data.IScadProvider;
import com.igorloborec.scad.data.PersonalCalendar;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Bikonja on 28.6.2015..
 */
public class WebScraperProvider implements IScadProvider {
    protected String mAuthToken;
    protected String mPortalUrl;

    public String getmAuthToken() {
        return mAuthToken;
    }

    public void setmAuthToken(String mAuthToken) {
        this.mAuthToken = mAuthToken;
    }

    public String getmPortalUrl() {
        return mPortalUrl;
    }

    public void setmPortalUrl(String mPortalUrl) {
        this.mPortalUrl = mPortalUrl;
    }

    public WebScraperProvider(String authToken, String portalUrl) {
        mAuthToken = authToken;
        mPortalUrl = portalUrl;
    }

    @Override
    public PersonalCalendar GetPersonalCalendar(GregorianCalendar calendar) {
        String url = String.format("%s/?q=student/calendar/%tY-%<tm-%<td", getmPortalUrl(), calendar);
        String html = null;
        try {
            html = Helper.GetHtmlForUrl(url, getmAuthToken());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return PersonalCalendarParser.Parse(html);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getmAuthToken());
        dest.writeString(getmPortalUrl());
    }

    public static final Parcelable.Creator<WebScraperProvider> CREATOR
            = new Parcelable.Creator<WebScraperProvider>() {
        public WebScraperProvider createFromParcel(Parcel in) {
            return new WebScraperProvider(in);
        }

        public WebScraperProvider[] newArray(int size) {
            return new WebScraperProvider[size];
        }
    };

    private WebScraperProvider(Parcel in) {
        setmAuthToken(in.readString());
        setmPortalUrl(in.readString());
    }
}
