/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.enviromentLogic;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;

import com.marek.thetowers.GameView;

public abstract class ModelObject {
    private PointF position;
    
    public ModelObject(PointF position) {
        this.position = new PointF(position.x, position.y);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public PointF getPosition() {
        return this.position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }
    
    public abstract void process();

    public boolean isOutOfSpace() {
        if (this.position.x < 0 || this.position.y > GameView.WIDTH) {
            return true;
        }
        return this.position.y < 0 || this.position.y > GameView.HEIGHT;
    }
    
    public abstract int getViewIdentifikator();
}
