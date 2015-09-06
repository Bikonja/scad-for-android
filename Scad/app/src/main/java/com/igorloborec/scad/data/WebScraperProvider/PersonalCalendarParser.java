package com.igorloborec.scad.data.WebScraperProvider;

import com.igorloborec.scad.data.PersonalCalendar;
import com.igorloborec.scad.data.PersonalCalendarEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bikonja on 28.6.2015..
 */
public class PersonalCalendarParser {
    final static String DATE_FORMAT = "dd.MM.yyyy. HH:mm";
    final static SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    final static Pattern datePattern = Pattern.compile("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})\\.");

    public static PersonalCalendar Parse(String html) {
        ArrayList<PersonalCalendarEntry> personalCalendarEntries = new ArrayList<>();

        // TODO: Sanity checks, logs, ...
        Document document = Jsoup.parse(html);

        Elements entryDivs = document.select(".bubbleInfo");

        Pattern datesPattern = Pattern.compile("([0-9]{2}\\.[0-9]{2}\\.[0-9]{4}\\.) ([0-9]{2}:[0-9]{2}) ([0-9]{2}:[0-9]{2}) ([0-9]h)");

        for (Element entryDiv : entryDivs) {
            PersonalCalendarEntry entry = new PersonalCalendarEntry();
            Element triggerBox = entryDiv.select("div.trigger").get(0);
            Element popupBox = entryDiv.select("div.popup").get(0);

            PersonalCalendarEntry.Type entryType = GetEntryTypeFromDiv(triggerBox);
            String[] triggerBoxEntries = Helper.GetNodeTextWithNewLines(triggerBox).split("\\r?\\n");
            int triggerBoxIndex = triggerBoxEntries[0].isEmpty() ? 1 : 0;
            String subjectAbbr = triggerBoxEntries[triggerBoxIndex].trim();
            String subjectType = triggerBoxEntries.length > triggerBoxIndex + 1 ? triggerBoxEntries[triggerBoxIndex + 1].trim() : "";
            String subjectGroup = triggerBoxEntries.length > triggerBoxIndex + 2 ? triggerBoxEntries[triggerBoxIndex + 2].trim() : "";

            String popupText = Helper.GetNodeTextWithNewLines(popupBox);
            String[] popupLines = popupText.split("\\r?\\n");
            GregorianCalendar date = GetEntryDateFromDiv(popupText);
            Elements subjectLinks = popupBox.select("a");
            String subjectUrl = subjectLinks != null && !subjectLinks.isEmpty() ? subjectLinks.get(0).attr("href") : "";
            String subjectLocation = popupLines[1].trim();
            GregorianCalendar subjectStart = null;
            GregorianCalendar subjectEnd = null;
            String subjectDuration = "";
            Matcher matcher = datesPattern.matcher(popupBox.text());
            if (matcher.find())
            {
                try {
                    subjectStart = new GregorianCalendar();
                    subjectStart.set(Calendar.SECOND, 0);
                    subjectStart.set(Calendar.MILLISECOND, 0);
                    subjectStart.setTime(dateFormatter.parse(matcher.group(1) + " " + matcher.group(2)));
                    subjectEnd = new GregorianCalendar();
                    subjectEnd.set(Calendar.SECOND, 0);
                    subjectEnd.set(Calendar.MILLISECOND, 0);
                    subjectEnd.setTime(dateFormatter.parse(matcher.group(1) + " " + matcher.group(3)));
                    subjectDuration = matcher.group(4);
                } catch (ParseException e) {
                    e.printStackTrace();

                    subjectStart = null;
                    subjectEnd = null;
                    subjectDuration = "";
                }
            }
            String subjectHolder = popupLines.length > 3 ? popupLines[3].trim() : "";
            String subjectStatus = popupLines[popupLines.length - 1].trim();

            entry.set_type(entryType);
            entry.set_date(date);
            entry.set_subjectAbbr(subjectAbbr);
            entry.set_subjectType(subjectType);
            entry.set_subjectGroup(subjectGroup);
            entry.set_subjectUrl(subjectUrl);
            entry.set_location(subjectLocation);
            entry.set_location(subjectLocation);
            entry.set_start(subjectStart);
            entry.set_end(subjectEnd);
            entry.set_duration(subjectDuration);
            entry.set_subjectHolder(subjectHolder);
            entry.set_status(subjectStatus);

            personalCalendarEntries.add(entry);
        }

        return new PersonalCalendar(personalCalendarEntries);
    }

    private static PersonalCalendarEntry.Type GetEntryTypeFromDiv(Element triggerBox) {
        PersonalCalendarEntry.Type type = PersonalCalendarEntry.Type.OTHER;

        if (triggerBox != null) {
            if (triggerBox.hasClass("act-green")) {
                type = PersonalCalendarEntry.Type.PASSED;
            } else if (triggerBox.hasClass("act-yellow")) {
                type = PersonalCalendarEntry.Type.PLANNED_NOT_HELD;
            } else if (triggerBox.hasClass("act-orange")) {
                type = PersonalCalendarEntry.Type.PASSED_IN_ALTERNATE_ENTRY;
            } else if (triggerBox.hasClass("act-red")) {
                type = PersonalCalendarEntry.Type.FAILED;
            } else if (triggerBox.hasClass("act-violet")) {
                type = PersonalCalendarEntry.Type.NOT_HELD;
            } else if (triggerBox.hasClass("act-violet-light")) {
                type = PersonalCalendarEntry.Type.OTHER;
            } else if (triggerBox.hasClass("act-blue")) {
                type = PersonalCalendarEntry.Type.PASSED_IN_PREVIOUS_ENTRY;
            } else if (triggerBox.hasClass("act-languida")) {
                type = PersonalCalendarEntry.Type.OTHER_GROUPS_ENTRY;
            } else if (triggerBox.hasClass("act-platinum")) {
                type = PersonalCalendarEntry.Type.FOR_ALL;
            }
        }

        return type;
    }

    private static GregorianCalendar GetEntryDateFromDiv(String popupText) {
        GregorianCalendar date = new GregorianCalendar();
        Matcher matcher = datePattern.matcher(popupText);

        if (matcher.find())
        {
            date.set(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(1)));
        }
        else
        {
            date = null;
        }

        return date;
    }
}
