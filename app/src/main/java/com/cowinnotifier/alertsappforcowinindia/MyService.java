package com.cowinnotifier.alertsappforcowinindia;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    MainActivity ma = new MainActivity();
//    public MyService(MainActivity maa) {
//        this.ma = maa;
//    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        ma.startChecking();
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}