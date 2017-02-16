package de.jschmucker.indoorcontroller.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observer;

import de.jschmucker.indoorcontroller.model.actions.Action;
import de.jschmucker.indoorcontroller.model.actions.ActionFragment;
import de.jschmucker.indoorcontroller.model.actions.ActionManagement;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;
import de.jschmucker.indoorcontroller.model.location.LocationManagement;
import de.jschmucker.indoorcontroller.model.location.detections.nfcdetection.NfcSpot;
import de.jschmucker.indoorcontroller.model.location.detections.nfcdetection.NfcSensor;
import de.jschmucker.indoorcontroller.model.location.detections.roomdetection.BeaconSensor;
import de.jschmucker.indoorcontroller.model.location.detections.roomdetection.Room;
import de.jschmucker.indoorcontroller.model.location.detections.wifidetection.WifiSensor;
import de.jschmucker.indoorcontroller.model.location.detections.wifidetection.WifiEnvironment;
import de.jschmucker.indoorcontroller.model.task.Task;
import de.jschmucker.indoorcontroller.model.task.TaskManagement;

public class IndoorService extends Service {
    private final IBinder binder = new IndoorBinder();
    private final String TAG = getClass().getSimpleName();
    public static final String KEY_ORTE = "KEY_ORTE";

    private LocationManagement locationManagement;
    private TaskManagement taskManagement;
    private ActionManagement actionManagement;

    @Override
    public void onCreate() {
        locationManagement = new LocationManagement(this);
        taskManagement = new TaskManagement();
        actionManagement = new ActionManagement();
        Log.d(TAG, "Service onCreate");

        /* Load locations and start the detections */
        locationManagement.loadLocations();
        locationManagement.startDetection();

        /* for test create one location of every type */
        if (locationManagement.getOrte().size() <= 0) {
            addOrt(new Room("TestRaum", new BeaconSensor[]{new BeaconSensor(), new BeaconSensor(),
                    new BeaconSensor(), new BeaconSensor()}));
            addOrt(new NfcSpot("TestNFCSpot", new NfcSensor("Test")));
            addOrt(new WifiEnvironment("TestWifiUmgebung", new ArrayList<WifiSensor>()));
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        locationManagement.saveLocations();
        locationManagement.stopDetection();

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

    public NfcSensor getFoundNfcSensor() {
        //ToDo
        return null;
    }

    public void startSingleNfcScan(Observer observer) {
        //ToDo
    }

    public void addOrt(Location neuerLocation) {
        locationManagement.addOrt(neuerLocation);
    }

    public Location getOrt(int locationId) {
        return locationManagement.getOrte().get(locationId);
    }

    public Task getRule(int ruleId) {
        return taskManagement.getRegeln().get(ruleId);
    }

    public ActionFragment[] getActionFragments() {
        return actionManagement.getActionFragments();
    }

    public void addAction(Action action) {
        actionManagement.addAction(action);
    }

    public Action getAction() {
        return actionManagement.getAction();
    }

    public void addTask(Task newTask) {
        taskManagement.addRule(newTask);
    }

    public int getLocationImage(Location location) {
        return locationManagement.getLocationImage(location);
    }

    public ArrayList<Action> getActions() {
        return actionManagement.getActions();
    }

    public LocationDetection getLocationDetection(Location location) {
        return locationManagement.getLocationDetection(location);
    }

    public void removeTask(Task rule) {
        taskManagement.removeTask(rule);
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

    public LocationManagement getLocationManagement() {
        return locationManagement;
    }

    public TaskManagement getTaskManagement() {
        return taskManagement;
    }

    public ActionManagement getActionManagement() {
        return actionManagement;
    }

    public boolean isLocationNameAvailable(String name) {
        if (name.equals("")) {
            return false;
        }
        return locationManagement.isNameAvailable(name);
    }

    public boolean isActionNameAvailable(String name) {
        if (name.equals("")) {
            return false;
        }
        return actionManagement.isNameAvailable(name);
    }
}
