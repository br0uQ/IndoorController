package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;

/**
 * Created by jschmucker on 01.02.17.
 */

public class NfcDetection extends LocationDetection {
    private final String TAG = getClass().getSimpleName();

    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";
    private final Context context;
    private boolean detectionToFragment = false;
    private boolean detect = false;
    private final HashMap<NfcSpot, Integer> activeSpots;  // key: active nfcspot, value: countdown time

    private volatile int timeToSetInactive = 5;

    private Thread detectionThread = null;

    private ArrayList<Location> locations;

    public NfcDetection(Context context) {
        this.context = context;
        fragment = new NfcDetectionFragment();
        name = context.getString(R.string.nfc_detection_name);
        activeSpots = new HashMap<>();
    }

    @Override
    public Location createLocation(String name) {
        return ((NfcDetectionFragment) fragment).createLocation(name);
    }

    @Override
    public void saveLocations(ArrayList<Location> locations) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Location location : locations) {
            if (location instanceof NfcSpot) {
                String data = NfcSpot.dataToString((NfcSpot) location);
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
                NfcSpot nfcSpot = NfcSpot.stringToData(data);
                locations.add(nfcSpot);
            }
        }
    }

    @Override
    public void startDetection(ArrayList<Location> locations) {
        loadSettings();

        this.locations = locations;
        detect = true;

        if (detectionThread == null) {
            detectionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (detect) {
                        for (HashMap.Entry<NfcSpot, Integer> entry : activeSpots.entrySet()) {
                            Log.d("NFCDetection", "count down NfcSpot: " + entry.getKey().getName());
                            checkNfcSpot(entry);
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.d(getClass().getSimpleName(), e.toString());
                        }
                    }

                    for (NfcSpot nfcSpot : activeSpots.keySet()) {
                        String log = "Set NfcSpot \"" + nfcSpot.getName() + "\": ";
                        Log.d(getClass().getSimpleName(), log + "false");
                        nfcSpot.setActive(false);
                    }
                    activeSpots.clear();
                    detectionThread = null;
                }

                private void checkNfcSpot(HashMap.Entry<NfcSpot, Integer> entry) {
                    int time = entry.getValue();
                    time--;
                    if (time <= 0) {
                        NfcSpot nfcSpot = entry.getKey();
                        String log = "Set NfcSpot \"" + nfcSpot.getName() + "\": ";
                        Log.d(getClass().getSimpleName(), log + "false");
                        nfcSpot.setActive(false);
                        activeSpots.remove(entry.getKey());
                    } else {
                        entry.setValue(time);
                    }
                }
            });
            detectionThread.start();
        }
    }

    @Override
    public void stopDetection() {
        detect = false;
    }

    @Override
    public boolean isDetectionOfLocation(Location location) {
        return location instanceof NfcSpot;
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
            }

            sb.deleteCharAt(sb.length() -1);

            String nfcTagSerialNum = sb.toString();

            ((NfcDetectionFragment) fragment).setNfcSensor(new NfcSensor(nfcTagSerialNum), tagFromIntent);
            detectionToFragment = false;
        }
    }

    @Override
    public int getPreferenceResource() {
        return R.xml.preference_nfc_detection;
    }

    @Override
    public void reloadSettings() {
        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String timerCountString = sharedPreferences.getString("nfc_pref_key_timer_count", "");
        Log.d(getClass().getSimpleName(), "TimerCountString: " + timerCountString);
        if (!timerCountString.equals("")) {
            int timerCount = Integer.valueOf(timerCountString); // scan interval in s
            timeToSetInactive = timerCount;
        }
    }

    public void detectedNfcSensor(NfcSensor foundSensor) {
        if (detect) {
            for (Location location : locations) {
                if (location instanceof NfcSpot) {
                    final NfcSpot nfcSpot = (NfcSpot) location;
                    if (nfcSpot.getNfcSensor().getSerialNumber().equals(foundSensor.getSerialNumber())) {
                        String log = "Set NfcSpot \"" + nfcSpot.getName() + "\": ";
                        Log.d(getClass().getSimpleName(), log + "true");
                        nfcSpot.setActive(true);
                        activeSpots.put(nfcSpot, timeToSetInactive);
                    }
                }
            }
        }
    }
}
