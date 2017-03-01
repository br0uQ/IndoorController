package de.jschmucker.indoorcontroller.controller;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;

/**
 * The fragment that includes the preferences of the app.
 * This fragment is used by the SettingsActivity.
 * Created by joshua on 28.02.17.
 */
public class SettingsFragment extends PreferenceFragment implements Observer {
    private IndoorServiceProvider indoorServiceProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);

        indoorServiceProvider.connectToService(getActivity());
    }

    @Override
    public void update(Observable o, Object arg) {
        int type = (int) arg;
        if (type == IndoorServiceProvider.CONNECTED) {
            IndoorService indoorService = indoorServiceProvider.getIndoorService();
            LocationDetection[] locationDetections = indoorService.getLocationDetections();

            for (LocationDetection locationDetection : locationDetections) {
                addPreferencesFromResource(locationDetection.getPreferenceResource());
            }

        } else if (type == IndoorServiceProvider.NOT_CONNECTED) {

        }
    }

    /**
     * Calls reloadSettings in the IndoorService to refresh the new settings in the server.
     */
    public void saveSettings() {
        if (indoorServiceProvider.getIndoorService() != null) {
            indoorServiceProvider.getIndoorService().reloadSettings();
        }
        indoorServiceProvider.disconnectFromService(getActivity());
    }
}
