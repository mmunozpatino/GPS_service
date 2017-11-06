package com.example.mecha.serviceapp;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.jar.Pack200;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bStart, bStop;
    private TextView textView;
    private Button bNotificacion;
    //para permitir que llegue el broadcast
    private BroadcastReceiver broadcastReceiver;


    //este método permite ir tomando los datos que toma el service
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.append("\n" + intent.getExtras().get("coordinates"));

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bStart = (Button) findViewById(R.id.buttonStart);
        bStop = (Button) findViewById(R.id.buttonStop);
        textView = (TextView) findViewById(R.id.textView);
        bNotificacion = (Button) findViewById(R.id.botonN);

        if(!runtime_permission()){
            enable_buttons();
        }
    }

    private boolean runtime_permission(){
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission
                        .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest
                    .permission.ACCESS_FINE_LOCATION},100);

            return true;
        }

        return false;
    }

    private void enable_buttons(){
        bStart.setOnClickListener(this);
        bStop.setOnClickListener(this);
        bNotificacion.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else{
                runtime_permission();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStart:
                Intent start = new Intent(getApplicationContext(), GPSService.class);
                startService(start);
                NotificationCompat.Builder mBuilder;
                NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                break;

            case R.id.buttonStop:
                Intent stop = new Intent(getApplicationContext(), GPSService.class);
                stopService(stop);
                break;

            case R.id.botonN:

        }
    }
}
