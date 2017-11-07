package com.marek.thetowers.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.graphics.PointF;
import android.graphics.RectF;

import com.marek.thetowers.GameView;
import com.marek.thetowers.model.enviromentLogic.EnemyTowerGenerator;
import com.marek.thetowers.model.enviromentLogic.EnemyUnitGenerator;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PlayerUnitGenerator;
import com.marek.thetowers.model.projectiles.Projectile;

//import theTowers.model.enviromentLogic.EnemyUnitGenerator;
//import theTowers.model.enviromentLogic.StaticSelectionObject;
//import theTowers.model.enviromentLogic.PathRingBuilder;
//import theTowers.model.enviromentLogic.EnemyTowerGenerator;
//import theTowers.model.enviromentLogic.PlayerUnitGenerator;
//import theTowers.model.enviromentLogic.StaticObjectTypes;
//import theTowers.model.enviromentLogic.AutomaticMoneyGenerator;
//import theTowers.model.projectiles.Projectile;
import java.util.ArrayList;
import java.util.List;
//import javafx.geometry.Point2D;
//import javafx.geometry.Rectangle2D;
//import theTowers.View;
/**
 *
 * @author beh01
 */
public class Model {
    private final int INIT_CASH = 100000000;
    private final int INIT_HP = 5;

    
    private final List<ModelObject> activeObjects = new ArrayList<>();
    private final List<ModelObject> enemyPath = new ArrayList<>();
    private final List<ModelObject> playerPath = new ArrayList<>();
    private final List<ModelObject> path = new ArrayList<>();
    private final List<Projectile>  projectiles = new ArrayList<>();
    private PlayerUnitGenerator playerUnitGenerator;
    private EnemyUnitGenerator enemyUnitGenerator;
    private EnemyTowerGenerator enemyTowerGenerator;
    Player player;
    private Enemy enemy;
    private RectF playerPart, enemyPart;
//    private AutomaticMoneyGenerator automaticMoneyGenerator;

    public Model() {
        this.initGame();
    }
    
    /**
     * Inicializuje hru (vytvoří trasy pro jednotky, inicializuje všechny potřebné objekty)
     */
    public synchronized void initGame(){
        activeObjects.clear();
        enemyPath.clear();
        playerPath.clear();
        projectiles.clear();
        this.playerPart = new RectF(2, 2, GameView.WIDTH - 2, GameView.HEIGHT / 2);
        this.enemyPart = new RectF(2, GameView.HEIGHT / 2 + 4, GameView.WIDTH - 2, (GameView.HEIGHT) - 4);
        //building enemyPath
        PathRingBuilder enemyPathBuilder = new PathRingBuilder(this.enemyPath, new PointF(0, GameUtil.dip2px(30)));
        enemyPathBuilder.addParts(Orientation.EAST, 8);
        enemyPathBuilder.addPart(Orientation.SOUTH);
        enemyPathBuilder.addPart(Orientation.EAST);
        enemyPathBuilder.addParts(Orientation.SOUTH, 1);
        enemyPathBuilder.addParts(Orientation.EAST, 4);
        enemyPathBuilder.addParts(Orientation.NORTH, 1);
        enemyPathBuilder.addPart(Orientation.EAST);
        enemyPathBuilder.addPart(Orientation.NORTH);
        enemyPathBuilder.addParts(Orientation.EAST, 9);

        PathRingBuilder playerPathBuilder = new PathRingBuilder(this.playerPath, new PointF(GameView.WIDTH, GameView.HEIGHT - GameUtil.dip2px(55)));
        playerPathBuilder.addParts(Orientation.WEST, 8);
        playerPathBuilder.addPart(Orientation.NORTH);
        playerPathBuilder.addPart(Orientation.WEST);
        playerPathBuilder.addParts(Orientation.NORTH, 1);
        playerPathBuilder.addParts(Orientation.WEST, 4);
        playerPathBuilder.addParts(Orientation.SOUTH, 1);
        playerPathBuilder.addPart(Orientation.WEST);
        playerPathBuilder.addPart(Orientation.SOUTH);
        playerPathBuilder.addParts(Orientation.WEST, 9);

        path.addAll(enemyPath);
        path.addAll(playerPath);

        player = new Player(INIT_CASH, INIT_HP);
        enemy = new Enemy(INIT_CASH, INIT_HP);
    }

    public synchronized List<ModelObject> getActiveObjects() {
        return activeObjects;
    }

    public synchronized List<ModelObject> getPath() {
        return path;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<ModelObject> getEnemyPath() {
        return enemyPath;
    }

    public List<ModelObject> getPlayerPath() {
        return playerPath;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public PlayerUnitGenerator getPlayerUnitGenerator() {
        return playerUnitGenerator;
    }

    public void setPlayerUnitGenerator(PlayerUnitGenerator playerUnitGenerator) {
        this.playerUnitGenerator = playerUnitGenerator;
    }
    public void createEnemyUnitGenerator(){
        this.enemyUnitGenerator = new EnemyUnitGenerator(activeObjects, enemyPath, this.enemy);
    }
    public void createEnemyTowerGenerator(int windowGapMillis, int generatingTowerIntervalMillis){
        this.enemyTowerGenerator = new EnemyTowerGenerator(activeObjects, this.playerPath, windowGapMillis, generatingTowerIntervalMillis, this.projectiles, this.enemyPart, this.enemy);
    }

    public void createPlayerUnitGenerator(){
        this.playerUnitGenerator = new PlayerUnitGenerator(activeObjects);
    }
//
//    public void createAutomaticMoneyGenerator(int windowGap, int intervalToGenerateMoneyMillis, int intervalToAmplifyGenerateMoneyMillis,int amplifiedMoneyAmmount){
//        this.automaticMoneyGenerator = new AutomaticMoneyGenerator(this.player, this.enemy, windowGap, intervalToGenerateMoneyMillis, intervalToAmplifyGenerateMoneyMillis, amplifiedMoneyAmmount);
//    }
//
//
    public EnemyUnitGenerator getEnemyUnitGenerator() {
        return enemyUnitGenerator;
    }
//
    public void setEnemyGenerator(EnemyUnitGenerator enemyGenerator) {
        this.enemyUnitGenerator = enemyGenerator;
    }
//
    public RectF getPlayerPart() {
        return playerPart;
    }

    public RectF getEnemyPart() {
        return enemyPart;
    }
//
    public EnemyTowerGenerator getEnemyTowerGenerator() {
        return enemyTowerGenerator;
    }
//
    public void setEnemyTowerGenerator(EnemyTowerGenerator enemyTowerGenerator) {
        this.enemyTowerGenerator = enemyTowerGenerator;
    }

//    public AutomaticMoneyGenerator getAutomaticMoneyGenerator() {
//        return automaticMoneyGenerator;
//    }
}
