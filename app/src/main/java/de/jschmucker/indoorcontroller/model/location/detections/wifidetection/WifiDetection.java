package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;

/**
 * Created by jschmucker on 01.02.17.
 */

public class WifiDetection extends LocationDetection {
    private final String TAG = getClass().getSimpleName();
    private final Context context;

    /* Preferences keys */
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";

    /* Detection Variables */
    private static volatile int timer = 5000; // scan interval in ms
    private static Thread thread;
    private static boolean running = false;
    private static BroadcastReceiver receiver;

    public WifiDetection(Context context) {
        this.context = context;
        fragment = new WifiDetectionFragment();
        name = context.getString(R.string.wifi_detection_name);
    }

    @Override
    public Location createLocation(String name) {
        return ((WifiDetectionFragment) fragment).createWifiEnvironment(name);
    }

    @Override
    public void saveLocations(ArrayList<Location> locations) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Location location : locations) {
            if (location instanceof WifiEnvironment) {
                String data = WifiEnvironment.dataToString((WifiEnvironment) location);
                editor.putString(KEY_SAVE_OBJECT + count++, data);
            }
        }
        editor.putInt(KEY_SAVE_COUNT, count);
        editor.commit();
    }

    @Override
    public void loadLocations(ArrayList<Location> locations) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int count = preferences.getInt(KEY_SAVE_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String data = preferences.getString(KEY_SAVE_OBJECT + i, null);
            if (data != null) {
                Log.d(TAG, "Load data from String: " + data);
                WifiEnvironment wifiEnvironment = WifiEnvironment.stringToData(data);
                locations.add(wifiEnvironment);
            }
        }
    }

    @Override
    public void startDetection(final ArrayList<Location> locations) {
        Log.d(TAG, "start Wifi detection");

        loadSettings();

        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive");

                List<ScanResult> results;
                results = wifiManager.getScanResults();

                for (Location location : locations) {
                    if (location instanceof WifiEnvironment) {
                        ArrayList<WifiSensor> sensors = ((WifiEnvironment) location).getWifis();
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
                        Log.d(TAG, "Set Location \"" + location.getName() + "\" active: " + b);
                        location.setActive(active);
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

        // context.unregisterReceiver(receiver);
    }

    @Override
    public boolean isDetectionOfLocation(Location location) {
        return location instanceof WifiEnvironment;
    }

    @Override
    public void setLocationValues(Location location) {
        WifiDetectionFragment wifiDetectionFragment = (WifiDetectionFragment) fragment;
        WifiEnvironment environment = (WifiEnvironment) location;
        ArrayList<WifiSensor> wifis = new ArrayList<>();
        for (WifiSensor sensor : environment.getWifis()) {
            wifis.add(sensor);
        }
        wifiDetectionFragment.setWifiList(wifis);
    }

    @Override
    public void saveLocationValues(Location location) {
        ((WifiDetectionFragment) fragment).saveLocationValues((WifiEnvironment) location);
    }

    @Override
    public int getPreferenceResource() {
        return R.xml.preference_wifi_detection;
    }

    @Override
    public void reloadSettings() {
        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String scanIntervalString = sharedPreferences.getString("wifi_pref_key_scan_interval", "");
        Log.d(getClass().getSimpleName(), "ScanIntervalString: " + scanIntervalString);
        if (!scanIntervalString.equals("")) {
            int scanInterval = Integer.valueOf(scanIntervalString); // scan interval in s
            timer = scanInterval * 1000;
        }
    }

}
