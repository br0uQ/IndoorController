package de.jschmucker.indoorcontroller.controller.location;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.ort.NFCSpot;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.Raum;
import de.jschmucker.indoorcontroller.model.ort.WifiUmgebung;
import de.jschmucker.indoorcontroller.model.ort.sensor.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.NFCSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.WifiSensor;

public class CreateOrtActivity extends AppCompatActivity {
    private EditText name;
    private Spinner ortsTypeChooser;

    private ArrayAdapter<String> adapter;

    private IndoorService indoorService;
    private boolean bound = false;

    private CreateOrtWifiFragment wifiFragment;
    private CreateOrtRaumFragment raumFragment;
    private CreateOrtNfcFragment nfcFragment;
    private Fragment actualFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ort);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.abort_save_actionbar);

        View v = getSupportActionBar().getCustomView();
        LinearLayout cancel = (LinearLayout) v.findViewById(R.id.action_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout save = (LinearLayout) v.findViewById(R.id.action_done);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ortsName = name.getText().toString();
                if (!name.getText().toString().matches("")) {
                    if (bound) {
                        Ort neuerOrt = null;
                        if (actualFragment == wifiFragment) {
                            ArrayList<WifiSensor> wifis = wifiFragment.getSelectedWifis();
                            if (wifis.size() > 0) {
                                neuerOrt = new WifiUmgebung(ortsName, (WifiSensor[]) wifis.toArray());
                            } else {
                                //ToDo: Fehler keine Wifis ausgewählt
                            }
                        } else if (actualFragment == raumFragment) {
                            BeaconSensor[] beacons = raumFragment.getSelectedBeacons();
                            if ((beacons[0] != null) && (beacons[1] != null) && (beacons[2] != null)
                                    && (beacons[3] != null)) {
                                neuerOrt = new Raum(ortsName, beacons);
                            } else {
                                //ToDo: Fehler nicht vier Beacons ausgewählt
                            }
                        } else if (actualFragment == nfcFragment) {
                            NFCSensor nfcSensor = nfcFragment.getFoundSensor();
                            if (nfcSensor != null) {
                                neuerOrt = new NFCSpot(ortsName, nfcSensor);
                            } else {
                                //ToDo: Fehler keine NFCSpot gefunden
                            }
                        }
                        if (neuerOrt != null) {
                            indoorService.addOrt(neuerOrt);
                            finish();
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrtActivity.this);

                    builder.setMessage(R.string.no_name_error)
                            .setTitle(R.string.error)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        // Bind to Service
        Intent intent = new Intent(this, IndoorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        name = (EditText) findViewById(R.id.textedit_ort_name);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item);

        wifiFragment = new CreateOrtWifiFragment();
        raumFragment = new CreateOrtRaumFragment();
        nfcFragment = new CreateOrtNfcFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!bound) {
            // Bind to Service
            Intent intent = new Intent(this, IndoorService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unbind from service
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    public IndoorService getIndoorService() {
        return indoorService;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IndoorService.IndoorBinder binder = (IndoorService.IndoorBinder) service;
            indoorService = binder.getService();
            bound = true;

            final String[] strings = indoorService.getOrtsManagement().getOrtstypen();

            ortsTypeChooser = (Spinner) findViewById(R.id.spinner_orts_type);
            for (String string : strings) {
                adapter.add(string);
            }
            ortsTypeChooser.setAdapter(adapter);

            ortsTypeChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (strings[position].matches("Raum")) {
                        actualFragment = raumFragment;
                    } else if (strings[position].matches("WifiUmgebung")) {
                        actualFragment = wifiFragment;
                    } else if (strings[position].matches("NFCSpot")) {
                        actualFragment = nfcFragment;
                    }
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.create_ort_fragment_container, actualFragment);
                    transaction.commit();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
