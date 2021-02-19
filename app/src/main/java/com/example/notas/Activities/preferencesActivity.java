package com.example.notas.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.notas.Preferences.PreferencesFragment;
import com.example.notas.R;

public class preferencesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferencesactivity);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaccion = fragmentManager.beginTransaction();
        transaccion.add(R.id.fragment_container, PreferencesFragment.newInstance());
        transaccion.commit();

    }
}