package com.marek.thetowers;

/**
 * Created by Marek on 12/11/2017.
 */

public class UnitEntry {
    public final String name;
    public final int hp;
    public final int price;
    public final String armorType;
    public final int unitImageValue;
    public final String description;

    public UnitEntry(String name, int hp, int price, String armorType, int unitImageValue, String description) {
        this.name = name;
        this.hp = hp;
        this.price = price;
        this.armorType = armorType;
        this.unitImageValue = unitImageValue;
        this.description = description;
    }
}
