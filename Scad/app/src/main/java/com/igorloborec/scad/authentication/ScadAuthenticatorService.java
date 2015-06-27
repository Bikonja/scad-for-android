/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.scad.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class ScadAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        ScadAuthenticator authenticator = new ScadAuthenticator(this);
        return authenticator.getIBinder();
    }
}
