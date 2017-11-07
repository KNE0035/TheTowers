/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model;

/**
 *
 * @author marek
 */
public class Enemy {
    private int hitPoints;
    private int cash;
    private int score;

    public Enemy(int cash, int hitPoints) {
        this.cash = cash;
        this.hitPoints = hitPoints;
        this.score = 0;
    }


    public int getHitPoints() {
        return hitPoints;
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
    
    public int getCash() {
        return cash;
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
    
}
