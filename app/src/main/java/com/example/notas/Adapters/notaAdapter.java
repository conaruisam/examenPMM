package com.example.notas.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notas.Classes.Nota;
import com.example.notas.R;

import java.util.ArrayList;


public class notaAdapter extends RecyclerView.Adapter<notaViewHolder> implements View.OnClickListener{

    private ArrayList<Nota> notas; // Listado de robots a mostrar....
    private Context context; // Para las imagenes...
    private View.OnClickListener listener;

    public notaAdapter(ArrayList<Nota> notas, Context context) {
        this.notas = notas;
        this.context = context;
    }

    @NonNull
    @Override
    public notaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_elemento_nota, parent, false);

        notaViewHolder viewHolder = new notaViewHolder(itemView, context);

        itemView.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull notaViewHolder holder, int position) {

        Nota nota = notas.get(position);
        holder.bindRobot(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }


    public void setOnClickListener(View.OnClickListener listener){

        this.listener = listener;
    }
    @Override
    public void onClick(View v) {


        System.out.println(v.getId());
        if (listener!=null) {
            listener.onClick(v);
        }

    }
}