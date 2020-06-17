package com.example.turistmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.turistmap.Model.Sitio;
import com.example.turistmap.Model.SitioActividad;
import com.example.turistmap.Model.SitioEvento;
import com.example.turistmap.Routes.Routes;
import com.example.turistmap.Controllers.ListarActividadesService;
import com.example.turistmap.Controllers.ListarEventosService;
import com.example.turistmap.Controllers.ListarSitiosService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.PolyUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndexCliente extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    JSONObject json;
    LatLng posicionActual;
    private TextView txt_nombre_sitio, txt_direccion_sitio, txt_descripcion_sitio, txt_favorito;
    private RoundedImageView imagen_sitio;
    private Toolbar nombreEventualidad;
    private LinearLayout btn_favorito;
    private ImageView img_favorito;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RevisarPermisos();
        setContentView(R.layout.activity_index);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        View itemEvento = (View) findViewById(R.id.item_eventos);
        View itemActividad = (View) findViewById(R.id.item_actividades);
        View itemSitios = (View) findViewById(R.id.item_sitios);
        View itemMisSitios = (View) findViewById(R.id.item_sitios_favoritos);
        nombreEventualidad = (Toolbar) findViewById(R.id.nombreEventualidad);



        itemEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListarEventosService servicio = new ListarEventosService(IndexCliente.this);
                servicio.execute();
            }
        });

        itemActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListarActividadesService servicio = new ListarActividadesService(IndexCliente.this);
                servicio.execute();
            }
        });

        itemSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListarSitiosService servicio = new ListarSitiosService(IndexCliente.this);
                servicio.execute();
            }
        });

        itemMisSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexCliente.this, ListaSitiosFavoritos.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //PolylineOptions lineOptions = null;
        ArrayList<LatLng> points = null;
        if(getMyPosition() != null) {
            posicionActual = new LatLng(getMyPosition().getLatitude(), getMyPosition().getLongitude());
            mMap.addMarker(new MarkerOptions().position(posicionActual).title("Aqui estoy yo"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,14));
        }else{
            posicionActual = new LatLng(10.4654325,-73.2604819);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,13));
        }

        Intent intentEvento = getIntent();
        int id_evento = intentEvento.getIntExtra("id_evento",0);
        if (id_evento != 0){

            nombreEventualidad.setTitle(intentEvento.getStringExtra("nombreEvento"));
            points = new ArrayList<LatLng>();
            //lineOptions = new PolylineOptions();
            List<Sitio> array_sitios = SitioEvento.FindAllSitiosPorEvento(this, id_evento);
            //points.add(posicionActual);
            for(Sitio s: array_sitios){
                double latitud = Double.parseDouble(s.getLatitud());
                double longitud = Double.parseDouble(s.getLongitud());
                LatLng punto = new LatLng(latitud, longitud);
                mMap.addMarker(new MarkerOptions().position(punto).title(s.getNombre()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,13));
                points.add(punto);
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng posicion_sitio = marker.getPosition();
                    final Sitio sitio = Sitio.FindMarkerSitio(IndexCliente.this, posicion_sitio.latitude, posicion_sitio.longitude);
                    if(sitio != null){
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(IndexCliente.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_sitios, (LinearLayout) findViewById(R.id.bottom_sheet_sitio));
                        txt_nombre_sitio = bottomSheetView.findViewById(R.id.txt_nombre_sitio);
                        txt_direccion_sitio = bottomSheetView.findViewById(R.id.txt_direccion_sitio);
                        txt_descripcion_sitio = bottomSheetView.findViewById(R.id.txt_descripcion_sitio);
                        imagen_sitio = bottomSheetView.findViewById(R.id.imagen_sitio);
                        btn_favorito = (LinearLayout) bottomSheetView.findViewById(R.id.btn_favorito);
                        img_favorito = bottomSheetView.findViewById(R.id.img_favorito);
                        txt_favorito = bottomSheetView.findViewById(R.id.txt_favorito);
                        if(sitio.getFavorito() == 1){
                            img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
                            txt_favorito.setText("Quitar de favoritos");
                        }else{
                            img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.icon_favoriteee));
                            txt_favorito.setText("Añadir a favoritos");

                        }
                        Picasso.get()
                                .load(Routes.directorio_imagenes+sitio.getRutaFoto())
                                //.resize(70,70)
                                .placeholder(R.drawable.loginn)
                                //.transform(new CropCircleTransformation())
                                .into(imagen_sitio);
                        txt_nombre_sitio.setText(sitio.getNombre());
                        txt_direccion_sitio.setText(sitio.getDireccion());
                        txt_descripcion_sitio.setText(sitio.getDescripcion());
                        btn_favorito.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(sitio.getFavorito() == 0){
                                    img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
                                    txt_favorito.setText("Quitar de favoritos");
                                }else{
                                    img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.icon_favoriteee));
                                    txt_favorito.setText("Añadir a favoritos");
                                }
                                if(sitio.EstablecerEstadoFavorito(getApplicationContext())){
                                    if(sitio.getFavorito() == 1) {
                                        Toast.makeText(IndexCliente.this, "Se agrego a favoritos", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(IndexCliente.this, "Se quito de favoritos", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(IndexCliente.this, "Ocurrio un error al intentar cambiar el estado", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
                    return true;
                }
            });
        }

        Intent ver_sitio_actividad = getIntent();
        int id_actividad = ver_sitio_actividad.getIntExtra("id_actividad",0);
        if (id_actividad != 0){
            nombreEventualidad.setTitle(ver_sitio_actividad.getStringExtra("nombreActividad"));
            points = new ArrayList<LatLng>();
            //lineOptions = new PolylineOptions();
            List<Sitio> array_sitios_actividad = SitioActividad.FindAllSitiosPorActividad(this, id_actividad);
            //points.add(posicionActual);
            for(Sitio s: array_sitios_actividad){
                double latitud = Double.parseDouble(s.getLatitud());
                double longitud = Double.parseDouble(s.getLongitud());
                LatLng punto = new LatLng(latitud, longitud);
                mMap.addMarker(new MarkerOptions().position(punto).title(s.getNombre()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,13));
                points.add(punto);
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng posicion_sitio = marker.getPosition();
                    final Sitio sitio = Sitio.FindMarkerSitio(IndexCliente.this, posicion_sitio.latitude, posicion_sitio.longitude);
                    if(sitio != null){
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(IndexCliente.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_sitios, (LinearLayout) findViewById(R.id.bottom_sheet_sitio));
                        txt_nombre_sitio = bottomSheetView.findViewById(R.id.txt_nombre_sitio);
                        txt_direccion_sitio = bottomSheetView.findViewById(R.id.txt_direccion_sitio);
                        txt_descripcion_sitio = bottomSheetView.findViewById(R.id.txt_descripcion_sitio);
                        imagen_sitio = bottomSheetView.findViewById(R.id.imagen_sitio);
                        btn_favorito = (LinearLayout) bottomSheetView.findViewById(R.id.btn_favorito);
                        img_favorito = bottomSheetView.findViewById(R.id.img_favorito);
                        txt_favorito = bottomSheetView.findViewById(R.id.txt_favorito);
                        if(sitio.getFavorito() == 1){
                            img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
                            txt_favorito.setText("Quitar de favoritos");
                        }else{
                            img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.icon_favoriteee));
                            txt_favorito.setText("Añadir a favoritos");
                        }
                        Picasso.get()
                                .load(Routes.directorio_imagenes+sitio.getRutaFoto())
                                //.resize(70,70)
                                .placeholder(R.drawable.loginn)
                                //.transform(new CropCircleTransformation())
                                .into(imagen_sitio);
                        txt_nombre_sitio.setText(sitio.getNombre());
                        txt_direccion_sitio.setText(sitio.getDireccion());
                        txt_descripcion_sitio.setText(sitio.getDescripcion());
                        btn_favorito.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(sitio.getFavorito() == 0){
                                    img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
                                    txt_favorito.setText("Quitar de favoritos");
                                }else{
                                    img_favorito.setImageDrawable(getResources().getDrawable(R.drawable.icon_favoriteee));
                                    txt_favorito.setText("Añadir a favoritos");
                                }
                                if(sitio.EstablecerEstadoFavorito(getApplicationContext())){
                                    if(sitio.getFavorito() == 1) {
                                        Toast.makeText(IndexCliente.this, "Se agrego a favoritos", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(IndexCliente.this, "Se quito de favoritos", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(IndexCliente.this, "Ocurrio un error al intentar cambiar el estado", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
                    return true;
                }
            });
        }

        Intent ubicar_sitio = getIntent();
        int id_sitio = ubicar_sitio.getIntExtra("id_sitio", 0);
        if(id_sitio != 0){
            Sitio sitio = Sitio.Find(IndexCliente.this, id_sitio);
            double latitud_sitio = Double.parseDouble(sitio.getLatitud());
            double longitud_sitio = Double.parseDouble(sitio.getLongitud());
            LatLng punto_sitio = new LatLng(latitud_sitio, longitud_sitio);
            mMap.addMarker(new MarkerOptions().position(punto_sitio).title(sitio.getNombre()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto_sitio,15));
        }
    }

    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){

                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length();k++){
                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.GRAY).width(5));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Location getMyPosition(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return  location;
    }

    public void RevisarPermisos(){
        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
        permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //Add a marker in Sydney and move the camera
        Location my_posicion = getMyPosition();
        double lat;
        double lon;
        if (my_posicion==null){
            //colocamos la localizacion de colombia si no lee la actual
            lat = 2.8894434;
            lon = -73.783892;
            //Snackbar.make(getCurrentFocus(), "Por favor asegurese de tener el GPS encendido es necesario para el uso de la aplicacion", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Toast.makeText(this, "Por favor asegurese de tener el GPS encendido es necesario para el uso de la aplicacion", Toast.LENGTH_LONG).show();
        }else{
            lat = my_posicion.getLatitude();
            lon = my_posicion.getLongitude();
        }
    }


}
