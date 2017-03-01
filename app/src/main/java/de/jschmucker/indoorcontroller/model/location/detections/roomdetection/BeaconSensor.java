package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;


import android.graphics.Point;

import org.altbeacon.beacon.Identifier;

import java.util.List;

/**
 * A BeaconSensor contains the identifier list of a Eddystone Bluetooth Beacon and the coordinates in the Room it belongs to
 * @author jschmucker
 * @version 1.0
 */
public class BeaconSensor {

	private final List<Identifier> identifierList;
    private final Point coordinates; // Coordinates of the Beacon in the Rectangle of the room

    /**
     * Create a new BeaconSensor with the given identifiers and its coordinates in the room it belongs to
     * @param identifierList Identifiers of the Eddystone Bluetooth Beacon it belongs to
     * @param coordinates Position in the room this BeaconSensor belongs to
     */
	public BeaconSensor(List<Identifier> identifierList, Point coordinates) {
        this.identifierList = identifierList;
        this.coordinates = coordinates;
    }

    /**
     * Check whether the identifiers of this BeaconSensor math with the given identifiers
     * @param identifierList Identifiers that shall be compared to the identifiers of this BeaconSensor
     * @return True if the identifier match, false if not
     */
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

    /**
     * @return The Position of this BeaconSensor in the room it belongs to
     */
    public Point getCoordinates() {
        return coordinates;
    }

    /**
     * @return The identifiers of this BeaconSensor
     */
    public List<Identifier> getIdentifiers() {
        return identifierList;
    }
}//end BeaconSensor