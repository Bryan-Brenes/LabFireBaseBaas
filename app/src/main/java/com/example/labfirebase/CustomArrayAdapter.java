package com.example.labfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<ModeloDatoItem> {
    ArrayList<ModeloDatoItem> listaItems;
    Context context;

    private static class ViewHolder{
        ImageView fotoItem;
        TextView nombreItem;
        TextView precioItem;
        TextView descripcionItem;
    }

    public CustomArrayAdapter(Context context, ArrayList<ModeloDatoItem> elementos){
        super(context, R.layout.item_layout, elementos);
        this.listaItems = elementos;
        this.context = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ModeloDatoItem dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomArrayAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomArrayAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
            viewHolder.nombreItem = (TextView) convertView.findViewById(R.id.nombreItem);
            viewHolder.precioItem = (TextView) convertView.findViewById(R.id.precioItem);
            viewHolder.descripcionItem = (TextView) convertView.findViewById(R.id.descripcionItem);
            viewHolder.fotoItem = (ImageView) convertView.findViewById(R.id.fotoItem);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomArrayAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.nombreItem.setText(dataModel.getNombre());
        viewHolder.nombreItem.setTextColor(Color.BLACK);
        viewHolder.precioItem.setText("₡ " + dataModel.getPrecio());
        viewHolder.precioItem.setTextColor(Color.BLACK);
        viewHolder.descripcionItem.setText(dataModel.getDescripcion());
        viewHolder.descripcionItem.setTextColor(Color.BLACK);
        viewHolder.fotoItem.setImageBitmap(dataModel.getFoto());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /*private Bitmap convertirBitmap(String str){
        ArrayList<Byte> bytes = new ArrayList<>();
        String[] byteArrayStr = str.split(" ");
        for(String byteStr : byteArrayStr){
            bytes.add(Byte.decode(byteStr));
        }
        Byte[] byteArray = (Byte[]) bytes.toArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }*/
}
