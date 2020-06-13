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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.turistmap.Model.Evento;
import com.example.turistmap.Model.Sitio;
import com.example.turistmap.IndexCliente;
import com.example.turistmap.R;
import com.example.turistmap.Routes.Routes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewEventos extends RecyclerView.Adapter<RecyclerViewEventos.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_nombre_evento, txt_fecha_inicio_evento, txt_fecha_fin_evento, txt_descripcion_evento;
        private LinearLayout btn_mostrar_sitios_evento, btn_evento_info;
        private RecyclerView recycler_imagenes_evento;
        private ImageView img_evento;
        private ViewPager viewPager_imagenes_evento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre_evento = (TextView) itemView.findViewById(R.id.txt_nombre_evento);
            txt_fecha_inicio_evento = (TextView) itemView.findViewById(R.id.txt_fecha_inicio_evento);
            txt_fecha_fin_evento = (TextView) itemView.findViewById(R.id.txt_fecha_fin_evento);
            txt_descripcion_evento = (TextView) itemView.findViewById(R.id.txt_descripcion_evento);
            btn_mostrar_sitios_evento = (LinearLayout) itemView.findViewById(R.id.btn_mostrar_sitios_evento);
            btn_evento_info = (LinearLayout) itemView.findViewById(R.id.btn_evento_info);
            //recycler_imagenes_evento = (RecyclerView) itemView.findViewById(R.id.recycler_imagenes_evento);
            img_evento = (ImageView) itemView.findViewById(R.id.img_evento);
            viewPager_imagenes_evento = (ViewPager) itemView.findViewById(R.id.viewPager_imagenes_evento);
        }
    }

    public List<Evento> listaEventos;
    public List<Sitio> listaSitios;
    public Context context;

    public RecyclerViewEventos(List<Evento> lista , Context context) {

        this.listaEventos = lista;
        this.listaSitios = listaSitios;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Evento evento = listaEventos.get(position);
        holder.txt_nombre_evento.setText(evento.getNombre().toUpperCase());
        holder.txt_fecha_inicio_evento.setText("Fecha Inicio: "+evento.getFechaInicio());
        holder.txt_fecha_fin_evento.setText("Fecha Fin: "+evento.getFechaFin());
        holder.txt_descripcion_evento.setText("Descripcion: "+evento.getDescripcion());

        /*
        holder.recycler_imagenes_evento.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        RecyclerViewImagenesEvento adapter = new RecyclerViewImagenesEvento(Evento.ImagenesDelEvento(context, evento.getId()), context);
        holder.recycler_imagenes_evento.setAdapter(adapter);
        */


         ViewPagerEventos viewPagerEventos = new ViewPagerEventos(Evento.ImagenesDelEvento(context, evento.getId()), context);
         holder.viewPager_imagenes_evento.setAdapter(viewPagerEventos);


        holder.btn_evento_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.txt_fecha_inicio_evento.getVisibility() == v.GONE ){
                    holder.txt_fecha_inicio_evento.setVisibility(v.VISIBLE);
                    holder.txt_fecha_fin_evento.setVisibility(v.VISIBLE);
                    holder.txt_descripcion_evento.setVisibility(v.VISIBLE);
                }else{
                    holder.txt_fecha_inicio_evento.setVisibility(v.GONE);
                    holder.txt_fecha_fin_evento.setVisibility(v.GONE);
                    holder.txt_descripcion_evento.setVisibility(v.GONE);
                }
            }
        });

        holder.btn_mostrar_sitios_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IndexCliente.class);
                int id_evento = evento.getId();
                intent.putExtra("id_evento", id_evento);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return listaEventos.size();
    }
}
