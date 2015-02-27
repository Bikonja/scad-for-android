package com.igorloborec.vsite.HtmlParse;

import android.test.AndroidTestCase;

import com.igorloborec.vsite.R;

/**
 * Created by Bikonja on 27.2.2015..
 */
public class PersonalCalendarTest extends AndroidTestCase {

    private String _personalCalendarHtml = null;

    public String getPersonalCalendarHtml() {
        if (this._personalCalendarHtml == null) {
            this._personalCalendarHtml = Helper.readStringFromResource(getContext(), R.raw.personalcalendarhtml);
        }
        return this._personalCalendarHtml;
    }

    public PersonalCalendarTest() {
        super();
    }

    public void testHtmlLoaded(){
        assertFalse("HTML loading failed", getPersonalCalendarHtml().isEmpty());
    }
}
