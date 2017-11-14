package com.marek.thetowers.model;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;

import com.marek.thetowers.GameView;

/**
 * Created by Marek on 06/11/2017.
 */

public final class GameUtil {
    private static Context context;

    public static int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static PointF normalizePoint(PointF point) {
        final float mag = magnitude(point);

        if (mag == 0.0) {
            return new PointF(0, 0);
        }

        return new PointF(
                point.x / mag,
                point.y / mag);
    }

    private static float magnitude(PointF point) {
        final float x = point.x;
        final float y = point.y;

        return (float) Math.sqrt(x * x + y * y);
    }

    public static void initialize(Context context){
        GameUtil.context = context.getApplicationContext();
    }
}
