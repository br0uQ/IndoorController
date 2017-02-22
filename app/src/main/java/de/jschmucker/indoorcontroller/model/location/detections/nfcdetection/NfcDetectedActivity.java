package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.IndoorServiceProvider;

public class NfcDetectedActivity extends AppCompatActivity implements Observer {
    private IndoorServiceProvider indoorServiceProvider;
    private NfcSensor foundSensor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_detected);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);

        handleNfcIntent(getIntent());
    }

    @Override
    public void onStart() {
        super.onStart();
        indoorServiceProvider.connectToService(this);
    }

    @Override
    public void onStop() {
        indoorServiceProvider.disconnectFromService(this);
        super.onStop();
    }

    private void handleNfcIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "handleNewIntent");
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            byte[] extraID = tagFromIntent.getId();

            StringBuilder sb = new StringBuilder();
            for (byte b : extraID) {
                sb.append(String.format("%02X", b));
                sb.append(":");
            };

            sb.deleteCharAt(sb.length() -1);

            String nfcTagSerialNum = sb.toString();

            foundSensor = new NfcSensor(nfcTagSerialNum);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        int msg = (int) arg;
        if (msg == IndoorServiceProvider.CONNECTED) {
            Log.d(getClass().getSimpleName(), "connected to IndoorController");
            if (foundSensor != null) {
                IndoorService indoorService = indoorServiceProvider.getIndoorService();
                NfcDetection nfcDetection = (NfcDetection) indoorService.getLocationDetection(new NfcSpot("", null));

                nfcDetection.detectedNfcSensor(foundSensor);
            }
            finish();
        }
    }
}
