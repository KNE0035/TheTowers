package com.marek.thetowers.model.enviromentLogic;

import android.graphics.PointF;
import android.graphics.RectF;

import com.marek.thetowers.model.DirectionImageOptions;
import com.marek.thetowers.model.Enemy;
import com.marek.thetowers.model.defenseTowers.Cannon;
import com.marek.thetowers.model.defenseTowers.DefenseTower;
import com.marek.thetowers.model.defenseTowers.MachineGun;
import com.marek.thetowers.model.defenseTowers.MissileTower;
import com.marek.thetowers.model.defenseTowers.PhotonCannon;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PathPartObject;
import com.marek.thetowers.model.projectiles.Projectile;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Unit;

import java.io.Console;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.marek.thetowers.model.DirectionImageOptions;
import com.marek.thetowers.model.Enemy;
import com.marek.thetowers.model.defenseTowers.Cannon;
import com.marek.thetowers.model.defenseTowers.DefenseTower;
import com.marek.thetowers.model.defenseTowers.MachineGun;
import com.marek.thetowers.model.defenseTowers.MissileTower;
import com.marek.thetowers.model.defenseTowers.PhotonCannon;
import com.marek.thetowers.model.projectiles.Projectile;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Unit;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Třída sloužící pro generování věží nepřítele.
 *
 * @author marek
 */
public class EnemyTowerGenerator {
    private final List<ModelObject> activeObjects;
    private int nOAlreadyCreatedTowers = 0;
    private final List<ModelObject> playerPath;
    private final List<Projectile> projectiles;
    private final RectF enemyTowerPart;
    private final Random randomGenerator = new Random();
    private int nextTowerCycleCounter;
    private final int numberOfCycleToTryGenerateTower;
    private final int windwoGapMillis;
    private static final int MAXIMUM_ENEMY_TOWERS = 10;
    private final Enemy enemy;

    /**
     * @param activeObjects                 aktivní objekty ve hře (věže, jednotky)
     * @param playerPath                    hráčova cesta
     * @param windowGapMillis               okno jednoho keyframe v ms
     * @param generatingTowerIntervalMillis časový interval pro vygenerování věže v ms
     * @param projectiles                   list všech vystřelených projectilů
     * @param enemyTowerPart                Nepřátelova část pro stavění věží
     * @param enemy                         instance nepřítele
     */
    public EnemyTowerGenerator(List<ModelObject> activeObjects, List<ModelObject> playerPath, int windowGapMillis, int generatingTowerIntervalMillis, List<Projectile> projectiles, RectF enemyTowerPart, Enemy enemy) {
        this.nextTowerCycleCounter = 0;
        this.windwoGapMillis = windowGapMillis;
        this.numberOfCycleToTryGenerateTower = generatingTowerIntervalMillis / windowGapMillis;
        this.activeObjects = activeObjects;
        this.playerPath = playerPath;
        this.projectiles = projectiles;
        this.enemyTowerPart = enemyTowerPart;
        this.enemy = enemy;
    }

    /**
     * Zkusí počítači vytvořit další věž.
     */
    public void tryGenerateTower() {
        if (this.nOAlreadyCreatedTowers == MAXIMUM_ENEMY_TOWERS) {
            return;
        }
        if (this.nextTowerCycleCounter == this.numberOfCycleToTryGenerateTower) {
            if (this.tryGenerateTowerIntern()) {
                this.nextTowerCycleCounter = 0;
            }
        } else {
            this.nextTowerCycleCounter++;
        }
    }

