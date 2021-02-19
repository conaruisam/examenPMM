package com.example.notas.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notas.*;
import com.example.notas.SQLite.MySQLiteHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class SeeNote extends AppCompatActivity implements View.OnClickListener {

    /***
     * Opciones para el tipo de robot...
     */

    private ArrayAdapter<String> adapter;


    private TextView txtTitulo;
    private TextView txtDescripcion;
    private TextView fecha;
    private TextView tipoNota;

    private TextView nombreNota;

    private ImageView imgNota;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_note);


        txtTitulo = (TextView)findViewById(R.id.txttitulo);
        txtDescripcion = (TextView)findViewById(R.id.txtdesc);
        nombreNota = (TextView)findViewById(R.id.txtNombreNota);
        fecha = (TextView)findViewById(R.id.txtfecha);
        tipoNota = (TextView)findViewById(R.id.txtTipoNota);
        imgNota = (ImageView) findViewById(R.id.img_nota);

        nombreNota.setText(getIntent().getStringExtra("titulo"));
        txtTitulo.setText(getIntent().getStringExtra("titulo"));
        txtDescripcion.setText(getIntent().getStringExtra("descripcion"));
        fecha.setText(getIntent().getStringExtra("fecha"));


        String tipo = getIntent().getStringExtra("tipo");
        switch (tipo) {
            case "WARNING":
                imgNota.setImageDrawable(this.getDrawable(R.drawable.warning));
                tipoNota.setText("Nota de alerta");
                break;
            case "CLAVESOL":
                imgNota.setImageDrawable(this.getDrawable(R.drawable.clavesol));
                tipoNota.setText("Letra de canciones");
                break;
            case "LISTACOMPRA":
                imgNota.setImageDrawable(this.getDrawable(R.drawable.listacompra));
                tipoNota.setText("Lista de la compra");
                break;
        }
    }

    @Override
    public void onClick(View v) {

        // Dependiendo del botón pulsado actuará de una forma u otra
        final String titulo, descripcion, fechaD, tipo;


        switch (v.getId()){
            case R.id.btnEditar:

                titulo = txtTitulo.getText().toString();
                descripcion = txtDescripcion.getText().toString();
                fechaD = fecha.getText().toString();
                tipo = getIntent().getStringExtra("tipo");

                // Y volvemos al menú principal...
                Intent i = new Intent(this, EditNote.class);

                i.putExtra("index",  getIntent().getIntExtra("index", 1));
                i.putExtra("titulo", titulo);
                i.putExtra("descripcion", descripcion);
                i.putExtra("tipo", tipo);
                i.putExtra("fecha", fechaD);

                startActivity(i);
                break;

            case R.id.btnCancelar:
                // Y volvemos al menú principal...
                Intent cancelIntent = new Intent(this, MainActivity.class);

                setResult(0, cancelIntent);

                finish();
                break;
            case R.id.btnBorrar:

                final Intent borrarIntent = new Intent(this, MainActivity.class);

                titulo = txtTitulo.getText().toString();
                descripcion = txtDescripcion.getText().toString();
                tipo = getIntent().getStringExtra("tipo");

                final Context contexto = this;

                new AlertDialog.Builder(this)
                        .setTitle("Alerta")
                        .setMessage("¿Estás seguro de que deseas borrar el nota?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                String titulo = getIntent().getStringExtra("titulo");
                                String descripcion = getIntent().getStringExtra("descripcion");
                                String tipo = getIntent().getStringExtra("tipo");


                                MySQLiteHelper sqLiteHelper2 = new MySQLiteHelper(contexto);
                                SQLiteDatabase db2 = sqLiteHelper2.getWritableDatabase();
                                db2 = sqLiteHelper2.getWritableDatabase();

                                if(db2 != null) {//Si la bd es correcta y ha sido abierta

                                    String notaABorrar[] = {
                                            titulo,
                                            descripcion,
                                            tipo
                                    };

                                    db2.delete("nota", "titulo = ? and " +
                                            "descripcion = ? and tipo = ? ", notaABorrar);

                                    //Cerramos la base de datos
                                    db2.close();

                                    Intent i = new Intent(contexto, MainActivity.class);

                                    startActivity(i);
                                }
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
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