package com.igorloborec.scad.data;

import java.util.Date;

/**
 * Created by Bikonja on 28.6.2015..
 */
public interface IScadProvider {
    public PersonalCalendar GetPersonalCalendar(Date date);
}
