/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.units;

import android.graphics.PointF;

import com.marek.thetowers.R;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.enviromentLogic.ModelObject;

import java.util.List;

/**
 *
 * @author marek
 */
public class QuadBike extends Unit{
    private static final int SPEED = 8;
    private static final int HIT_POINTS = 25;
    private static final ArmorType ARMOR = ArmorType.LOW;
    private static final int PRICE = 50;
    private static final int VIEW_IDENTIFIKATOR = R.drawable.quad_bike;
    
    public QuadBike(PointF imageOffset, List<ModelObject> path, float initDirection, boolean enemy, List<ModelObject> activeObjests) {
        super(imageOffset, SPEED, path, initDirection, HIT_POINTS, enemy, ARMOR, PRICE, activeObjests);
    }

    @Override
    public int getViewIdentifikator() {
        return VIEW_IDENTIFIKATOR;
    }
    
    public static int getPRICE() {
        return PRICE;
    }
    
    public static int getHIT_POINTS() {
        return HIT_POINTS;
    }
}
