/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.vsite.authentication;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class AccountGeneral {
    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "default";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = null;

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_DEFAULT = "Default";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Default access to Vsite portal";

    /**
     * Instance of implementation for server authentication class
     */
    public static final VsiteAuthenticate sServerAuthenticate = new DummyVsiteAuthenticate();
}