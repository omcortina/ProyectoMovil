package com.example.turistmap.GeneradoresLista;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turistmap.IndexCliente;
import com.example.turistmap.Model.Evento;
import com.example.turistmap.Model.Galeria;
import com.example.turistmap.Model.Sitio;
import com.example.turistmap.R;
import com.example.turistmap.Routes.Routes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewImagenesEvento extends RecyclerView.Adapter<RecyclerViewImagenesEvento.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_galeria_evento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_galeria_evento = (ImageView) itemView.findViewById(R.id.img_galeria_evento);
        }
    }

    public List<Galeria> listaGaleria;
    public Context context;

    public RecyclerViewImagenesEvento(List<Galeria> listaGaleria, Context context) {
        this.listaGaleria = listaGaleria;
        this.context = context;
    }

    @Override
    public RecyclerViewImagenesEvento.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagen_evento,parent,false);
        RecyclerViewImagenesEvento.ViewHolder viewHolder = new RecyclerViewImagenesEvento.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewImagenesEvento.ViewHolder holder, int position) {
        final Galeria galeria = listaGaleria.get(position);
        Picasso.get()
                .load(Routes.directorio_imagenes+galeria.getImagen())
                //.resize(120,70)
                //.placeholder(R)
                //.transform(new CropCircleTransformation())
                .into(holder.img_galeria_evento);
    }

    @Override
    public int getItemCount() {
        return listaGaleria.size();
    }
}
