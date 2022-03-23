package com.example.projetws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.adapter.EtudiantAdapter;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Affichages extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Etudiant> lst = new ArrayList<>();
    String url = "http://10.0.2.2:8081/projet/ws/loadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichages);
        recyclerView = findViewById(R.id.recycler);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type type = new TypeToken<Collection<Etudiant>>() {
                }.getType();
                Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                for (Etudiant e : etudiants) {
                    lst.add(e);
                }

                //GsonBuilder gsonBuilder=new GsonBuilder();
                //Gson gson=gsonBuilder.create();
                //Etudiant[] data =gson.fromJson(response,Etudiant[].class);
                //Toast.makeText(Affichages.this, lst+"", Toast.LENGTH_SHORT).show();
                EtudiantAdapter etudiantAdapter = new EtudiantAdapter(Affichages.this, lst);
                recyclerView.setAdapter(etudiantAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Affichages.this));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}