package de.jschmucker.indoorcontroller.model.actions.examplecontrol;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import de.jschmucker.indoorcontroller.model.actions.Action;

/**
 * Created by jschmucker on 12.02.17.
 */

public class ExampleAction extends Action {
    private final String toPrint;
    private final Handler handler;

    public ExampleAction(String name, String text) {
        this.name = name;
        toPrint = text;
        handler = new Handler();
    }

    @Override
    public void execute(final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, toPrint, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        new Thread(runnable).start();
        /*Context c = context.getApplicationContext();
        Toast.makeText(c, toPrint, Toast.LENGTH_SHORT).show();*/
    }

    public String getText() {
        return toPrint;
    }
}
