package com.igorloborec.vsite.HtmlParse;

import android.test.AndroidTestCase;

import com.igorloborec.vsite.HtmlParser.PersonalCalendar;
import com.igorloborec.vsite.HtmlParser.PersonalCalendarEntry;
import com.igorloborec.vsite.R;

import java.util.Date;

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

    public void testHtmlLoaded() {
        assertFalse("HTML loading failed", getPersonalCalendarHtml().isEmpty());
    }

    public void testEntriesParsed() {
        PersonalCalendar personalCalendar = new PersonalCalendar(getPersonalCalendarHtml());
        assertTrue("No entries parsed", personalCalendar.get_entries().size() > 0);

        int entryIndex = 0;

        // assert first entry
        assertTrue(String.format("Entry %1s not parsed", entryIndex), personalCalendar.get_entries().size() >= entryIndex + 1);
        PersonalCalendarEntry entry = personalCalendar.get_entries().get(entryIndex);
        assertEquals(String.format("Entry %1s: subjectAbbr not correct", entryIndex), "DOSI", entry.get_subjectAbbr());
        assertEquals(String.format("Entry %1s: subjectType not correct", entryIndex), "P1", entry.get_subjectType());
        assertEquals(String.format("Entry %1s: subjectGroup not correct", entryIndex), "U1", entry.get_subjectGroup());
        assertEquals(String.format("Entry %1s: subjectUrl not correct", entryIndex), "/?q=student/subjectperformance/508", entry.get_subjectUrl());
        assertEquals(String.format("Entry %1s: location not correct", entryIndex), "zg - Laboratorij 5", entry.get_location());
        assertEquals(String.format("Entry %1s: start not correct", entryIndex), new Date(2015 - 1900, 1, 25, 16, 45), entry.get_start());
        assertEquals(String.format("Entry %1s: end not correct", entryIndex), new Date(2015 - 1900, 1, 25, 18, 20), entry.get_end());
        assertEquals(String.format("Entry %1s: subjectDuration not correct", entryIndex), "2h", entry.get_duration());
        assertEquals(String.format("Entry %1s: subjectHolder not correct", entryIndex), "Mario Lamešić", entry.get_subjectHolder());
        assertEquals(String.format("Entry %1s: status not correct", entryIndex), "Nije održano!", entry.get_status());
    }
}
