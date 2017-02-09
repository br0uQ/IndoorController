package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 07.02.17.
 */

class WifiReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<WifiUmgebung> wifiUmgebungen;

    WifiReceiver() {
        wifiUmgebungen = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        List<ScanResult> results;
        results = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();
        for (WifiUmgebung wifiUmgebung : wifiUmgebungen) {
            ArrayList<WifiSensor> sensors = wifiUmgebung.getWifis();
            boolean active = true;
            for (WifiSensor sensor : sensors) {
                boolean inIt = false;
                for (ScanResult result : results) {
                    if ((result.SSID == sensor.getSsid())
                            && (result.BSSID == sensor.getBssid())) {
                        inIt = true;
                    }
                }
                if (!inIt) {
                    active = false;
                    break;
                }
            }
            wifiUmgebung.setActive(active);
        }
    }

    void addWifiUmgebung(WifiUmgebung wifiUmgebung) {
        wifiUmgebungen.add(wifiUmgebung);
    }

    void deleteWifiUmgebung(WifiUmgebung wifiUmgebung) {
        wifiUmgebungen.remove(wifiUmgebung);
    }
}
