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
    public static final String TASK_ID = "TASK_ID";
    private EditText name;
    private ImageButton configureLocationList;
    private ImageButton addAction;
    private ListView locationList;
    private ListView actionsList;

    private Task task;
    private int taskId;

    final ArrayList<Location> chosenLocations = new ArrayList<>();
    private ArrayList<Boolean> chosenLocationsActive = new ArrayList<>();
    private ArrayList<Action> chosenActions = new ArrayList<>();
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

        name = (EditText) findViewById(R.id.textedit_regel_name);

        locationList = (ListView) findViewById(R.id.create_regel_ortsliste);
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
                            mainTaskActionAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setTitle(getString(R.string.chooseActions));

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        configureLocationList = (ImageButton) findViewById(R.id.create_regel_configure_ortsliste);
        configureLocationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Location> orte = indoorServiceProvider.getIndoorService().getLocationManagement().getLocations();
                if (orte.size() > 0) {
                    String[] liste = new String[orte.size()];
                    for (int i = 0; i < orte.size(); i++) {
                        liste[i] = orte.get(i).getName();
                    }

                    final boolean[] checkedItems = new boolean[liste.length];
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (chosenLocations.contains(orte.get(i))) {
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
                                            if (!chosenLocations.contains(orte.get(i))) {
                                                chosenLocations.add(orte.get(i));
                                                chosenLocationsActive.add(true);
                                            }
                                        } else {
                                            if (chosenLocations.contains(orte.get(i))) {
                                                int index = chosenLocations.indexOf(orte.get(i));
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
                task = indoorService.getRule(taskId);

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
}
