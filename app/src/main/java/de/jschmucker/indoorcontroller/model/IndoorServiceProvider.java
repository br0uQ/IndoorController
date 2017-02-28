package de.jschmucker.indoorcontroller.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Observable;

import de.jschmucker.indoorcontroller.model.IndoorService;

/**
 * Created by jschmucker on 14.02.17.
 */

public class IndoorServiceProvider extends Observable {
    private IndoorService indoorService;
    private boolean bound;
    public static final int CONNECTED = 1;
    public static final int NOT_CONNECTED = 0;

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

    public boolean isBound() {
        return bound;
    }

    public void connectToService(Context context) {
        if (!bound) {
            // Bind to Service
            Intent intent = new Intent(context, IndoorService.class);
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void disconnectFromService(Context context) {
        if (bound) {
            context.unbindService(mConnection);
            bound = false;
        }
    }

    public IndoorService getIndoorService() {
        return indoorService;
    }
}
