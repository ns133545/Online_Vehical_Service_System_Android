package com.example.vehicle_try;

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

import static android.content.Context.MODE_PRIVATE;

public class HistoryAdapter extends ArrayAdapter<String> {
    public final String servicecentername[];
    public final String phoneno[];
    public final String date[];

    public final Context context;

    public HistoryAdapter(Context context, String servicecentername[], String phoneno[], String date[]) {
        super(context, R.layout.historylist, servicecentername);
        this.context = context;
        this.servicecentername = servicecentername;
        this.phoneno = phoneno;
        this.date = date;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {
        if (convertview == null) {
            convertview = LayoutInflater.from(getContext()).inflate(R.layout.historylist, parent, false);
        }

        final TextView list_txt1 = (TextView) convertview.findViewById(R.id.H_Name);
        final TextView list_txt2 = (TextView) convertview.findViewById(R.id.H_phoneno);
        final TextView list_txt3 = (TextView) convertview.findViewById(R.id.DateofBooking);

        list_txt1.setText(servicecentername[position]);
        list_txt2.setText(phoneno[position]);
        list_txt3.setText(date[position]);
        list_txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list_txt2.getText()));
                context.startActivity(intent);
            }
        });

        return convertview;
    }
}
