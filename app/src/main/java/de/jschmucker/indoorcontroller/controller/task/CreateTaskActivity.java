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
import de.jschmucker.indoorcontroller.controller.IndoorServiceBound;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.task.Task;
import de.jschmucker.indoorcontroller.model.actions.Action;

/**
 * With this Activity a new Task can be created.
 * The Task contains to parts:
 * 1. A list of locations and whether they should be active or inactive
 * 2. A list of action to be activated when all locations are active/inactive (depending on what the user chose)
 */
public class CreateTaskActivity extends AppCompatActivity implements Observer, IndoorServiceBound {
    public static final String TASK_ID = "TASK_ID";
    private EditText name;

    private Task task;
    private int taskId;

    private final ArrayList<Location> chosenLocations = new ArrayList<>();
    private final ArrayList<Boolean> chosenLocationsActive = new ArrayList<>();
    private final ArrayList<Action> chosenActions = new ArrayList<>();
    private MainTaskActionAdapter mainTaskActionAdapter;
    private MainTaskLocationAdapter mainTaskLocationAdapter;

    private IndoorServiceProvider indoorServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Intent intent = getIntent();
        taskId = intent.getIntExtra(TASK_ID, -1);

        if (taskId == -1) {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setCustomView(R.layout.abort_save_actionbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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
                        for (int i = 0; i < chosenLocations.size(); i++) {
                            locationBooleanMap.put(chosenLocations.get(i), chosenLocationsActive.get(i));
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

        name = (EditText) findViewById(R.id.textedit_task_name);

        ListView locationList = (ListView) findViewById(R.id.create_task_ortsliste);
        ListView actionsList = (ListView) findViewById(R.id.create_task_actionliste);

        ImageButton addAction = (ImageButton) findViewById(R.id.create_task_add_action);
        addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<Action> actions = new ArrayList<>();
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
                            mainTaskActionAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setTitle(getString(R.string.chooseActions));

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ImageButton configureLocationList = (ImageButton) findViewById(R.id.create_task_configure_ortsliste);
        configureLocationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Location> locations = indoorServiceProvider.getIndoorService().getLocations();
                if (locations.size() > 0) {
                    String[] list = new String[locations.size()];
                    for (int i = 0; i < locations.size(); i++) {
                        list[i] = locations.get(i).getName();
                    }

                    final boolean[] checkedItems = new boolean[list.length];
                    for (int i = 0; i < checkedItems.length; i++) {
                        checkedItems[i] = chosenLocations.contains(locations.get(i));
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this);
                    builder.setTitle(getString(R.string.chooseLocations))
                            .setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
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
                                            if (!chosenLocations.contains(locations.get(i))) {
                                                chosenLocations.add(locations.get(i));
                                                chosenLocationsActive.add(true);
                                            }
                                        } else {
                                            if (chosenLocations.contains(locations.get(i))) {
                                                int index = chosenLocations.indexOf(locations.get(i));
                                                chosenLocations.remove(index);
                                                chosenLocationsActive.remove(index);
                                            }
                                        }
                                    }
                                    mainTaskLocationAdapter.notifyDataSetChanged();
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

        mainTaskActionAdapter = new MainTaskActionAdapter(this, chosenActions);
        actionsList.setAdapter(mainTaskActionAdapter);
        mainTaskLocationAdapter = new MainTaskLocationAdapter(this, chosenLocations, chosenLocationsActive);
        locationList.setAdapter(mainTaskLocationAdapter);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (taskId == -1) {
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
            if (!name.getText().toString().matches("")) {
                Map<Location, Boolean> locationBooleanMap = new HashMap<>();
                for (int i = 0; i < chosenLocations.size(); i++) {
                    locationBooleanMap.put(chosenLocations.get(i), chosenLocationsActive.get(i));
                }
                task.setName(name.getText().toString());
                task.setLocations(locationBooleanMap);
                task.setActions(chosenActions);
                finish();
            }

            return true;
        }
        if (id == R.id.change_ort_menu_delete) {
            indoorServiceProvider.getIndoorService().removeTask(task);
            finish();
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

            if (taskId != -1) {
                task = indoorService.getTask(taskId);

                chosenActions.clear();
                ArrayList<Action> actions = task.getActions();
                for (Action a : actions) {
                    chosenActions.add(a);
                }
                mainTaskActionAdapter.notifyDataSetChanged();

                chosenLocations.clear();
                chosenLocationsActive.clear();
                for (Map.Entry<Location, Boolean> entry : task.getLocations().entrySet()) {
                    chosenLocationsActive.add(entry.getValue());
                    chosenLocations.add(entry.getKey());
                }
                mainTaskLocationAdapter.notifyDataSetChanged();

                name.setText(task.getName());
            } else {
                Action action = indoorService.getAction();
                if (action != null) {
                    chosenActions.add(action);
                    mainTaskActionAdapter.notifyDataSetChanged();
                }
            }
        } else if (msg == IndoorServiceProvider.NOT_CONNECTED) {

        }
    }

    @Override
    public IndoorService getIndoorService() {
        return indoorServiceProvider.getIndoorService();
    }
}
