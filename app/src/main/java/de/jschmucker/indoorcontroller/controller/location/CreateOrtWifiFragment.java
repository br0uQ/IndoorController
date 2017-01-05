package de.jschmucker.indoorcontroller.controller.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.sensor.WifiSensor;


public class CreateOrtWifiFragment extends Fragment {
    private ImageButton plusButton;
    private ListView listView;
    final ArrayList<WifiSensor> selectedWifis = new ArrayList<WifiSensor>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_ort_wifi, container, false);

        final Activity activity = getActivity();

        listView = (ListView) view.findViewById(R.id.create_ort_wifiliste);

        plusButton = (ImageButton) view.findViewById(R.id.create_ort_add_wifiliste);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IndoorServiceProvider createOrtActivity = (IndoorServiceProvider) activity;
                final ArrayList<WifiSensor> wifis = createOrtActivity.getIndoorService().getWifiEnvironment();


                String[] liste = new String[wifis.size()];
                for (int i = 0; i < wifis.size(); i++) {
                    liste[i] = wifis.get(i).getSsid();
                }

                final boolean[] checkedItems = new boolean[liste.length];
                for (boolean b : checkedItems) {
                    b = false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(getString(R.string.chooseWifis))
                        .setMultiChoiceItems(liste, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkedItems[which] = isChecked;
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < checkedItems.length; i++) {
                                    if (checkedItems[i]) {
                                        selectedWifis.add(wifis.get(i));
                                    }
                                }
                                ArrayList<String> strings = new ArrayList<String>();
                                for (WifiSensor wifi : selectedWifis) {
                                    strings.add(wifi.toString());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                                        android.R.layout.simple_list_item_1, strings);
                                listView.setAdapter(adapter);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public ArrayList<WifiSensor> getSelectedWifis() {
        return selectedWifis;
    }

    public void setWifiList(WifiSensor[] wifis) {
        for (WifiSensor wifi : wifis) {
            selectedWifis.add(wifi);
        }
    }
}
