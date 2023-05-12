package com.example.dasmeet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentConver#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConver extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> nombres=new ArrayList<String>();
    private ArrayList<String> mensajes=new ArrayList<String>();
    private ArrayList<String> horas=new ArrayList<String>();

    public FragmentConver() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConver.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConver newInstance(String param1, String param2) {
        FragmentConver fragment = new FragmentConver();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*nombres.add("jowi");
        nombres.add("sara");
        mensajes.add("hola sara");
        mensajes.add("hola jowi");
        horas.add("11:01");
        horas.add("11:02");*/
        Data inputData = new Data.Builder()
                .putString("tipo", "select_men")
                .putInt("idRel", 1)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(conexionBDWebService.class).setInputData(inputData).build();
        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(otwr.getId())
                .observe((LifecycleOwner) getContext(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            String nom = workInfo.getOutputData().getString("nom");
                            String txt = workInfo.getOutputData().getString("txt");
                            String hora = workInfo.getOutputData().getString("hora");
                            //comprobamos que existe el usuario
                            if (nom.equals("mal")) {
                                //no hay mensajes
                                Toast.makeText(getContext(), "No hay mensajes", Toast.LENGTH_SHORT).show();
                            }  else {// Obtiene la contraseña hasheada almacenada en la base de datos
                                System.out.println("los emisores de los mensajes son: "+nom);
                                String[] arrayn = nom.split(",");
                                String[] arraym = txt.split(",");
                                String[] arrayh = hora.split(",");

                                nombres = new ArrayList<String>(Arrays.asList(arrayn));
                                mensajes = new ArrayList<String>(Arrays.asList(arraym));
                                horas = new ArrayList<String>(Arrays.asList(arrayh));
                            }
                            WorkManager.getInstance(getContext()).enqueue(otwr);
                            ListView conver = view.findViewById(R.id.listaMen);
                            AdaptadorMensaje eladap = new AdaptadorMensaje(getContext(), nombres, mensajes, horas);
                            conver.setAdapter(eladap);
                        }
                    }
                });
        WorkManager.getInstance(getContext()).enqueue(otwr);
    }
}