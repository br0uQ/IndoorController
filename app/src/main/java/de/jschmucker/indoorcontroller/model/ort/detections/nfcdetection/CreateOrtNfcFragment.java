package de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.controller.location.CreateOrtActivity;
import de.jschmucker.indoorcontroller.controller.location.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.ort.LocationDetectionFragment;
import de.jschmucker.indoorcontroller.model.ort.sensor.NFCSensor;

public class CreateOrtNfcFragment extends LocationDetectionFragment implements Observer {
    private NFCSensor foundSensor = null;
    private Button refreshButton;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_ort_nfc, container, false);
        refreshButton = (Button) view.findViewById(R.id.create_ort_nfc_refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNfcScan();
            }
        });
        textView = (TextView) view.findViewById(R.id.create_ort_nfc_fragment_textview);
        progressBar = (ProgressBar) view.findViewById(R.id.create_ort_nfc_progressbar);

        if (foundSensor != null) {
            setGuiHasSensor(true);
        }

        IndoorServiceProvider activity = (IndoorServiceProvider) getActivity();
        activity.getIndoorService().startSingleNfcScan(this);

        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        IndoorServiceProvider activity = (IndoorServiceProvider) getActivity();
        foundSensor = activity.getIndoorService().getFoundNfcSensor();
        refreshButton.setEnabled(true);
        refreshButton.setVisibility(View.VISIBLE);
        textView.setText(foundSensor.toString());
        progressBar.setEnabled(false);
    }

    private void startNfcScan() {
        textView.setText(getString(R.string.searchingNfcTag));
        refreshButton.setEnabled(false);
        refreshButton.setVisibility(View.VISIBLE);
        progressBar.setEnabled(true);

        CreateOrtActivity activity = (CreateOrtActivity) getActivity();
        activity.getIndoorService().startSingleNfcScan(this);
    }

    public NFCSensor getFoundSensor() {
        return foundSensor;
    }

    public void setNfcSensor(NFCSensor nfcSensor) {
        foundSensor = nfcSensor;
    }

    private void setGuiHasSensor(boolean b) {
        refreshButton.setEnabled(b);
        refreshButton.setVisibility(View.VISIBLE);
        textView.setText(foundSensor.toString());
        progressBar.setEnabled(!b);
    }
}
