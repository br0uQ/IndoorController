package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;

/**
 * Adapter for listing WiFis
 * Created by jschmucker on 11.01.17.
 */
class WifiAdapter extends BaseAdapter {
    private final ArrayList<WifiSensor> wifis;
    private final boolean deletable;

    private static LayoutInflater inflater = null;

    /**
     * Create a new WifiAdapter with the given parameters
     * @param context
     * @param wifis Wifi that will be listed
     * @param deletable Defines whether this adapter will have a delete button for each element
     */
    public WifiAdapter(Context context, ArrayList<WifiSensor> wifis, boolean deletable) {
        this.wifis = wifis;
        this.deletable = deletable;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wifis.size();
    }

    @Override
    public Object getItem(int i) {
        return wifis.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.wifi_item, null);
        }

        TextView ssid = (TextView) v.findViewById(R.id.wifi_item_ssid);
        TextView bssid = (TextView) v.findViewById(R.id.wifi_item_bssid);
        ssid.setText(wifis.get(i).getSsid());
        bssid.setText(wifis.get(i).getBssid());
        final int pos = i;

        if (deletable) {
            ImageButton deleteButton = (ImageButton) v.findViewById(R.id.wifi_item_btn_delete);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setClickable(true);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wifis.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }

        return v;
    }
}