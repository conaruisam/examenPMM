package com.example.notas.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notas.*;
import com.example.notas.SQLite.MySQLiteHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditNote extends AppCompatActivity implements View.OnClickListener {

    /***
     * Opciones para el tipo de robot...
     */

    final String[] opcionesNotas = new String[] {
            "Nota de alerta",
            "Nota músical",
            "Lista de la compra"
    };
    final String[] opcionesNotas2 = new String[] {
            "WARNING", "CLAVESOL", "LISTACOMPRA"
    };

    private Spinner SpinerTipoNota;
    private ArrayAdapter<String> adapter;


    private EditText Edtitulo;
    private EditText EdDesripcion;
    private TextView fecha;

    private TextView nombreNota;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        SpinerTipoNota = (Spinner)findViewById(R.id.SpinerTipoNota);

        Edtitulo = (EditText)findViewById(R.id.edTitulo);
        EdDesripcion = (EditText)findViewById(R.id.edDescripcion);
        nombreNota = (TextView)findViewById(R.id.txtNombreNota);


        // Creo un adaptador el cual será un dropdown, y le asigno las opciones que he creado arriba...
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcionesNotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Meto el adaptador a nuestro spinner
        SpinerTipoNota.setAdapter(adapter);


        // La información de la nota llegando...
        Edtitulo.setText(getIntent().getStringExtra("titulo"));
        EdDesripcion.setText(getIntent().getStringExtra("descripcion"));

        int positionSpinner = 0;
        String tipoNota = getIntent().getStringExtra("tipo");

        switch (tipoNota){
            case "WARNING":
                positionSpinner = 0;
                break;
            case "CLAVESOL":
                positionSpinner = 1;
                break;
            case "LISTACOMPRA":
                positionSpinner = 2;
                break;
        }

        SpinerTipoNota.setSelection(positionSpinner);
    }

    @Override
    public void onClick(View v) {

        // Dependiendo del botón pulsado actuará de una forma u otra
        final String titulo, descripcion, fecha, tipo;


        switch (v.getId()){
            case R.id.btnEditar:

                String tituloN = Edtitulo.getText().toString();
                String descripcionN = EdDesripcion.getText().toString();
                String tipoNota = "";

                switch ((int) SpinerTipoNota.getSelectedItemId()){
                    case 0:

                        tipoNota = "WARNING";;
                        break;
                    case 1:

                        tipoNota = "CLAVESOL";
                        break;
                    case 2:

                        tipoNota = "LISTACOMPRA";
                        break;
                }

                //Formateando la fecha:

                Date fechaActual = new Date();
                DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                String fechaNueva = "Editado el "+ formatoFecha.format(fechaActual) + "  "+
                        formatoHora.format(fechaActual);


                /***
                 *  Actualizar BBDD...
                 *
                 */

                MySQLiteHelper sqLiteHelper = new MySQLiteHelper(this);
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                db = sqLiteHelper.getWritableDatabase();
                if(db != null)
                {//Si la bd es correcta y ha sido abierta

                    //Creamos contentValues con los datos a actualizar
                    ContentValues camposACambiar = new ContentValues();
                    camposACambiar.put("titulo", tituloN);
                    camposACambiar.put("descripcion", descripcionN);
                    camposACambiar.put("tipo", tipoNota);
                    camposACambiar.put("fecha", fechaNueva);

                    // Ac
                    String camposViejos[] = {
                            getIntent().getStringExtra("titulo"),
                            getIntent().getStringExtra("descripcion"),
                            getIntent().getStringExtra("tipo")
                    };

                    db.update("nota", camposACambiar, "titulo = ? and descripcion = ?" +
                            " and tipo = ?", camposViejos);

                    //Cerramos la base de datos
                    db.close();

                    Intent i = new Intent(this, MainActivity.class);

                    startActivity(i);

                }

                break;

            case R.id.btnCancelar:
                // Y volvemos al menú principal...
                Intent cancelIntent = new Intent(this, MainActivity.class);

                setResult(0, cancelIntent);

                finish();
                break;
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