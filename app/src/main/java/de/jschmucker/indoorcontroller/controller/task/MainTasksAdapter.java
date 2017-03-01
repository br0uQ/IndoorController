package de.jschmucker.indoorcontroller.controller.task;

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
import de.jschmucker.indoorcontroller.model.task.Task;

/**
 * Adapter for the Task Fragment in the MainActivity listing all Tasks that are saved in the IndoorController and whether they are activated.
 * Created by jschmucker on 04.01.17.
 */
class MainTasksAdapter extends BaseAdapter {
    private final ArrayList<Task> tasks;

    private static LayoutInflater inflater = null;

    /**
     * Create a new MainTaskAdapter in the given context and with the given tasks to be shown.
     * @param context
     * @param tasks Tasks to be shown by this adapter
     */
    public MainTasksAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.task_item, null);
        }

        TextView name = (TextView) view.findViewById(R.id.rules_item_name);
        name.setText(tasks.get(position).getName());

        Switch onOff = (Switch) view.findViewById(R.id.rules_item_on_off_switch);
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tasks.get(position).setEnabled(isChecked);
            }
        });
        onOff.setChecked(tasks.get(position).isEnabled());

        return view;
    }
}
