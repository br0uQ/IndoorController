package de.jschmucker.indoorcontroller.controller.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * Created by joshua on 15.02.17.
 */

public class TaskLocationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Location> locations;
    private ArrayList<Boolean> active;

    private static LayoutInflater inflater = null;

    public TaskLocationAdapter(Context context, ArrayList<Location> locations, ArrayList<Boolean> active) {
        this.context = context;
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

        //ToDo location images
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
