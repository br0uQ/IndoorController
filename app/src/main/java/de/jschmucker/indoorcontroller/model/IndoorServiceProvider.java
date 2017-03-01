package de.jschmucker.indoorcontroller.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Observable;

import de.jschmucker.indoorcontroller.model.IndoorService;

/**
 * This class can be used to connect to the IndoorService.
 * To do so:
 * 1. Create a new IndoorServiceProvider object.
 * 2. Add an Observer for this IndoorServiceProvider that will be notified if the service is connected.
 * 3. call connectToService() method.
 * When the service is connected the update method of the Observer will be called.
 * The argument parameter in the update method will be either CONNECTED or NOT_CONNECTED.
 * Created by jschmucker on 14.02.17.
 */
public class IndoorServiceProvider extends Observable {
    private IndoorService indoorService;
    private boolean bound;
    public static final int CONNECTED = 1;
    public static final int NOT_CONNECTED = 0;

    /**
     * ServiceConnection to the IndoorService.
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IndoorService.IndoorBinder binder = (IndoorService.IndoorBinder) service;
            indoorService = binder.getService();
            bound = true;

            setChanged();
            notifyObservers(CONNECTED);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;

            setChanged();
            notifyObservers(NOT_CONNECTED);
        }
    };

    /**
     * @return True if connected to the IndoorService, false if not
     */
    public boolean isBound() {
        return bound;
    }

    /**
     * Connect to the IndoorService.
     * @param context
     */
    public void connectToService(Context context) {
        if (!bound) {
            // Bind to Service
            Intent intent = new Intent(context, IndoorService.class);
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * Disconnect from the IndoorService.
     * @param context
     */
    public void disconnectFromService(Context context) {
        if (bound) {
            context.unbindService(mConnection);
            bound = false;
        }
    }

    /**
     * @return The IndoorService.
     */
    public IndoorService getIndoorService() {
        return indoorService;
    }
}
