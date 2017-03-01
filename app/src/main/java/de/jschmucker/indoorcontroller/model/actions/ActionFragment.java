package de.jschmucker.indoorcontroller.model.actions;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * This class has to be extended to include a new action category.
 * Created by jschmucker on 14.02.17.
 */
public abstract class ActionFragment extends Fragment {

    /**
     * Creates a new action with the given name using the settings made by the user in this fragment.
     * @param name Name of the new created action
     * @return The new created action
     */
    public abstract Action createAction(String name);
    // Name to be shown in the list of creatable actions

    /**
     * Every action category has to offer a name that describes that category
     * @param context
     * @return The description of the action category
     */
    public abstract String getActionName(Context context);

    /**
     * This function will be called when the IndoorService or the app starts/restarts.
     * Each ActionFragment has to load all the actions that it saved the last time saveActions() was called and save them into the actions list.
     * @param context
     * @param actions The ActionFragment has to add the loaded Actions to this list
     */
    public abstract void loadActions(Context context, ArrayList<Action> actions);

    /**
     * This function will be called when the IndoorService or the app stops.
     * Each ActionFragment has to save all the actions in the actions-list that are of the ActionFragments type.
     * @param context
     * @param actions List with all actions that have to be saved. Each ActionFragment only has to save its own actions
     */
    public abstract void saveActions(Context context, ArrayList<Action> actions);
}
