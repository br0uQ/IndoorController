package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * Created by joshua on 01.02.17.
 */

public class WifiDetection extends LocationDetection {
    private static Context context;
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";

    private static WifiReceiver receiver;
    private static Thread thread;
    private static boolean running = false;
    private static int sleepTimer = 1000;

    public WifiDetection(Context context) {
        this.context = context;
        fragment = new CreateOrtWifiFragment();
        name = context.getString(R.string.wifi_detection_name);
    }

    @Override
    public Ort createLocation(String name) {
        return ((CreateOrtWifiFragment) fragment).createWifiUmgebung(name);
    }

    @Override
    public void saveLocations(ArrayList<Ort> orte) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        int count = 0;
        for (Ort ort : orte) {
            if (ort instanceof WifiUmgebung) {
                String ortJSONString = new Gson().toJson(ort);
                editor.putString(KEY_SAVE_OBJECT + count++, ortJSONString);
            }
        }
        editor.putInt(KEY_SAVE_COUNT, count);
        editor.commit();
    }

    @Override
    public void loadLoactions(ArrayList<Ort> orte) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int count = preferences.getInt(KEY_SAVE_COUNT, 0);

        for (int i = 0; i < count; i++) {
            Gson gson = new Gson();
            String json = preferences.getString(KEY_SAVE_OBJECT + i, null);
            if (json != null) {
                WifiUmgebung obj = gson.fromJson(json, WifiUmgebung.class);
                orte.add(obj);
                receiver.addWifiUmgebung(obj);
            }
        }

        if (count > 0) {
            startDetection();
        }
    }

    public static void startDetection() {
        if (thread == null) {
            final WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            receiver = new WifiReceiver();
            context.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            running = true;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running) {
                        manager.startScan();
                        try {
                            Thread.sleep(sleepTimer);
                        } catch (InterruptedException ie) {
                            Log.d(getClass().getSimpleName(), ie.toString());
                        }
                    }
                    thread = null;
                }
            });
            thread.start();
        }
    }

    public static void stopDetection() {
        running = false;
        context.unregisterReceiver(receiver);
    }

    public static void setTimer(int timer) {
        sleepTimer = timer;
    }
}
