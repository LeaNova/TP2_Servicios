package com.ulp.tp2_servicios;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

public class ServiceSMS extends Service {
    private Boolean running = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Salida", "Servicio iniciado");
        Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();

        running = true;
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running) {
                    try {
                        accederMensajes();
                        Thread.sleep(9000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        });

        hilo.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Salida", "Servicio finalizado");
        Toast.makeText(this, "Servicio finalizado", Toast.LENGTH_SHORT).show();

        running = false;

        stopSelf();
    }

    private void accederMensajes() {
        Uri mensajes = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(mensajes, null, null, null, null);

        if(cursor.getCount() > 0) {
            int cant = 0;
            int colFrom = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
            int colBody = cursor.getColumnIndex(Telephony.Sms.BODY);

            Log.d("Salida", "________Servisio de mensajeria_______");
            while (cursor.moveToNext()  && cant < 5) {
                String from = cursor.getString(colFrom);
                String body = cursor.getString(colBody);
                Log.d("Salida", "Remitente: " + from + "\nMensaje: " + body);
                cant++;
            }
        }
    }
}