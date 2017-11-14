package com.marek.thetowers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.marek.thetowers.model.units.Behemoth;
import com.marek.thetowers.model.units.HardenedTank;
import com.marek.thetowers.model.units.QuadBike;
import com.marek.thetowers.model.units.Tank;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marek on 12/11/2017.
 */

public class Tab1Units extends ListFragment {
    List<UnitEntry> unitList = new ArrayList<>();
    private Context baseContext = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1units, container, false);
        createUnitList();
        UnitInfoAdaper adaper = new UnitInfoAdaper(getActivity(), R.layout.list_unit_entry_layout, unitList);
        setListAdapter(adaper);




        return rootView;
    }

    private void createUnitList(){
        unitList.add(new UnitEntry("Tank", Tank.getHIT_POINTS(), Tank.getPRICE(), Tank.getARMOR().name(), R.drawable.static_tank, "Tank is a basic unit that has average speed and dont cost to much money, but is not so resistant."));
        unitList.add(new UnitEntry("Quad bike", QuadBike.getHIT_POINTS(), QuadBike.getPRICE(), QuadBike.getARMOR().name(), R.drawable.static_quad_bike, "Quad bike is very fast unit, but has very low hp and practically every tower kill this unit fast, especially machine gun."));
        unitList.add(new UnitEntry("Hardened tank", HardenedTank.getHIT_POINTS(), HardenedTank.getPRICE(), HardenedTank.getARMOR().name(), R.drawable.static_hardened_tank, "Hardened tank is very durable but cost a lot of money. His speed is not best, but enemy should have problem to beat it down."));
        unitList.add(new UnitEntry("Behemoth", Behemoth.getHIT_POINTS(), Behemoth.getPRICE(), Behemoth.getARMOR().name(), R.drawable.static_behemoth, "Behemoth is the most durable unit in the game. Unfortunately it cost the most money too. This unit is very slow, so you have to be careful to not block other units "));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        UnitEntry entry = (UnitEntry) l.getAdapter().getItem(position);
        Intent unitDetailIntent = new Intent(baseContext, GameActiveObjectDetailActivity.class);
        unitDetailIntent.putExtra("IMAGE_VALUE", entry.unitImageValue);
        unitDetailIntent.putExtra("DESCRIPTION", entry.description);
        startActivity(unitDetailIntent);

    }

    public void setBaseContext(Context baseContext) {
        this.baseContext = baseContext;
    }
}
