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
public class Cannon extends DefenseTowerWithProjectile{
    private static final int RADIUS = 200;
    private static final int PRICE = 50;
    private static final int RATE_OF_FIRE = 500;
    private static final ProjectileType PROJECTILE_TYPE = ProjectileType.Cannon_Ball;
    private static final int VIEW_IDENTIFIKATOR = R.drawable.cannon;
    public static final int DAMAGE = 3;
    private static final ArmorType ARMOR_COUNTER = ArmorType.MEDIUM;
    
    public Cannon(PointF position, PointF imageOffset, float direction, List<ModelObject> activeObjects, int windowGapMillis, List<Projectile> projectiles, boolean enemy) {
        super(position, imageOffset, direction, RADIUS, PRICE, activeObjects, RATE_OF_FIRE, windowGapMillis, PROJECTILE_TYPE, projectiles, enemy, DAMAGE, ARMOR_COUNTER);
    }


    @Override
    public int getViewIdentifikator() {
        return VIEW_IDENTIFIKATOR;
    }

    public static int getPRICE() {
        return PRICE;
    }

    public static ArmorType getARMOR_COUNTER() {
        return ARMOR_COUNTER;
    }
}
