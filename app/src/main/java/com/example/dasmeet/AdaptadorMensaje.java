package com.example.dasmeet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorMensaje extends BaseAdapter {
    private Context contexto;
    private LayoutInflater inflater;
    private ArrayList<String> nombres;
    private ArrayList<String> mensajes;
    private ArrayList<String> horasEnv;
    public AdaptadorMensaje(Context pcontext, ArrayList<String> pnombres, ArrayList<String> pmensajes,ArrayList<String> phorasEnv){
        contexto = pcontext;
        nombres = pnombres;
        mensajes=pmensajes;
        horasEnv=phorasEnv;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return nombres.size();
    }

    @Override
    public Object getItem(int i) {
        return nombres.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.adaptador_mensajes,null);
        TextView nombre= (TextView) view.findViewById(R.id.emisor);
        TextView men=(TextView) view.findViewById(R.id.mensaje);
        TextView hora=(TextView) view.findViewById(R.id.horaMen);
        nombre.setText(nombres.get(i));
        men.setText(mensajes.get(i));
        hora.setText(horasEnv.get(i));
        return view;
    }
}
