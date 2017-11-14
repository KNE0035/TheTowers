package com.marek.thetowers;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Marek on 12/11/2017.
 */

public class UnitInfoAdaper extends ArrayAdapter<UnitEntry>{
    Context context;
    int layoutResourceId;
    List<UnitEntry> data = null;

    public UnitInfoAdaper(Context context, int layoutResourceId, List<UnitEntry> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        EntryHolder holder = null;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EntryHolder();
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtArmorType = (TextView)row.findViewById(R.id.txtArmorType);
            holder.txtHp = (TextView)row.findViewById(R.id.txtHp);
            holder.txtPrice = (TextView)row.findViewById(R.id.txtPrice);
            holder.unitImageValue = (ImageView) row.findViewById(R.id.unitImage);

            row.setTag(holder);
        } else {
            holder = (EntryHolder)row.getTag();
        }

            UnitEntry entry = data.get(position);
            holder.txtName.setText("Name: " + entry.name);
            holder.txtArmorType.setText("Armor: " + entry.armorType);
            holder.txtHp.setText("Hit points: " + Integer.toString(entry.hp));
            holder.txtPrice.setText("Price: " + Integer.toString(entry.price) + " $");
            holder.unitImageValue.setImageResource(entry.unitImageValue);
            return row;
    }

    static class EntryHolder{
        TextView txtName;
        TextView txtHp;
        TextView txtArmorType;
        TextView txtPrice;
        ImageView unitImageValue;
    }
}
