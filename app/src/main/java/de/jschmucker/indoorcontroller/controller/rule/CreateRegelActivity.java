package de.jschmucker.indoorcontroller.controller.rule;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.steuerung.Action;

public class CreateRegelActivity extends AppCompatActivity {
    private EditText name;
    private ImageButton configureOrtsliste;
    private ImageButton configureActions;
    private ListView ortsliste;
    private ListView actionsList;
    private boolean chooserReady = false;

    final ArrayList<Ort> chosenOrte = new ArrayList<Ort>();
    final ArrayList<Action> chosenActions = new ArrayList<Action>();

    private IndoorService indoorService;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_regel);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.abort_save_actionbar);

        View v = getSupportActionBar().getCustomView();

        name = (EditText) findViewById(R.id.textedit_regel_name);

        ortsliste = (ListView) findViewById(R.id.create_regel_ortsliste);
        actionsList = (ListView) findViewById(R.id.create_regel_actionliste);

        configureActions = (ImageButton) findViewById(R.id.create_regel_configure_actions);
        configureActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Action> actions = indoorService.getSteuerung().getActions();

                if (actions.size() > 0) {
                    String[] liste = new String[actions.size()];
                    for (int i = 0; i < actions.size(); i++) {
                        liste[i] = actions.get(i).toString();
                    }

                    final boolean[] checkedItems = new boolean[liste.length];
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (chosenOrte.contains(actions.get(i))) {
                            checkedItems[i] = true;
                        } else checkedItems[i] = false;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateRegelActivity.this);
                    builder.setTitle(getString(R.string.chooseActions))
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
                                        chosenActions.clear();
                                        if (checkedItems[i]) {
                                            chosenActions.add(actions.get(i));
                                        }
                                    }
                                    ArrayList<String> strings = new ArrayList<String>();
                                    for (Action action : chosenActions) {
                                        strings.add(action.toString());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateRegelActivity.this,
                                            android.R.layout.simple_list_item_1, strings);
                                    ortsliste.setAdapter(adapter);
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.chooseActions) + ": " +
                            getString(R.string.noelements), Toast.LENGTH_SHORT).show();
                }
            }
        });

        configureOrtsliste = (ImageButton) findViewById(R.id.create_regel_configure_ortsliste);
        configureOrtsliste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Ort> orte = indoorService.getOrtsManagement().getOrte();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateRegelActivity.this);
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
                                        chosenOrte.clear();
                                        if (checkedItems[i]) {
                                            chosenOrte.add(orte.get(i));
                                        }
                                    }
                                    ArrayList<String> strings = new ArrayList<String>();
                                    for (Ort ort : chosenOrte) {
                                        strings.add(ort.getName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateRegelActivity.this,
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
                    finish();
                }
            }
        });

        // Bind to Service
        Intent intent = new Intent(this, IndoorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
