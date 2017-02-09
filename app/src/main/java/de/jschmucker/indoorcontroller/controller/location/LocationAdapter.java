package de.jschmucker.indoorcontroller.controller.location;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NFCSpot;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.Raum;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiUmgebung;

/**
 * Created by jschmucker on 12/12/16.
 */

public class LocationAdapter extends BaseAdapter implements Observer {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ArrayList<Ort> orte;

    private static LayoutInflater inflater = null;

    public LocationAdapter(Context context, ArrayList<Ort> orte) {
        this.context = context;
        this.orte = orte;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Ort ort : orte) {
            ort.addObserver(this);
        }
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
        if (orte.get(i) instanceof Raum) {
            imageView.setImageResource(R.drawable.ic_room_white24dp);
        } else if (orte.get(i) instanceof WifiUmgebung) {
            imageView.setImageResource(R.drawable.ic_wifis_white24dp);
        } else if (orte.get(i) instanceof NFCSpot) {
            imageView.setImageResource(R.drawable.ic_nfc_spot_white24dp);
        }

        return v;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "update");
        Toast.makeText(context,
                "Ort " + ((Ort) o).getName() + " hat sich geändert.",
                Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }
}
