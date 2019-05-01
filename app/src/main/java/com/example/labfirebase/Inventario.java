package com.example.labfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.util.ArrayList;

public class Inventario extends AppCompatActivity {

    DatabaseReference databaseReference;

    private final String DATABASE_REFERENCE = "productos";
    private ArrayList<ModeloDatoItem> items;
    private CustomArrayAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        items = new ArrayList<>();
        listView = findViewById(R.id.inventarioListView);

        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), nuevoItemActivity.class);
                startActivity(intent);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot post : dataSnapshot.getChildren()){

                    String key = post.getKey().toString();
                    String nombre = post.child("nombre").getValue().toString();
                    String precio = post.child("precio").getValue().toString();
                    String fotoUrl = post.child("fotoUrl").getValue().toString();
                    String descripcion = post.child("descripcion").getValue().toString();

                    ModeloDatoItem item = new ModeloDatoItem(true, key, getApplicationContext(), nombre, precio, descripcion, null, fotoUrl);

                    //ModeloDatoItem item = post.getValue(ModeloDatoItem.class);
                    items.add(item);
                }

                adapter = new CustomArrayAdapter(Inventario.this, items);
                listView.setAdapter(adapter);

                // BORRAR ELEMENTOS DE LA BASE DE DATOS

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("list", String.valueOf(position));
                        showDeleteDialog(items.get(position), position);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Inventario.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteDialog(final ModeloDatoItem item, final int position){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogbuilder.setView(dialogView);
        dialogbuilder.setTitle("Confirmar borrado");

        final Button acceptDeleteBtn = dialogView.findViewById(R.id.acceptDeletionBtn);
        final Button cancelDeleteBtn = dialogView.findViewById(R.id.cancelDeletionBtn);

        final AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();

        acceptDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete", "se presionó aceptar");
                deleteProduct(item.getKey(), position);
                alertDialog.dismiss();
            }
        });

        cancelDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete", "se presionó cancelar");
                alertDialog.dismiss();
            }
        });


    }

    private void deleteProduct(String id, int position){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE).child(id);
        databaseReference.removeValue();
        items.remove(position);
        //Log.e("delete", databaseReference.toString());
    }

}
