package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;


import android.graphics.Point;

import org.altbeacon.beacon.Identifier;

import java.util.List;

import de.jschmucker.indoorcontroller.model.location.sensor.LocationSensor;

/**
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class BeaconSensor extends LocationSensor {

	private final List<Identifier> identifierList;
    private final Point coordinates; // Coordinates of the Beacon in the Rectangle of the room

	public BeaconSensor(List<Identifier> identifierList, Point coordinates) {
        this.identifierList = identifierList;
        this.coordinates = coordinates;
    }

    public String getIdentifierAsString() {
        StringBuilder sb = new StringBuilder();
        int id = 1;
        for (Identifier identifier : identifierList) {
            sb.append("Id");
            sb.append(id++);
            sb.append(": ");
            sb.append(identifier.toHexString());
            sb.append("\n");
        }
        // delete last newline
        sb.deleteCharAt(sb.length() -1);

        return sb.toString();
    }

    public boolean matchesIdentifier(List<Identifier> identifierList) {
        boolean ret = true;
        if (identifierList.size() == this.identifierList.size()) {
            for (int i = 0; i < identifierList.size(); i++) {
                if (!identifierList.get(i).toHexString().matches(this.identifierList.get(i).toHexString())) {
                    ret = false;
                }
            }
        } else {
            ret = false;
        }

        return ret;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public List<Identifier> getIdentifiers() {
        return identifierList;
    }
}//end BeaconSensor