package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.controller.location.CreateLocationActivity;
import de.jschmucker.indoorcontroller.controller.location.IndoorServiceBound;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetectionFragment;

public class NfcDetectionFragment extends LocationDetectionFragment {
    private NfcSensor foundSensor = null;
    private Button refreshButton;
    private TextView textView;
    private ProgressBar progressBar;

    private NfcAdapter nfcAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detection_nfc, container, false);
        refreshButton = (Button) view.findViewById(R.id.create_ort_nfc_refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNfcScan();
            }
        });
        textView = (TextView) view.findViewById(R.id.create_ort_nfc_fragment_textview);
        progressBar = (ProgressBar) view.findViewById(R.id.create_ort_nfc_progressbar);

        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (foundSensor != null) {
            setGuiHasSensor(true);
        } else {
            IndoorServiceBound activity = (IndoorServiceBound) getActivity();
            NfcDetection detection = (NfcDetection) activity
                    .getIndoorService().getLocationDetection(new NfcSpot("", null));
            detection.setNfcDetectToFragment(true);

            setupForegroundDispatch(getActivity(), nfcAdapter);
        }

        return view;
    }

    private void startNfcScan() {
        IndoorServiceBound activity = (IndoorServiceBound) getActivity();
        NfcDetection detection = (NfcDetection) activity
                .getIndoorService().getLocationDetection(new NfcSpot("", null));
        detection.setNfcDetectToFragment(true);

        setupForegroundDispatch(getActivity(), nfcAdapter);

        setGuiHasSensor(false);
    }

    public void setNfcSensor(NfcSensor nfcSensor) {
        foundSensor = nfcSensor;

        setGuiHasSensor(true);

        //stopForegroundDispatch(getActivity(), nfcAdapter);
    }

    private void setGuiHasSensor(boolean b) {
        refreshButton.setEnabled(b);
        if (b) {
            refreshButton.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.found_nfc_sensor) + " " + foundSensor.getSerialNumber());
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            refreshButton.setVisibility(View.INVISIBLE);
            textView.setText(getString(R.string.searchingNfcTag));
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void saveLocationValues(NfcSpot location) {
        location.setNfcSensor(foundSensor);
    }

    private static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        adapter.enableForegroundDispatch(activity, pendingIntent, null, null);
    }

    private static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    public Location createLocation(String name) {
        return new NfcSpot(name, foundSensor);
    }

    public void setNfcValue(NfcSensor nfcSensor) {
        foundSensor = nfcSensor;
    }
}
