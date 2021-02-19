package com.example.notas.Classes;

import java.util.Date;

public class Nota {

    private String titulo, descripcion, tipo;
    private String fecha;

    public Nota(String titulo, String descripcion, String tipo, String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
