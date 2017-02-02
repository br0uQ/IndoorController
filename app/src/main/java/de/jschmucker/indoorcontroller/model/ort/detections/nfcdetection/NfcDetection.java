package de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection;

import android.content.Context;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * Created by joshua on 01.02.17.
 */

public class NfcDetection extends LocationDetection {
    private Context context;

    public NfcDetection(Context context) {
        this.context = context;
        fragment = new CreateOrtNfcFragment();
    }
    @Override
    public String getDetectionName() {
        return context.getString(R.string.nfc_detection_name);
    }

    @Override
    public Ort createLocation() {
        // ToDo tell fragment to create Location
        return null;
    }
}
