package com.igorloborec.scad.data;

import java.util.GregorianCalendar;

/**
 * Created by Bikonja on 28.6.2015..
 */
public interface IScadProvider {
    public PersonalCalendar GetPersonalCalendar(GregorianCalendar calendar);
}
