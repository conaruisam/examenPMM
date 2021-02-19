package com.example.notas.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.notas.Adapters.notaAdapter;
import com.example.notas.*;
import com.example.notas.Classes.Nota;
import com.example.notas.SQLite.MySQLiteHelper;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView TVNonota;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    // Almacenará el listado de notas y posteriormente se pondrá en el recyclerView
    private ArrayList<Nota> listadoNotas = new ArrayList<>();

    // Adaptador de nota...
    public notaAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        TVNonota = (TextView) findViewById(R.id.TVNoNotas);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);



        String pathDatabase = getDatabasePath("Nota.sqlite").getAbsolutePath();

        System.out.println("PATHHH DATABESE- : "+pathDatabase);

        rellenarArraylist();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /***
         *  RESULTCODE DEFINICIONES
         *  -1 = CREAR NOTA
         *   1 = EDITAR NOTA
         *   2 = BORRAR NOTA
         */


         // Si se ha creado el nota entonces lo añadimos al recyclerview.. sino el resultCode no será -1.
        if (resultCode == -1) { // CREAR nota....
        /*
            Una vez se haya terminado la anterior activity añadimos el nuevo nota.
         */
            String titulo = data.getStringExtra("titulo");
            String descripcion = data.getStringExtra("descripcion");
            String tipo = data.getStringExtra("tipo");

            //Formateando la fecha:

            Date fechaActual = new Date();
            DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = formatoFecha.format(fechaActual) + "  "+ formatoHora.format(fechaActual);

            Nota nota = new Nota(titulo,descripcion, tipo , fecha);

            listadoNotas.add(nota);

            actualizarRecycler();

            /***
             * BBDD -- Insertar el nota...
             */

            MySQLiteHelper sqLiteHelper = new MySQLiteHelper(this);
            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
            db = sqLiteHelper.getWritableDatabase();

            if(db != null) {
                //Si la bd es correcta y ha sido abierta
                //Creamos el ContentValues con los datos de la nueva fila
                ContentValues nuevaFila = new ContentValues();
                nuevaFila.put("titulo", titulo);
                nuevaFila.put("descripcion", descripcion);
                nuevaFila.put("tipo", tipo);
                nuevaFila.put("fecha", fecha);

                //Insertamos la fila
                db.insert("nota", null, nuevaFila);

                //Cerramos la base de datos
                db.close();
            }

            // Si le da a deshacer entonces habría que borrarlo de la BBDD
            final int[] notaDeshacer = {0};

            final MySQLiteHelper sqLiteHelper2 = new MySQLiteHelper(this);
            Snackbar.make(recyclerView, "nota "+ nota.getTitulo() +  " añadido correctamente.", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {

                            /***
                             * Borrarlo de la BBDD primero...
                             */
                            SQLiteDatabase db2 = sqLiteHelper2.getWritableDatabase();
                            db2 = sqLiteHelper2.getWritableDatabase();
                            if(db2 != null) {//Si la bd es correcta y ha sido abierta

                                String notaABorrar[] = {
                                        listadoNotas.get(listadoNotas.size()-1).getTitulo(),
                                        listadoNotas.get(listadoNotas.size()-1).getDescripcion(),
                                        listadoNotas.get(listadoNotas.size()-1).getTipo(),
                                        listadoNotas.get(listadoNotas.size()-1).getFecha(),
                                };

                                db2.delete("nota", "titulo = ? and " +
                                        "descripcion = ? and tipo = ? and fecha = ? ", notaABorrar);

                                //Cerramos la base de datos
                                db2.close();
                            }


                            listadoNotas.remove(listadoNotas.size()-1);

                            actualizarRecycler();

                            // Si se queda vacío volvemos a poner el texto de que no hay notas guardados...
                            if (listadoNotas.size() == 0){
                                TVNonota.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .setActionTextColor(getColor(R.color.colorRojo))
                    .show();

        } else if (resultCode == 1) { // EDITAR nota

            String titulo = data.getStringExtra("titulo");
            String descripcion = data.getStringExtra("descripcion");
            String fecha = data.getStringExtra("fecha");
            String tipo = data.getStringExtra("tipo");

            int index = data.getIntExtra("index", 1);

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
                camposACambiar.put("titulo", titulo);
                camposACambiar.put("descripcion", descripcion);
                camposACambiar.put("tipo", tipo);
                camposACambiar.put("fecha", fecha);

                // Ac
                String camposViejos[] = {
                        listadoNotas.get(index).getTitulo(),
                        listadoNotas.get(index).getDescripcion(),
                        listadoNotas.get(index).getTipo(),
                        listadoNotas.get(index).getFecha()
                };

                db.update("nota", camposACambiar, "titulo = ? and descripcion = ?" +
                                " and tipo = ? and fecha = ?", camposViejos);

                //Cerramos la base de datos
                db.close();
            }

            /***
             * Actualizar recyclerView.
             */
            listadoNotas.get(index).setTitulo(titulo);
            listadoNotas.get(index).setDescripcion(descripcion);
            listadoNotas.get(index).setFecha(fecha);
            listadoNotas.get(index).setTipo(tipo);

            actualizarRecycler();

        } else if (resultCode == 2) { // BORRAR nota

            String titulo = data.getStringExtra("titulo");
            String descripcion = data.getStringExtra("descripcion");
            String tipo = data.getStringExtra("tipo");

            int index = data.getIntExtra("index", 1);

            listadoNotas.remove(index); // Lo borramos del arraylist

            actualizarRecycler();
            MySQLiteHelper sqLiteHelper2 = new MySQLiteHelper(this);
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
            }

        }
    }

    /*
        Para actualizar el RecyclerView.
     */

    private void actualizarRecycler(){
        adaptador = new notaAdapter(listadoNotas, this);
        recyclerView.setAdapter(adaptador);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        /*
            ESTO VA A SER LA EDICIÓN DEL nota, UNA VEZ UNO SEA PULSADO.
         */
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEditarnota = new Intent(getApplicationContext(), SeeNote.class);

                /*
                    Envío la información del nota seleccionado...
                 */
                int posicionnota = recyclerView.getChildAdapterPosition(v);

                intentEditarnota.putExtra("index", posicionnota);
                intentEditarnota.putExtra("titulo", listadoNotas.get(recyclerView.getChildAdapterPosition(v)).getTitulo());
                intentEditarnota.putExtra("descripcion", listadoNotas.get(recyclerView.getChildAdapterPosition(v)).getDescripcion());
                intentEditarnota.putExtra("fecha", listadoNotas.get(recyclerView.getChildAdapterPosition(v)).getFecha());
                intentEditarnota.putExtra("tipo", listadoNotas.get(recyclerView.getChildAdapterPosition(v)).getTipo());

                startActivity(intentEditarnota);

                /***
                    Toast.makeText(getApplicationContext(), "Seleccion: "+listadonotas.get(recyclerView.getChildAdapterPosition(v)), Toast.LENGTH_LONG).show();
                */

            }
        });

        if (listadoNotas.size() != 0){
            TVNonota.setVisibility(View.GONE);
        } else {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idSelect = item.getItemId(); // He creado una variable que almacena el item seleccionado

        /***
         *      ACTION_NEW : Nuevo nota
         *      ACTION_RESTART: Reinicia los notas...
         *      ACTION_PREFERENCIAS: Abre la ventana de preferencais
         */

        switch (idSelect){
            case R.id.action_new:

                Intent i;

                i = new Intent(this, NewNote.class);

                startActivityForResult(i, 0);
                break;

            case R.id.action_preferencias:

                Intent inicio = new Intent(this, preferencesActivity.class);

                startActivity(inicio);

                break;

            case R.id.action_reiniciar:

                listadoNotas.clear();
                actualizarRecycler();

                MySQLiteHelper sqLiteHelper = new MySQLiteHelper(this);
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                db = sqLiteHelper.getWritableDatabase();
                if(db != null)
                {//Si la bd es correcta y ha sido abierta


                    db.delete("nota", "", null);

                    //Cerramos la base de datos
                    db.close();
                }

                TVNonota.setVisibility(View.VISIBLE);
                break;
        }

        return super.onOptionsItemSelected(item);

    }


    /***
     * Para rellenar el arraylist de listado de notas.
     */

    private void rellenarArraylist() {
        MySQLiteHelper sqLiteHelper = new MySQLiteHelper(this);

        //NO NECESARIO MODO ESCRITURA
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        if(db != null)
        {//Si la bd es correcta y ha sido abierta
            String[] args = null;
            Cursor c = db.rawQuery("SELECT titulo, descripcion, tipo, fecha FROM nota", args);

            //Nos aseguramos de que existe al menos una fila
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más filas
                do {
                    Log.i("APP", "Titulo: " + c.getString(0) +
                            " -- Descripcion: " + c.getString(1) +
                            " -- Tipo: " + c.getString(2) +
                            " -- Fecha: " + c.getString(3));

                    String tituloBBDD = c.getString(0);
                    String descripcionBBDD = c.getString(1);
                    String tipoBBDD = c.getString(2);
                    String fechaBDD = c.getString(3);

                    Nota r = new Nota(tituloBBDD,descripcionBBDD,tipoBBDD,fechaBDD);

                    listadoNotas.add(r);

                } while(c.moveToNext());
            }

            //Cerramos la base de datos
            db.close();

            if (listadoNotas.size() != 0){
                actualizarRecycler();
            }
        }
    }
}