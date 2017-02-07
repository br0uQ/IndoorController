package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

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
 * Created by joshua on 11.01.17.
 */

public class WifiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WifiSensor> wifis;
    private boolean deletable;

    private static LayoutInflater inflater = null;

    public WifiAdapter(Context context, ArrayList<WifiSensor> wifis, boolean deletable) {
        this.context = context;
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