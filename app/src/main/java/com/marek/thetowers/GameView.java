package com.marek.thetowers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.marek.thetowers.model.DirectionImageOptions;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.Model;
import com.marek.thetowers.model.Orientation;
import com.marek.thetowers.model.RotationObject;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PathPartObject;

/**
 * Created by Marek on 21/10/2017.
 */

public class GameView extends SurfaceView {

    public static int HEIGHT;
    public static int WIDTH;
    public static Context CONTEXT;
    private boolean backgroundDraw = true;
    private Matrix identityMatrix;
    private Bitmap myCanvasBitmap = null;
    private Canvas myCanvas = null;

    private Model model;
    private GameThread gameThread;

    private Bitmap bmp;
    private SurfaceHolder holder;

    public GameView(Context context) {
        super(context);
        CONTEXT = context;
        init();
    }

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        gameThread = new GameThread(this);
        CONTEXT = context;
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CONTEXT = context;
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                HEIGHT = getHeight();
                WIDTH = getWidth();

                myCanvasBitmap = Bitmap.createBitmap(getWidth(),  getHeight(), Bitmap.Config.ARGB_8888);
                myCanvas = new Canvas();
                myCanvas.setBitmap(myCanvasBitmap);

                model = new Model();
                model.createEnemyUnitGenerator();
                model.createPlayerUnitGenerator();
                model.createEnemyTowerGenerator(1000, 1000);

                gameThread.setModel(model);
                gameThread.setRunning(true);
                gameThread.start();
                identityMatrix = new Matrix();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameThread.setRunning(false);

                while (retry){
                    try {
                        gameThread.join();
                        retry = false;
                    }catch (InterruptedException e){

                    }
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(backgroundDraw){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
            myCanvas.drawBitmap(bitmap, 0, 0, null);
        }
        backgroundDraw = false;
        canvas.drawBitmap(myCanvasBitmap, identityMatrix, null);

        drawPlayerEnemyPart(canvas);
        drawPath(canvas);
        drawActiveObjects(canvas);
    }

    private void drawPlayerEnemyPart(Canvas canvas){
        RectF playerPart = model.getPlayerPart();
        RectF enemyPart = model.getEnemyPart();

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(playerPart, paint);

        paint.setColor(Color.RED);
        canvas.drawRect(enemyPart, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        canvas.drawText("Enemy tower Part", enemyPart.left + 15, enemyPart.bottom - 10, paint);

        paint.setColor(Color.BLUE);
        canvas.drawText("Player tower Part", playerPart.left + 15, playerPart.bottom - 10, paint);
    }

    private void drawPath(Canvas canvas){
        //Generating path
        for(ModelObject part: model.getPath()){
            PathPartObject pathPart = (PathPartObject) part;
            PointF imagePoint;
            imagePoint = new PointF(pathPart.getPosition().x, pathPart.getPosition().y);
            if(pathPart.getImageChoice() == null){
                if(pathPart.getOrientation() == Orientation.NORTH || pathPart.getOrientation() == Orientation.SOUTH){
                    drawImage(DirectionImageOptions.STRAIGHT_VERTICALLY.getImageValue(), imagePoint, canvas, false);
                } else {
                    drawImage(DirectionImageOptions.STRAIGHT_HORIZONTALLY.getImageValue(), imagePoint, canvas,false);
                }
            } else {
                drawImage(pathPart.getViewIdentifikator(), imagePoint, canvas, false);
            }
        }
    }

    private void drawImage(int imageValue, PointF point, Canvas canvas, boolean imageOffset) {
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), imageValue, opt);
        if(imageOffset){
            canvas.drawBitmap(bmp, point.x - (GameUtil.dip2px(opt.outWidth) / 2), point.y - (GameUtil.dip2px(opt.outHeight)) / 2, null);
        } else {
            canvas.drawBitmap(bmp, point.x, point.y, null);
        }
    }

    private void drawActiveObjects(Canvas canvas){
        //Draw activeObjects
        synchronized (model){
            for(ModelObject item: model.getActiveObjects()){
                canvas.save();
                rotate(((RotationObject)(item)).getDirection(), item.getPosition(), canvas);
//                final int hitpointBarWidth = 70;
//                final int hitpointBarHeight = 10;
//                int hpPercentage;
//                if(item instanceof Unit){
//                    drawUnitHitBar((Unit)item);
//                }

                drawImage(item.getViewIdentifikator(), item.getPosition(), canvas, true);
                canvas.restore();
            }

            for(ModelObject item: model.getProjectiles()){
                canvas.save();
                rotate(((RotationObject)(item)).getDirection(), item.getPosition(), canvas);
                drawImage(item.getViewIdentifikator(), item.getPosition(), canvas, true);
                canvas.restore();
            }
        }
    }

    private void rotate(float angle, PointF center, Canvas canvas) {
        canvas.rotate(angle, center.x, center.y);
    }

    public Model getModel() {
        return model;
    }
}
