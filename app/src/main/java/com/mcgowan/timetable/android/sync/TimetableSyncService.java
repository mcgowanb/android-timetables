package com.mcgowan.timetable.android.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Brian on 23/06/2016.
 */
public class TimetableSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static TimetableSyncAdapter sTimetableSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("TimetableSyncService", "onCreate - TimetableSyncService");
        synchronized (sSyncAdapterLock) {
            if (sTimetableSyncAdapter == null) {
                sTimetableSyncAdapter = new TimetableSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sTimetableSyncAdapter.getSyncAdapterBinder();
    }
}
