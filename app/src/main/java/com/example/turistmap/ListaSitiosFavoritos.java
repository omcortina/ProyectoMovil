package com.example.turistmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.turistmap.GeneradoresLista.RecyclerViewMisSitios;
import com.example.turistmap.GeneradoresLista.RecyclerViewTiposDeSitios;
import com.example.turistmap.Model.Sitio;
import com.example.turistmap.Model.TipoSitio;

public class ListaSitiosFavoritos extends AppCompatActivity {
    public static RecyclerView recyclerMisSitios;
    private RecyclerViewMisSitios adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sitios_favoritos);

        recyclerMisSitios = (RecyclerView) findViewById(R.id.recyclerMisSitios);
        recyclerMisSitios.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewMisSitios(Sitio.FindAllFavoritos(this),this);
        recyclerMisSitios.setAdapter(adapter);
    }
}
