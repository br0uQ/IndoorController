package de.jschmucker.indoorcontroller.controller.action;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.MainActivity;
import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.actions.Action;

public class MainActionFragment extends Fragment {

    public MainActionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_action, container, false);
        MainActivity activity = (MainActivity) getActivity();
        IndoorService indoorService = activity.getIndoorService();

        ArrayList<Action> actions = indoorService.getActions();

        MainActionAdapter adapter = new MainActionAdapter(getActivity(), actions);
        ListView listView = (ListView) view.findViewById(R.id.action_list);
        listView.setAdapter(adapter);

        return view;
    }
}
