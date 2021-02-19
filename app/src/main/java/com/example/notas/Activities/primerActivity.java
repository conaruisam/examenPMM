package com.example.notas.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notas.R;

public class primerActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etNombre;
    private TextView txtView;
    private boolean userExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer);

        etNombre = (EditText) findViewById(R.id.eTNombre);
        txtView = (TextView) findViewById(R.id.textView8);
/*

        SharedPreferences prefs = getSharedPreferences("Mispreferencias", Context.MODE_PRIVATE);

        prefsEditor.clear();
        prefsEditor.commit();

        String nombreUsuario = prefs.getString("username","");


        Log.i("TAG", nombreUsuario);

        System.out.println("EL USUARIO GUARDADO ES: "+nombreUsuario);
        System.out.println(nombreUsuario);
*/

        /**
         * A partir del ejercicio 4 de ficheros pasará a pillar las sharedprefferencs de la pantalla
         * de configuración.
         */

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nombreUsuario = prefs.getString("opc_nick", "");

        System.out.println(etNombre);
        if (!nombreUsuario.equals("")){
            userExist = true;
            etNombre.setVisibility(View.GONE);
            txtView.setText("Bienvenido de nuevo "+nombreUsuario);
        }



    }


    @Override
    public void onClick(View v) {

        String nombre = etNombre.getText().toString();

        Intent mainActivity = new Intent(this, MainActivity.class);



        System.out.println(nombre);
        if (userExist == true){
            System.out.println("-- YA EXISTE UN USUARIO --");
            startActivity(mainActivity);
        } else {
            if (nombre.equals("")){
                Toast toast;

                toast = Toast.makeText(getApplicationContext(), "Introduzca su nombre, por favor", Toast.LENGTH_SHORT);;
                toast.setGravity(Gravity.CENTER,0 ,0);
                toast.show();
            } else {
                SharedPreferences prefs = getSharedPreferences("Mispreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();

                prefsEditor.putString("username", nombre);
                prefsEditor.commit();

                String nombreUsuario = prefs.getString("username", "Usuario");
                Log.i("TAG", nombreUsuario);
                System.out.println(prefs.getString("username", "Usuario"));
                startActivity(mainActivity);
            }
        }
    }
}