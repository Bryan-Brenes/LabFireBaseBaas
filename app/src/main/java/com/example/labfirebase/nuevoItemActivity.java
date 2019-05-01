package com.example.labfirebase;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class nuevoItemActivity extends AppCompatActivity {

    ImageView fotoItemImageView;
    EditText nombreItemEditText;
    EditText precioItemEditText;
    EditText descripcionItemEditText;
    Button agregarBtn;

    private final int PICK_IMAGE = 1234;
    private final String DATABASE_REFERENCE = "productos";

    Uri imageURI;
    Bitmap imageBitmap;

    DatabaseReference databaseProductos;
    StorageReference storageReferenceProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_item);

        databaseProductos = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);
        storageReferenceProductos = FirebaseStorage.getInstance().getReference(DATABASE_REFERENCE);

        fotoItemImageView = findViewById(R.id.fotoImageView);
        nombreItemEditText = findViewById(R.id.nombreItemEditText);
        precioItemEditText = findViewById(R.id.precioEditText);
        descripcionItemEditText = findViewById(R.id.descripcionEditText);
        agregarBtn = findViewById(R.id.agregarBtn);

        fotoItemImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                abrirGaleria();
                return false;
            }
        });

        imageURI = null;
        imageBitmap = null;
    }

    private boolean validateForm(){
        String nombre = nombreItemEditText.getText().toString();
        String precio = precioItemEditText.getText().toString();
        String descripcion = descripcionItemEditText.getText().toString();

        if(nombre.equals("")){
            Toast.makeText(this, "Nombre requerido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(precio.equals("")){
            Toast.makeText(this, "Precio requerido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(descripcion.equals("")){
            Toast.makeText(this, "Descripción requerida", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    public void agregarProducto(View view){
        if(validateForm()){
            String nombre = nombreItemEditText.getText().toString();
            String precio = precioItemEditText.getText().toString();
            String descripcion = descripcionItemEditText.getText().toString();
            ModeloDatoItem producto = null;

            if(imageURI != null){
                StorageReference fileReference = storageReferenceProductos.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageURI));

                fileReference.putFile(imageURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String nombre = nombreItemEditText.getText().toString();
                                String precio = precioItemEditText.getText().toString();
                                String descripcion = descripcionItemEditText.getText().toString();
                                ModeloDatoItem producto = new ModeloDatoItem(nuevoItemActivity.this, nombre,
                                        precio, descripcion, null, taskSnapshot.getDownloadUrl().toString());
                                String id = databaseProductos.push().getKey();
                                databaseProductos.child(id).setValue(producto);
                                Intent intent = new Intent(getApplicationContext(), Inventario.class);
                                startActivity(intent);

                                Toast.makeText(nuevoItemActivity.this, "Producto Agregado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(nuevoItemActivity.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                producto = new ModeloDatoItem(this, nombre, precio, descripcion, null, "noFile");
                String id = databaseProductos.push().getKey();
                databaseProductos.child(id).setValue(producto);
                Toast.makeText(nuevoItemActivity.this, "Producto Agregado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Inventario.class);
                startActivity(intent);
            }

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PICK_IMAGE:

                    try {
                        imageURI = data.getData();
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                        fotoItemImageView.setImageBitmap(imageBitmap);
                        fotoItemImageView.setScaleX(1);
                        fotoItemImageView.setScaleY(1);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
