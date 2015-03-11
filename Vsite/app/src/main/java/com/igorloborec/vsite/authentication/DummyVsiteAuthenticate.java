/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.vsite.authentication;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class DummyVsiteAuthenticate implements VsiteAuthenticate {
    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        return "dummyToken";
    }
}
