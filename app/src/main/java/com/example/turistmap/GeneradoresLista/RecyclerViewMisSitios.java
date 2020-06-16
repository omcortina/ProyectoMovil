package com.example.turistmap.GeneradoresLista;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turistmap.IndexCliente;
import com.example.turistmap.Model.Sitio;
import com.example.turistmap.R;
import com.example.turistmap.Routes.Routes;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewMisSitios extends RecyclerView.Adapter<RecyclerViewMisSitios.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_nombre_info, txt_direccion_info, txt_descripcion_info;
        private RoundedImageView img_sitio;
        private CardView cv_sitios_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre_info = (TextView) itemView.findViewById(R.id.txt_nombre_info);
            txt_direccion_info = (TextView) itemView.findViewById(R.id.txt_direccion_info);
            txt_descripcion_info = (TextView) itemView.findViewById(R.id.txt_descripcion_info);
            img_sitio = (RoundedImageView) itemView.findViewById(R.id.imagen_sitio);
            cv_sitios_info = (CardView) itemView.findViewById(R.id.cv_sitios_info);
        }
    }
    public List<Sitio> listaSitio;
    public Context context;

    public RecyclerViewMisSitios(List<Sitio> listaSitio, Context context) {
        this.listaSitio = listaSitio;
        this.context = context;
    }

    @Override
    public RecyclerViewMisSitios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sitio_info,parent,false);
        RecyclerViewMisSitios.ViewHolder viewHolder = new RecyclerViewMisSitios.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewMisSitios.ViewHolder holder, int position) {
        final Sitio sitio = listaSitio.get(position);
        holder.txt_nombre_info.setText(sitio.getNombre().toUpperCase());
        holder.txt_direccion_info.setText(Character.toUpperCase(sitio.getDireccion().charAt(0)) + sitio.getDireccion().substring(1));
        holder.txt_descripcion_info.setText(Character.toUpperCase(sitio.getDescripcion().charAt(0)) + sitio.getDescripcion().substring(1));
        Picasso.get()
                .load(Routes.directorio_imagenes+sitio.getRutaFoto())
                //.resize(70,70)
                //.placeholder(R)
                //.transform(new CropCircleTransformation())
                .into(holder.img_sitio);

        holder.cv_sitios_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubicar_sitio = new Intent(context, IndexCliente.class);
                int id_sitio = sitio.getId();
                ubicar_sitio.putExtra("id_sitio", id_sitio);
                context.startActivity(ubicar_sitio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaSitio.size();
    }
}
