package com.igorloborec.scad;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.igorloborec.scad.data.PersonalCalendarEntry;

import java.util.ArrayList;

public class CalendarEntryAdapter extends ArrayAdapter<PersonalCalendarEntry> {

    public CalendarEntryAdapter(Context c, ArrayList<PersonalCalendarEntry> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarEntryView itemView = (CalendarEntryView)convertView;
        if (null == itemView)
            itemView = CalendarEntryView.inflate(parent);

        itemView.setItem(getItem(position));
        return itemView;
    }

}
