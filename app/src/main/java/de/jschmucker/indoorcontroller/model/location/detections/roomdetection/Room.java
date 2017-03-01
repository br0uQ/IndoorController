package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import org.altbeacon.beacon.Identifier;

import java.util.ArrayList;
import java.util.List;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * A room is a location defined by its room size (length, width) and three Eddystone Bluetooth Beacons that are placed in this room
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
class Room extends Location {

    private Rect rect;  // Rectangle that defines the room
	private BeaconSensor[] beaconSensors;

    /**
     * Create a new Room with the given parameters.
     * @param name Name of the room
     * @param rect Size of the room (length, width), the rect shall start at (0,0)
     * @param sensors BeaconSensors that are placed in that room
     */
	public Room(String name, Rect rect, BeaconSensor[] sensors){
		this.name = name;
        this.rect = rect;
		beaconSensors = sensors;
		imageResourceId = R.drawable.ic_room_white24dp;
	}

    /**
     * @return The BeaconSensors of this room
     */
	public BeaconSensor[] getBeacons() {
		return beaconSensors;
	}

    /**
     * Converts the data of a Room into a String
     * @param room The room that shall be parsed into a String
     * @return The String parsed from the room
     */
	static String dataToString(Room room) {
		StringBuilder sb = new StringBuilder();

		sb.append(room.getName());
		sb.append("\n");
        sb.append(room.getRect().height());
        sb.append("\n");
        sb.append(room.getRect().width());
        sb.append("\n");

		for (BeaconSensor sensor : room.getBeacons()) {
            sb.append(sensor.getIdentifiers().size());
            sb.append("\n");
            for (Identifier identifier : sensor.getIdentifiers()) {
                sb.append(identifier.toHexString());
                sb.append("\n");
            }
            sb.append(sensor.getCoordinates().x);
            sb.append("\n");
            sb.append(sensor.getCoordinates().y);
            sb.append("\n");
		}

		return sb.toString();
	}

    /**
     * Creates a Room with the data given in form of a String
     * @param data String that will be parsed into a Room
     * @return The parsed Room
     */
	static Room stringToData(String data) {
        Log.d("Room: stringToData", data);
        String[] dataArray = data.split("\n");
		String name = dataArray[0];
        Rect rect = new Rect(0, 0, Integer.parseInt(dataArray[2]), Integer.parseInt(dataArray[1]));
		BeaconSensor[] sensors = new BeaconSensor[3];

		int index = 3;

        for (int i = 0; i < sensors.length; i++) {
            int identifierSize = Integer.parseInt(dataArray[index++]);
            List<Identifier> identifierList = new ArrayList<>();
            for (int e = 0; e < identifierSize; e++) {
                identifierList.add(Identifier.parse(dataArray[index++]));
            }
            int x = Integer.parseInt(dataArray[index++]);
            int y = Integer.parseInt(dataArray[index++]);
            Point coordinates = new Point(x, y);
            sensors[i] = new BeaconSensor(identifierList, coordinates);
        }

		return new Room(name, rect, sensors);
	}

    /**
     * Set a new BeaconSensor array for this room
     * @param beacons The new BeaconSensors
     */
	public void setBeacons(BeaconSensor[] beacons) {
		beaconSensors = beacons;
	}

    /**
     * @return The Rect of this room
     */
    public Rect getRect() {
        return rect;
    }

    /**
     * Sets the given Rect for this Room
     * @param rect The new Rect that will be set
     */
	public void setRect(Rect rect) {
		this.rect = rect;
	}
}//end Room