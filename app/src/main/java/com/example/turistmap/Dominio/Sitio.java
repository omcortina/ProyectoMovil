package com.example.turistmap.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class Sitio {
    private int Id;
    private String Codigo;
    private String Nombre;
    private String Direccion;
    private String Descripcion;
    private String Latitud;
    private String Longitud;
    private String RutaFoto;
    private int IdDominioTipo;

    public Sitio() {
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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getRutaFoto() {
        return RutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        RutaFoto = rutaFoto;
    }

    public int getIdDominioTipo() {
        return IdDominioTipo;
    }

    public void setIdDominioTipo(int idDominioTipo) {
        IdDominioTipo = idDominioTipo;
    }

    public void Save(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("id_sitio", this.Id);
        registro.put("codigo", this.Codigo);
        registro.put("nombre", this.Nombre);
        registro.put("direccion", this.Direccion);
        registro.put("descripcion", this.Descripcion);
        registro.put("latitud", this.Latitud);
        registro.put("longitud", this.Longitud);
        registro.put("ruta_foto", this.RutaFoto);
        registro.put("id_dominio_tipo", this.IdDominioTipo);

        db.insert("Sitio", null, registro);
        db.close();
    }

    public static List<Sitio> FindByTipo(Context context, int id_dominio_tipo){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<Sitio> lista = new ArrayList<>();

        String sql = "select * from Sitio where id_dominio_tipo="+id_dominio_tipo;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                Sitio sitio = new Sitio();
                sitio.Id = Integer.parseInt(cursor.getString(0));
                sitio.Codigo = cursor.getString(1);
                sitio.Nombre = cursor.getString(2);
                sitio.Direccion = cursor.getString(3);
                sitio.Descripcion = cursor.getString(4);
                sitio.Latitud = cursor.getString(5);
                sitio.Longitud = cursor.getString(6);
                sitio.RutaFoto = cursor.getString(7);
                sitio.IdDominioTipo = cursor.getInt(8);
                lista.add(sitio);
            }while(cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public static Sitio Find(Context context, int id_sitio){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String sql = "select * from Sitio where id_sitio="+id_sitio;
        Cursor cursor = db.rawQuery(sql, null);
        Sitio sitio = new Sitio();
        if (cursor.moveToFirst()){
            sitio.Id = cursor.getInt(0);
            sitio.Codigo = cursor.getString(1);
            sitio.Nombre = cursor.getString(2);
            sitio.Direccion = cursor.getString(3);
            sitio.Descripcion = cursor.getString(4);
            sitio.Latitud = cursor.getString(5);
            sitio.Longitud = cursor.getString(6);
            sitio.RutaFoto = cursor.getString(7);
            sitio.IdDominioTipo = cursor.getInt(8);
            return sitio;
        }
        db.close();
        return null;
    }

    public static Sitio FindMarkerSitio(Context context, double latitud, double longitud){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String sql = "select * from Sitio where latitud="+latitud+" and longitud="+longitud;
        Cursor cursor = db.rawQuery(sql, null);
        Sitio sitio = new Sitio();
        if (cursor.moveToFirst()){
            sitio.Id = cursor.getInt(0);
            sitio.Codigo = cursor.getString(1);
            sitio.Nombre = cursor.getString(2);
            sitio.Direccion = cursor.getString(3);
            sitio.Descripcion = cursor.getString(4);
            sitio.Latitud = cursor.getString(5);
            sitio.Longitud = cursor.getString(6);
            sitio.RutaFoto = cursor.getString(7);
            sitio.IdDominioTipo = cursor.getInt(8);
            return sitio;
        }
        db.close();
        return null;
    }

    public static List<Sitio> FindAll(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<Sitio> lista = new ArrayList<>();

        String sql = "select * from Sitio";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                Sitio sitio = new Sitio();
                sitio.Id = Integer.parseInt(cursor.getString(0));
                sitio.Codigo = cursor.getString(1);
                sitio.Nombre = cursor.getString(2);
                sitio.Direccion = cursor.getString(3);
                sitio.Descripcion = cursor.getString(4);
                sitio.Latitud = cursor.getString(5);
                sitio.Longitud = cursor.getString(6);
                sitio.RutaFoto = cursor.getString(7);
                sitio.IdDominioTipo = cursor.getInt(8);
                lista.add(sitio);
            }while(cursor.moveToNext());
        }
        db.close();
        return lista;
    }
}
