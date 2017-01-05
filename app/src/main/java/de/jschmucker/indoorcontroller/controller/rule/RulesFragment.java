package de.jschmucker.indoorcontroller.controller.rule;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.jschmucker.indoorcontroller.MainActivity;
import de.jschmucker.indoorcontroller.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RulesFragment extends Fragment {


    public RulesFragment() {
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
        RulesAdapter adapter = new RulesAdapter(getActivity(), activity.getIndoorService().getRegelManagement().getRegeln());
        ListView listView = (ListView) view.findViewById(R.id.rules_list);
        listView.setAdapter(adapter);

        return view;
    }

    private void openAddDialog(View view) {
        Intent intent = new Intent(getActivity(), CreateRegelActivity.class);
        startActivity(intent);
    }

}
