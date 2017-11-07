/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.units;

/**
 * Definice všech možných armor typů
 * @author marek
 */
public enum ArmorType {
    LOW(1), MEDIUM(2), HARDENED(3), HEAVY(4);
    
    private final int value;
    private ArmorType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
