package com.example.labfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ModeloDatoItem {

    private String nombre;
    private String precio;
    private String descripcion;
    private Bitmap foto;
    private Context context;
    private String fotoUrl;
    private String key;

    public ModeloDatoItem(Context context, String nombre, String precio, String descripcion, Bitmap foto, String fotoUrl) {
        this.context = context;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
        if (foto != null)
            this.foto = foto;
        else
            this.foto = BitmapFactory.decodeResource(context.getResources(), R.drawable.producto_default);
    }

    public ModeloDatoItem(boolean modo, String key ,Context context, String nombre, String precio, String descripcion, Bitmap foto, String fotoUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.context = context;

        if (fotoUrl.equals("noFile")){
            this.foto = BitmapFactory.decodeResource(context.getResources(), R.drawable.producto_default);
        } else {
            ImageDownloader imageDownloader = new ImageDownloader();
            try {
                this.foto = imageDownloader.execute(fotoUrl).get();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        this.key = key;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getFoto() {
        return foto;
    }


    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {


            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
