package com.mcgowan.timetable.itsligotimetables.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Brian on 23/06/2016.
 */
public class TimetableAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private TimetableAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new TimetableAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
