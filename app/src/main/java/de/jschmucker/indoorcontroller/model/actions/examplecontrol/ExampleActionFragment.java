package de.jschmucker.indoorcontroller.model.actions.examplecontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.actions.ActionFragment;

/**
 * Created by joshua on 13.02.17.
 */

public class ExampleActionFragment extends ActionFragment {
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_example_action, container, false);

        editText = (EditText) view.findViewById(R.id.example_action_editText);

        return view;
    }

    public ExampleAction createAction(String name) {
        return new ExampleAction(name, editText.getText().toString());
    }

    @Override
    public String getActionName(Context context) {
        return context.getString(R.string.example_action_name);
    }
}
