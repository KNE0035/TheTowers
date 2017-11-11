/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.enviromentLogic;


import com.marek.thetowers.model.Enemy;
import com.marek.thetowers.model.Player;

/**
 * Třída pro automatické přídávání peněz jak hráčovy tak počítače vždy po určítém časovém intervalu.
 * @author marek
 */
public class AutomaticMoneyGenerator {
    private final Player player;
    private final Enemy enemy;
    private final int nOCycleToGenerateMoney;
    private int nextMoneyCycleCounter;
    private int nextMoneyAmplifierCycleCounter;
    private final int nOCycleToAmplifiedGenerateMoney;
    private final int amplifiedMoneyAmmount;
    private static int ammountOfGeneratedMoney = 50;

    /**
     * @param player instance hráče
     * @param enemy  instance nepřítele
     * @param windowGap doba jednoho keyFramu
     * @param generateMoneyRate časový interval pro vygenerování peněz v ms
     * @param amplifyGenerateMoneyRate časový interval pro zvýšení generované částky peněz v ms
     * @param amplifiedMoneyAmmount o kolik se má částka zvyšovat v průběhu času
     */
    public AutomaticMoneyGenerator(Player player, Enemy enemy, int windowGap, int generateMoneyRate, int amplifyGenerateMoneyRate,int amplifiedMoneyAmmount) {
        this.nextMoneyCycleCounter = 0;
        this.nextMoneyAmplifierCycleCounter = 0;
        this.nOCycleToGenerateMoney = generateMoneyRate / windowGap;
        this.nOCycleToAmplifiedGenerateMoney = amplifyGenerateMoneyRate / windowGap;
        this.player = player;
        this.enemy = enemy;
        this.amplifiedMoneyAmmount = amplifiedMoneyAmmount;
    }
    
    /**
     * Zkusí hráči a počítači předat další peníze (projde jen pokud nastal interval generování peněz).
     */
    public boolean tryGenerateMoney(){
        if(this.nextMoneyAmplifierCycleCounter == this.nOCycleToAmplifiedGenerateMoney){
            ammountOfGeneratedMoney += amplifiedMoneyAmmount;
            this.nextMoneyAmplifierCycleCounter = 0;
        } else {
            this.nextMoneyAmplifierCycleCounter++;
        }
        
        if(this.nextMoneyCycleCounter == this.nOCycleToGenerateMoney){
            this.player.addCash(ammountOfGeneratedMoney);
            this.enemy.addCash(ammountOfGeneratedMoney);
            this.nextMoneyCycleCounter = 0;
            return true;
        } else {
            this.nextMoneyCycleCounter++;
            return false;
        }
    }
}
