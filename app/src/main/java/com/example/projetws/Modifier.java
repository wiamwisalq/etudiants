package com.example.projetws;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Modifier extends AppCompatActivity {

    private FloatingActionButton modImage;
    private CircleImageView profilImage;
    private EditText nom;
    private EditText prenom;
    private EditText ville;
    private RadioButton m;
    private RadioButton f;
    private Button mod;
    private Bitmap bitmap;
    String encodeImage;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:8081/projet/ws/updateEtudiant.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier);
        modImage = findViewById(R.id.modImage);
        profilImage = findViewById(R.id.profile_img);
        nom = findViewById(R.id.nom1);
        prenom = findViewById(R.id.prenom1);
        ville = findViewById(R.id.ville1);
        m = (RadioButton) findViewById(R.id.m1);
        f = (RadioButton) findViewById(R.id.f1);
        mod = findViewById(R.id.mod);
        //Toast.makeText(this, getIntent().getStringExtra("sexe")+"", Toast.LENGTH_SHORT).show();
        //Affichege
        Bitmap b = StringToBitMap(getIntent().getStringExtra("image"));
        profilImage.setImageBitmap(b);
        nom.setText(getIntent().getStringExtra("name"));
        prenom.setText(getIntent().getStringExtra("prenom"));
        ville.setText(getIntent().getStringExtra("ville"));
        if (getIntent().getStringExtra("sexe").equals("homme")) {
            m.setChecked(true);
        }else{
            f.setChecked(true);

        }
        /////
        //Charger Image
        modImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(Modifier.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        ////
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Modifier.this, "Bien Modifer", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Modifier.this, Affichages.class);
                        startActivity(intent);
                        Modifier.this.finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String sexe = "";
                        if(m.isChecked())
                            sexe = "homme";
                        else
                            sexe = "femme";
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("id", getIntent().getStringExtra("id"));
                        params.put("nom", nom.getText().toString());
                        params.put("prenom", prenom.getText().toString());
                        params.put("ville", ville.getText().toString());
                        params.put("sexe", sexe);

                        if(encodeImage==null){
                            params.put("flag","0");
                        }else{
                            params.put("image",encodeImage);
                            params.put("flag","1");

                        }

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
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri filePath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profilImage.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex){

            }
        }
        //Uri uri=data.getData();
        //profilImage.setImageURI(uri);
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] byteOfImage=byteArrayOutputStream.toByteArray();
        encodeImage=android.util.Base64.encodeToString(byteOfImage, Base64.DEFAULT);
    }
}