package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * Created by joshua on 01.02.17.
 */

public class WifiDetection extends LocationDetection {
    private final String TAG = getClass().getSimpleName();
    private Context context;

    /* Preferences keys */
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";

    /* Detection Variables */
    private static int timer = 5000;
    private static Thread thread;
    private static boolean running = false;
    private static BroadcastReceiver receiver;

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
                String data = WifiUmgebung.dataToString((WifiUmgebung) ort);
                editor.putString(KEY_SAVE_OBJECT + count++, data);
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
            String data = preferences.getString(KEY_SAVE_OBJECT + i, null);
            if (data != null) {
                Log.d(TAG, "Load data from String: " + data);
                WifiUmgebung wifiUmgebung = WifiUmgebung.stringToData(data);
                orte.add(wifiUmgebung);
            }
        }
    }

    @Override
    public void startDetection(final ArrayList<Ort> locations) {
        Log.d(TAG, "start Wifi detection");

        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive");

                List<ScanResult> results;
                results = wifiManager.getScanResults();

                for (Ort ort : locations) {
                    if (ort instanceof WifiUmgebung) {
                        ArrayList<WifiSensor> sensors = ((WifiUmgebung) ort).getWifis();
                        boolean active = true;
                        for (WifiSensor sensor : sensors) {
                            boolean found = false;
                            for (ScanResult result : results) {
                                if ((sensor.getSsid().equals(result.SSID))
                                        && sensor.getBssid().equals(result.BSSID)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                active = false;
                                break;
                            }
                        }

                        String b = active ? "true" : "false";
                        Log.d(TAG, "Set Ort \"" + ort.getName() + "\" active: " + b);
                        ort.setActive(active);
                    }
                }
            }
        };
        context.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    wifiManager.startScan();

                    try {
                        Thread.sleep(timer);
                    } catch (InterruptedException ex) {
                        Log.d(TAG, ex.toString());
                    }
                }

                thread = null;
            }
        });

        running = true;
        thread.start();
    }

    @Override
    public void stopDetection() {
        Log.d(TAG, "stop Wifi detection");
        running = false;

        context.unregisterReceiver(receiver);
    }
}
