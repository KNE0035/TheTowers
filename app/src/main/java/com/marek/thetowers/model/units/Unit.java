/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.units;

import android.graphics.PointF;

import com.marek.thetowers.GameView;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.MovableObject;
import com.marek.thetowers.model.Orientation;
import com.marek.thetowers.model.Purchasable;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PathPartObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída reprezentující jednotku
 * @author marek
 */
public abstract class Unit extends MovableObject implements Purchasable {
    private static PointF START_POSSITION;
    private List<ModelObject> path = new ArrayList<>();
    private int nONarrowPixels;
    private Orientation currOrientation;
    private int actualHitPoints;
    private int maxHitPoints;
    private final boolean enemy;
    private final ArmorType armor;
    private final int price;
    private final List<ModelObject> activeObjests;
    private int instantSpeed;
    
    /**
     *
     * @param speed rychlost jednotky
     * @param path cesta po které se má jednotka pohybovat
     * @param initDirection počáteční směr jednotky
     * @param hitpoints zdraví jednotky
     * @param enemy flag jestli se jedná o nepřátelskou jednotku
     * @param armor typ armoru jednotky
     * @param price cena jednotky
     * @param activeObjests list všech aktivních objektů ve hře (věže, jednotky)
     */
    public Unit(PointF imageOffset, int speed, List<ModelObject> path, float initDirection, int hitpoints, boolean enemy, ArmorType armor, int price, List<ModelObject> activeObjests) {
        super(new PointF(0, 0), imageOffset, initDirection, speed);
        this.path = path;
        if(((PathPartObject)path.get(0)).getOrientation() == Orientation.EAST){
            START_POSSITION = new PointF(path.get(0).getX(), path.get(0).getY() + PathPartObject.DIM2 / 2);
        } else if(((PathPartObject)path.get(0)).getOrientation() == Orientation.WEST) {
            START_POSSITION = new PointF(path.get(0).getX() + PathPartObject.DIM1 - 1, path.get(0).getY() + PathPartObject.DIM2 / 2);
        }
        this.setPosition(START_POSSITION);
        this.nONarrowPixels = 0;
        this.actualHitPoints = hitpoints;
        this.maxHitPoints = hitpoints;
        this.enemy = enemy;
        this.armor = armor;
        this.price = price;
        this.activeObjests = activeObjests;
        this.instantSpeed = speed;
    }

    /**
     * pohne s jednotkou o další krok (krok = její rychlost) na její cestě (podle cesty vypočítává další směr, kudy má udělat krok), pokud je bezprostředně před jednotkou
     * nějaké další jednotka, původní jednotka jí nepředjede ale pojedu stejnou rychlosti jako jednotka před ní.
     */
    @Override
    public void process() {
        PathPartObject currentPathPart = null;
        boolean isAddPixels = false;
        boolean isFirst = true;
        if(this.nONarrowPixels <= 0){
            for(ModelObject pathObject: this.path){
                if(((PathPartObject)pathObject).getApproximationObject().contains(this.getPosition().x, this.getPosition().y)){
                    currentPathPart = ((PathPartObject)pathObject);
                    this.currOrientation = currentPathPart.getOrientation();
                    isAddPixels = true;
                    if(path.indexOf(currentPathPart) != 0){
                        isFirst = false;
                    }
                }
                if(isAddPixels){
                    if(((PathPartObject)pathObject).getOrientation() != this.currOrientation){
                        if(isFirst){
                            this.nONarrowPixels += PathPartObject.DIM2 / 2;
                        }
                        break;
                    }
                    this.nONarrowPixels += PathPartObject.DIM1;
                }
            }
        }
        
        this.instantSpeed = this.getSpeed();

        for(int i = this.activeObjests.indexOf(this) - 1; i >= 0; i--){
            if(this.isEnemy()){
                if(this.activeObjests.get(i) instanceof Unit && ((Unit)this.activeObjests.get(i)).isEnemy()){
                    if(Math.sqrt(Math.pow(this.activeObjests.get(i).getPosition().x - this.getPosition().x, 2) + Math.pow(this.activeObjests.get(i).getPosition().y - this.getPosition().y, 2)) <= GameUtil.dip2px(50)){
                        this.instantSpeed = ((Unit)this.activeObjests.get(i)).instantSpeed;
                    }
                    break;
                }
            } else {
                if(this.activeObjests.get(i) instanceof Unit && !((Unit)this.activeObjests.get(i)).isEnemy()){
                    if(Math.sqrt(Math.pow(this.activeObjests.get(i).getPosition().x - this.getPosition().x, 2) + Math.pow(this.activeObjests.get(i).getPosition().y - this.getPosition().y, 2)) <= GameUtil.dip2px(50)){
                        this.instantSpeed = ((Unit)this.activeObjests.get(i)).instantSpeed;
                    }
                    break;
                }
            }
        }

        moveWithOrientation(this.currOrientation, Math.min(this.getSpeed(), this.instantSpeed));
        this.nONarrowPixels -= Math.min(this.getSpeed(), this.instantSpeed);
    }
        
    public static PointF getStartPosition() {
        return START_POSSITION;
    }

    /*pohne jednotkou ve směru zadané orientace se zadaným krokem (parametr speed)*/
    private void moveWithOrientation(Orientation orientation, int speed){
        switch(orientation){
            case NORTH:
                this.move(0, -speed);
                this.setDirection(270);
                break;
            case WEST:
                this.move(-speed, 0);
                this.setDirection(180);
                break;
            case SOUTH:
                this.move(0 ,speed);
                this.setDirection(90);
                break;
            case EAST:
                this.move(speed , 0);
                this.setDirection(0);
                break;
        } 
    }
    
    /**
     * Zkontroluje jestli je jednotka ještě na hracím plátně, pokud ne vrátí true;
     */
    @Override
    public boolean isOutOfSpace() {
        if (this.getPosition().x< 0 || this.getPosition().x - 30 > GameView.WIDTH) {
            return true;
        }
        return this.getPosition().y < 0 || this.getPosition().y - 30 > GameView.HEIGHT;
    }
    
    public void minusHitpoins(int hitPoints){
        this.actualHitPoints -= hitPoints;
    }

    public int getActualHitPoints() {
        return actualHitPoints;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public ArmorType getArmor() {
        return armor;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }
}

   
    
    
