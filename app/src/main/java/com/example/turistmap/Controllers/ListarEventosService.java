package com.example.turistmap.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;
import com.example.turistmap.Model.Evento;
import com.example.turistmap.Model.Galeria;
import com.example.turistmap.Model.Sitio;
import com.example.turistmap.Model.SitioEvento;
import com.example.turistmap.ListaEventos;
import com.example.turistmap.Routes.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListarEventosService extends AsyncTask<Void,Void,String> {
    Context context;
    ProgressDialog progressDialog;
    Boolean error;

    public ListarEventosService(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.context,"Eventos","Validando informacion...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String uri = Routes.listar_eventos;
        URL url = null;
        try {
            url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            int response_code = urlConnection.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";
                while ((linea = br.readLine()) != null){
                    sb.append(linea);
                    break;
                }
                br.close();

                String jo = "";
                jo = sb.toString();

                JSONArray array = null;
                array = new JSONArray(jo);
                BD admin = new BD(context, Config.database_name, null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();

                db.execSQL("DELETE FROM Evento");
                db.close();

                for (int i=0; i<array.length(); i++){
                    JSONObject json = array.getJSONObject(i);
                    Evento evento = new Evento();
                    evento.setId(json.getInt("id_evento"));
                    evento.setCodigo(json.getString("codigo"));
                    evento.setNombre(json.getString("nombre"));
                    evento.setDescripcion(json.getString("descripcion"));
                    evento.setFechaInicio(json.getString("fecha_inicio"));
                    evento.setFechaFin(json.getString("fecha_fin"));

                    SitioEvento.EliminarSitiosDelEvento(context, evento.getId());

                    JSONArray array_sitios = json.getJSONArray("sitios");
                    for (int j=0; j<array_sitios.length(); j++) {
                        Sitio sitio = new Sitio();
                        SitioEvento sitioEvento = new SitioEvento();
                        JSONObject json_sitio = array_sitios.getJSONObject(j);
                        sitio.setId(json_sitio.getInt("id_sitio"));
                        sitio.setCodigo(json_sitio.getString("codigo"));
                        sitio.setNombre(json_sitio.getString("nombre"));
                        sitio.setDireccion(json_sitio.getString("direccion"));
                        sitio.setDescripcion(json_sitio.getString("descripcion"));
                        sitio.setLatitud(json_sitio.getString("latitud"));
                        sitio.setLongitud(json_sitio.getString("longitud"));
                        sitio.setRutaFoto(json_sitio.getString("imagen"));
                        sitio.setIdDominioTipo(json_sitio.getInt("id_dominio_tipo"));
                        sitio.Save(this.context);

                        sitioEvento.setId_evento(evento.getId());
                        sitioEvento.setId_sitio(sitio.getId());
                        sitioEvento.Save(this.context);
                    }

                    JSONArray array_imagenes = json.getJSONArray("imagenes");
                    for (int k=0; k<array_imagenes.length(); k++){
                        Galeria galeria = new Galeria();
                        JSONObject json_imagen = array_imagenes.getJSONObject(k);
                        galeria.setId(json_imagen.getInt("id_galeria"));
                        galeria.setImagen(json_imagen.getString("imagen"));
                        galeria.setId_dominio_tipo_eventualidad(json_imagen.getInt("id_dominio_tipo_eventualidad"));
                        galeria.setId_eventualidad(json_imagen.getInt("id_eventualidad"));
                        galeria.Save(context);
                    }
                    evento.Save(this.context);
                }
                this.error = false;
                return "ok";
            }

        } catch (MalformedURLException e) {
            error = true;
            return "Error de ruta: "+e.getMessage();
        } catch (IOException e) {
            error = true;
            return "Error de conexion a la ruta: "+e.getMessage();
        } catch (JSONException e) {
            error = true;
            return "Error con los parametros del json de envio: "+e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String respuesta) {
        super.onPostExecute(respuesta);
        progressDialog.dismiss();
        if (error){
            Toast.makeText(context, "No se pudieron cargar los eventos de forma on-line", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(context, ListaEventos.class);
        context.startActivity(intent);
    }
}
