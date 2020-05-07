package com.example.turistmap.Servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;
import com.example.turistmap.Dominio.Actividad;
import com.example.turistmap.Dominio.Sitio;
import com.example.turistmap.Dominio.SitioActividad;
import com.example.turistmap.ListaActividades;
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

public class ListarActividadesService extends AsyncTask<Void,Void,String> {
    Context context;
    ProgressDialog progressDialog;
    Boolean error;

    public ListarActividadesService(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.context,"Actividades","Validando informacion...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String uri = Routes.listar_actividades;
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

                db.execSQL("DELETE FROM Actividad");
                db.close();

                for (int i=0; i<array.length(); i++){
                    JSONObject json = array.getJSONObject(i);
                    Actividad actividad = new Actividad();
                    actividad.setId(json.getInt("id_actividad"));
                    actividad.setCodigo(json.getString("codigo"));
                    actividad.setNombre(json.getString("nombre"));
                    actividad.setDescripcion(json.getString("descripcion"));
                    actividad.setRutaFoto(json.getString("imagen"));

                    SitioActividad.EliminarSitioActividad(context, actividad.getId());
                    Sitio sitio = new Sitio();
                    SitioActividad sitioActividad = new SitioActividad();
                    JSONArray array_sitios = json.getJSONArray("sitios");
                    for (int j=0; j<array_sitios.length(); j++) {
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

                        sitioActividad.setId_actividad(json.getInt("id_actividad"));
                        sitioActividad.setId_sitio(json_sitio.getInt("id_sitio"));
                        sitioActividad.Save(this.context);
                    }
                    actividad.Save(this.context);
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
            Toast.makeText(context, "No se pudieron cargar las actividades", Toast.LENGTH_SHORT).show();
        }else{
            //aca puedes hacer un intent
            Intent intent = new Intent(context, ListaActividades.class);
            context.startActivity(intent);
        }
    }
}
