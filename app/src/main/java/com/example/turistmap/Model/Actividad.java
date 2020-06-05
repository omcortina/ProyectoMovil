package com.example.turistmap.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class Actividad {
    public int Id;
    public String Codigo;
    public String Nombre;
    public String Descripcion;

    public Actividad() {
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
        registro.put("id_actividad", this.Id);
        registro.put("codigo", this.Codigo);
        registro.put("nombre", this.Nombre);
        registro.put("descripcion", this.Descripcion);
        db.insert("Actividad", null, registro);
        db.close();
    }

    public Actividad Find(Context context, int id){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String sql = "select * from Actividad where id_actividad="+id;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            this.Id = Integer.parseInt(cursor.getString(0));
            this.Codigo = cursor.getString(1);
            this.Nombre = cursor.getString(2);
            this.Descripcion = cursor.getString(3);
            return this;
        }
        db.close();
        return null;
    }

    public static List<Actividad> FindAll(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<Actividad> lista = new ArrayList<>();

        String sql = "select * from Actividad";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                Actividad actividad = new Actividad();
                actividad.Id = Integer.parseInt(cursor.getString(0));
                actividad.Codigo = cursor.getString(1);
                actividad.Nombre = cursor.getString(2);
                actividad.Descripcion = cursor.getString(3);
                lista.add(actividad);
            }while(cursor.moveToNext());
            db.close();
            return lista;
        }
        db.close();
        return null;
    }

    public static List<Galeria> ImagenesDeActividd(Context context, int id){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<Galeria> lista = new ArrayList<>();

        String sql = "select * from Galeria where id_dominio_tipo_eventualidad=13 and id_eventualidad="+id;
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
