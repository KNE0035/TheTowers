/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PathPartObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Třída pro generování okruhu cest
 * @author marek
 */
public class PathRingBuilder {
    private List<ModelObject> pathRing = new ArrayList<>();

    private final PointF startingPosition;
    
    public PathRingBuilder(List<ModelObject> pathRing, final PointF startingPosition) {
        this.pathRing = pathRing;
        this.startingPosition = startingPosition;
    }
    
     /*Podle zadané orientace další části cesty přípojí další část na konec již zbuildované cesty*/
    public void addPart(Orientation orientation){
        PointF lastTopCorner;
        Orientation lastOrientation;
        if(!pathRing.isEmpty()){
            lastTopCorner  = pathRing.get(pathRing.size() - 1).getPosition();
            lastOrientation = ((PathPartObject) (pathRing.get(pathRing.size() - 1))).getOrientation();
        } else {
            this.pathRing.add(new PathPartObject(this.startingPosition, orientation,  null));
            return;
        }
        PointF topCorner = new PointF(0, 0);
        DirectionImageOptions imageChoice = null;
        switch(orientation){
            case NORTH:
                switch(lastOrientation){
                    case NORTH:
                        topCorner = new PointF(lastTopCorner.x, lastTopCorner.y);
                        imageChoice = DirectionImageOptions.STRAIGHT_VERTICALLY;
                        break;
                    case WEST:
                        topCorner = new PointF(lastTopCorner.x - PathPartObject.getDIM2(), lastTopCorner.y + PathPartObject.getDIM2());
                        imageChoice = DirectionImageOptions.WEST_TO_NORTH;
                        break;
                    case EAST:
                        topCorner = new PointF(lastTopCorner.x + PathPartObject.getDIM1(), lastTopCorner.y + PathPartObject.getDIM2());
                        imageChoice = DirectionImageOptions.EAST_TO_NORTH;
                        break;
                    default:
                        System.err.println("Cant move from south to north directly");
                        return;
                }
                break;
            case WEST:
                switch(lastOrientation){
                    case NORTH:
                        topCorner = new PointF(lastTopCorner.x + PathPartObject.getDIM2(), lastTopCorner.y - PathPartObject.getDIM2());
                        imageChoice = DirectionImageOptions.NORTH_TO_WEST;
                        break;
                    case WEST:
                        topCorner = new PointF(lastTopCorner.x, lastTopCorner.y);
                        imageChoice = DirectionImageOptions.STRAIGHT_HORIZONTALLY;
                        break;
                    case SOUTH:
                        topCorner = new PointF(lastTopCorner.x + PathPartObject.getDIM2(), lastTopCorner.y + PathPartObject.getDIM1());
                        imageChoice = DirectionImageOptions.SOUTH_TO_WEST;
                        break;
                    default:
                        System.err.println("Cant move from east to west directly");
                        return;
                }
                break;
            case SOUTH:
                switch(lastOrientation){
                    case EAST:
                        topCorner = new PointF(lastTopCorner.x + PathPartObject.getDIM1(), lastTopCorner.y);
                        imageChoice = DirectionImageOptions.EAST_TO_SOUTH;
                        break;
                    case WEST:
                        topCorner = new PointF(lastTopCorner.x - PathPartObject.getDIM2(), lastTopCorner.y);
                        imageChoice = DirectionImageOptions.WEST_TO_SOUTH;
                        break;
                    case SOUTH:
                        topCorner = new PointF(lastTopCorner.x, lastTopCorner.y + PathPartObject.getDIM1());
                        imageChoice = DirectionImageOptions.STRAIGHT_VERTICALLY;
                        break;
                    default:
                        System.err.println("Cant move from north to south directly");
                        return;
                }
                break;
            case EAST:
                switch(lastOrientation){
                    case EAST:
                        topCorner = new PointF(lastTopCorner.x + PathPartObject.getDIM1(), lastTopCorner.y);
                        imageChoice = DirectionImageOptions.STRAIGHT_HORIZONTALLY;
                        break;
                    case NORTH:
                        topCorner = new PointF(lastTopCorner.x, lastTopCorner.y- PathPartObject.getDIM2());
                        imageChoice = DirectionImageOptions.NORTH_TO_EAST;
                        break;
                    case SOUTH:
                        topCorner = new PointF(lastTopCorner.x, lastTopCorner.y + PathPartObject.getDIM1());
                        imageChoice = DirectionImageOptions.SOUTH_TO_EAST;
                        break;
                    default:
                        System.err.println("Cant move from west to east directly");
                        return;
                }
                break;
        }
        this.pathRing.add(new PathPartObject(topCorner, orientation, imageChoice));
    }
    
    /*Přídá n částí ve stejné orientaci za sebe na konec již zbudované cesty*/
    public void addParts(Orientation orientation, int count){
        for(int i = 0; i < count; i++){
            this.addPart(orientation);
        }
    }
}
