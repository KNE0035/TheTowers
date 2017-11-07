/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model;


import com.marek.thetowers.R;
/**
 *
 * @author marek
 */
public enum DirectionImageOptions {
    NORTH_TO_EAST (R.drawable.path_north_to_east),
    NORTH_TO_WEST (R.drawable.path_north_to_west),
    SOUTH_TO_EAST (R.drawable.path_south_to_east),
    SOUTH_TO_WEST (R.drawable.path_south_to_west),
    EAST_TO_NORTH (R.drawable.path_east_to_north),
    EAST_TO_SOUTH (R.drawable.path_east_to_south),
    WEST_TO_NORTH (R.drawable.path_west_to_north),
    WEST_TO_SOUTH (R.drawable.path_west_to_south),
    STRAIGHT_VERTICALLY (R.drawable.path_straight_vertically),
    STRAIGHT_HORIZONTALLY (R.drawable.path_straight_horizontally);

    private final int imageValue;

    private DirectionImageOptions(int imageValue) {
        this.imageValue = imageValue;
    }

    public int getImageValue() {
        return imageValue;
    }
}
