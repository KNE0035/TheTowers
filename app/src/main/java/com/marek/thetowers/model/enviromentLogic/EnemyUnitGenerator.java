/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.enviromentLogic;

import android.graphics.PointF;

import com.marek.thetowers.model.Enemy;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.defenseTowers.DefenseTower;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Behemoth;
import com.marek.thetowers.model.units.HardenedTank;
import com.marek.thetowers.model.units.QuadBike;
import com.marek.thetowers.model.units.Tank;
import com.marek.thetowers.model.units.Unit;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Třída pro generování jednotek nepřítele.
 * @author marek
 */
public class EnemyUnitGenerator {
    private final List<ModelObject> activeObjects;
    private final List<ModelObject> path;
    private final Random randomGenerator = new Random();
    private final Enemy enemy;
    
    
    public EnemyUnitGenerator(List<ModelObject> activeObjects, List<ModelObject> path, Enemy enemy) {
        this.activeObjects = activeObjects;
        this.path = path;
        this.enemy = enemy;
    }
    
    
    /**
     * Zkusí vygenrovat další nepřátelskou jednotku (vygeneruje jí pokud má dost peněz a má místo pro vygenerování jednotky na trase)
     */
    public boolean tryGenerateEnemy(){
        Unit generatedEnemyUnit = null;
        
        //money resolving
        if(this.enemy.getCash() > 500){
            if(tryChance(50)){
                generatedEnemyUnit = generateEnemy();
            } else {
                return false;
            }
        } else if(this.enemy.getCash() > 400){
            if(tryChance(30)){
                generatedEnemyUnit = generateEnemy();
            } else {
                return false;
            }
        } else if(this.enemy.getCash() > 300){
            if(tryChance(15)){
                generatedEnemyUnit = generateEnemy();
            } else {
                return false;
            }
        } else if(this.enemy.getCash() > 200){
            if(tryChance(10)){
                generatedEnemyUnit = generateEnemy();
            } else {
                return false;
            }
        } else if(this.enemy.getCash() < 200){
            return  false;
        }
        
        Unit lastUnit = null;
        for(int i = activeObjects.size() - 1; i >= 0; i--){
            if(activeObjects.get(i) instanceof Unit){
                if(((Unit)activeObjects.get(i)).isEnemy()){
                    lastUnit = (Unit)activeObjects.get(i);
                    break;
                }
            }
        }
        if(generatedEnemyUnit == null || (lastUnit != null && Math.sqrt(Math.pow(lastUnit.getPosition().x - generatedEnemyUnit.getPosition().x, 2) + Math.pow(lastUnit.getPosition().y - generatedEnemyUnit.getPosition().y, 2)) <= 100)){
            return false;
        }
            
        if(this.enemy.minusCash(generatedEnemyUnit.getPrice())){
            activeObjects.add(generatedEnemyUnit);
            return true;
        } else {
            return false;
        }
       
    }
    
