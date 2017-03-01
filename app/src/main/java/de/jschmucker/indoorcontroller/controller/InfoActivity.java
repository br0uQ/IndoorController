package de.jschmucker.indoorcontroller.controller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import de.jschmucker.indoorcontroller.BuildConfig;
import de.jschmucker.indoorcontroller.R;

/**
 * Activity to show informations about the app.
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle(R.string.about);

        String versionName = BuildConfig.VERSION_NAME;

        TextView version = (TextView) findViewById(R.id.info_textView_version);
        String versionText = "Version: " + versionName;
        version.setText(versionText);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
