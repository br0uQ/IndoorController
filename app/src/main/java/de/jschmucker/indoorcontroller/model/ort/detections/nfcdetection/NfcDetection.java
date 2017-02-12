package de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiUmgebung;

/**
 * Created by joshua on 01.02.17.
 */

public class NfcDetection extends LocationDetection {
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";
    private Context context;

    public NfcDetection(Context context) {
        this.context = context;
        fragment = new CreateOrtNfcFragment();
        name = context.getString(R.string.nfc_detection_name);
    }

    @Override
    public Ort createLocation(String name) {
        // ToDo tell fragment to create Location
        return null;
    }

    @Override
    public void saveLocations(ArrayList<Ort> orte) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Ort ort : orte) {
            if (ort instanceof NFCSpot) {
                String data = NFCSpot.dataToString((NFCSpot) ort);
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
                NFCSpot nfcSpot = NFCSpot.stringToData(data);
                orte.add(nfcSpot);
            }
        }
    }

    @Override
    public void startDetection(ArrayList<Ort> locations) {
        // ToDo: Implement Detection
    }

    @Override
    public void stopDetection() {
        // ToDo: Implement Detection
    }
}
