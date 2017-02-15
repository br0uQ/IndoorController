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
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.location.detections.nfcdetection.NfcSpot;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.detections.roomdetection.Room;
import de.jschmucker.indoorcontroller.model.location.detections.wifidetection.WifiEnvironment;

/**
 * Created by jschmucker on 12/12/16.
 */

public class LocationAdapter extends BaseAdapter {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ArrayList<Location> orte;
    private ArrayList<Integer> imgRes;

    private static LayoutInflater inflater = null;

    public LocationAdapter(Context context, ArrayList<Location> orte, ArrayList<Integer> imgRes) {
        this.context = context;
        this.orte = orte;
        this.imgRes = imgRes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orte.size();
    }

    @Override
    public Object getItem(int i) {
        return orte.get(i);
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
        name.setText(orte.get(i).getName());
        ImageView imageView = (ImageView) v.findViewById(R.id.location_item_icon);
        imageView.setImageResource(imgRes.get(i));

        return v;
    }
}
