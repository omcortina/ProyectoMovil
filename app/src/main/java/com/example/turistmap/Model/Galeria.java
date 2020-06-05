package com.example.turistmap.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;

public class Galeria {
    private int Id;
    private String Imagen;
    private int Id_dominio_tipo_eventualidad;
    private int Id_eventualidad;

    public Galeria() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public int getId_dominio_tipo_eventualidad() {
        return Id_dominio_tipo_eventualidad;
    }

    public void setId_dominio_tipo_eventualidad(int id_dominio_tipo_eventualidad) {
        Id_dominio_tipo_eventualidad = id_dominio_tipo_eventualidad;
    }

    public int getId_eventualidad() {
        return Id_eventualidad;
    }

    public void setId_eventualidad(int id_eventualidad) {
        Id_eventualidad = id_eventualidad;
    }

    public void Save(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("id_galeria", this.Id);
        registro.put("imagen", this.Imagen);
        registro.put("id_dominio_tipo_eventualidad", this.Id_dominio_tipo_eventualidad);
        registro.put("id_eventualidad", this.Id_eventualidad);

        db.insert("Galeria", null, registro);
        db.close();
    }

    public Galeria Find(Context context, int id){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String sql = "select * from Galeria where id_galeria="+id;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            this.Id = Integer.parseInt(cursor.getString(0));
            this.Imagen = cursor.getString(1);
            this.Id_dominio_tipo_eventualidad = cursor.getInt(2);
            this.Id_eventualidad = cursor.getInt(3);
            return this;
        }
        db.close();
        return null;
    }
}
