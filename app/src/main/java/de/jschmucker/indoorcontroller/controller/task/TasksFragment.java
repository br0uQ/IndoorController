package de.jschmucker.indoorcontroller.controller.task;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.jschmucker.indoorcontroller.MainActivity;
import de.jschmucker.indoorcontroller.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rules, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_rule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog(view);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        TasksAdapter adapter = new TasksAdapter(getActivity(), activity.getIndoorService().getTaskManagement().getRegeln());
        ListView listView = (ListView) view.findViewById(R.id.rules_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
                intent.putExtra(CreateTaskActivity.RULE_ID, position);
                startActivity(intent);
            }
        });

        return view;
    }

    private void openAddDialog(View view) {
        Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
        startActivity(intent);
    }

}
