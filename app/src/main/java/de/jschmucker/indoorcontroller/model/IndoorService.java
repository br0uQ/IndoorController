package de.jschmucker.indoorcontroller.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Observer;

import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.OrtsManagement;
import de.jschmucker.indoorcontroller.model.ort.sensor.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.NFCSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.WifiSensor;
import de.jschmucker.indoorcontroller.model.regel.Ortsregel;
import de.jschmucker.indoorcontroller.model.regel.RegelManagement;
import de.jschmucker.indoorcontroller.model.steuerung.Steuerung;

public class IndoorService extends Service {
    private final IBinder binder = new IndoorBinder();

    private OrtsManagement ortsManagement;
    private RegelManagement regelManagement;
    private Steuerung steuerung;

    public IndoorService() {
        ortsManagement = new OrtsManagement();
        regelManagement = new RegelManagement();
        steuerung = new Steuerung();
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
