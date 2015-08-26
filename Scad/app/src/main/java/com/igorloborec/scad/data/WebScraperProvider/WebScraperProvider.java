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
import java.util.HashMap;

/**
 * Created by Bikonja on 28.6.2015..
 */
public class WebScraperProvider implements IScadProvider {
    protected String mAuthToken;
    protected String mPortalUrl;
    protected HashMap<GregorianCalendar, PersonalCalendar> mPersonalCalendars = new HashMap<>();

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
        GregorianCalendar firstDayOfWeek = (GregorianCalendar)calendar.clone();
        firstDayOfWeek.add(Calendar.DATE, -1);
        firstDayOfWeek.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        firstDayOfWeek.add(Calendar.DATE, 1);
        firstDayOfWeek.set(Calendar.HOUR_OF_DAY, 0);
        firstDayOfWeek.set(Calendar.MINUTE, 0);
        firstDayOfWeek.set(Calendar.MILLISECOND, 0);

        PersonalCalendar personalCalendar = mPersonalCalendars.get(firstDayOfWeek);
        if (personalCalendar == null) {
            String url = String.format("%s/?q=student/calendar/%tY-%<tm-%<td", getmPortalUrl(), calendar);
            try {
                String html = Helper.GetHtmlForUrl(url, getmAuthToken());
                personalCalendar = PersonalCalendarParser.Parse(html);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                personalCalendar = null;
            }

            if (personalCalendar != null) {
                mPersonalCalendars.put(firstDayOfWeek, personalCalendar);
            }
        }

        return personalCalendar;
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
