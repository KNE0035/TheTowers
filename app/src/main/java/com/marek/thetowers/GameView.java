package com.marek.thetowers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.marek.thetowers.model.DirectionImageOptions;
import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.Model;
import com.marek.thetowers.model.Orientation;
import com.marek.thetowers.model.RotationObject;
import com.marek.thetowers.model.defenseTowers.DefenseTower;
import com.marek.thetowers.model.defenseTowers.DefenseTowerWithProjectile;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.enviromentLogic.PathPartObject;
import com.marek.thetowers.model.units.Unit;

import java.util.HashMap;
import java.util.Map;

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
    private GameViewActivity gameViewActivity;
    private Map<Integer, Bitmap> activeObjectsImageBitMaps = new HashMap<>();
    private Map<Integer, BitmapFactory.Options> imagesOptions = new HashMap<>();

    private Model model;
    private GameThread gameThread;

    private Bitmap bmp;
    private SurfaceHolder holder;

    public GameView(Context context) {
        super(context);
        CONTEXT = context;
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
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
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                HEIGHT = getHeight();
                WIDTH = getWidth();

                myCanvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                myCanvas = new Canvas();
                myCanvas.setBitmap(myCanvasBitmap);

                model = new Model();
                model.createEnemyUnitGenerator();
                model.createPlayerUnitGenerator();
                model.createEnemyTowerGenerator(GameThread.WINDOW_GAP_MILLIS, 1000);
                model.createAutomaticMoneyGenerator(GameThread.WINDOW_GAP_MILLIS, 4000, 6000, 25);

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

                while (retry) {
                    try {
                        gameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (backgroundDraw) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
            myCanvas.drawBitmap(bitmap, 0, 0, null);
            drawPlayerEnemyPart(myCanvas);
            drawPath(myCanvas);
        }
        backgroundDraw = false;
        canvas.drawBitmap(myCanvasBitmap, identityMatrix, null);
        drawActiveObjects(canvas);

        gameViewActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        gameViewActivity.hitPointText.setText("Hit points: " + model.getPlayer().getHitPoints());
                        gameViewActivity.moneyText.setText(model.getPlayer().getCash() + "$");
                        gameViewActivity.scoreText.setText("Score: " + model.getPlayer().getScore());
                    }
                });
        checkEndGame();
    }

    private void checkEndGame() {
        //checking player hp
        if(model.getPlayer().getHitPoints() <= 0){
            gameViewActivity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {gameViewActivity.endGame("Game over");}
                    });
        } else if(model.getEnemy().getHitPoints() <= 0){
            gameViewActivity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {gameViewActivity.endGame("Congratulations, you win");}
                    });
        }
    }

    private void drawPlayerEnemyPart(Canvas canvas) {
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

    private void drawPath(Canvas canvas) {
        //Generating path
        for (ModelObject part : model.getPath()) {
            PathPartObject pathPart = (PathPartObject) part;
            PointF imagePoint;
            imagePoint = new PointF(pathPart.getPosition().x, pathPart.getPosition().y);
            if (pathPart.getImageChoice() == null) {
                if (pathPart.getOrientation() == Orientation.NORTH || pathPart.getOrientation() == Orientation.SOUTH) {
                    drawImage(DirectionImageOptions.STRAIGHT_VERTICALLY.getImageValue(), imagePoint, canvas, false);
                } else {
                    drawImage(DirectionImageOptions.STRAIGHT_HORIZONTALLY.getImageValue(), imagePoint, canvas, false);
                }
            } else {
                drawImage(pathPart.getViewIdentifikator(), imagePoint, canvas, false);
            }
        }
    }

    private void drawImage(int imageValue, PointF point, Canvas canvas, boolean imageOffset) {
        if(!activeObjectsImageBitMaps.containsKey(imageValue)){
            final BitmapFactory.Options opt = new BitmapFactory.Options();
            final Bitmap bmp = BitmapFactory.decodeResource(getResources(), imageValue, opt);
            activeObjectsImageBitMaps.put(imageValue, bmp);
            imagesOptions.put(imageValue, opt);
        }

        if (imageOffset) {
            canvas.drawBitmap(activeObjectsImageBitMaps.get(imageValue), point.x - (GameUtil.dip2px(imagesOptions.get(imageValue).outWidth) / 2), point.y - (GameUtil.dip2px(imagesOptions.get(imageValue).outHeight)) / 2, null);
        } else {
            canvas.drawBitmap(activeObjectsImageBitMaps.get(imageValue), point.x, point.y, null);
        }
    }

    private void drawActiveObjects(Canvas canvas) {
        //Draw activeObjects
        synchronized (model) {
            for (ModelObject item : model.getActiveObjects()) {
                canvas.save();
                rotate(((RotationObject) (item)).getDirection(), item.getPosition(), canvas);
                final int hitpointBarWidth = 70;
                final int hitpointBarHeight = 10;
                int hpPercentage;
                if(item instanceof Unit){
                    drawUnitHitBar((Unit)item, canvas);
                }

                drawImage(item.getViewIdentifikator(), item.getPosition(), canvas, true);
                canvas.restore();
            }

            for (ModelObject item : model.getProjectiles()) {
                canvas.save();
                rotate(((RotationObject) (item)).getDirection(), item.getPosition(), canvas);
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

    public void setGameViewActivity(GameViewActivity gameViewActivity) {
        this.gameViewActivity = gameViewActivity;
    }

    private void drawUnitHitBar(Unit unit, Canvas canvas){
        final float hitpointBarWidth = 70;
        final float hitpointBarHeight = 10;
        int hpPercentage = 0;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        hpPercentage = (int)(((unit.getActualHitPoints() / (float)unit.getMaxHitPoints()) * 100.0));
        PointF hpStartPos = new PointF(unit.getPosition().x - 30, unit.getPosition().y - 40);

        paint.setColor(Color.RED);

        RectF maxHealthRect = new RectF(hpStartPos.x, hpStartPos.y, hpStartPos.x + hitpointBarWidth, hpStartPos.y + hitpointBarHeight);
        RectF actualHealthRect = new RectF(hpStartPos.x, hpStartPos.y, ((hitpointBarWidth / 100) * hpPercentage) + hpStartPos.x, hpStartPos.y + hitpointBarHeight);

        canvas.drawRect(maxHealthRect, paint);

        paint.setColor(Color.GREEN);
        canvas.drawRect(actualHealthRect, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(model.getPlayer().getSelectedTower() == null){
            return true;
        }

        if(model.getPlayer().isTowerMaxed()){
            setInfoMesseageInUiThread("Towers depleted");
            return true;
        }

        PointF possition = new PointF(event.getX(), event.getY());
        if(!DefenseTower.validatePossition(possition, model.getPlayerPart(), model.getPath(), model.getActiveObjects())) {
            setInfoMesseageInUiThread("wrong possition");
            return true;
        }
        model.getPlayer().getSelectedTower().setActiveObjects(model.getActiveObjects());
        model.getPlayer().getSelectedTower().setPosition(possition);
        if(model.getPlayer().getSelectedTower() instanceof DefenseTowerWithProjectile){
            ((DefenseTowerWithProjectile) model.getPlayer().getSelectedTower()).setProjectiles(model.getProjectiles());
        }

        model.getPlayer().minusCash(model.getPlayer().getSelectedTower().getPrice());
        model.getActiveObjects().add(model.getPlayer().getSelectedTower());
        model.getPlayer().addTowerCounter();
        model.getPlayer().setSelectedTower(null);
        setInfoMesseageInUiThread("");
        return true;
    }

    private void setInfoMesseageInUiThread(String infoMesseage) {
        final String finallInfoMesseage = infoMesseage;
        gameViewActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        gameViewActivity.infoText.setText(finallInfoMesseage);
                    }
                });
    }

    public GameThread getGameThread() {
        return gameThread;
    }
}
