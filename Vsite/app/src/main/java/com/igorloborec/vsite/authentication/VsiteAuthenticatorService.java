/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.vsite.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Bikonja on 3.2.2015..
 */
public class VsiteAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        VsiteAuthenticator authenticator = new VsiteAuthenticator(this);
        return authenticator.getIBinder();
    }
}
