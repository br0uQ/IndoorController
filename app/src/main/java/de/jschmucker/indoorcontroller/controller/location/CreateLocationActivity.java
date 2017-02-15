package de.jschmucker.indoorcontroller.controller.location;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;

public class CreateLocationActivity extends AppCompatActivity
        implements IndoorServiceBound, Observer {
    private EditText name;
    private Spinner ortsTypeChooser;

    private ArrayAdapter<String> adapter;

    private Fragment actualFragment;
    private LocationDetection[] detections;
    private int selected = -1;

    private IndoorServiceProvider indoorServiceProvider;

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
                Log.d("CreateOrt", "clicked save");
                if (!ortsName.matches("") && indoorServiceProvider.isBound() && (selected != -1)) {
                    // ToDo: check name for double use
                    indoorServiceProvider.getIndoorService().addOrt(detections[selected].createLocation(ortsName));
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateLocationActivity.this);

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

        name = (EditText) findViewById(R.id.textedit_ort_name);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        indoorServiceProvider.connectToService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unbind from service
        indoorServiceProvider.disconnectFromService(this);
    }

    public IndoorService getIndoorService() {
        return indoorServiceProvider.getIndoorService();
    }

    @Override
    public void update(Observable o, Object arg) {
        int type = (int) arg;
        if (type == IndoorServiceProvider.CONNECTED) {
            IndoorService indoorService = indoorServiceProvider.getIndoorService();

            selected = 0;

            detections = indoorService.getLocationManagement().getLocationDetections();

            ortsTypeChooser = (Spinner) findViewById(R.id.spinner_orts_type);
            for (LocationDetection detection : detections) {
                adapter.add(detection.getDetectionName());
            }
            ortsTypeChooser.setAdapter(adapter);

            ortsTypeChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    actualFragment = detections[position].getFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.create_ort_fragment_container, actualFragment);
                    transaction.commit();
                    selected = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (type == IndoorServiceProvider.NOT_CONNECTED) {

        }
    }
}
