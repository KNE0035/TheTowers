/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.projectiles;


import android.graphics.PointF;

import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.MovableObject;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Unit;

/**
 * Třída reprezentující projektil letící na jednotku
 * @author marek
 */
public abstract class Projectile extends MovableObject {

    private final Unit lockedEnemy;
    private boolean targetHit;
    private final int damage;
    private static final int DAMAGE_DIVIDER = 2;
    private static final int DAMAGE_AMPLIFIER = 2; 
    private final ArmorType armorCounter;
    
    /**
     *
     * @param position počační pozice
     * @param speed rychlost projectilu
     * @param lockedEnemy jednotka na kterou projectil letí
     * @param damage poškození projectilu
     * @param armorCounter armor který projectil countruje
     */
    public Projectile(PointF position, PointF imageOffset, float direction, int speed, Unit lockedEnemy, int damage, ArmorType armorCounter) {
        super(position, imageOffset, direction, speed);
        this.lockedEnemy = lockedEnemy;
        this.targetHit = false;
        this.damage = damage;
        this.armorCounter = armorCounter;
    }
    
    /**
     * Metoda která aktualizuje směr projektilu na cíl a posune ho blíže k cíli.
     */
    @Override
    public void process() {
        PointF toPoint = this.lockedEnemy.getPosition();
        PointF speedVector = new PointF(toPoint.x - this.getPosition().x, toPoint.y - this.getPosition().y);
        speedVector = GameUtil.normalizePoint(speedVector);
        speedVector =  new PointF(speedVector.x * this.getSpeed(), speedVector.y * this.getSpeed());
        move(speedVector.x, speedVector.y);
        this.setDirectionToPoint(this.lockedEnemy.getPosition());
        if(Math.sqrt(Math.pow(this.lockedEnemy.getPosition().x - this.getPosition().x, 2) + Math.pow(this.lockedEnemy.getPosition().y - this.getPosition().y, 2)) <= 5){
            targetHit = true;
            if(this.armorCounter == this.lockedEnemy.getArmor()){
                this.lockedEnemy.minusHitpoins(this.getDamage() * DAMAGE_AMPLIFIER);
            } else if(this.armorCounter.getValue() > this.lockedEnemy.getArmor().getValue()){
                this.lockedEnemy.minusHitpoins(this.getDamage());
            } else {
                this.lockedEnemy.minusHitpoins(this.getDamage() / DAMAGE_DIVIDER);
            }
        }
    }

    public boolean isTargetHit() {
        return targetHit;
    }

    public Unit getLockedEnemy() {
        return lockedEnemy;
    }

    public int getDamage() {
        return damage;
    }
}
