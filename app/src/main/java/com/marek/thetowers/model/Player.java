/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model;

import com.marek.thetowers.model.defenseTowers.DefenseTower;

/**
 *
 * @author marek
 */
public class Player {
    private int cash;
    private int hitPoints;
    private int score;
    private DefenseTower selectedTower = null;
    private int nOPlayerTowers;
    private static final int MAX_PLAYER_TOWERS = 20;

    public Player(int cash, int hitPoints) {
        this.cash = cash;
        this.hitPoints = hitPoints;
        this.score = 0;
        this.nOPlayerTowers = 0;
    }

    public int getCash() {
        return cash;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void addCash(int cash){
        this.cash += cash;
    }
    
    public boolean minusCash(int cash){
        if(this.cash - cash < 0){
            return false;
        }
        this.cash -= cash;
        return true;
    }
    
    public void minusHP(int hitPoints){
        this.hitPoints -= hitPoints;
    }

    public int getScore() {
        return score;
    }
    
    public void addScore(int score){
        this.score += score;
    }

    public DefenseTower getSelectedTower() {
        return selectedTower;
    }

    public void setSelectedTower(DefenseTower selectedTower) {
        this.selectedTower = selectedTower;
    }

    public void addTowerCounter(){
        this.nOPlayerTowers++;
    }
    
    public boolean isTowerMaxed(){
        return this.nOPlayerTowers >= MAX_PLAYER_TOWERS;
    }
}
