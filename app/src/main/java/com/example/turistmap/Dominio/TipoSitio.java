package com.example.turistmap.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class TipoSitio {
    private int Id;
    private String Nombre;

    public TipoSitio() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }


    public void Save(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("id_dominio", this.Id);
        registro.put("nombre", this.Nombre);

        db.insert("TipoSitio", null, registro);
        db.close();
    }

    public static List<TipoSitio> FindAll(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<TipoSitio> lista = new ArrayList<>();

        String sql = "select * from TipoSitio";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                TipoSitio tipoSitio = new TipoSitio();
                tipoSitio.Id = cursor.getInt(0);
                tipoSitio.Nombre = cursor.getString(1);
                lista.add(tipoSitio);
            }while(cursor.moveToNext());
            return lista;
        }
        db.close();
        return null;
    }
}
