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
public class MainTasksFragment extends Fragment {

    public MainTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_rule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        MainTasksAdapter adapter = new MainTasksAdapter(getActivity(), activity.getIndoorService().getTasks());
        ListView listView = (ListView) view.findViewById(R.id.rules_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
                intent.putExtra(CreateTaskActivity.TASK_ID, position);
                startActivity(intent);
            }
        });

        return view;
    }

    private void openAddDialog() {
        Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
        startActivity(intent);
    }

}