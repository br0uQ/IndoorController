package de.jschmucker.indoorcontroller.controller.location;


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
public class LocationFragment extends Fragment {

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_location);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LocationAdapter adapter = new LocationAdapter(getActivity(), activity.getIndoorService().getOrtsManagement().getOrte());
        ListView listView = (ListView) view.findViewById(R.id.location_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChangeOrtActivity.class);
                intent.putExtra(ChangeOrtActivity.LOCATION_ID, position);
                startActivity(intent);
            }
        });
        return view;
    }

    public void openAddDialog() {
        Intent intent = new Intent(getActivity(), CreateOrtActivity.class);
        startActivity(intent);
    }

}
