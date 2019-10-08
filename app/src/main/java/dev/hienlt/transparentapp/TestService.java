package dev.hienlt.transparentapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestService extends Service {
    private static final String TAG = TestService.class.getSimpleName();
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private Handler handler;
    private SimpleDateFormat simpleDateFormat;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        appendLog("Service Started");
        Log.d(TAG, "onCreate: Service Started");
        handler.post(runnable);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            appendLog("Running");
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Test Foreground Service")
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        appendLog("Service Stopped");
        Log.d(TAG, "onDestroy: Service Stopped");
        restartService();
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void appendLog(String text) {
        File logFile = new File(Environment.getExternalStorageDirectory(), "service_log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            String time = simpleDateFormat.format(new Date());
            buf.append(time + ": " + text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        restartService();
        super.onTaskRemoved(rootIntent);
        appendLog("Service Removed");
        Log.d(TAG, "onTaskRemoved: Service Removed");
    }

    private void restartService(){
        final int TIME_TO_INVOKE = 5 * 1000; // try to re-start service in 5 seconds.
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AutoStartServiceReceiver.class);
        intent.setAction("dev.hienlt.transparentapp.RESTART_SERVICE");
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // set repeating alarm.
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                TIME_TO_INVOKE, TIME_TO_INVOKE, pendingIntent);
    }
}
