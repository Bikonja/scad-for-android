package com.igorloborec.scad.data;

import android.os.Parcelable;

import java.util.GregorianCalendar;

/**
 * Created by Bikonja on 28.6.2015..
 */
public interface IScadProvider extends Parcelable {
    PersonalCalendar GetPersonalCalendar(GregorianCalendar calendar);
}
