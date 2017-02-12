package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;
import de.jschmucker.indoorcontroller.model.location.Location;

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
        fragment = new RoomDetectionFragment();
        // ToDo Initialisation of the fragment...
    }

    @Override
    public Location createLocation(String name) {
        // ToDo: tell the fragment to create a location
        return null;
    }

    @Override
    public void saveLocations(ArrayList<Location> orte) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Location location : orte) {
            if (location instanceof Room) {
                String data = Room.dataToString((Room) location);
                editor.putString(KEY_SAVE_OBJECT + count++, data);
            }
        }
        editor.putInt(KEY_SAVE_COUNT, count);
        editor.commit();
    }

    @Override
    public void loadLoactions(ArrayList<Location> orte) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int count = preferences.getInt(KEY_SAVE_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String data = preferences.getString(KEY_SAVE_OBJECT + i, null);
            if (data != null) {
                Room room = Room.stringToData(data);
                orte.add(room);
            }
        }
    }

    @Override
    public void startDetection(ArrayList<Location> locations) {
        // ToDo: Implement Detection
    }

    @Override
    public void stopDetection() {
        // ToDo: Implement Detection
    }
}
