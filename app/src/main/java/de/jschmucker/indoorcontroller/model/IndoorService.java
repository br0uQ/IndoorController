package de.jschmucker.indoorcontroller.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.actions.Action;
import de.jschmucker.indoorcontroller.model.actions.ActionFragment;
import de.jschmucker.indoorcontroller.model.actions.ActionManagement;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;
import de.jschmucker.indoorcontroller.model.location.LocationManagement;
import de.jschmucker.indoorcontroller.model.task.Task;
import de.jschmucker.indoorcontroller.model.task.TaskManagement;

public class IndoorService extends Service {
    private final IBinder binder = new IndoorBinder();
    private final String TAG = getClass().getSimpleName();
    public static final String KEY_LOCATIONS = "KEY_LOCATIONS";

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

        actionManagement.loadActions(this);

        taskManagement.loadTasks(this);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        locationManagement.saveLocations();
        locationManagement.stopDetection();

        actionManagement.saveActions(this);

        taskManagement.saveTasks(this);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        return START_STICKY;
    }

    public void addOrt(Location location) {
        locationManagement.addOrt(location);
    }

    public Location getOrt(int locationId) {
        return locationManagement.getLocations().get(locationId);
    }

    public Task getRule(int ruleId) {
        return taskManagement.getTasks().get(ruleId);
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

    public ArrayList<Action> getActions() {
        return actionManagement.getActions();
    }

    public LocationDetection getLocationDetection(Location location) {
        return locationManagement.getLocationDetection(location);
    }

    public void removeTask(Task rule) {
        taskManagement.removeTask(rule);
    }

    public Location getLocation(String name) {
        return locationManagement.getLocation(name);
    }

    public Action getAction(String name) {
        return actionManagement.getAction(name);
    }

    public void removeLocation(Location location) {
        locationManagement.removeLocation(location);
    }

    public ArrayList<Location> getLocations() {
        return locationManagement.getLocations();
    }

    public LocationDetection[] getLocationDetections() {
        return locationManagement.getLocationDetections();
    }

    public ArrayList<Task> getTasks() {
        return taskManagement.getTasks();
    }

    public void reloadSettings() {
        locationManagement.reloadSettings();
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