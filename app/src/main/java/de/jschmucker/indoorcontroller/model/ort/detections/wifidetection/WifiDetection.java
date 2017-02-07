package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.OrtsManagement;

/**
 * Created by joshua on 01.02.17.
 */

public class WifiDetection extends LocationDetection {
    private Context context;
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";

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
            }
        }
    }
}
