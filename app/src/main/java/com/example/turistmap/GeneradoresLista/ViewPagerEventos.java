package com.example.turistmap.GeneradoresLista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.turistmap.Model.Galeria;
import com.example.turistmap.R;
import com.example.turistmap.Routes.Routes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerEventos extends PagerAdapter {
    public List<Galeria> listaGaleria;
    public Context context;
    private LayoutInflater layoutInflater;

    public ViewPagerEventos(List<Galeria> listaGaleria, Context context) {
        this.listaGaleria = listaGaleria;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaGaleria.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_imagen_evento, null);

        ImageView img_galeria_evento = (ImageView) view.findViewById(R.id.img_galeria_evento);
        final Galeria galeria = listaGaleria.get(position);
        Picasso.get()
                .load(Routes.directorio_imagenes+galeria.getImagen())
                //.resize(120,70)
                //.placeholder(R)
                //.transform(new CropCircleTransformation())
                .into(img_galeria_evento);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
