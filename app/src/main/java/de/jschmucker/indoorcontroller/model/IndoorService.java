package de.jschmucker.indoorcontroller.model;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.prefs.PreferenceChangeEvent;

import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.OrtsManagement;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NFCSensor;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiSensor;
import de.jschmucker.indoorcontroller.model.regel.Ortsregel;
import de.jschmucker.indoorcontroller.model.regel.RegelManagement;
import de.jschmucker.indoorcontroller.model.steuerung.Steuerung;

public class IndoorService extends Service {
    private final IBinder binder = new IndoorBinder();
    private final String TAG = getClass().getSimpleName();
    public static final String KEY_ORTE = "KEY_ORTE";

    private OrtsManagement ortsManagement;
    private RegelManagement regelManagement;
    private Steuerung steuerung;

    @Override
    public void onCreate() {
        ortsManagement = new OrtsManagement(this);
        regelManagement = new RegelManagement();
        steuerung = new Steuerung();
        Log.d(TAG, "Service onCreate");

        for (LocationDetection detection : ortsManagement.getLocationDetections()) {
            detection.loadLoactions(ortsManagement.getOrte());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        for (LocationDetection detection : getOrtsManagement().getLocationDetections()) {
            detection.saveLocations(ortsManagement.getOrte());
        }

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    public ArrayList<BeaconSensor> getBeacons() {
        //ToDo
        return new ArrayList<BeaconSensor>();
    }

    public void startBeaconScan() {
        //ToDo
    }

    public NFCSensor getFoundNfcSensor() {
        //ToDo
        return null;
    }

    public void startSingleNfcScan(Observer observer) {
        //ToDo
    }

    public ArrayList<WifiSensor> getWifiEnvironment() {
        //ToDo
        return new ArrayList<>();
    }

    public void addOrt(Ort neuerOrt) {
        ortsManagement.addOrt(neuerOrt);
    }

    public Ort getOrt(int locationId) {
        return ortsManagement.getOrte().get(locationId);
    }

    public Ortsregel getRule(int ruleId) {
        return regelManagement.getRegeln().get(ruleId);
    }

    public class IndoorBinder extends Binder {
        public IndoorService getService() {
            return IndoorService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public OrtsManagement getOrtsManagement() {
        return ortsManagement;
    }

    public RegelManagement getRegelManagement() {
        return regelManagement;
    }

    public Steuerung getSteuerung() {
        return steuerung;
    }
}
