/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.authentication;

/**
 * Created by Bikonja on 3.2.2015..
 */
public interface ScadAuthenticate {
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}
