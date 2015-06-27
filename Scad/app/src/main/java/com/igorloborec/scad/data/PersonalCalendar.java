/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.data;

import java.util.ArrayList;

/**
 * Created by Bikonja on 4.3.2015..
 */
public class PersonalCalendar {
    protected ArrayList<PersonalCalendarEntry> _entries;

    public ArrayList<PersonalCalendarEntry> get_entries() {
        return _entries;
    }

    public PersonalCalendar()
    {
        _entries = new ArrayList<PersonalCalendarEntry>();
    }
}