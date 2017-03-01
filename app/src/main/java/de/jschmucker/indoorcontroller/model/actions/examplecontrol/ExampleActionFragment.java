package de.jschmucker.indoorcontroller.model.actions.examplecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.actions.Action;
import de.jschmucker.indoorcontroller.model.actions.ActionFragment;

/**
 * Created by jschmucker on 13.02.17.
 */
public class ExampleActionFragment extends ActionFragment {
    private EditText editText;
    private final String KEY_SAVE_NAME = getClass().getName() + "KEY_SAVE_NAME";
    private final String KEY_SAVE_TEXT = getClass().getName() + "KEY_SAVE_TEXT";
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_example_action, container, false);

        editText = (EditText) view.findViewById(R.id.example_action_editText);

        return view;
    }

    /**
     * Creates a new ExampleAction with the settings in this fragment and the given name and returns it.
     * @param name Name of the new ExampleAction
     * @return The new created ExampleAction
     */
    public ExampleAction createAction(String name) {
        return new ExampleAction(name, editText.getText().toString());
    }

    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.example_action_name);
    }

    @Override
    public void loadActions(Context context, ArrayList<Action> actions) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int count = preferences.getInt(KEY_SAVE_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String name = preferences.getString(KEY_SAVE_NAME + i, "");
            String text = preferences.getString(KEY_SAVE_TEXT + i, "");
            if (!name.equals("")) {
                actions.add(new ExampleAction(name, text));
            }
        }
    }

    @Override
    public void saveActions(Context context, ArrayList<Action> actions) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Action action : actions) {
            if (action instanceof ExampleAction) {
                editor.putString(KEY_SAVE_NAME + count, action.getName());
                editor.putString(KEY_SAVE_TEXT + count, ((ExampleAction) action).getText());
                count++;
            }
        }

        editor.putInt(KEY_SAVE_COUNT, count);
        editor.commit();
    }
}
