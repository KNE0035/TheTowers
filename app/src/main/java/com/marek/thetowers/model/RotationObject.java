/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model;

import android.graphics.PointF;

import com.marek.thetowers.model.enviromentLogic.ModelObject;


/**
 *
 * @author marek
 */
public abstract class RotationObject extends ModelObject {
    protected float direction;
    private PointF imageOffset;
    
    public RotationObject(PointF position, PointF imageOffset, float direction) {
        super(position);
        this.imageOffset = imageOffset;
        this.direction = direction;
    }
    
    public float getDirection() {
        return direction;
    }

    public void setDirectionToPoint(PointF direction) {
        PointF position = getPosition();
        position.set(position.x + imageOffset.x, position.y + imageOffset.y);
        PointF imageRotation = position;
        float angle = angleOf(direction, imageRotation);
        this.direction = angle;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void setImageOffset(PointF imageOffset) {
        this.imageOffset = imageOffset;
    }

    public static float angleOf(PointF p1, PointF p2) {
        // NOTE: Remember that most math has the Y axis as positive above the X.
        // However, for screens we have Y as positive below. For this reason,
        // the Y values are inverted to get the expected results.
        final float deltaY = (p1.y - p2.y);
        final float deltaX = (p2.x - p1.x);
        final float result = (float)Math.toDegrees(Math.atan2(deltaY, deltaX));
        return (result < 0) ? (360f + result) : result;
    }
}
