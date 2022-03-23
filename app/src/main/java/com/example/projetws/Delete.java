package com.example.projetws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Delete extends AppCompatActivity {
    CircleImageView imageView;
    Button delete;
    TextView name;
    int id;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:8081/projet/ws/deleteEtudiant.php"; //10.0.2.2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        imageView=findViewById(R.id.imgProfil);
        delete=findViewById(R.id.del);
        name=findViewById(R.id.name);
        Bitmap b=StringToBitMap(getIntent().getStringExtra("image"));
        imageView.setImageBitmap(b);
        name.setText(getIntent().getStringExtra("name")+" "+getIntent().getStringExtra("prenom"));
        id=Integer.parseInt(getIntent().getStringExtra("id"));
        //Toast.makeText(this, id+"", Toast.LENGTH_LONG).show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Delete.this, "Bien Supprimer", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Delete.this, Affichages.class);
                        startActivity(intent);
                        Delete.this.finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("id",getIntent().getStringExtra("id"));
                       return params;
                    }
                };
                requestQueue.add(request);
            }
        });
    }


    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}