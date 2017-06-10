package com.grupoactive.bi.rosellcardus.gerard.tfg_bi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gerard on 17/05/17.
 */

public class EntryAdapter extends ArrayAdapter<Entry> {
    public EntryAdapter(Context context, ArrayList<Entry> entries){
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Entry entry =getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.activity_hours_list_item, parent, false);
        }

        TextView IDProj = (TextView) convertView.findViewById(R.id.IDProj);
        TextView IDTasca = (TextView) convertView.findViewById(R.id.IDTasca);
        TextView NumH = (TextView) convertView.findViewById(R.id.NumH);

        IDProj.setText(entry.IDJob);
        IDTasca.setText(entry.IDTask);
        String H = String.valueOf(entry.Hours);
        NumH.setText(H);

        return convertView;
    }

}
