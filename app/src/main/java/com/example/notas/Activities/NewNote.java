package com.example.notas.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notas.Classes.Nota;
import com.example.notas.R;

import java.util.ArrayList;

public class NewNote extends AppCompatActivity  implements View.OnClickListener{
    /***
     * Opciones para el tipo de nota...
     */

    final String[] opcionesNotas = new String[] {
            "Nota de alerta",
            "Nota músical",
            "Lista de la compra"
    };

    final String[] opcionesNotas2 = new String[] {
            "WARNING", "CLAVESOL", "LISTACOMPRA"
    };


    private Spinner spinnerNota;
    private ArrayAdapter<String> adapter;


    private EditText EdTitulo;
    private EditText EDescripcion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        spinnerNota = (Spinner)findViewById(R.id.SpinerTipoNota);

        EdTitulo = (EditText)findViewById(R.id.edTitulo);
        EDescripcion = (EditText)findViewById(R.id.edDescripcion);



        // Creo un adaptador el cual será un dropdown, y le asigno las opciones que he creado arriba...
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcionesNotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Meto el adaptador a nuestro spinner
        spinnerNota.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        // Si le da a crear vamos a almacenar lo escrito del nota...

        String titulo, descripcion,  tipo ="";


        titulo = EdTitulo.getText().toString();
        descripcion = EDescripcion.getText().toString();

        System.out.println(opcionesNotas[0]);

        switch (spinnerNota.getSelectedItem().toString()){
            case "Nota de alerta":
                tipo = opcionesNotas2[0];
                break;
            case "Nota músical":

                tipo = opcionesNotas2[1];
                break;
            case "Lista de la compra":

                tipo = opcionesNotas2[2];
                break;
        }




        if (!titulo.equals("")   &&
            !descripcion.equals("") &&
            !tipo.equals("")) {
            // Para intercambiar la información entre activities

            Intent returnIntent = new Intent(this, MainActivity.class);

            returnIntent.putExtra("titulo", titulo);
            returnIntent.putExtra("descripcion", descripcion);
            returnIntent.putExtra("tipo", tipo);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {

                Toast toast;

                toast = Toast.makeText(getApplicationContext(), "Introduzca todos los datos, por favor", Toast.LENGTH_SHORT);;
                toast.setGravity(Gravity.CENTER,0 ,0);
                toast.show();
        }
    }

    /*
        Para comprobar si una cadena es numerica
     */

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

}