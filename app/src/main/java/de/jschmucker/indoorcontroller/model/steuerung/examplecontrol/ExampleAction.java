package de.jschmucker.indoorcontroller.model.steuerung.examplecontrol;

import android.content.Context;
import android.widget.Toast;

import de.jschmucker.indoorcontroller.model.steuerung.Action;

/**
 * Created by joshua on 12.02.17.
 */

public class ExampleAction implements Action {
    String toPrint;

    ExampleAction(String toPrint) {
        this.toPrint = toPrint;
    }

    @Override
    public void execute(Context context) {
        Toast.makeText(context, toPrint, Toast.LENGTH_SHORT).show();
    }
}
