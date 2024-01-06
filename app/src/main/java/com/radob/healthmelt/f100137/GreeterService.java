package com.radob.healthmelt.f100137;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class GreeterService extends Service {
    boolean isStarted;

    public GreeterService() {
        isStarted = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isStarted){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            Toast.makeText(getApplicationContext(), "Здравейте " + username, Toast.LENGTH_SHORT).show();
            isStarted = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Довиждане", Toast.LENGTH_SHORT).show();
        isStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}