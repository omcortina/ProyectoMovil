package com.example.turistmap.GeneradoresLista;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.turistmap.Model.Actividad;
import com.example.turistmap.IndexCliente;
import com.example.turistmap.Model.Evento;
import com.example.turistmap.R;
import com.example.turistmap.Routes.Routes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewActividades extends RecyclerView.Adapter<RecyclerViewActividades.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_nombre_actividad, txt_descripcion_actividad;
        private LinearLayout btn_mostrar_sitios_actividad, btn_actividad_info;
        private ImageView img_actividad;
        private ViewPager viewPager_imagenes_actividad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre_actividad = (TextView) itemView.findViewById(R.id.txt_nombre_actividad);
            txt_descripcion_actividad = (TextView) itemView.findViewById(R.id.txt_descripcion_actividad);
            btn_mostrar_sitios_actividad = (LinearLayout) itemView.findViewById(R.id.btn_mostrar_sitios_actividad);
            btn_actividad_info = (LinearLayout) itemView.findViewById(R.id.btn_actividad_info);
            img_actividad = (ImageView) itemView.findViewById(R.id.img_actividad);
            viewPager_imagenes_actividad = (ViewPager) itemView.findViewById(R.id.viewPager_imagenes_actividad);
        }
    }

    public List<Actividad> listaActividades;
    public Context context;

    public RecyclerViewActividades(List<Actividad> listaActividades, Context context) {
        this.listaActividades = listaActividades;
        this.context = context;
    }

    @Override
    public RecyclerViewActividades.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad,parent,false);
        RecyclerViewActividades.ViewHolder viewHolder = new RecyclerViewActividades.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Actividad actividad = listaActividades.get(position);
        holder.txt_nombre_actividad.setText(actividad.getNombre().toUpperCase());
        holder.txt_descripcion_actividad.setText("Descripcion: "+actividad.getDescripcion());

         ViewPagerActividades viewPagerActividades = new ViewPagerActividades(Actividad.ImagenesDeActividd(context, actividad.getId()), context);
         holder.viewPager_imagenes_actividad.setAdapter(viewPagerActividades);

        holder.btn_actividad_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.txt_descripcion_actividad.getVisibility() == v.GONE ){
                    holder.txt_descripcion_actividad.setVisibility(v.VISIBLE);
                }else{
                    holder.txt_descripcion_actividad.setVisibility(v.GONE);
                }
            }
        });
        holder.btn_mostrar_sitios_actividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IndexCliente.class);
                int id_actividad = actividad.getId();
                intent.putExtra("id_actividad", id_actividad);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }
}