    /*Vygeneruje nepřítele podle algoritmu*/
    private synchronized Unit generateEnemy(){
        Map<ArmorType, Integer> towerTypeOccurenceValues = new HashMap<>();
        Unit unitTogenerate = null;
        
        for(ModelObject object: this.activeObjects){
            if(object instanceof DefenseTower){
                DefenseTower tower = (DefenseTower)object;
                if(!towerTypeOccurenceValues.containsKey(tower.getArmorCounter())){
                    towerTypeOccurenceValues.put(tower.getArmorCounter(), 1);
                }
                else {
                    Integer count = towerTypeOccurenceValues.get(tower.getArmorCounter());
                    towerTypeOccurenceValues.put(tower.getArmorCounter(), count + 1);
                }
            }
        }

        if(towerTypeOccurenceValues.isEmpty()){
            unitTogenerate = generateEnemyByChances(25,25,25,25);
            return unitTogenerate;
        }

       if(isRemainingQuantityOfTowerTypesEqual(towerTypeOccurenceValues)){
            unitTogenerate = generateEnemyByChances(25,25,25,25);
            return unitTogenerate;
       }

        ArmorType keyOfMaxValue = Collections.max(towerTypeOccurenceValues.entrySet(), new Comparator<Entry<ArmorType, Integer>>() {
            @Override
            public int compare(Map.Entry<ArmorType, Integer> entry1, Map.Entry<ArmorType, Integer> entry2) {
                return entry1.getValue() - entry2.getValue();
            }
        }).getKey();

        ArmorType keyOfSecondNextValue = Collections.max(towerTypeOccurenceValues.entrySet(), new Comparator<Entry<ArmorType, Integer>>() {
            @Override
            public int compare(Map.Entry<ArmorType, Integer> entry1, Map.Entry<ArmorType, Integer> entry2) {
                return entry1.getValue() - entry2.getValue();
            }
        }).getKey();

        int armorTypeValue = keyOfMaxValue.getValue() + keyOfSecondNextValue.getValue();
        switch (armorTypeValue) {
            case 7:
            case 8:
                unitTogenerate = generateEnemyByChances(60,20,10,10);
                break;
            case 1:
            case 2:
            case 3:
                unitTogenerate = generateEnemyByChances(10,10,20,60);
                break;
            case 4:
                unitTogenerate = generateEnemyByChances(10,40,40,10);
                break;
            case 5:
            case 6:
                unitTogenerate = generateEnemyByChances(20,60,10,10);
                break;
            default:
                break;
        }
        return unitTogenerate;
    }
    
    /*Vrátí true pokud počet jednotlivých armor counterů (součet věží countrující stejný armor) bude totožný*/
    private boolean isRemainingQuantityOfTowerTypesEqual(Map<ArmorType, Integer> towerTypeOccurenceValues){
        if(towerTypeOccurenceValues.size() < 3){
            return false;
        }
        
        int lastValue = -1;
        for (Entry<ArmorType, Integer> entry : towerTypeOccurenceValues.entrySet()) {  // Itrate through hashmap
            if(lastValue == -1){
                lastValue = entry.getValue();
            } else if(lastValue != entry.getValue()){
                return false;
            }
        }
        return true;
    }
    
    //ordering indexes in chances array is quadBike - 0, tank - 1, hardenedTank - 2, behemoth - 3
    /*Rozhodne podle šancí na jednotlivé typy jednotek, který typ vygeneruje*/
    private Unit generateEnemyByChances(int...chances){
        int nOUnitTypesInGame = 4;
        if(chances.length != nOUnitTypesInGame){
            return null;
        }
        int randomNumber0To100 = randomGenerator.nextInt(100);
        int nONewUnit = -1;
        int chanceAddingCounter = 0;
        for(int i = 0; i < chances.length; i++){
            if(randomNumber0To100 < chances[i] + chanceAddingCounter){
                nONewUnit = i;
                break;
            } else {
                chanceAddingCounter += chances[i];
            }
        }
        Unit unitTogenerate = null;
        unitTogenerate = new Tank(new PointF(GameUtil.dip2px(15), 0), this.path, 0, true, activeObjects);
        switch(nONewUnit){
            case 0:
                unitTogenerate = new QuadBike(new PointF(GameUtil.dip2px(15), 0), this.path, 0, true, activeObjects);
                break;
            case 1:
                unitTogenerate = new Tank(new PointF(GameUtil.dip2px(15), 0), this.path, 0, true, activeObjects);
                break;
            case 2:
                unitTogenerate = new HardenedTank(new PointF(GameUtil.dip2px(15), 0), this.path, 0, true, activeObjects);
                break;
            case 3:
                unitTogenerate = new Behemoth(new PointF(GameUtil.dip2px(15), 0), this.path, 0, true, activeObjects);
                break;
        }
        return unitTogenerate;
    }
    
    private boolean tryChance(int chance){
        int randomNum = randomGenerator.nextInt(100);
        return randomNum < chance;
    }
}
