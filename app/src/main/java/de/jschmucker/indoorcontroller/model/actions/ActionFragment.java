package de.jschmucker.indoorcontroller.model.actions;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by joshua on 14.02.17.
 */

public abstract class ActionFragment extends Fragment {

    public abstract Action createAction(String name);
    // Name to be shown in the list of creatable actions
    public abstract String getActionName(Context context);
}