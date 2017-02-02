package de.jschmucker.indoorcontroller.model.ort.detections.roomdetection;

import android.content.Context;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * Created by joshua on 01.02.17.
 */

public class RoomDetection extends LocationDetection {
    private Context context;

    public RoomDetection(Context context) {
        this.context = context;
        name = context.getString(R.string.room_detection_name);
        fragment = new CreateOrtRaumFragment();
        // ToDo Initialisation of the fragment...
    }

    @Override
    public Ort createLocation() {
        // ToDo: tell the fragment to create a location
        return null;
    }
}
