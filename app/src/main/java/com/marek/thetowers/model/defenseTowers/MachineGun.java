/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.defenseTowers;

import android.graphics.PointF;

import com.marek.thetowers.R;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.units.ArmorType;

import java.util.List;

/**
 *
 * @author marek
 */
public class MachineGun extends DefenseTowerWithoutProjectile{
    private static final int RADIUS = 200;
    private static final int PRICE = 30;
    private static final int DAMAGE = 1;
    private static final int RATE_OF_FIRE = 10;
    private int viewIdentifikator = R.drawable.machine_gun_unfire;
    private static final ArmorType ARMOR_COUNTER = ArmorType.LOW;
    
    public MachineGun(PointF position, PointF imageOffset, float direction, List<ModelObject> activeObjects, int windowGapMillis, boolean enemy) {
        super(position, imageOffset, direction, RADIUS, PRICE, activeObjects, RATE_OF_FIRE, windowGapMillis, enemy, DAMAGE, ARMOR_COUNTER);
    }

    @Override
    public int getViewIdentifikator() {
        return viewIdentifikator;
    }

    public static int getDAMAGE() {
        return DAMAGE;
    }

     /**
     * Přehodí identifikátor obrázku který má věž představovat (slouží pro animaci střelby).
     * Pokud concreteState není null nastaví identifikátor podle vstupního paramtru.
     */
    @Override
    public void toggleViewState(Integer concreteStateView) {
        if(concreteStateView != null){
            this.viewIdentifikator = concreteStateView;
            return;
        }

        if(R.drawable.machine_gun_unfire == viewIdentifikator){
            viewIdentifikator = R.drawable.machine_gun_fire;
        } else {
            viewIdentifikator = R.drawable.machine_gun_unfire;
        }
    }
    
    public static int getPRICE() {
        return PRICE;
    }

    public static ArmorType getARMOR_COUNTER() {
        return ARMOR_COUNTER;
    }
}
