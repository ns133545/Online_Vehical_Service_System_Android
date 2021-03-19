package com.example.vehicle_try;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends ArrayAdapter<String>{
    public final String id[];
    public final String servicecentername[];
    public final String phoneno[];
    public final String address[];
    public final String area[];
    public final Context context;
    public MyAdapter(Context context,String id[],String servicecentername[],String phoneno[],String address[],String area[]){
        super(context,R.layout.mylist,id);
        this.context=context;
        this.id=id;
        this.servicecentername=servicecentername;
        this.phoneno=phoneno;
        this.address=address;
        this.area=area;
    }
    @Override
    public View getView(final int position, View convertview, ViewGroup parent){
        if(convertview==null){
            convertview= LayoutInflater.from(getContext()).inflate(R.layout.mylist,parent,false);
        }

        final TextView list_txt1=(TextView)convertview.findViewById(R.id.name);
        final TextView list_txt2=(TextView)convertview.findViewById(R.id.phoneno);
        final TextView list_txt3=(TextView)convertview.findViewById(R.id.address);
        Button btn=convertview.findViewById(R.id.btn);
        Button location=convertview.findViewById(R.id.Location);
        list_txt1.setText(servicecentername[position]);
        list_txt2.setText(phoneno[position]);
        list_txt3.setText(address[position]);
        list_txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list_txt2.getText()));
                context.startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("myKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("service_id", id[position]);
                editor.putString("value", (String) list_txt1.getText());
                editor.apply();
                Intent intent = new Intent(getContext(), BookService.class);
                context.startActivity(intent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity mainActivity=new MainActivity();
//                mainActivity.getMap((String)list_txt3.getText());
                SharedPreferences sharedPref = context.getSharedPreferences("myKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("value", (String)list_txt3.getText());
                editor.apply();


                Intent intent=new Intent(getContext(),MapCall.class);
                context.startActivity(intent);


            }
        });

        return convertview;
    }
}
