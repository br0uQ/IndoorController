package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.LocationDetectionFragment;
import de.jschmucker.indoorcontroller.model.location.Location;


public class WifiDetectionFragment extends LocationDetectionFragment {
    private ImageButton plusButton;
    private ListView listView;
    final ArrayList<WifiSensor> selectedWifis = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();

    private WifiManager wifi;
    private ArrayList<WifiSensor> wifis = new ArrayList<>();
    private WifiAdapter adapter;
    private WifiAdapter selectedAdapter;

    private BroadcastReceiver receiver;

    Comparator<WifiSensor> wifiComparator = new Comparator<WifiSensor>() {
        @Override
        public int compare(WifiSensor o1, WifiSensor o2) {
            return o1.getSsid().compareToIgnoreCase(o2.getSsid());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_ort_wifi, container, false);

        listView = (ListView) view.findViewById(R.id.create_ort_wifiliste);

        plusButton = (ImageButton) view.findViewById(R.id.create_ort_add_wifiliste);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.startScan();
                openDialog();
                Log.d(TAG, "startScan");
            }
        });
        wifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        wifis.add(new WifiSensor("Scanning", "..."));

        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                Log.d(TAG, "onReceive");
                List<ScanResult> results;
                results = wifi.getScanResults();
                wifis.clear();
                for (ScanResult result : results) {
                    wifis.add(new WifiSensor(result.SSID, result.BSSID));
                    Log.d(TAG, "added Wifi: BSSID=" + result.BSSID + " SSID=" + result.SSID);
                }
                Collections.sort(wifis, wifiComparator);
                adapter.notifyDataSetChanged();

            }
        };
        getActivity().registerReceiver(receiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0x12345);
        }

        adapter = new WifiAdapter(getActivity(), wifis, false);
        selectedAdapter = new WifiAdapter(getActivity(), selectedWifis, true);
        listView.setAdapter(selectedAdapter);

        return view;
    }

    public ArrayList<WifiSensor> getSelectedWifis() {
        return selectedWifis;
    }

    public void setWifiList(ArrayList<WifiSensor> wifis) {
        for (WifiSensor wifi : wifis) {
            selectedWifis.add(wifi);
        }
    }

    private void openDialog() {
        final ArrayList<WifiSensor> select = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedWifis.add((WifiSensor) adapter.getItem(which));
                selectedAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(getString(R.string.scanning_for_wifis))
        .show();
    }

    public Location createWifiUmgebung(String name) {
        WifiEnvironment res = new WifiEnvironment(name, selectedWifis);
        return res;
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(receiver);
        super.onStop();
    }
}