    /**
     * Zkusí počítači vytvořit další věž.
     */
    private boolean tryGenerateTowerIntern() {
        DefenseTower defenseTower = null;

        int nOPlayerUnits = 0;
        //resolving towers numbers;
        Unit firstUnit = null;
        int dangerousRadiusRoundFirstUnit = 200;
        int nOCreatedTowersInDangerouRadiusOfFirstUnit = 0;
        for (ModelObject item : this.activeObjects) {
            if (item instanceof Unit) {
                Unit unit = (Unit) item;
                if (!unit.isEnemy()) {
                    if (firstUnit == null) {
                        firstUnit = unit;
                    }
                    nOPlayerUnits++;
                }
            }
        }
        if (firstUnit != null) {
            for (ModelObject item : this.activeObjects) {
                if (item instanceof DefenseTower) {
                    DefenseTower nearDefenseTower = (DefenseTower) item;
                    if (nearDefenseTower.isEnemy()) {
                        if (Math.sqrt(Math.pow(firstUnit.getPosition().x - item.getPosition().x, 2) + Math.pow(firstUnit.getPosition().y - item.getPosition().y, 2)) <= dangerousRadiusRoundFirstUnit) {
                            nOCreatedTowersInDangerouRadiusOfFirstUnit++;
                        }
                    }
                }
            }
        }

        if (firstUnit != null) {
            for (ModelObject item : this.activeObjects) {
                if (item instanceof DefenseTower) {
                    DefenseTower nearDefenseTower = (DefenseTower) item;
                    if (nearDefenseTower.isEnemy()) {
                        if (Math.sqrt(Math.pow(firstUnit.getPosition().x - item.getPosition().x, 2) + Math.pow(firstUnit.getPosition().y - item.getPosition().y, 2)) <= dangerousRadiusRoundFirstUnit) {
                            nOCreatedTowersInDangerouRadiusOfFirstUnit++;
                        }

                    }
                }
            }
        }


        if (nOCreatedTowersInDangerouRadiusOfFirstUnit >= 2 && nOPlayerUnits * 2 < this.nOAlreadyCreatedTowers) {
            return false;
        }

        defenseTower = resolveDefenseTowerTypeAndPossition();
        if (defenseTower == null) {
            return false;
        }

        if (this.enemy.minusCash(defenseTower.getPrice())) {
            this.activeObjects.add(defenseTower);
            nOAlreadyCreatedTowers++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vygeneruje podle algoritmu Typ nové věže a její pozici
     */
    private DefenseTower resolveDefenseTowerTypeAndPossition() {
        Map<ArmorType, Integer> unitTypeOccurenceValues = new HashMap<>();
        DefenseTower selectedTower = null;
        PointF possition = null;

        for (ModelObject object : this.activeObjects) {
            if (object instanceof Unit) {
                Unit unit = (Unit) object;
                if (unit.isEnemy()) {
                    continue;
                }
                if (!unitTypeOccurenceValues.containsKey(unit.getArmor())) {
                    unitTypeOccurenceValues.put(unit.getArmor(), 1);
                } else {
                    Integer count = unitTypeOccurenceValues.get(unit.getArmor());
                    unitTypeOccurenceValues.put(unit.getArmor(), count + 1);
                }
            }
        }

        if (unitTypeOccurenceValues.isEmpty()) {
            return null;
        }

        if (isRemainingQuantityOfUnitTypesEqual(unitTypeOccurenceValues)) {
            selectedTower = generateEnemyToweryByChances(25, 25, 25, 25);
        }

        ArmorType keyOfMaxValue = Collections.max(unitTypeOccurenceValues.entrySet(), new Comparator<Map.Entry<ArmorType, Integer>>() {
            @Override
            public int compare(Map.Entry<ArmorType, Integer> entry1, Map.Entry<ArmorType, Integer> entry2) {
                return entry1.getValue() - entry2.getValue();
            }
        }).getKey();

        unitTypeOccurenceValues.remove(keyOfMaxValue);

        if (unitTypeOccurenceValues.isEmpty() || isRemainingQuantityOfUnitTypesEqual(unitTypeOccurenceValues)) {
            switch (keyOfMaxValue.getValue()) {
                case 1:
                    selectedTower = generateEnemyToweryByChances(100, 0, 0, 0);
                    break;
                case 2:
                    selectedTower = generateEnemyToweryByChances(0, 40, 40, 20);
                    break;
                case 3:
                    selectedTower = generateEnemyToweryByChances(0, 20, 40, 40);
                    break;
                case 4:
                    selectedTower = generateEnemyToweryByChances(0, 20, 20, 60);
                    break;
            }
            possition = resolveDefenseTowerPossition();
            if (possition == null) {
                return null;
            }
            selectedTower.setPosition(possition);
            return selectedTower;
        }


        ArmorType keyOfSecondNextValue = Collections.max(unitTypeOccurenceValues.entrySet(), new Comparator<Map.Entry<ArmorType, Integer>>() {
            @Override
            public int compare(Map.Entry<ArmorType, Integer> entry1, Map.Entry<ArmorType, Integer> entry2) {
                return entry1.getValue() - entry2.getValue();
            }
        }).getKey();

        int armorTypeValue = keyOfMaxValue.getValue() + keyOfSecondNextValue.getValue();
        switch (armorTypeValue) {
            case 7:
                selectedTower = generateEnemyToweryByChances(0, 10, 20, 70);
                break;
            case 3:
                selectedTower = generateEnemyToweryByChances(70, 20, 5, 5);
                break;
            case 4:
                selectedTower = generateEnemyToweryByChances(0, 45, 45, 10);
                break;
            case 5:
            case 6:
                selectedTower = generateEnemyToweryByChances(0, 10, 45, 45);
        }
        possition = resolveDefenseTowerPossition();
        if (possition == null) {
            return null;
        }
        selectedTower.setPosition(possition);
        return selectedTower;
    }

    /**
     * Vygeneruje pozici věže
     */
    private PointF resolveDefenseTowerPossition() {
        PointF possition = null;
        PathPartObject nearTowerPathPart = null;
        int maxOffset = 3;
        for (int i = 0; i < maxOffset; i++) {
            nearTowerPathPart = getNearPathObjectToTowerBasedOnfirstUnit(i);
            possition = resolvePossitionFromNearObject(nearTowerPathPart);
            if (possition != null) {
                break;
            }
        }
        return possition;
    }

    /**
     * Zvolí specificky (podle offsetu) vzdálenou část cesty od prní jednotky na cestě
     */
    private PathPartObject getNearPathObjectToTowerBasedOnfirstUnit(int offset) {
        Unit firstPlayerUnit = null;
        PathPartObject pathPartNearFirstUnit = null;
        int nOHorizontalPathParts = 0;
        int nOVerticalPathParts = 0;

        for (ModelObject item : this.activeObjects) {
            if (item instanceof Unit) {
                firstPlayerUnit = (Unit) item;
                if (!firstPlayerUnit.isEnemy()) {
                    break;
                }
            }
        }
        Iterator<ModelObject> iter = playerPath.iterator();
        while (iter.hasNext()) {
            pathPartNearFirstUnit = (PathPartObject) iter.next();

            if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                nOHorizontalPathParts++;
            } else if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                nOVerticalPathParts++;
            }

            if (firstPlayerUnit != null && pathPartNearFirstUnit.getApproximationObject().contains(firstPlayerUnit.getPosition().x, firstPlayerUnit.getPosition().y)) {
                break;
            }
        }

        while (iter.hasNext()) {
            PathPartObject pathPart = (PathPartObject) iter.next();
            if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                nOHorizontalPathParts++;
            } else if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                nOVerticalPathParts++;
            }
        }

        if (nOHorizontalPathParts >= nOVerticalPathParts) {
            if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                for (int i = 0; i < offset; i++) {
                    if (this.playerPath.indexOf(pathPartNearFirstUnit) + 1 != this.playerPath.size()) {
                        pathPartNearFirstUnit = (PathPartObject) this.playerPath.get(this.playerPath.indexOf(pathPartNearFirstUnit) + 1);
                        if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                            continue;
                        }
                        i--;
                    }
                }
            } else {
                if (offset == 0) {
                    offset++;
                }
                for (int i = 0; i < offset; i++) {
                    if (this.playerPath.indexOf(pathPartNearFirstUnit) + 1 != this.playerPath.size()) {
                        pathPartNearFirstUnit = (PathPartObject) this.playerPath.get(this.playerPath.indexOf(pathPartNearFirstUnit) + 1);
                        if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                            continue;
                        }
                        i--;
                    }
                }
            }
        } else {
            if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                for (int i = 0; i < offset; i++) {
                    if (this.playerPath.indexOf(pathPartNearFirstUnit) + 1 != this.playerPath.size()) {
                        pathPartNearFirstUnit = (PathPartObject) this.playerPath.get(this.playerPath.indexOf(pathPartNearFirstUnit) + 1);
                        if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                            continue;
                        }
                        i--;
                    }
                }
            } else {
                if (offset == 0) {
                    offset++;
                }
                for (int i = 0; i < offset; i++) {
                    if (this.playerPath.indexOf(pathPartNearFirstUnit) + 1 != this.playerPath.size()) {
                        pathPartNearFirstUnit = (PathPartObject) this.playerPath.get(this.playerPath.indexOf(pathPartNearFirstUnit) + 1);
                        if (pathPartNearFirstUnit.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                            continue;
                        }
                        i--;
                    }
                }
            }
        }
        return pathPartNearFirstUnit;
    }

    //ordering indexes in chances array is machine gun - 0, cannon - 1, Missile tower - 2, photon cannnon - 3
    private DefenseTower generateEnemyToweryByChances(int... chances) {
        int nODefenseTowerTypesInGame = 4;
        if (chances.length != nODefenseTowerTypesInGame) {
            return null;
        }
        int randomNumber0To100 = randomGenerator.nextInt(100);
        int nONewTower = -1;
        int chanceAddingCounter = 0;
        for (int i = 0; i < chances.length; i++) {
            if (randomNumber0To100 < chances[i] + chanceAddingCounter) {
                nONewTower = i;
                break;
            } else {
                chanceAddingCounter += chances[i];
            }
        }
        PointF zero = new PointF(0, 0);
        switch (nONewTower) {
            case 0:
                return new MachineGun(zero, new PointF(0, 0), 0, activeObjects, windwoGapMillis, true);
            case 1:
                return new Cannon(zero, new PointF(0, 0), 0, activeObjects, windwoGapMillis, projectiles, true);
            case 2:
                return new MissileTower(zero, new PointF(0, 0), 0, activeObjects, windwoGapMillis, projectiles, true);
            case 3:
                return new PhotonCannon(zero, new PointF(0, 0), 0, activeObjects, windwoGapMillis, projectiles, true);
        }
        return null;
    }

    /**
     * Pokud má hráč stejné počet jednotek ve všech typech vrátí true
     */
    private boolean isRemainingQuantityOfUnitTypesEqual(Map<ArmorType, Integer> unitTypeOccurenceValues) {
        if (unitTypeOccurenceValues.size() < 3) {
            return false;
        }
        int lastValue = -1;
        for (Map.Entry<ArmorType, Integer> entry : unitTypeOccurenceValues.entrySet()) {  // Itrate through hashmap
            if (lastValue == -1) {
                lastValue = entry.getValue();
            } else if (lastValue != entry.getValue()) {
                lastValue = -1;
                return false;
            }
        }
        return true;
    }

    /**
     * Náhodně vybere část cesty
     */
    private PathPartObject getRandomNearPathObjectToTower() {
        int nOHorizontalPathParts = 0;
        int nOVerticalPathParts = 0;
        PathPartObject pathPartObject = null;

        for (ModelObject item : this.playerPath) {
            PathPartObject parhPart = (PathPartObject) item;
            if (parhPart.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                nOHorizontalPathParts++;
            } else if (parhPart.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                nOVerticalPathParts++;
            }
        }

        if (nOHorizontalPathParts >= nOVerticalPathParts) {
            int randomNum = randomGenerator.nextInt(nOHorizontalPathParts);
            int i = 0;

            for (ModelObject item : this.playerPath) {
                PathPartObject pathPart = (PathPartObject) item;
                if (pathPart.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
                    if (i == randomNum) {
                        pathPartObject = pathPart;
                    }
                    i++;
                }
            }
        } else {
            int randomNum = randomGenerator.nextInt(nOVerticalPathParts);
            int i = 0;
            for (ModelObject item : this.playerPath) {
                PathPartObject pathPart = (PathPartObject) item;
                if (pathPart.getImageChoice() == DirectionImageOptions.STRAIGHT_VERTICALLY) {
                    if (i == randomNum) {
                        pathPartObject = pathPart;
                    }
                    i++;
                }
            }
        }
        return pathPartObject;
    }

    /**
     * Vygeneruje podle algoritmu pozici blízko zadané části cesty
     */
    private PointF resolvePossitionFromNearObject(PathPartObject nearPathPartObject) {
        PointF possition = null;
        final RectF aproxPathObject = nearPathPartObject.getApproximationObject();
        boolean direction;
        boolean directionSwitched = false;
        int nOTries = 3;
        int possDiffCounter = 5;
        direction = randomGenerator.nextBoolean();
        if (nearPathPartObject.getImageChoice() == DirectionImageOptions.STRAIGHT_HORIZONTALLY) {
            float x = aproxPathObject.left + PathPartObject.getDIM1() / 2;
            float y;
            for (int i = 0; i < nOTries; i++) {
                if (direction) {
                    y = aproxPathObject.bottom - i * (DefenseTower.DIM1 / 2) - possDiffCounter;
                    possDiffCounter += possDiffCounter;
                } else {
                    y = aproxPathObject.bottom + ((PathPartObject.getDIM2())) + i * (DefenseTower.DIM1 / 2) + possDiffCounter;
                    possDiffCounter += possDiffCounter;
                }
                possition = new PointF(x, y);
                if (DefenseTower.validatePossition(possition, this.enemyTowerPart, playerPath, activeObjects)) {
                    break;
                } else {
                    if (i == nOTries - 1 && !directionSwitched) {
                        direction = !direction;
                        directionSwitched = true;
                        i = 0;
                    }
                    possition = null;
                }
            }
        } else {
            float y = aproxPathObject.bottom + PathPartObject.getDIM1() / 2;
            float x;
            for (int i = 0; i < nOTries; i++) {
                if (direction) {
                    x = aproxPathObject.left - i * (DefenseTower.DIM1 / 2) - possDiffCounter;
                    possDiffCounter += possDiffCounter;
                } else {
                    x = aproxPathObject.left + ((PathPartObject.getDIM1())) + i * (DefenseTower.DIM1 / 2) + possDiffCounter;
                    possDiffCounter += possDiffCounter;
                }
                possition = new PointF(x, y);
                if (DefenseTower.validatePossition(possition, this.enemyTowerPart, playerPath, activeObjects)) {
                    break;
                } else {
                    if (i == nOTries - 1 && !directionSwitched) {
                        direction = !direction;
                        directionSwitched = true;
                        i = 0;
                    }
                    possition = null;
                }
            }
        }
        return possition;
    }
}