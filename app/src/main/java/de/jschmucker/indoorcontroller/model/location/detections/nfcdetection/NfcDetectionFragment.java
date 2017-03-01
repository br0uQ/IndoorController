package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.controller.IndoorServiceBound;
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

    /**
     * Starts the scan for a NFC tag.
     */
    private void startNfcScan() {
        IndoorServiceBound activity = (IndoorServiceBound) getActivity();
        NfcDetection detection = (NfcDetection) activity
                .getIndoorService().getLocationDetection(new NfcSpot("", null));
        detection.setNfcDetectToFragment(true);

        setupForegroundDispatch(getActivity(), nfcAdapter);

        setGuiHasSensor(false);
    }

    /**
     * Is called if a NFC tag is found. It writes a ndef record to that tag.
     * This ndef record is needed for the NfcDetectedActivity to be started if android detects such an NFC tag.
     * @param nfcSensor Sensor that is found
     * @param tag NFC tag that is found
     */
    public void setNfcSensor(NfcSensor nfcSensor, Tag tag) {
        foundSensor = nfcSensor;

        NdefRecord record = createTextRecord("IndoorController", getCurrentLocale(), true);

        try {
            write(record, tag);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        } catch (FormatException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }

        setGuiHasSensor(true);

        //stopForegroundDispatch(getActivity(), nfcAdapter);
    }

    /**
     * This function is called to changed the GUI of the fragment depending on whether a scan was started or a NFC tag was found
     * @param b True if sensor was found, false if scan was started
     */
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

    /**
     * Called to set the values of a already created location in the fragment
     * @param location Location that has to be changed
     */
    public void saveLocationValues(NfcSpot location) {
        location.setNfcSensor(foundSensor);
    }

    /**
     * Activate the NFC tag scan for this fragment
     * @param activity Activity where this fragment is attached to
     * @param adapter NfcAdapter for the NFC tag scan
     */
    private static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        adapter.enableForegroundDispatch(activity, pendingIntent, null, null);
    }

    /**
     * stop NFC scan
     * @param activity
     * @param adapter
     */
    private static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * Creates a new NfcSpot with the given name and the detected NFC tag
     * @param name Name of the new NfcSpot
     * @return New created NfcSpot
     */
    public Location createLocation(String name) {
        return new NfcSpot(name, foundSensor);
    }

    /**
     * Set the values of the given NfcSensor in the fragment
     * @param nfcSensor NfcSensor that contains the values to be set in this fragment
     */
    public void setNfcValue(NfcSensor nfcSensor) {
        foundSensor = nfcSensor;
    }

    /**
     * Creates a new ndef record that will be stored on a connected NFC tag.
     * @param payload String that should be saved as ndef message
     * @param locale Locale for the record
     * @param encodeInUtf8 whether Utf8 should be used
     * @return A ndef record with the given parameters
     */
    private NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
    }

    /**
     * @return the Locale of device this app is running on
     */
    @TargetApi(Build.VERSION_CODES.N)
    private Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    /**
     * Writes a ndef record on the given tag
     * @param record Record that will be written on the tag
     * @param tag Tag where the record shall be written on
     * @throws IOException
     * @throws FormatException
     */
    private void write(NdefRecord record, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { record };
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();

        if (ndef.isWritable()) {
            NdefMessage ndefMessage = ndef.getNdefMessage();
            if (ndefMessage == null) {
                ndef.writeNdefMessage(message);
            } else if (!Arrays.equals(ndefMessage.getRecords()[0].getPayload(),
                    record.getPayload())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.already_message_on_nfc_message));
                builder.setTitle(getString(R.string.already_message_on_nfc_title));
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ndef.writeNdefMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (FormatException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        ndef.close();
    }
}
