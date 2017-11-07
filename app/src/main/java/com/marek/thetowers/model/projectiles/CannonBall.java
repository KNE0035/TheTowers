/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.projectiles;

import android.graphics.PointF;

import com.marek.thetowers.R;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Unit;

/**
 *
 * @author marek
 */
public class CannonBall extends Projectile{
    private static final int SPEED = 10;
    private static final ArmorType ARMOR_COUNTER = ArmorType.MEDIUM;
    private static final int VIEW_IDENTIFIKATOR = R.drawable.cannon_ball;
    
    public CannonBall(PointF position, PointF imageOffset, Unit lockedEnemy, int damage) {
        super(position, imageOffset, 0, SPEED, lockedEnemy, damage, ARMOR_COUNTER);
    }
    

    public static int getSPEED() {
        return SPEED;
    }

    @Override
    public int getViewIdentifikator() {
        return VIEW_IDENTIFIKATOR;
    }
    
    
}
