package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;

/**
 * Created by joshua on 01.02.17.
 */

public class NfcDetection extends LocationDetection {
    private final String TAG = getClass().getSimpleName();

    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";
    private Context context;
    private boolean detectionToFragment = false;

    public NfcDetection(Context context) {
        this.context = context;
        fragment = new NfcDetectionFragment();
        name = context.getString(R.string.nfc_detection_name);
    }

    @Override
    public Location createLocation(String name) {
        return ((NfcDetectionFragment) fragment).createLocation(name);
    }

    @Override
    public void saveLocations(ArrayList<Location> orte) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Location location : orte) {
            if (location instanceof NfcSpot) {
                String data = NfcSpot.dataToString((NfcSpot) location);
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
                NfcSpot nfcSpot = NfcSpot.stringToData(data);
                orte.add(nfcSpot);
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

    @Override
    public boolean isDetectionOfLocation(Location location) {
        if (location instanceof NfcSpot) {
            return true;
        } else return false;
    }

    @Override
    public int getLocationImage() {
        return R.drawable.ic_nfc_spot_white24dp;
    }

    @Override
    public void setLocationValues(Location location) {
        NfcSpot nfcSpot = (NfcSpot) location;
        NfcDetectionFragment nfcDetectionFragment = (NfcDetectionFragment) fragment;
        NfcSensor nfcSensor = new NfcSensor(nfcSpot.getNfcSensor().getSerialNumber());
        nfcDetectionFragment.setNfcValue(nfcSensor);
    }

    @Override
    public void saveLocationValues(Location location) {
        ((NfcDetectionFragment) fragment).saveLocationValues((NfcSpot) location);
    }

    public void setNfcDetectToFragment(boolean b) {
        detectionToFragment = b;
    }

    @Override
    public void handleNewIntent(Intent intent) {
        Log.d(TAG, "handleNewIntent");
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                && detectionToFragment) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            byte[] extraID = tagFromIntent.getId();

            StringBuilder sb = new StringBuilder();
            for (byte b : extraID) {
                sb.append(String.format("%02X", b));
                sb.append(":");
            };

            sb.deleteCharAt(sb.length() -1);

            String nfcTagSerialNum = sb.toString();

            ((NfcDetectionFragment) fragment).setNfcSensor(new NfcSensor(nfcTagSerialNum));
            detectionToFragment = false;
        }
    }
}
