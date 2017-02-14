package de.jschmucker.indoorcontroller.model.actions;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import de.jschmucker.indoorcontroller.controller.IndoorServiceProvider;

/**
 * Created by joshua on 13.02.17.
 */

public class ChooseActionActivity extends AppCompatActivity implements Observer {
    private EditText actionNameEditText;
    private Spinner actionTypeSpinner;
    private ActionFragment fragment;
    private IndoorServiceProvider indoorServiceProvider;

    private int selectedAction = -1;
    private ActionFragment[] actionFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

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
                String actionName = actionNameEditText.getText().toString();
                Log.d(getClass().getSimpleName(), "clicked save");
                if (!actionName.matches("") && indoorServiceProvider.isBound() && (selectedAction != -1)) {
                    // ToDo: check name for double use
                    Action action = fragment.createAction(actionName);
                    indoorServiceProvider.getIndoorService().setAction(action);

                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseActionActivity.this);

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

        actionNameEditText = (EditText) findViewById(R.id.textedit_action_name);
        actionTypeSpinner = (Spinner) findViewById(R.id.spinner_action_type);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        indoorServiceProvider.connectToService(this);
    }

    public void onStop() {
        indoorServiceProvider.disconnectFromService(this);
        super.onStop();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (((int) arg) == IndoorServiceProvider.CONNECTED) {
            // ToDo:
            actionFragments = indoorServiceProvider.getIndoorService().getActionFragments();
            selectedAction = 0;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item);

            for (ActionFragment actionFragment : actionFragments) {
                adapter.add(actionFragment.getActionName(this));
            }

            actionTypeSpinner.setAdapter(adapter);

            actionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fragment = actionFragments[position];

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.choose_action_fragment_container, fragment);
                    transaction.commit();

                    selectedAction = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else if (((int) arg) == IndoorServiceProvider.NOT_CONNECTED) {
            // ToDo
        }
    }
}
