package com.igorloborec.scad;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igorloborec.scad.data.PersonalCalendarEntry;

public class CalendarEntryView extends RelativeLayout {
    private TextView calendar_entry_start_time;
    private TextView calendar_entry_end_time;
    private LinearLayout calendar_entry_right_side;
    private TextView calendar_entry_text;

    public static CalendarEntryView inflate(ViewGroup parent) {
        CalendarEntryView itemView = (CalendarEntryView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_entry, parent, false);
        return itemView;
    }

    public CalendarEntryView(Context c) {
        this(c, null);
    }

    public CalendarEntryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarEntryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(getContext(), R.layout.calendar_entry_children, this);
        setupChildren();
    }

    private void setupChildren() {
        calendar_entry_start_time = (TextView) findViewById(R.id.calendar_entry_start_time);
        calendar_entry_end_time = (TextView) findViewById(R.id.calendar_entry_end_time);
        calendar_entry_right_side = (LinearLayout) findViewById(R.id.calendar_entry_right_side);
        calendar_entry_text = (TextView) findViewById(R.id.calendar_entry_text);
    }

    public void setItem(PersonalCalendarEntry item) {
        calendar_entry_start_time.setText(String.format("%02d:%02d", item.get_start().getHours(), item.get_start().getMinutes()));
        calendar_entry_end_time.setText(String.format("%02d:%02d", item.get_end().getHours(), item.get_end().getMinutes()));
        calendar_entry_text.setText(getEntryText(item));
        switch (item.get_type()) {
            case FAILED:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_FAILED));
                break;
            case NOT_HELD:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_NOT_HELD));
                break;
            case OTHER_GROUPS_ENTRY:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_OTHER_GROUPS_ENTRY));
                break;
            case PASSED:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_PASSED));
                break;
            case PASSED_IN_ALTERNATE_ENTRY:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_PASSED_IN_ALTERNATE_ENTRY));
                break;
            case PASSED_IN_PREVIOUS_ENTRY:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_PASSED_IN_PREVIOUS_ENTRY));
                break;
            case PLANNED_NOT_HELD:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_PLANNED_NOT_HELD));
                break;
            case FOR_ALL:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_FOR_ALL));
                break;
            case OTHER:
            default:
                calendar_entry_right_side.setBackgroundColor(getResources().getInteger(R.integer.CalendarEntry_BgColor_OTHER));
                break;
        }
    }

    private String getEntryText(PersonalCalendarEntry item) {
        StringBuilder stringBuilder = new StringBuilder();

        if (item != null) {
            addText(stringBuilder, item.get_subjectAbbr());
            addText(stringBuilder, item.get_location());
            addText(stringBuilder, item.get_subjectGroup());
            addText(stringBuilder, item.get_subjectHolder());
            addText(stringBuilder, item.get_subjectType());
            addText(stringBuilder, item.get_status());
        }

        return stringBuilder.toString().replaceAll("\\s+$", "");
    }

    private void addText(StringBuilder stringBuilder, String text) {
        if (text != null && text.length() > 0) {
            stringBuilder.append(text + "\n");
        }
    }

    public TextView getCalendar_entry_text() {
        return calendar_entry_text;
    }
}
