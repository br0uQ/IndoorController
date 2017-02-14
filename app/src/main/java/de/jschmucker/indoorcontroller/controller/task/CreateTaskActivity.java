package de.jschmucker.indoorcontroller.controller.task;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.actions.ChooseActionActivity;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.task.Task;
import de.jschmucker.indoorcontroller.model.actions.Action;

public class CreateTaskActivity extends AppCompatActivity {
    public static final String RULE_ID = "RULE_ID";
    private EditText name;
    private ImageButton configureOrtsliste;
    private ImageButton addAction;
    private ListView ortsliste;
    private ListView actionsList;
    private boolean chooserReady = false;

    private Task rule;
    private int ruleId;

    private ArrayList<Action> actions;

    final ArrayList<Location> chosenOrte = new ArrayList<Location>();
    final ArrayList<Action> chosenActions = new ArrayList<Action>();

    private IndoorService indoorService;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_regel);

        Intent intent = getIntent();
        ruleId = intent.getIntExtra(RULE_ID, -1);

        if (ruleId == -1) {
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
                    if (!name.getText().toString().matches("")) {
                        Map<Location, Boolean> locationBooleanMap = new HashMap<Location, Boolean>();
                        for (Location location : chosenOrte) {
                            locationBooleanMap.put(location, true);
                        }
                        Task newTask = new Task(indoorService, name.getText().toString(),
                                locationBooleanMap, actions);
                        indoorService.addTask(newTask);
                        finish();
                    }
                }
            });
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        name = (EditText) findViewById(R.id.textedit_regel_name);

        ortsliste = (ListView) findViewById(R.id.create_regel_ortsliste);
        actionsList = (ListView) findViewById(R.id.create_regel_actionliste);

        addAction = (ImageButton) findViewById(R.id.create_regel_add_action);
        addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start Action creation
                Intent intent = new Intent(CreateTaskActivity.this, ChooseActionActivity.class);
                startActivity(intent);
            }
        });

        configureOrtsliste = (ImageButton) findViewById(R.id.create_regel_configure_ortsliste);
        configureOrtsliste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Location> orte = indoorService.getLocationManagement().getOrte();
                if (orte.size() > 0) {
                    String[] liste = new String[orte.size()];
                    for (int i = 0; i < orte.size(); i++) {
                        liste[i] = orte.get(i).getName();
                    }

                    final boolean[] checkedItems = new boolean[liste.length];
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (chosenOrte.contains(orte.get(i))) {
                            checkedItems[i] = true;
                        } else checkedItems[i] = false;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this);
                    builder.setTitle(getString(R.string.chooseLocations))
                            .setMultiChoiceItems(liste, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    checkedItems[which] = isChecked;
                                }
                            })
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    chosenOrte.clear();
                                    for (int i = 0; i < checkedItems.length; i++) {
                                        if (checkedItems[i]) {
                                            chosenOrte.add(orte.get(i));
                                        }
                                    }
                                    ArrayList<String> strings = new ArrayList<String>();
                                    for (Location location : chosenOrte) {
                                        strings.add(location.getName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateTaskActivity.this,
                                            android.R.layout.simple_list_item_1, strings);
                                    ortsliste.setAdapter(adapter);
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.chooseLocations) + ": " +
                            getString(R.string.noelements), Toast.LENGTH_SHORT).show();
                }

            }
        });

        actions = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (ruleId == -1) {
            return super.onCreateOptionsMenu(menu);
        }
        getMenuInflater().inflate(R.menu.change_ort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_ort_menu_save) {
            //ToDo save changes and exit activity
            return true;
        }
        if (id == R.id.change_ort_menu_delete) {
            //ToDo delete rule and exit activity
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private boolean[] openChooser(String titel, String[] liste) {
        if (liste != null) {
            final boolean[] checkedItems = new boolean[liste.length];
            for (boolean b : checkedItems) {
                b = false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(titel)
                    .setMultiChoiceItems(liste, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checkedItems[which] = isChecked;
                            Log.d(getClass().getSimpleName(), "choose Elements: Chose Element " + which);
                            Toast.makeText(getApplicationContext(), "Chose Element " + which, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chooserReady = true;
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            return checkedItems;
        } else {
            Toast.makeText(this, titel + ": " +
                    getString(R.string.noelements), Toast.LENGTH_SHORT).show();
            return null;
        }


    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IndoorService.IndoorBinder binder = (IndoorService.IndoorBinder) service;
            indoorService = binder.getService();
            bound = true;

            if (ruleId != -1) {
                rule = indoorService.getRule(ruleId);

                ArrayList<String> stringsActions = new ArrayList<String>();
                for (Action action : rule.getActions()) {
                    stringsActions.add(action.toString());
                }

                ArrayAdapter<String> adapterActions = new ArrayAdapter<>(CreateTaskActivity.this,
                        android.R.layout.simple_list_item_1, stringsActions);
                actionsList.setAdapter(adapterActions);

                name.setText(rule.getName());

                /*ArrayList<String> stringsLocations = new ArrayList<String>();
                for (Location location : rule.getLocations()) {
                    stringsLocations.add(location.getName());
                }

                ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(CreateTaskActivity.this,
                        android.R.layout.simple_list_item_1, stringsLocations);
                ortsliste.setAdapter(adapterLocations);*/
            } else {
                Action action = indoorService.getAction();
                if (action != null) {
                    actions.add(action);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateTaskActivity.this,
                            android.R.layout.simple_list_item_1, new String[] {action.getName()});
                    actionsList.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
