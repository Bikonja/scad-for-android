package com.igorloborec.scad;

import android.content.Context;
import android.support.v7.internal.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igorloborec.scad.data.IScadProvider;
import com.igorloborec.scad.data.PersonalCalendar;
import com.igorloborec.scad.data.PersonalCalendarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Bikonja on 19.8.2015..
 */
public class CalendarFragment {

    public static void setUp(View rootView, IScadProvider scadProvider) {
        GregorianCalendar calendar = new GregorianCalendar();

        // HACK: For testing
        //calendar.set(2015, Calendar.MAY, 11);

        GregorianCalendar firstDayOfWeek = (GregorianCalendar)calendar.clone();
        firstDayOfWeek.add(Calendar.DATE, -1);
        firstDayOfWeek.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        firstDayOfWeek.add(Calendar.DATE, 1);

        GregorianCalendar lastDayOfWeek = (GregorianCalendar)firstDayOfWeek.clone();
        lastDayOfWeek.add(Calendar.DATE, 6);

        PersonalCalendar personalCalendar = scadProvider.GetPersonalCalendar(calendar);
        //((TextView) rootView.findViewById(R.id.section_label)).setText(personalCalendar.get_entries().get(0).get_subjectAbbr());
        ((TextView) rootView.findViewById(R.id.calendar_date_label)).setText(String.format("%tY-%<tm-%<td  -  %tY-%<tm-%<td", firstDayOfWeek, lastDayOfWeek));

        CalendarEntryAdapter adapter = new CalendarEntryAdapter(rootView.getContext(), personalCalendar.get_entries());

        ((ListView) rootView.findViewById(R.id.calendar_item_list)).setAdapter(adapter);
    }
}