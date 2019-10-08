package dev.hienlt.transparentapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class AutoStartServiceReceiver extends BroadcastReceiver {

    private static final String TAG = AutoStartServiceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // check broadcast action whether action was
        // boot completed or it was alarm action.
        if (intent.getAction().equals("dev.hienlt.transparentapp.RESTART_SERVICE")) {
            ContextCompat.startForegroundService(context, intent);
        }
    }
}
