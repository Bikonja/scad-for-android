/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.authentication;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class DummyScadAuthenticate implements ScadAuthenticate {
    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        return "dummyToken";
    }
}
