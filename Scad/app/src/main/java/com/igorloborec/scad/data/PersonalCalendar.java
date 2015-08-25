/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Bikonja on 4.3.2015..
 */
public class PersonalCalendar {
    protected ArrayList<PersonalCalendarEntry> _entries;
    protected HashMap<GregorianCalendar, ArrayList<PersonalCalendarEntry>> _entriesByDay;

    /**
     * Gets the entries for the specified day of the
     * @param date The date for which to get the entries
     * @return An ArrayList of PersonalCalendarEntry objects for the specified date
     */
    public ArrayList<PersonalCalendarEntry> get_entries(GregorianCalendar date) {
        ArrayList<PersonalCalendarEntry> entries = _entriesByDay.get(date);

        if (entries == null) {
            entries = new ArrayList<>();
            for (PersonalCalendarEntry entry : _entries) {
                if (
                        entry.get_date().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        entry.get_date().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                        entry.get_date().get(Calendar.DATE) == date.get(Calendar.DATE)
                    ) {
                    entries.add(entry);
                }
            }
            _entriesByDay.put(date, entries);
        }

        return entries;
    }

    public PersonalCalendar() {
        this(null);
    }

    public PersonalCalendar(ArrayList allEntries) {
        _entries = allEntries != null ? allEntries : new ArrayList<>();
        _entriesByDay = new HashMap<>();
    }
}