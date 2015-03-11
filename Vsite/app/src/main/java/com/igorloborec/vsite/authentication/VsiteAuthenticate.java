/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.vsite.authentication;

/**
 * Created by Bikonja on 3.2.2015..
 */
public interface  VsiteAuthenticate {
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}
