package com.example.notas.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notas.Classes.Nota;
import com.example.notas.R;

public class notaViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgNota;
    private TextView titulo;
    private TextView fecha;

    private Context contexto;


    public notaViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        imgNota = itemView.findViewById(R.id.img_nota);
        titulo = itemView.findViewById(R.id.id_titulo);
        fecha = itemView.findViewById(R.id.id_fecha);

        contexto = context;

    }

    public void bindRobot(Nota r) {

        // Para poner la imagen
        switch (r.getTipo()) {
            case "WARNING":
                imgNota.setImageDrawable(contexto.getDrawable(R.drawable.warning));
                break;
            case "CLAVESOL":
                imgNota.setImageDrawable(contexto.getDrawable(R.drawable.clavesol));
                break;
            case "LISTACOMPRA":
                imgNota.setImageDrawable(contexto.getDrawable(R.drawable.listacompra));
                break;
        }

        titulo.setText(r.getTitulo().toString());
        String fechString = r.getFecha()+ "";
        fecha.setText(fechString);
    }

}