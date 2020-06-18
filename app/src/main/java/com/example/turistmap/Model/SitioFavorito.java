package com.example.turistmap.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class SitioFavorito {
    private int IdSitioFavorito;
    private int IdSitio;

    public SitioFavorito() {
    }

    public int getIdSitioFavorito() {
        return IdSitioFavorito;
    }

    public void setIdSitioFavorito(int idSitioFavorito) {
        IdSitioFavorito = idSitioFavorito;
    }

    public int getIdSitio() {
        return IdSitio;
    }

    public void setIdSitio(int idSitio) {
        IdSitio = idSitio;
    }
    public void Save(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("id_sitio_favorito", this.IdSitioFavorito);
        registro.put("id_sitio", this.IdSitio);

        db.insert("SitioFavorito", null, registro);
        db.close();
    }
    public static List<SitioFavorito> FindAll(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<SitioFavorito> lista = new ArrayList<>();

        String sql = "select * from SitioFavorito";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                SitioFavorito sitioFavorito = new SitioFavorito();
                sitioFavorito.IdSitioFavorito = cursor.getInt(0);
                sitioFavorito.IdSitio = cursor.getInt(1);
                lista.add(sitioFavorito);
            }while(cursor.moveToNext());
            return lista;
        }
        db.close();
        return null;
    }
}
