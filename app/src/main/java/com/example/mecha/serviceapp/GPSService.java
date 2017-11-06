package com.example.mecha.serviceapp;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;

public class GPSService extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private NotificationManager notificationManager;
    private static final int ID_NOTIFICACION = 1234;

    public GPSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        //instanciamos el manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location.getLongitude() <= -68.845  && location.getLongitude() >= -68.849 && location.getLatitude() >= -32.916 && location.getLatitude() <= -32.912){
                    long vibrate[]={0,100,100};
                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("Ubicacion marcada")
                            .setContentText("te encuentas ubicado en una ubicacion predefinida!")
                            .setVibrate(vibrate)
                            .setWhen(System.currentTimeMillis());

                    notificationManager.notify(ID_NOTIFICACION,builder.build());
                }else{
                    notificationManager.cancel(ID_NOTIFICACION);
                }


                //tomo cada nueva location
                Intent i = new Intent("location_update");
                i.putExtra("coordinates", location.getLongitude()+" "+location.getLatitude());
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //hacemo que active ubicacion si no lo tiene activado

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context
                .LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(locationManager != null){
            locationManager.removeUpdates(listener);
        }
    }
}
