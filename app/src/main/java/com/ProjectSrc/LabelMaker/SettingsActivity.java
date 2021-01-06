package com.ProjectSrc.LabelMaker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity
{
    private EditText elargeur, elongeur;
    private EditText tlongeur;
    private CheckBox redbox, blackbox, bluebox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        elongeur = (EditText) findViewById(R.id.editTextlong);
        elargeur = (EditText)findViewById(R.id.editTextlarg);
        tlongeur = (EditText)findViewById(R.id.eTextsize);
        redbox = (CheckBox)findViewById(R.id.redbox);
        bluebox = (CheckBox)findViewById(R.id.bluebox);
        blackbox = (CheckBox)findViewById(R.id.blackbox);

        elongeur.setText(String.valueOf(MainActivity.longeur));
        elargeur.setText(String.valueOf(MainActivity.largeur));
        tlongeur.setText(String.valueOf(MainActivity.tailleTexte));

        int col = MainActivity.couleurTexte;
        if (col == 0)
        {
           blackbox.setChecked(true);
        }
        else if (col == 1)
        {
            bluebox.setChecked(true);
        }
        else
        {
            redbox.setChecked(true);
        }


       /* getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit(); */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void validerparam(View view)
    {
        try
        {
            MainActivity.longeur = Integer.parseInt(elongeur.getText().toString());
            MainActivity.largeur = Integer.parseInt(elargeur.getText().toString());
            MainActivity.tailleTexte = Integer.parseInt(tlongeur.getText().toString());
        }
        catch (Exception e)
        {
            Log.i("ex", "validerparam: " + e);
        }

        int m = 0, nb = 0;
        if (redbox.isChecked())
        {
            nb += 1;
            m = 2;
        }
        if (bluebox.isChecked())
        {
            m = 1;
            nb += 1;
        }
        if (blackbox.isChecked())
        {
            m = 0;
            nb += 1;
        }

        MainActivity.couleurTexte = m;
        MainActivity.complexRecyclerViewAdapter.notifyDataSetChanged();
        finish();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}