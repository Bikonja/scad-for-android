/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.HtmlParser;

import java.util.Date;

public class PersonalCalendarEntry {
    // possible boxes:
    // - green
    // - purple
    // - yellow
    // - red (example: https://student.vsite.hr/?q=student/calendar/2014-12-16)
    // - gray (example: https://student.vsite.hr/?q=student/calendar/2015-01-29)
    // - teal (example: https://student.vsite.hr/?q=student/calendar/2015-02-19)

    protected Date _start;
    protected Date _end;
    protected String _duration;
    protected String _subjectAbbr;
    protected String _subjectUrl;
    protected String _subjectType;
    protected String _subjectGroup;
    protected String _location;
    protected String _subjectHolder;
    protected String _status;

    public Date get_start() {
        return _start;
    }

    public void set_start(Date _start) {
        this._start = _start;
    }

    public Date get_end() {
        return _end;
    }

    public void set_end(Date _end) {
        this._end = _end;
    }

    public String get_duration() {
        return _duration;
    }

    public void set_duration(String _duration) {
        this._duration = _duration;
    }

    public String get_subjectAbbr() {
        return _subjectAbbr;
    }

    public void set_subjectAbbr(String _subjectAbbr) {
        this._subjectAbbr = _subjectAbbr;
    }

    public String get_subjectUrl() {
        return _subjectUrl;
    }

    public void set_subjectUrl(String _subjectUrl) {
        this._subjectUrl = _subjectUrl;
    }

    public String get_subjectType() {
        return _subjectType;
    }

    public void set_subjectType(String _subjectType) {
        this._subjectType = _subjectType;
    }

    public String get_subjectGroup() {
        return _subjectGroup;
    }

    public void set_subjectGroup(String _subjectGroup) {
        this._subjectGroup = _subjectGroup;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String get_subjectHolder() {
        return _subjectHolder;
    }

    public void set_subjectHolder(String _subjectHolder) {
        this._subjectHolder = _subjectHolder;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }
}
