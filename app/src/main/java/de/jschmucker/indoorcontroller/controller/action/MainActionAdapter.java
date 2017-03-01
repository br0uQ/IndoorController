package de.jschmucker.indoorcontroller.controller.action;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.actions.Action;

/**
 * Created by jschmucker on 22.02.17.
 * Adapter to list Actions in the MainActivity
 */

class MainActionAdapter extends BaseAdapter {
    private final ArrayList<Action> actions;

    private static LayoutInflater inflater = null;

    /**
     * Create a new MainActionAdapter in the given context and with the given actions to be shown.
     * @param context
     * @param actions Actions to be shown by this adapter
     */
    public MainActionAdapter(Context context, ArrayList<Action> actions) {
        this.actions = actions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int i) {
        return actions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.action_item, null);
        }

        TextView actionItemName = (TextView) v.findViewById(R.id.action_item_name);
        actionItemName.setText(actions.get(i).getName());
        final int pos = i;

        ImageButton deleteButton = (ImageButton) v.findViewById(R.id.action_item_btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.remove(pos);
                notifyDataSetChanged();
            }
        });

        return v;
    }
}