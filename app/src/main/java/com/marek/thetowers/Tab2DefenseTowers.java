package com.marek.thetowers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.marek.thetowers.model.defenseTowers.Cannon;
import com.marek.thetowers.model.defenseTowers.MachineGun;
import com.marek.thetowers.model.defenseTowers.MissileTower;
import com.marek.thetowers.model.defenseTowers.PhotonCannon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek on 12/11/2017.
 */

public class Tab2DefenseTowers extends ListFragment {

    private List<TowerEntry> towerList = new ArrayList<>();
    private Context baseContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1units, container, false);
        createTowerList();
        TowerInfoAdaper adaper = new TowerInfoAdaper(getActivity(), R.layout.list_tower_entry_layout, towerList);
        setListAdapter(adaper);

        return rootView;
    }

    private void createTowerList(){
        towerList.add(new TowerEntry("Cannon", Cannon.DAMAGE, Cannon.getPRICE(), Cannon.getARMOR_COUNTER().name(), R.drawable.static_cannon, "Cannon is basic tower, that has average rate of fire and damage. It is very good against medium type armor, but dont build it against hardened or heavy armor."));
        towerList.add(new TowerEntry("Machine gun", MachineGun.getDAMAGE(), MachineGun.getPRICE(), MachineGun.getARMOR_COUNTER().name(), R.drawable.static_machine_gun, "Machine gun is very specific tower. This tower is designed to shot down low armor really fast with its fast rate of fire, but on on other types of armor it is not effective a do zero damage."));
        towerList.add(new TowerEntry("Missle tower", MissileTower.getDAMAGE(), MissileTower.getPRICE(), MissileTower.getARMOR_COUNTER().name(), R.drawable.static_missile_tower, "Missile tower is very good against hardened armor. Tower has the bigest range in the game, so dont build it next to the road. This tower has very good damage too."));
        towerList.add(new TowerEntry("Photon cannon", PhotonCannon.getDAMAGE(), PhotonCannon.getPRICE(), PhotonCannon.getARMOR_COUNTER().name(), R.drawable.static_photon_cannon, "Photon cannon is the most powerful tower in the game. This tower has best damage and counters Heavy armor, so for example behemoth will not have chance against this tower. This tower is the most expensive tower in the game"));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TowerEntry entry = (TowerEntry) l.getAdapter().getItem(position);
        Intent towerDetailIntent = new Intent(baseContext, GameActiveObjectDetailActivity.class);
        towerDetailIntent.putExtra("IMAGE_VALUE", entry.towerImageValue);
        towerDetailIntent.putExtra("DESCRIPTION", entry.description);
        startActivity(towerDetailIntent);
    }

    public void setBaseContext(Context baseContext) {
        this.baseContext = baseContext;
    }
}
