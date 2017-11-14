package com.marek.thetowers;

/**
 * Created by Marek on 12/11/2017.
 */

public class TowerEntry {
    public final String name;
    public final int damage;
    public final int price;
    public final String armorType;
    public final int towerImageValue;
    public  final String description;

    public TowerEntry(String name, int damage, int price, String armorType, int towerImageValue, String description) {
        this.name = name;
        this.damage = damage;
        this.price = price;
        this.armorType = armorType;
        this.towerImageValue = towerImageValue;
        this.description = description;
    }
}
