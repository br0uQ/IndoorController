package de.jschmucker.indoorcontroller.model.ort.detections.roomdetection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.controller.location.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.ort.LocationDetectionFragment;

public class CreateOrtRaumFragment extends LocationDetectionFragment implements AdapterView.OnItemSelectedListener {
    Spinner leftTop;
    Spinner rightTop;
    Spinner leftBottom;
    Spinner rightBottom;

    private BeaconSensor[] selectedBeacons = new BeaconSensor[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_ort_raum, container, false);

        IndoorServiceProvider activity = (IndoorServiceProvider) getActivity();
        IndoorService service = activity.getIndoorService();

        leftTop = (Spinner) view.findViewById(R.id.create_ort_raum_spinner_top_left);
        rightTop = (Spinner) view.findViewById(R.id.create_ort_raum_spinner_top_right);
        leftBottom = (Spinner) view.findViewById(R.id.create_ort_raum_spinner_bottom_left);
        rightBottom = (Spinner) view.findViewById(R.id.create_ort_raum_spinner_bottom_right);

        // Set Beacon Adapter to the Spinner
        BeaconAdapter adapter = new BeaconAdapter(getActivity(), service.getBeacons());
        service.startBeaconScan();

        leftTop.setAdapter(adapter);
        rightTop.setAdapter(adapter);
        leftBottom.setAdapter(adapter);
        rightBottom.setAdapter(adapter);

        // Set OnItemClickListener
        leftTop.setOnItemSelectedListener(this);
        rightTop.setOnItemSelectedListener(this);
        leftBottom.setOnItemSelectedListener(this);
        rightBottom.setOnItemSelectedListener(this);

        return inflater.inflate(R.layout.fragment_create_ort_raum, container, false);
    }

    public BeaconSensor[] getSelectedBeacons() {
        return selectedBeacons;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BeaconSensor selectedSensor;
        BeaconAdapter adapter = (BeaconAdapter) parent.getAdapter();
        selectedSensor = (BeaconSensor) adapter.getItem(position);
        if (view == leftBottom) {
            selectedBeacons[BeaconSensor.BOTTOM_LEFT] = selectedSensor;
        } else if (view == rightBottom) {
            selectedBeacons[BeaconSensor.BOTTOM_RIGHT] = selectedSensor;
        } else if (view == leftTop) {
            selectedBeacons[BeaconSensor.TOP_LEFT] = selectedSensor;
        } else if (view == rightTop) {
            selectedBeacons[BeaconSensor.TOP_RIGHT] = selectedSensor;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setBeacons(BeaconSensor[] beacons) {
        selectedBeacons = beacons;
        //ToDo set Beacons in Spinner
    }

    public class BeaconAdapter extends BaseAdapter {
        private ArrayList<BeaconSensor> beacons;

        private LayoutInflater inflater = null;

        public BeaconAdapter(Context context, ArrayList<BeaconSensor> beacons) {
            this.beacons = beacons;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return beacons.size();
        }

        @Override
        public Object getItem(int i) {
            return beacons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if (v == null) {
                v = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }
            TextView name = (TextView) v.findViewById(android.R.id.text1);
            name.setText(beacons.get(i).toString());
            return v;
        }
    }
}
