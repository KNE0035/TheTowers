/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model;

import android.graphics.PointF;

/**
 *
 * @author marek
 */
public abstract class MovableObject extends RotationObject{
    private final int speed;
    public MovableObject(PointF position, PointF imageOffset, float direction, int speed) {
        super(position, imageOffset, direction);
        this.speed = speed;
    }
    
    protected void move(float offsetX, float offsetY) {
        this.setPosition(new PointF(getX() + offsetX, getY() + offsetY));
    }

    public int getSpeed() {
        return speed;
    }
}
