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
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
class Room extends Location {

    private Rect rect;  // Rectangle that defines the room
	private BeaconSensor[] beaconSensors;

	public Room(String name, Rect rect, BeaconSensor[] sensors){
		this.name = name;
        this.rect = rect;
		beaconSensors = sensors;
		imageResourceId = R.drawable.ic_room_white24dp;
	}

	public BeaconSensor[] getBeacons() {
		return beaconSensors;
	}

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

	public void setBeacons(BeaconSensor[] beacons) {
		beaconSensors = beacons;
	}

    public Rect getRect() {
        return rect;
    }

	public void setRect(Rect rect) {
		this.rect = rect;
	}
}//end Room