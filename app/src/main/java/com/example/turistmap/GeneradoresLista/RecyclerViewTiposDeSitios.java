package com.example.turistmap.GeneradoresLista;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turistmap.Model.Sitio;
import com.example.turistmap.Model.TipoSitio;
import com.example.turistmap.ListaSitios;
import com.example.turistmap.R;

import java.util.List;

public class RecyclerViewTiposDeSitios extends RecyclerView.Adapter<RecyclerViewTiposDeSitios.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private Button btn_tipo_sitio;
        private RecyclerViewSitios adapter;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            btn_tipo_sitio = (Button) itemView.findViewById(R.id.btn_tipo_sitio);
        }
    }

    public List<TipoSitio> listaTiposSitio;
    public Context context;
    public Activity activity;

    public RecyclerViewTiposDeSitios(List<TipoSitio> listaTiposSitio, Context context, Activity activity) {
        this.listaTiposSitio = listaTiposSitio;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerViewTiposDeSitios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipo_sitio,parent,false);
        RecyclerViewTiposDeSitios.ViewHolder viewHolder = new RecyclerViewTiposDeSitios.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewTiposDeSitios.ViewHolder holder, int position) {
        final TipoSitio tipo_sitio = listaTiposSitio.get(position);
        holder.btn_tipo_sitio.setText(tipo_sitio.getNombre().toUpperCase());
        holder.adapter = new RecyclerViewSitios(Sitio.FindAll(context), context);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListaSitios.recyclerView_sitios.setAdapter(holder.adapter);
            }
        });
        holder.btn_tipo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapter = new RecyclerViewSitios(Sitio.FindByTipo(context, tipo_sitio.getId()), context);
                if(tipo_sitio.getId() == 0){
                    holder.adapter = new RecyclerViewSitios(Sitio.FindAll(context), context);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListaSitios.recyclerView_sitios.setAdapter(holder.adapter);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTiposSitio.size();
    }
}
