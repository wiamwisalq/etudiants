package com.example.projetws.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetws.Affichages;
import com.example.projetws.Delete;
import com.example.projetws.MainActivity;
import com.example.projetws.Modifier;
import com.example.projetws.R;
import com.example.projetws.beans.Etudiant;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.MyViewHolder> {

    private Context context;
    private static List<Etudiant> etudiants;

    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        this.etudiants = etudiants;
        this.context = context;
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.card, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Veuillez choisir une option :");

                alertDialogBuilder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap bitmap = ((BitmapDrawable)((ImageView)view
                                .findViewById(R.id.imgProfil)).getDrawable()).getBitmap();
                        Intent intent = new Intent(context, Delete.class);
                        intent.putExtra("image", BitMapToString(bitmap));
                        intent.putExtra("id", ((TextView)view.findViewById(R.id.ide)).getText().toString());
                        intent.putExtra("name", ((TextView)view.findViewById(R.id.nomP)).getText().toString());
                        intent.putExtra("prenom", ((TextView)view.findViewById(R.id.prenomP)).getText().toString());
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap bitmap = ((BitmapDrawable)((ImageView)view
                                .findViewById(R.id.imgProfil)).getDrawable()).getBitmap();
                        Intent intent = new Intent(context, Modifier.class);
                        intent.putExtra("image", BitMapToString(bitmap));
                        intent.putExtra("id", ((TextView)view.findViewById(R.id.ide)).getText().toString());
                        intent.putExtra("name", ((TextView)view.findViewById(R.id.nomP)).getText().toString());
                        intent.putExtra("prenom", ((TextView)view.findViewById(R.id.prenomP)).getText().toString());
                        intent.putExtra("ville", ((TextView)view.findViewById(R.id.villeP)).getText().toString());
                        intent.putExtra("sexe", ((TextView)view.findViewById(R.id.sexeP)).getText().toString());
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Etudiant etudiant = etudiants.get(position);
        Glide.with(holder.profil.getContext())
                .load("http://10.0.2.2:8081/projet/images/" + etudiant.getImage())
                .into(holder.profil);
        holder.id.setText(etudiant.getId() + "");
        holder.nom.setText(etudiant.getNom());
        holder.prenom.setText(etudiant.getPrenom());
        holder.sexe.setText(etudiant.getSexe());
        holder.ville.setText(etudiant.getVille());
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profil;
        TextView id;
        TextView nom;
        TextView prenom;
        TextView sexe;
        TextView ville;

        //RelativeLayout parent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.ide);
            profil = itemView.findViewById(R.id.imgProfil);
            nom = itemView.findViewById(R.id.nomP);
            prenom = itemView.findViewById(R.id.prenomP);
            sexe = itemView.findViewById(R.id.sexeP);
            ville = itemView.findViewById(R.id.villeP);
            // parent=itemView.findViewById(R.id.parent);
        }
    }
}
