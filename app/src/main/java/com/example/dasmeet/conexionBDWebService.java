package com.example.dasmeet;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class conexionBDWebService extends Worker {

    private String URL_BASE = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/sgarcia216/WEB/";

    private int idRel;
    private String nom="";
    private String txt="";
    private String hora="";


    public conexionBDWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }


    @NonNull
    @Override
    public Result doWork() {
        String tipo = getInputData().getString("tipo");

        switch (tipo) {
            case "selectIdRel":
                String emi = getInputData().getString("emisor");
                String rec = getInputData().getString("receptor");

                selectIdRel(emi,rec);
                Data resreg = new Data.Builder()
                        .putInt("id",idRel)
                        .build();
                return Result.success(resreg);
            case "select_men":
                int id = getInputData().getInt("idRel",0);
                System.out.println("el id que llega es: "+id+"");
                select_men(id);
                Data resultados = new Data.Builder()
                        .putString("nom",nom)
                        .putString("txt",txt)
                        .putString("hora",hora)
                        .build();
                return Result.success(resultados);
            default:
                return Result.failure();
        }

    }

    private void select_men(int id) {
        String url = URL_BASE + "select_mensaje.php?idRel="+String.valueOf(id);
        System.out.println("url: "+url);
        HttpURLConnection urlConnection = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line, result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();

                JSONArray jsonArray = new JSONArray(result);
                System.out.println("resultado:"+result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    nom = nom+jsonArray.getJSONObject(i).getString("idEmi")+",";
                    txt = txt+jsonArray.getJSONObject(i).getString("mensaje")+",";
                    hora = hora+jsonArray.getJSONObject(i).getString("fecHora")+",";
                }
                System.out.println("nombres:"+nom);
                System.out.println("men:"+txt);
                System.out.println("hora:"+hora);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void selectIdRel(String emi, String rec) {
        String url = URL_BASE + "select_idRelacion.php";
        System.out.println("url: "+url);
        HttpURLConnection urlConnection = null;
        try {
            URL requestUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line, result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    idRel = jsonArray.getJSONObject(i).getInt("res");
                }


            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}

