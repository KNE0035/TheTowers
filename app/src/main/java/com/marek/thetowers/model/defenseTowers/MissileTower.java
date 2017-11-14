/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.defenseTowers;

import android.graphics.PointF;

import com.marek.thetowers.R;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.projectiles.Projectile;
import com.marek.thetowers.model.projectiles.ProjectileType;
import com.marek.thetowers.model.units.ArmorType;

import java.util.List;

/**
 *
 * @author marek
 */
public class MissileTower extends DefenseTowerWithProjectile{
    private static final int RADIUS = 350;
    private static final int PRICE = 100;
    private static final int RATE_OF_FIRE = 750;
    private static final ProjectileType PROJECTILE_TYPE = ProjectileType.Missile;
    private static final int VIEW_IDENTIFIKATOR = R.drawable.missile_tower;
    private static final int DAMAGE = 8;
    private static final ArmorType ARMOR_COUNTER = ArmorType.HARDENED;
    
    public MissileTower(PointF position, PointF imageOffset, float direction, List<ModelObject> activeObjects, int windowGapMillis, List<Projectile> projectiles, boolean enemy) {
        super(position, imageOffset, direction, RADIUS, PRICE, activeObjects, RATE_OF_FIRE, windowGapMillis, PROJECTILE_TYPE, projectiles, enemy, DAMAGE, ARMOR_COUNTER);
    }

    @Override
    public int getViewIdentifikator() {
        return VIEW_IDENTIFIKATOR;
    }
    
    public static int getPRICE() {
        return PRICE;
    }

    public static int getDAMAGE() {
        return DAMAGE;
    }

    public static ArmorType getARMOR_COUNTER() {
        return ARMOR_COUNTER;
    }
}
