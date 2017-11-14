package com.marek.thetowers;

import android.content.Context;
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

public class TowerInfoAdaper extends ArrayAdapter<TowerEntry>{
    Context context;
    int layoutResourceId;
    List<TowerEntry> data = null;

    public TowerInfoAdaper(Context context, int layoutResourceId, List<TowerEntry> data) {
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
            holder.txtArmorCounterType = (TextView)row.findViewById(R.id.txtArmorCounterType);
            holder.txtDamage = (TextView)row.findViewById(R.id.txtDamage);
            holder.txtPrice = (TextView)row.findViewById(R.id.txtPrice);
            holder.defenseTowerFlag = (ImageView) row.findViewById(R.id.defenseTowerFlag);

            row.setTag(holder);
        } else {
            holder = (EntryHolder)row.getTag();
        }

            TowerEntry entry = data.get(position);
            holder.txtName.setText("Name: " + entry.name);
            holder.txtArmorCounterType.setText("Armor counter: " + entry.armorType);
            holder.txtDamage.setText("Damage: " + Integer.toString(entry.damage));
            holder.txtPrice.setText("Price: " + Integer.toString(entry.price) + " $");
            holder.defenseTowerFlag.setImageResource(entry.towerImageValue);
            return row;
    }

    static class EntryHolder{
        TextView txtName;
        TextView txtDamage;
        TextView txtArmorCounterType;
        TextView txtPrice;
        ImageView defenseTowerFlag;
    }
}
