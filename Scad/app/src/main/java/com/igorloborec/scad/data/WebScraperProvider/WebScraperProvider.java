package com.igorloborec.scad.data.WebScraperProvider;

import android.app.Activity;

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
    protected final MainActivity _mainActivity;

    public WebScraperProvider(MainActivity mainActivity) {
        _mainActivity = mainActivity;
    }

    @Override
    public PersonalCalendar GetPersonalCalendar(GregorianCalendar calendar) {
        String url = String.format("%s/?q=student/calendar/%tY-%<tm-%<td", _mainActivity.getmPortalUrl(), calendar);
        String html = null;
        try {
            html = Helper.GetHtmlForUrl(url, _mainActivity);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return PersonalCalendarParser.Parse(html);
    }
}
