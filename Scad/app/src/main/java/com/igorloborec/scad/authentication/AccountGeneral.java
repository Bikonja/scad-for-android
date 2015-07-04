/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.authentication;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class AccountGeneral {
    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "SCAD";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = null;

    public static final String PORTAL_ADDRESS = "PORTAL_URL";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_DEFAULT = "Default";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Default access to Vsite portal";

    /**
     * Instance of implementation for server authentication class
     */
    public static final ScadAuthenticate sServerAuthenticate = new DummyScadAuthenticate();
}