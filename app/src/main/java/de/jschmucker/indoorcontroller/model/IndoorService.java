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

/**
 * The IndoorService is the Interface for all Activities to the model.
 * It contains the LocationsManagement, the TaskManagement and the ActionManagement.
 * It starts and stops the Detections.
 */
public class IndoorService extends Service {
    private final IBinder binder = new IndoorBinder();
    private final String TAG = getClass().getSimpleName();

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
        actionManagement.loadActions(this);
        taskManagement.loadTasks(this);
        locationManagement.startDetection();
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

    /**
     * Add the given Location to the LocationManagement.
     * @param location
     */
    public void addLocation(Location location) {
        locationManagement.addLocation(location);
    }

    /**
     * Get the Location with the given location id.
     * @param locationId Location id of the Location to return
     * @return The Location with the given location id.
     */
    public Location getLocation(int locationId) {
        return locationManagement.getLocations().get(locationId);
    }

    /**
     * @param taskId
     * @return The Task with the given task id.
     */
    public Task getTask(int taskId) {
        return taskManagement.getTasks().get(taskId);
    }

    /**
     * @return All ActionFragments
     */
    public ActionFragment[] getActionFragments() {
        return actionManagement.getActionFragments();
    }

    /**
     * Add the given Action to the ActionManagement.
     * @param action
     */
    public void addAction(Action action) {
        actionManagement.addAction(action);
    }

    /**
     * Returns null if this method has been called already without adding a new Action.
     * @return The last Action that was added to the ActionManagement.
     */
    public Action getAction() {
        return actionManagement.getAction();
    }

    /**
     * Add the given Task to the TaskManagement.
     * @param newTask
     */
    public void addTask(Task newTask) {
        taskManagement.addTask(newTask);
    }

    /**
     * @return All Actions from the ActionManagement.
     */
    public ArrayList<Action> getActions() {
        return actionManagement.getActions();
    }

    /**
     * @param location
     * @return The LocationDetection that belongs to the given Location
     */
    public LocationDetection getLocationDetection(Location location) {
        return locationManagement.getLocationDetection(location);
    }

    /**
     * Removes the given task in the TaskManagement.
     * @param task
     */
    public void removeTask(Task task) {
        taskManagement.removeTask(task);
    }

    /**
     * @param name
     * @return The Location with the given name
     */
    public Location getLocation(String name) {
        return locationManagement.getLocation(name);
    }

    /**
     * @param name
     * @return The Action with the given name
     */
    public Action getAction(String name) {
        return actionManagement.getAction(name);
    }

    /**
     * Removes the given Location.
     * @param location
     */
    public void removeLocation(Location location) {
        locationManagement.removeLocation(location);
    }

    /**
     * @return All Locations in the LocationManagement.
     */
    public ArrayList<Location> getLocations() {
        return locationManagement.getLocations();
    }

    /**
     * @return An Array with all LocationDetections
     */
    public LocationDetection[] getLocationDetections() {
        return locationManagement.getLocationDetections();
    }

    /**
     * @return All Tasks in the TaskManagement.
     */
    public ArrayList<Task> getTasks() {
        return taskManagement.getTasks();
    }

    /**
     * Reloads all settings in the LocationManagement.
     */
    public void reloadSettings() {
        locationManagement.reloadSettings();
    }

    /**
     * Binder for this service.
     */
    public class IndoorBinder extends Binder {
        public IndoorService getService() {
            return IndoorService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * @param name
     * @return False if the another Location has the given name, true if not
     */
    public boolean isLocationNameAvailable(String name) {
        if (name.equals("")) {
            return false;
        }
        return locationManagement.isNameAvailable(name);
    }

    /**
     * @param name
     * @return False if the another Action has the given name, true if not
     */
    public boolean isActionNameAvailable(String name) {
        if (name.equals("")) {
            return false;
        }
        return actionManagement.isNameAvailable(name);
    }
}