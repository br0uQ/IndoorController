package de.jschmucker.indoorcontroller.controller.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NFCSpot;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NfcDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.Raum;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.RoomDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiUmgebung;

/**
 * Created by jschmucker on 12/12/16.
 */

public class LocationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LocationDetection> detections;

    private static LayoutInflater inflater = null;

    public LocationAdapter(Context context, ArrayList<LocationDetection> detections) {
        this.context = context;
        this.detections = detections;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return detections.size();
    }

    @Override
    public Object getItem(int i) {
        return detections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.location_item, null);
        }
        TextView name = (TextView) v.findViewById(R.id.location_item_name);
        name.setText(detections.get(i).getDetectionName());
        ImageView imageView = (ImageView) v.findViewById(R.id.location_item_icon);
        if (detections.get(i) instanceof RoomDetection) {
            imageView.setImageResource(R.drawable.ic_room_white24dp);
        } else if (detections.get(i) instanceof WifiDetection) {
            imageView.setImageResource(R.drawable.ic_wifis_white24dp);
        } else if (detections.get(i) instanceof NfcDetection) {
            imageView.setImageResource(R.drawable.ic_nfc_spot_white24dp);
        }

        return v;
    }
}
