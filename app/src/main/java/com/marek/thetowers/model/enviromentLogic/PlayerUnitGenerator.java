/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.enviromentLogic;

import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.units.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída starající se o generování hráčových zvolených jednotek
 * @author marek
 */
public class PlayerUnitGenerator {
    private List<ModelObject> activeObjects = new ArrayList<>();
    private Unit unitToPop;
    
    public PlayerUnitGenerator(List<ModelObject> activeObjects) {
        this.activeObjects = activeObjects;
    }
    
    /*pokusí se vygenerovat jednotku .... pokud jednotce bude na začátku cesty bránit jiná jednotka, nevygeneruje se*/
    public boolean popUnit(){
        Unit lastUnit = null;
        for(int i = activeObjects.size() - 1; i >= 0; i--){
            if(activeObjects.get(i) instanceof Unit){
                if(!((Unit)activeObjects.get(i)).isEnemy()){
                    lastUnit = (Unit)activeObjects.get(i);
                    break;
                }
            }
        }
        
        if(unitToPop == null || (lastUnit != null && Math.sqrt(Math.pow(lastUnit.getPosition().x - this.unitToPop.getPosition().x, 2) + Math.pow(lastUnit.getPosition().y - this.unitToPop.getPosition().y, 2)) <= GameUtil.dip2px(50))){
            return false;
        } else {
            this.generateUnit();
        }  
        return true;
    } 
    
    private synchronized void generateUnit(){
        activeObjects.add(unitToPop);
        this.unitToPop = null;
    }

    public void setUnitToPop(Unit unitToPop) {
        this.unitToPop = unitToPop;
    }

    public Unit getUnitToPop() {
        return unitToPop;
    }

    public boolean isReady() {
        return this.unitToPop == null;
    }
}
