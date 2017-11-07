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
import com.marek.thetowers.model.units.Unit;

import java.util.List;

/**
 *
 * @author marek
 */
public abstract class DefenseTowerWithoutProjectile extends DefenseTower{

    public DefenseTowerWithoutProjectile(PointF position, PointF imageOffset, float direction, int radius, int price, List<ModelObject> activeObjects, int rateOfFireMillis, int windowGapMillis, boolean enemy, int damage, ArmorType armorCounter) {
        super(position, imageOffset, direction, radius, price, activeObjects, rateOfFireMillis, windowGapMillis, enemy, damage, armorCounter);
    }

     /**
     * Zkontroluje, zda má na dostřel nějakou nepřátelskou jednotku, pokud ano lockne ji a začne jí ubírat životy v intervalech podle kadence věže.
     */
    @Override
    public void process() {
        if(lockedEnemy == null || lockedEnemy.isOutOfSpace() || lockedEnemy.getHitPoints() <= 0){
            this.toggleViewState(R.drawable.machine_gun_unfire);
            for(ModelObject item: this.activeObjects){
                if(item instanceof Unit){
                    if((!((Unit)item).isEnemy() && !this.enemy) || (((Unit)item).isEnemy() && this.enemy)){
                        continue;
                    }
                    if(Math.sqrt(Math.pow(item.getPosition().x - this.getPosition().x, 2) + Math.pow(item.getPosition().y - this.getPosition().y, 2)) <= this.radius){
                        this.lockedEnemy = (Unit)item;
                        this.setDirectionToPoint(item.getPosition());
                        break;
                    }
                }
            }
        } else {
            if(Math.sqrt(Math.pow(this.lockedEnemy.getPosition().x - this.getPosition().x, 2) + Math.pow(this.lockedEnemy.getPosition().y - this.getPosition().y, 2)) <= this.radius){
                this.setDirectionToPoint(this.lockedEnemy.getPosition());
                
                if(this.rateOfFireCounter == this.numberOfCycleToFire){
                    if(this.armorCounter == this.lockedEnemy.getArmor()){
                        this.lockedEnemy.minusHitpoins(this.damage * DAMAGE_AMPLIFIER);
                    } else if(this.armorCounter.getValue() > this.lockedEnemy.getArmor().getValue()){
                        this.lockedEnemy.minusHitpoins(this.damage);
                    } else {
                        this.lockedEnemy.minusHitpoins(this.damage / DAMAGE_DIVIDER);
                    }
                    this.rateOfFireCounter = 0;
                    this.toggleViewState(null);
                }
            } else {
                this.lockedEnemy = null;
                this.rateOfFireCounter = this.numberOfCycleToFire;
            }
        }
        if(this.rateOfFireCounter != this.numberOfCycleToFire){
            this.rateOfFireCounter++;
        }
    }

    @Override
    public abstract int getViewIdentifikator();
    
    public abstract void toggleViewState(Integer concreteStateView);
}
