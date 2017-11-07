/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.enviromentLogic;

import android.graphics.PointF;
import android.graphics.RectF;

import com.marek.thetowers.R;
import com.marek.thetowers.model.DirectionImageOptions;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.Orientation;

/**
 * Třída definující jednu část cesty pro jednotky
 * @author marek
 */
public class PathPartObject extends ModelObject{
    private RectF approximationObject;
    public final static float DIM1 = GameUtil.dip2px(30);
    public final static float DIM2 = GameUtil.dip2px(20);
    private final Orientation orientation;
    private final DirectionImageOptions imageChoice;
    
    public PathPartObject(PointF position, Orientation orientation, DirectionImageOptions imageChoice) {
        super(position);
        this.orientation = orientation;
        this.imageChoice = imageChoice;
        switch(this.orientation){
            case NORTH:
                this.approximationObject = new RectF(position.x, position.y - DIM1, position.x + DIM2,  position.y);
                this.setPosition(new PointF(position.x, position.y - DIM1));
                break;
            case WEST:
                this.approximationObject = new RectF(position.x - DIM1, position.y, position.x, position.y + DIM2);
                this.setPosition(new PointF(position.x - DIM1, position.y));
                break;
            case SOUTH:
                this.approximationObject = new RectF(position.x, position.y, position.x +  DIM2, position.y + DIM1);
                break;
            case EAST:
                this.approximationObject = new RectF(position.x, position.y, position.x +  DIM1, position.y + DIM2);
                break;
        }
   }

    public Orientation getOrientation() {
        return orientation;
    }

    public static float getDIM1() {
        return DIM1;
    }

    public static float getDIM2() {
        return DIM2;
    }

    public RectF getApproximationObject() {
        return approximationObject;
    }
    
    @Override
    public void process() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DirectionImageOptions getImageChoice() {
        return imageChoice;
    }

    @Override
    public int getViewIdentifikator() {
        return imageChoice.getImageValue();
    }
}
