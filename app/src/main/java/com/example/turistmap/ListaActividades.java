package com.example.turistmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.turistmap.Model.Actividad;
import com.example.turistmap.GeneradoresLista.RecyclerViewActividades;

public class ListaActividades extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewActividades adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividades);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerActividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewActividades(Actividad.FindAll(this), this);
        recyclerView.setAdapter(adapter);
    }
}
