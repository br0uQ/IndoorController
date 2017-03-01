package de.jschmucker.indoorcontroller.model.actions.examplecontrol;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import de.jschmucker.indoorcontroller.model.actions.Action;

/**
 * An example action to show a text as a android-toast.
 * Created by jschmucker on 12.02.17.
 */
public class ExampleAction extends Action {
    private final String toPrint;
    private final Handler handler;

    /**
     * Create a new ExampleAction with a name and a text to be shown as a toast when being executed.
     * @param name Name of the new ExampleAction
     * @param text Text that the ExampleAction shall show as a toast when executed
     */
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

    /**
     * @return Text that this ExampleAction shows when being executed
     */
    public String getText() {
        return toPrint;
    }
}
