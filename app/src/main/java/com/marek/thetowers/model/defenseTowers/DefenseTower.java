/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.defenseTowers;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.marek.thetowers.GameView;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.Purchasable;
import com.marek.thetowers.model.RotationObject;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PathPartObject;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Unit;

import java.sql.Ref;
import java.util.List;
/**
 *
 * @author marek
 */
public abstract class  DefenseTower extends RotationObject implements Purchasable {
    protected RectF approximationObject;
    public final static float DIM1 = GameUtil.dip2px(24);
    public final static float DIM2 = GameUtil.dip2px(24);
    protected final int radius;
    protected final int price;
    protected final int numberOfCycleToFire;
    protected int rateOfFireCounter;
    protected List<ModelObject> activeObjects;
    protected Unit lockedEnemy = null;
    protected final boolean enemy;
    protected final int damage;
    protected static final int DAMAGE_DIVIDER = 2;
    protected static final int DAMAGE_AMPLIFIER = 2; 
    protected final ArmorType armorCounter;
    
     DefenseTower(PointF position, PointF imageOffset, float direction, int radius, int price, List<ModelObject> activeObjects, int rateOfFire, int windowGapMillis, boolean enemy, final int damage, final ArmorType armorCounter) {
        super(position, imageOffset, direction);
        this.approximationObject = new RectF(position.x - DIM1/2, position.y - DIM2/2, position.x - DIM1/2 + DIM1, position.y - DIM2/2 + DIM2);
        this.radius = radius;
        this.price = price;
        this.activeObjects = activeObjects;
        if(rateOfFire < windowGapMillis){
            rateOfFire = windowGapMillis;
        }
        this.numberOfCycleToFire = rateOfFire / windowGapMillis;
        this.rateOfFireCounter = this.numberOfCycleToFire;
        this.enemy = enemy;
        this.damage = damage;
        this.armorCounter = armorCounter;
    }

    
    @Override
    public abstract void process();

    public RectF getApproximationObject() {
        return approximationObject;
    }

    @Override
    public void setPosition(PointF position) {
        super.setPosition(position);
        this.approximationObject = new RectF(position.x - DIM1/2, position.y - DIM2/2, position.x - DIM1/2 + DIM1,  position.y - DIM2/2 + DIM2);
    }

    @Override
    public int getPrice() {
        return price;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public ArmorType getArmorCounter() {
        return armorCounter;
    }

    public static synchronized boolean validatePossition(PointF position, RectF towerPart, List<ModelObject> path, List<ModelObject> activeObjects) {
        RectF towerAproxObject = new RectF(position.x - DefenseTower.DIM1 / 2, position.y - DefenseTower.DIM2 / 2, position.x - DefenseTower.DIM1 / 2 + DefenseTower.DIM1, position.y - DefenseTower.DIM2 / 2 + DefenseTower.DIM2);

        if (!towerPart.contains(towerAproxObject)){
            return false;
        }

        for (ModelObject item : path) {
            PathPartObject pathPart = (PathPartObject) item;
            if (RectF.intersects(pathPart.getApproximationObject(), towerAproxObject)) {
                return false;
            }
        }

        for (ModelObject item : activeObjects) {
            if (item instanceof DefenseTower) {
                DefenseTower tower = (DefenseTower) item;
                if (tower.isEnemy() && RectF.intersects(tower.getApproximationObject(), towerAproxObject)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setApproximationObject(RectF approximationObject) {
        this.approximationObject = approximationObject;
    }

    public void setActiveObjects(List<ModelObject> activeObjects) {
        this.activeObjects = activeObjects;
    }
}
