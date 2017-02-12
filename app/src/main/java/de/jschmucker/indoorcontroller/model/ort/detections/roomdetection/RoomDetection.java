package de.jschmucker.indoorcontroller.model.ort.detections.roomdetection;

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

public class RoomDetection extends LocationDetection {
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";
    private Context context;

    public RoomDetection(Context context) {
        this.context = context;
        name = context.getString(R.string.room_detection_name);
        fragment = new CreateOrtRaumFragment();
        // ToDo Initialisation of the fragment...
    }

    @Override
    public Ort createLocation(String name) {
        // ToDo: tell the fragment to create a location
        return null;
    }

    @Override
    public void saveLocations(ArrayList<Ort> orte) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Ort ort : orte) {
            if (ort instanceof Raum) {
                String data = Raum.dataToString((Raum) ort);
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
                Raum raum = Raum.stringToData(data);
                orte.add(raum);
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
