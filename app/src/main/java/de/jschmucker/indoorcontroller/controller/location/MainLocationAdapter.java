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
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * Created by jschmucker on 12/12/16.
 * Adapter to list Locations in the MainActivity
 */

class MainLocationAdapter extends BaseAdapter {
    private final ArrayList<Location> locations;

    private static LayoutInflater inflater = null;

    /**
     * Create a new MainLocationAdapter in the given context and with the given locations to be shown.
     * @param context
     * @param locations Locations to be shown by this adapter
     */
    public MainLocationAdapter(Context context, ArrayList<Location> locations) {
        this.locations = locations;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int i) {
        return locations.get(i);
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
        name.setText(locations.get(i).getName());
        ImageView imageView = (ImageView) v.findViewById(R.id.location_item_icon);
        imageView.setImageResource(locations.get(i).getImageResourceId());

        return v;
    }
}
