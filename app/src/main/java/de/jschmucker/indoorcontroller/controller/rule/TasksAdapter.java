package de.jschmucker.indoorcontroller.controller.rule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.regel.Task;

/**
 * Created by joshua on 04.01.17.
 */

public class TasksAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Task> regeln;

    private static LayoutInflater inflater = null;

    public TasksAdapter(Context context, ArrayList<Task> regeln) {
        this.context = context;
        this.regeln = regeln;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return regeln.size();
    }

    @Override
    public Object getItem(int position) {
        return regeln.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.rules_item, null);
        }

        TextView name = (TextView) view.findViewById(R.id.rules_item_name);
        name.setText(regeln.get(position).getName());

        Switch onOff = (Switch) view.findViewById(R.id.rules_item_on_off_switch);
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                regeln.get(position).setEnabled(isChecked);
            }
        });
        onOff.setChecked(regeln.get(position).isEnabled());

        return view;
    }
}
