package com.example.turistmap.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private int Id;
    private String Codigo;
    private String Nombre;
    private String FechaInicio;
    private String FechaFin;
    private String Descripcion;
    private ArrayList<Sitio> Sitios;

    public ArrayList<Sitio> getSitios() {
        return Sitios;
    }

    public void setSitios(ArrayList<Sitio> sitios) {
        Sitios = sitios;
    }

    public Evento() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        FechaFin = fechaFin;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void Save(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("id_evento", this.Id);
        registro.put("codigo", this.Codigo);
        registro.put("nombre", this.Nombre);
        registro.put("fecha_inicio", this.FechaInicio);
        registro.put("fecha_fin", this.FechaFin);
        registro.put("descripcion", this.Descripcion);

        db.insert("Evento", null, registro);
        db.close();
    }

    public Evento Find(Context context, int id){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String sql = "select * from Evento where id_evento="+id;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            this.Id = Integer.parseInt(cursor.getString(0));
            this.Codigo = cursor.getString(1);
            this.Nombre = cursor.getString(2);
            this.FechaInicio = cursor.getString(3);
            this.FechaFin = cursor.getString(4);
            this.Descripcion = cursor.getString(5);
            return this;
        }
        db.close();
        return null;
    }

    public static List<Evento> FindAll(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<Evento> lista = new ArrayList<>();

        String sql = "select * from Evento";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                Evento evento = new Evento();
                evento.Id = Integer.parseInt(cursor.getString(0));
                evento.Codigo = cursor.getString(1);
                evento.Nombre = cursor.getString(2);
                evento.FechaInicio = cursor.getString(3);
                evento.FechaFin = cursor.getString(4);
                evento.Descripcion = cursor.getString(5);
                lista.add(evento);
            }while(cursor.moveToNext());
            return lista;
        }
        db.close();
        return null;
    }

    public static List<Galeria> ImagenesDelEvento(Context context, int id){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<Galeria> lista = new ArrayList<>();

        String sql = "select * from Galeria where id_dominio_tipo_eventualidad=14 and id_eventualidad="+id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do{

                int id_galeria = cursor.getInt(0);
                lista.add(new Galeria().Find(context, id_galeria));
            }while(cursor.moveToNext());
        }
        db.close();
        return lista;
    }
}
