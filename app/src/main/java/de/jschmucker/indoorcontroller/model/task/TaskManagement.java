package de.jschmucker.indoorcontroller.model.task;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.actions.Action;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class TaskManagement {
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_TASK_NAME = getClass().getName() + "KEY_SAVE_TASK_NAME";
    private final String KEY_SAVE_TASK_ACTION = getClass().getName() + "KEY_SAVE_TASK_ACTION";
    private final String KEY_SAVE_TASK_LOCATION = getClass().getName() + "KEY_SAVE_TASK_LOCATION";
    private final String KEY_SAVE_LOCATION_COUNT = getClass().getName() + "KEY_SAVE_LOCATION_COUNT";
    private final String KEY_SAVE_ACTION_COUNT = getClass().getName() + "KEY_SAVE_ACTION_COUNT";
    private final String KEY_SAVE_LOCATION_BOOL = getClass().getName() + "KEY_SAVE_LOCATION_BOOL";
    private final String KEY_SAVE_TASK_ENABLED = getClass().getName() + "KEY_SAVE_TASK_ENABLED";

	private final ArrayList<Task> tasks;

	public TaskManagement(){
		tasks = new ArrayList<>();
	}

	/**
	 * 
	 * @param task    task
	 */
	public void addRule(Task task){
		tasks.add(task);
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void removeTask(Task task) {
		tasks.remove(task);
	}

	public void saveTasks(IndoorService indoorService) {
        Log.d(getClass().getSimpleName(), "saveTasks");
		SharedPreferences.Editor editor =
				PreferenceManager.getDefaultSharedPreferences(indoorService).edit();

        int count = 0;

		for (Task task : tasks) {
            String name = task.getName();
            int countActions = task.getActions().size();
            int countLocations = task.getLocations().size();

            String log = "save Task \"" + name + "\" \n";
            if (task.isEnabled()) {
                log += "active; ";
            } else {
                log += "not active; ";
            }
            log += countActions + " Actions; " + countLocations + " Locations;";

            editor.putString(KEY_SAVE_TASK_NAME + count, name);
            editor.putBoolean(KEY_SAVE_TASK_ENABLED + count, task.isEnabled());
            editor.putInt(KEY_SAVE_ACTION_COUNT + count, countActions);
            editor.putInt(KEY_SAVE_LOCATION_COUNT + count, countLocations);

            String keyLocation = KEY_SAVE_TASK_LOCATION + count;
            String keyBool = KEY_SAVE_LOCATION_BOOL + count;
            String keyAction = KEY_SAVE_TASK_ACTION + count;

            int countAction = 0;
            int countLocation = 0;

            for (Action action : task.getActions()) {
                editor.putString(keyAction + countAction++, action.getName());
            }

            for (Map.Entry<Location, Boolean> entry : task.getLocations().entrySet()) {
                editor.putString(keyLocation + countLocation, entry.getKey().getName());
                editor.putBoolean(keyBool + countLocation, entry.getValue());
                countLocation++;
            }

            Log.d(getClass().getSimpleName(), log);

            count++;
        }

        Log.d(getClass().getSimpleName(), "saved " + count + " tasks.");

        editor.putInt(KEY_SAVE_COUNT, count);
        editor.commit();
	}

    public void loadTasks(IndoorService indoorService) {
        Log.d(getClass().getSimpleName(), "loadTasks");
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(indoorService);

        int count = sharedPreferences.getInt(KEY_SAVE_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String name = sharedPreferences.getString(KEY_SAVE_TASK_NAME + i, "");

            if (!name.equals("")) {
                HashMap<Location, Boolean> locations = new HashMap<>();
                ArrayList<Action> actions = new ArrayList<>();

                int locationCount = sharedPreferences.getInt(KEY_SAVE_LOCATION_COUNT + i, 0);
                int actionCount = sharedPreferences.getInt(KEY_SAVE_ACTION_COUNT + i, 0);

                String keyLocation = KEY_SAVE_TASK_LOCATION + i;
                String keyBool = KEY_SAVE_LOCATION_BOOL + i;
                String keyAction = KEY_SAVE_TASK_ACTION + i;

                for (int e = 0; e < locationCount; e++) {
                    String locName = sharedPreferences.getString(keyLocation + e, "");
                    if (!locName.equals("")) {
                        Location location = indoorService.getLocation(locName);
                        boolean active = sharedPreferences.getBoolean(keyBool + e, true);
                        if (location != null) {
                            locations.put(location, active);
                        }
                    }
                }

                for (int e = 0; e < actionCount; e++) {
                    String actionName = sharedPreferences.getString(keyAction + e, "");

                    if (!actionName.equals("")) {
                        Action action = indoorService.getAction(actionName);
                        if (action != null) {
                            actions.add(action);
                        }
                    }
                }

                Task task = new Task(indoorService, name, locations, actions);
                task.setEnabled(sharedPreferences.getBoolean(KEY_SAVE_TASK_ENABLED + i, true));

                tasks.add(task);
            }
        }
    }
}//end TaskManagement