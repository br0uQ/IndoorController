package de.jschmucker.indoorcontroller.controller.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.task.Task;
import de.jschmucker.indoorcontroller.model.actions.Action;

public class CreateTaskActivity extends AppCompatActivity implements Observer {
    public static final String RULE_ID = "RULE_ID";
    private EditText name;
    private ImageButton configureOrtsliste;
    private ImageButton addAction;
    private ListView ortsliste;
    private ListView actionsList;

    private Task rule;
    private int ruleId;

    final ArrayList<Location> chosenOrte = new ArrayList<>();
    private ArrayList<Boolean> chosenLocationsActive = new ArrayList<>();
    private ArrayList<Action> chosenActions = new ArrayList<>();
    private TaskActionAdapter taskActionAdapter;
    private TaskLocationAdapter taskLocationAdapter;

    private IndoorServiceProvider indoorServiceProvider;

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
                        Map<Location, Boolean> locationBooleanMap = new HashMap<>();
                        for (int i = 0; i < chosenOrte.size(); i++) {
                            locationBooleanMap.put(chosenOrte.get(i), chosenLocationsActive.get(i));
                        }
                        Task newTask = new Task(indoorServiceProvider.getIndoorService(),
                                name.getText().toString(),
                                locationBooleanMap, chosenActions);
                        indoorServiceProvider.getIndoorService().addTask(newTask);
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

                final ArrayList<Action> actions = new ArrayList<Action>();
                for (Action a : indoorServiceProvider.getIndoorService().getActions()) {
                    if (!chosenActions.contains(a)) {
                        actions.add(a);
                    }
                }

                String[] list = new String[actions.size() + 1];
                for (int i = 0; i < actions.size(); i++) {
                    list[i] = actions.get(i).getName();
                }
                list[actions.size()] = getString(R.string.create_new_action);

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this);
                builder.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= actions.size()) {
                            // create new action
                            Intent intent = new Intent(CreateTaskActivity.this, ChooseActionActivity.class);
                            startActivity(intent);
                        } else {
                            chosenActions.add(actions.get(which));
                            taskActionAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setTitle(getString(R.string.chooseActions));

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        configureOrtsliste = (ImageButton) findViewById(R.id.create_regel_configure_ortsliste);
        configureOrtsliste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Location> orte = indoorServiceProvider.getIndoorService().getLocationManagement().getOrte();
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
                                    for (int i = 0; i < checkedItems.length; i++) {
                                        if (checkedItems[i]) {
                                            if (!chosenOrte.contains(orte.get(i))) {
                                                chosenOrte.add(orte.get(i));
                                                chosenLocationsActive.add(true);
                                            }
                                        } else {
                                            if (chosenOrte.contains(orte.get(i))) {
                                                int index = chosenOrte.indexOf(orte.get(i));
                                                chosenOrte.remove(index);
                                                chosenLocationsActive.remove(index);
                                            }
                                        }
                                    }
                                    taskLocationAdapter.notifyDataSetChanged();
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

        taskActionAdapter = new TaskActionAdapter(this, chosenActions);
        actionsList.setAdapter(taskActionAdapter);
        taskLocationAdapter = new TaskLocationAdapter(this, chosenOrte, chosenLocationsActive);
        ortsliste.setAdapter(taskLocationAdapter);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);
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

        indoorServiceProvider.connectToService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unbind from service
        indoorServiceProvider.disconnectFromService(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        int msg = (int) arg;
        if (msg == IndoorServiceProvider.CONNECTED) {
            IndoorService indoorService = indoorServiceProvider.getIndoorService();

            if (ruleId != -1) {
                rule = indoorService.getRule(ruleId);

                chosenActions.clear();
                ArrayList<Action> actions = rule.getActions();
                for (Action a : actions) {
                    chosenActions.add(a);
                }
                taskActionAdapter.notifyDataSetChanged();

                chosenOrte.clear();
                chosenLocationsActive.clear();
                for (Map.Entry<Location, Boolean> entry : rule.getLocations().entrySet()) {
                    chosenLocationsActive.add(entry.getValue());
                    chosenOrte.add(entry.getKey());
                }
                taskLocationAdapter.notifyDataSetChanged();

                name.setText(rule.getName());
            } else {
                Action action = indoorService.getAction();
                if (action != null) {
                    chosenActions.add(action);
                    taskActionAdapter.notifyDataSetChanged();
                }
            }
        } else if (msg == IndoorServiceProvider.NOT_CONNECTED) {

        }
    }
}
