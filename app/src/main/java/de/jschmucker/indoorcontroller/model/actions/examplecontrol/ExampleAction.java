package de.jschmucker.indoorcontroller.model.actions.examplecontrol;

import android.content.Context;
import android.widget.Toast;

import de.jschmucker.indoorcontroller.model.actions.Action;

/**
 * Created by joshua on 12.02.17.
 */

public class ExampleAction extends Action {
    String toPrint;

    public ExampleAction(String name, String text) {
        this.name = name;
        toPrint = text;
    }

    @Override
    public void execute(Context context) {
        Toast.makeText(context, toPrint, Toast.LENGTH_SHORT).show();
    }
}
