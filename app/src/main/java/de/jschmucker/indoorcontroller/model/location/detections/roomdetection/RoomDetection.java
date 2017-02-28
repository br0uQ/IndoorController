package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * Created by jschmucker on 01.02.17.
 */

public class RoomDetection extends LocationDetection implements BeaconConsumer, RangeNotifier {
    private final String KEY_SAVE_COUNT = getClass().getName() + "KEY_SAVE_COUNT";
    private final String KEY_SAVE_OBJECT = getClass().getName() + "KEY_SAVE_OBJECT";
    private final Context context;

    private BeaconManager mBeaconManager;
    private ArrayList<Location> locations;

    public RoomDetection(Context context) {
        this.context = context;
        name = context.getString(R.string.room_detection_name);
        fragment = new RoomDetectionFragment();
    }

    @Override
    public Location createLocation(String name) {
        return ((RoomDetectionFragment) fragment).createRoom(name);
    }

    @Override
    public void saveLocations(ArrayList<Location> locations) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();

        int count = 0;
        for (Location location : locations) {
            if (location instanceof Room) {
                String data = Room.dataToString((Room) location);
                editor.putString(KEY_SAVE_OBJECT + count++, data);
            }
        }
        editor.putInt(KEY_SAVE_COUNT, count);
        editor.commit();
    }

    @Override
    public void loadLocations(ArrayList<Location> locations) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int count = preferences.getInt(KEY_SAVE_COUNT, 0);

        for (int i = 0; i < count; i++) {
            String data = preferences.getString(KEY_SAVE_OBJECT + i, null);
            if (data != null) {
                Room room = Room.stringToData(data);
                locations.add(room);
            }
        }
    }

    @Override
    public void startDetection(ArrayList<Location> locations) {
        this.locations = locations;

        loadSettings();

        mBeaconManager = BeaconManager.getInstanceForApplication(context);
        // Detect the URL frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.bind(this);
    }

    @Override
    public void stopDetection() {
        mBeaconManager.unbind(this);
    }

    @Override
    public boolean isDetectionOfLocation(Location location) {
        return location instanceof Room;
    }

    @Override
    public void setLocationValues(Location location) {
        RoomDetectionFragment roomDetectionFragment = (RoomDetectionFragment) fragment;
        Room room = (Room) location;

        roomDetectionFragment.setRoomValues(room);
    }

    @Override
    public void saveLocationValues(Location location) {
        ((RoomDetectionFragment) fragment).saveLocationValues((Room) location);
    }

    @Override
    public int getPreferenceResource() {
        return R.xml.preference_room_detection;
    }

    @Override
    public void reloadSettings() {
        loadSettings();
    }

    private void loadSettings() {
        // load Settings
        // atm no settings
    }

    /*
                     C
        -------------x-----
       |       distanceC   |
       |                   |
       |      P x          |
       |         distanceB x B
       |  distanceA        |
        ----x--------------
            A
     */
    private boolean contains(Rect rect,
                                   Point a,
                                   Point b,
                                   Point c,
                                   double distanceA,
                                   double distanceB,
                                   double distanceC) {
        double[][] positions = new double[][] { { a.x, a.y }, { b.x, b.y }, { c.x, c.y } };
        double[] distances = new double[] { distanceA, distanceB, distanceC };

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        double[] centroid = optimum.getPoint().toArray();
        Point position = new Point((int) centroid[0], (int) centroid[1]);
        String log = "Rect(0,0," + rect.width() + "," + rect.height()
                + ") contains Point(" + position.x + "," + position.y + "): "
                + rect.contains(position.x, position.y) + "\n";
        log += "Sensors at: (" + a.x + "," + a.y + "), (" + b.x + "," + b.y + "), (" + c.x + "," + c.y + ")\n";
        log += "Distances: " + distanceA + ", " + distanceB + ", " + distanceC;
        Log.d(getClass().getSimpleName(), log);
        return rect.contains(position.x, position.y);
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            Log.d(getClass().getSimpleName(), "onBeaconServiceConnect");
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent, serviceConnection, i);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.d(getClass().getSimpleName(), "didRangeBeaconsInRegion: Count=" + beacons.size());
        HashMap<List<Identifier>, Double> beaconDistanceMap = new HashMap<>();
        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Log.d(getClass().getSimpleName(), "beacon in region found. Message: ");
                String message = "I see a beacon approximately "
                        + beacon.getDistance() + " meters away.\n";
                List<Identifier> identifierList = beacon.getIdentifiers();
                message += "Beacon Identifiers:\n";
                int i = 1;
                for (Identifier identifier : identifierList) {
                    message += "Id" + i++ + ": " + identifier.toHexString() + "\n";
                }
                Log.d(getClass().getSimpleName(), message);
                beaconDistanceMap.put(identifierList, beacon.getDistance());
            }
        }

        for (Location location : locations) {
            if (location instanceof Room) {
                checkRoom((Room) location, beaconDistanceMap);
            }
        }
    }

    private void checkRoom(Room room, Map<List<Identifier>, Double> beaconDistanceMap) {
        double distances[] = new double[3];

        BeaconSensor[] beaconSensors = room.getBeacons();
        int beaconCount = 0;
        for (int i = 0; i < beaconSensors.length; i++) {
            for (Map.Entry<List<Identifier>, Double> entry : beaconDistanceMap.entrySet()) {
                if (beaconSensors[i].matchesIdentifier(entry.getKey())) {
                    distances[i] = entry.getValue() * 100;
                    beaconCount++;
                    break;
                }
            }
        }

        if (beaconCount == 3) {
            room.setActive(contains(room.getRect(),
                    beaconSensors[0].getCoordinates(),
                    beaconSensors[1].getCoordinates(),
                    beaconSensors[2].getCoordinates(),
                    distances[0], distances[1], distances[2]));

        }
    }
}
