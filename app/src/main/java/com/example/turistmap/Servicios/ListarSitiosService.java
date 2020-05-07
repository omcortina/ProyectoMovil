package com.example.turistmap.Servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.turistmap.BD;
import com.example.turistmap.Config.Config;
import com.example.turistmap.Dominio.Sitio;
import com.example.turistmap.Dominio.TipoSitio;
import com.example.turistmap.ListaSitios;
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

public class ListarSitiosService extends AsyncTask<Void,Void,String> {
    Context context;
    ProgressDialog progressDialog;
    Boolean error;

    public ListarSitiosService(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.context,"Sitios","Validando informacion...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String uri = Routes.listar_sitios;
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

                JSONObject response = null;
                response = new JSONObject(jo);

                BD admin = new BD(context, Config.database_name, null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();

                db.execSQL("DELETE FROM Dominio");
                db.execSQL("DELETE FROM Sitio");
                db.close();

                JSONArray array_sitios = response.getJSONArray("Sitios");
                for (int i=0; i<array_sitios.length(); i++){
                    JSONObject json = array_sitios.getJSONObject(i);
                    Sitio sitio = new Sitio();
                    sitio.setId(json.getInt("id_sitio"));
                    sitio.setCodigo(json.getString("codigo"));
                    sitio.setNombre(json.getString("nombre"));
                    sitio.setDireccion(json.getString("direccion"));
                    sitio.setDescripcion(json.getString("descripcion"));
                    sitio.setLatitud(json.getString("latitud"));
                    sitio.setLongitud(json.getString("longitud"));
                    sitio.setRutaFoto(json.getString("imagen"));
                    sitio.setIdDominioTipo(json.getInt("id_dominio_tipo"));
                    sitio.Save(this.context);
                }

                TipoSitio tipo_sitio_todos = new TipoSitio();
                tipo_sitio_todos.setId(0);
                tipo_sitio_todos.setNombre("Todos");
                tipo_sitio_todos.Save(this.context);

                JSONArray array_tipos_sitios = response.getJSONArray("Tipos");
                for (int j=0; j<array_tipos_sitios.length(); j++){
                    JSONObject json = array_tipos_sitios.getJSONObject(j);
                    TipoSitio tipo = new TipoSitio();
                    tipo.setId(json.getInt("id_dominio"));
                    tipo.setNombre(json.getString("nombre"));
                    tipo.Save(this.context);
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
            Toast.makeText(context, "No se pudieron cargar los sitios de forma on-line", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(context, ListaSitios.class);
        context.startActivity(intent);
    }
}
