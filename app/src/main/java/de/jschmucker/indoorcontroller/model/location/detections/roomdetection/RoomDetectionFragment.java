package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.controller.IndoorServiceBound;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetectionFragment;

/**
 * The fragment for the RoomDetection
 * Implements BeaconConsumer and RangeNotifier for the Eddystone Bluetooth BeaconDetection
 */
public class RoomDetectionFragment extends LocationDetectionFragment implements BeaconConsumer, RangeNotifier {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private EditText editTextRoomWidth;
    private EditText editTextRoomLength;
    private EditText editTextDistanceB1;
    private EditText editTextDistanceB2;
    private EditText editTextDistanceB3;
    private TextView textViewChosenB1;
    private TextView textViewChosenB2;
    private TextView textViewChosenB3;

    private Room room = null; // if the fragment is used to change a rooms settings

    private BeaconManager mBeaconManager;
    private ArrayList<List<Identifier>> foundIdentifiers;
    private IdentifierListAdapter adapter;

    private List<Identifier> identifiersBeacon1;
    private List<Identifier> identifiersBeacon2;
    private List<Identifier> identifiersBeacon3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detection_room, container, false);

        IndoorServiceBound activity = (IndoorServiceBound) getActivity();
        IndoorService indoorService = activity.getIndoorService();
        RoomDetection roomDetection = (RoomDetection) indoorService.getLocationDetection(new Room(null, null, null));

        editTextDistanceB1 = (EditText) view.findViewById(R.id.room_fragment_editText_beacon1_distance);
        editTextDistanceB2 = (EditText) view.findViewById(R.id.room_fragment_editText_beacon2_distance);
        editTextDistanceB3 = (EditText) view.findViewById(R.id.room_fragment_editText_beacon3_distance);

        textViewChosenB1 = (TextView) view.findViewById(R.id.textView_chosen_beacon1);
        textViewChosenB2 = (TextView) view.findViewById(R.id.textView_chosen_beacon2);
        textViewChosenB3 = (TextView) view.findViewById(R.id.textView_chosen_beacon3);

        editTextRoomLength = (EditText) view.findViewById(R.id.room_fragment_editText_room_length);
        editTextRoomWidth = (EditText) view.findViewById(R.id.room_fragment_editText_room_width);

        Button beacon1 = (Button) view.findViewById(R.id.button_beacon1);
        beacon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBeacon(0);
            }
        });

        Button beacon2 = (Button) view.findViewById(R.id.button_beacon2);
        beacon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBeacon(1);
            }
        });

        Button beacon3 = (Button) view.findViewById(R.id.button_beacon3);
        beacon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBeacon(2);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission Check
            if (getActivity().checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        if (room != null) {
            // load settings from room
            editTextRoomLength.setText(String.valueOf(room.getRect().width()));
            editTextRoomWidth.setText(String.valueOf(room.getRect().height()));
            editTextDistanceB1.setText(String.valueOf(room.getBeacons()[0].getCoordinates().x));
            editTextDistanceB2.setText(String.valueOf(room.getBeacons()[1].getCoordinates().y));
            editTextDistanceB3.setText(String.valueOf(room.getBeacons()[2].getCoordinates().x));
            identifiersBeacon1 = room.getBeacons()[0].getIdentifiers();
            textViewChosenB1.setText(beaconStringFromIdentifierList(identifiersBeacon1));
            identifiersBeacon2 = room.getBeacons()[1].getIdentifiers();
            textViewChosenB2.setText(beaconStringFromIdentifierList(identifiersBeacon2));
            identifiersBeacon3 = room.getBeacons()[2].getIdentifiers();
            textViewChosenB3.setText(beaconStringFromIdentifierList(identifiersBeacon3));
        }

        foundIdentifiers = new ArrayList<>();
        adapter = new IdentifierListAdapter(getActivity(), foundIdentifiers);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupBeaconDetection();
    }

    @Override
    public void onPause() {
        mBeaconManager.unbind(this);
        super.onPause();
    }

    /**
     * Set the values of the given room for loading the GUI
     * @param room
     */
    public void setRoomValues(Room room) {
        this.room = room;
    }

    /**
     * Saves the settings made by the user in this fragment into the given room
     * @param room
     */
    public void saveLocationValues(Room room) {
        // values
        int roomLength;
        int roomWidth;
        int b1distance;
        int b2distance;
        int b3distance;

        if (editTextRoomLength.getText().toString().equals("")
                || editTextRoomWidth.getText().toString().equals("")
                || editTextDistanceB1.getText().toString().equals("")
                || editTextDistanceB2.getText().toString().equals("")
                || editTextDistanceB3.getText().toString().equals("")
                || (identifiersBeacon1 == null)
                || (identifiersBeacon1 == null)
                || (identifiersBeacon1 == null)) {
            return;
        }
        roomLength = (int) (Double.valueOf(editTextRoomLength.getText().toString()) * 100);
        roomWidth = (int) (Double.valueOf(editTextRoomWidth.getText().toString()) * 100);
        b1distance = (int) (Double.valueOf(editTextDistanceB1.getText().toString()) * 100);
        b2distance = (int) (Double.valueOf(editTextDistanceB2.getText().toString()) * 100);
        b3distance = (int) (Double.valueOf(editTextDistanceB3.getText().toString()) * 100);

        BeaconSensor beaconSensors[] = new BeaconSensor[3];
        beaconSensors[0] = new BeaconSensor(identifiersBeacon1, new Point(b1distance, 0));
        beaconSensors[1] = new BeaconSensor(identifiersBeacon2, new Point(roomLength, b2distance));
        beaconSensors[2] = new BeaconSensor(identifiersBeacon3, new Point(b3distance, roomWidth));

        room.setBeacons(beaconSensors);
        room.setRect(new Rect(0, 0, roomLength, roomWidth));
    }

    /**
     * Starts the BeaconDetection for this fragment
     */
    private void setupBeaconDetection() {
        mBeaconManager = BeaconManager.getInstanceForApplication(getActivity());
        // Detect the URL frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.bind(this);
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
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.d(getClass().getSimpleName(), "didRangeBeaconsInRegion: Count=" + beacons.size());
        if (beacons.size() >= 1) {
            foundIdentifiers.clear();
        }
        for (Beacon beacon : beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                List<Identifier> identifierList = beacon.getIdentifiers();
                foundIdentifiers.add(identifierList);
            }
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    /**
     * Creates a new Room with the settings made by the user in this fragment
     * @param name Name of the new Room
     * @return The created Room. Returns null if settings are not valid
     */
    public Location createRoom(String name) {
        Room room;

        // values
        int roomLength;
        int roomWidth;
        int b1distance;
        int b2distance;
        int b3distance;

        if (editTextRoomLength.getText().toString().equals("")
                || editTextRoomWidth.getText().toString().equals("")
                || editTextDistanceB1.getText().toString().equals("")
                || editTextDistanceB2.getText().toString().equals("")
                || editTextDistanceB3.getText().toString().equals("")
                || (identifiersBeacon1 == null)
                || (identifiersBeacon1 == null)
                || (identifiersBeacon1 == null)) {
            return null;
        }
        roomLength = (int) (Double.valueOf(editTextRoomLength.getText().toString()) * 100);
        roomWidth = (int) (Double.valueOf(editTextRoomWidth.getText().toString()) * 100);
        b1distance = (int) (Double.valueOf(editTextDistanceB1.getText().toString()) * 100);
        b2distance = (int) (Double.valueOf(editTextDistanceB2.getText().toString()) * 100);
        b3distance = (int) (Double.valueOf(editTextDistanceB3.getText().toString()) * 100);

        BeaconSensor beaconSensors[] = new BeaconSensor[3];
        beaconSensors[0] = new BeaconSensor(identifiersBeacon1, new Point(b1distance, 0));
        beaconSensors[1] = new BeaconSensor(identifiersBeacon2, new Point(roomLength, b2distance));
        beaconSensors[2] = new BeaconSensor(identifiersBeacon3, new Point(b3distance, roomWidth));

        room = new Room(name, new Rect(0, 0, roomLength, roomWidth), beaconSensors);
        return room;
    }

    /**
     * Adapter class for the Identifiers of the detected beacons
     */
    private class IdentifierListAdapter extends BaseAdapter {
        private final ArrayList<List<Identifier>> identifierList;
        private final LayoutInflater inflater;

        /**
         * Create a new IdentifierListAdapter with the given list of identifiers
         * @param context
         * @param identifierList List of identifiers
         */
        IdentifierListAdapter(Context context, ArrayList<List<Identifier>> identifierList) {
            this.identifierList = identifierList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return identifierList.size();
        }

        @Override
        public Object getItem(int position) {
            return identifierList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = inflater.inflate(android.R.layout.simple_list_item_2, null);
            }

            TextView text1 = (TextView) v.findViewById(android.R.id.text1);
            String text = beaconStringFromIdentifierList(identifierList.get(position));

            text1.setText(text);

            return v;
        }
    }

    /**
     * Opens an AlertDialog to choose a Beacon for the specified BeaconSensor
     * @param index Specifies the BeaconSensor that will be chosen. (0 = Beacon1, 1 = Beacon2, 2 = Beacon3)
     */
    private void chooseBeacon(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (index) {
                    case 0:
                        identifiersBeacon1 = foundIdentifiers.get(which);
                        textViewChosenB1.setText(beaconStringFromIdentifierList(identifiersBeacon1));
                        break;
                    case 1:
                        identifiersBeacon2 = foundIdentifiers.get(which);
                        textViewChosenB2.setText(beaconStringFromIdentifierList(identifiersBeacon2));
                        break;
                    case 2:
                        identifiersBeacon3 = foundIdentifiers.get(which);
                        textViewChosenB3.setText(beaconStringFromIdentifierList(identifiersBeacon3));
                        break;
                }
            }
        }).setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(getString(R.string.choose_beacon))
                .show();
    }

    /**
     * Creates a maximal four lines String from the given identifiers.
     * The String is in the following form (depending on how many identifiers the beacon has; max. 3):
     * Beacon:
     * Id1:
     * Id2:
     * Id3:
     * @param identifiers Identifiers that will be parsed into a String
     * @return String parsed from the identifiers
     */
    private String beaconStringFromIdentifierList(List<Identifier> identifiers) {
        String text = "Beacon:\n";
        int i = 1;
        for (Identifier identifier : identifiers) {
            text += "Id" + i++ + ": " + identifier.toHexString() + "\n";
        }
        text = text.substring(0, text.length() - 2); // cut away last newline

        return text;
    }
}
