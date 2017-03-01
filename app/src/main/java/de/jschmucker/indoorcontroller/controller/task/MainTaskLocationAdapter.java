package de.jschmucker.indoorcontroller.controller.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * Location Adapter for the Task Fragment in the MainActivity
 * Created by jschmucker on 15.02.17.
 */
class MainTaskLocationAdapter extends BaseAdapter {
    private final ArrayList<Location> locations;
    private final ArrayList<Boolean> active;

    private static LayoutInflater inflater = null;

    /**
     * Create a new MainTaskLocationAdapter in the given context and with the given locations and whether they should be active or inactive for the task.
     * @param context
     * @param locations Locations to be shown by this adapter
     * @param active Whether the shown location should be active or inactive
     */
    public MainTaskLocationAdapter(Context context, ArrayList<Location> locations, ArrayList<Boolean> active) {
        this.locations = locations;
        this.active = active;

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
            v = inflater.inflate(R.layout.task_location_item, null);
        }

        TextView actionItemName = (TextView) v.findViewById(R.id.task_location_item_name);
        actionItemName.setText(locations.get(i).getName());
        final int pos = i;

        ImageView iconView = (ImageView) v.findViewById(R.id.task_location_item_iconview);
        iconView.setImageResource(locations.get(i).getImageResourceId());

        CheckBox checkBox = (CheckBox) v.findViewById(R.id.task_location_item_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                active.set(pos, isChecked);
            }
        });
        checkBox.setChecked(active.get(i));

        return v;
    }
}
