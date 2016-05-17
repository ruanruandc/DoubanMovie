package com.example.ruandongchuan.doubanmovie.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocalService extends Service implements LocationListener {

    private static final String TAG = "LocalService";
    public static final String LOCATION_RECEIVER = "com.ruandongchuan.doubanmovie.LOCATION_RECEIVER";
    private LocationManager mLocationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null)
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    this);
        /*else if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    this);*/
        else
            Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Get the current position ");
        Intent intent = new Intent(LOCATION_RECEIVER);
        Bundle bundle = new Bundle();
        bundle.putParcelable("location",location);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        stopSelf();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
