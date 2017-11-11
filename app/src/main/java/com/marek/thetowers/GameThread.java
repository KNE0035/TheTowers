package com.marek.thetowers;

import android.graphics.Canvas;

import com.marek.thetowers.model.Model;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.projectiles.Projectile;
import com.marek.thetowers.model.units.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek on 06/11/2017.
 */

class GameThread extends Thread {
    private GameView gameView;
    private static boolean RUNNING = false;
    private Model model;
    public static final int WINDOW_GAP_MILLIS = 20;

    public GameThread(GameView gameView) {
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        this.RUNNING = running;
    }

    @Override
    public void run() {
        while (RUNNING) {
            Canvas c = null;

            try {
                c = gameView.getHolder().lockCanvas();

                synchronized (gameView.getHolder()) {
                    nextCycle();
                    gameView.onDraw(c);
                    Thread.sleep(WINDOW_GAP_MILLIS);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    gameView.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setModel(Model model) {
        this.model = model;
    }

    private void nextCycle() {
        synchronized (model) {
            model.getEnemyUnitGenerator().tryGenerateEnemy();
            model.getPlayerUnitGenerator().popUnit();
            model.getEnemyTowerGenerator().tryGenerateTower();
            model.getAutomaticMoneyGenerator().tryGenerateMoney();
            List<ModelObject> objectsToDelete = new ArrayList<>();
            int playerHitpointCounter = 0;
            int enemyHitPointCounter = 0;
            for (ModelObject object : model.getActiveObjects()) {
                object.process();
                if (object.isOutOfSpace()) {
                    objectsToDelete.add(object);
                    if (((Unit) object).isEnemy()) {
                        playerHitpointCounter++;
                        continue;
                    } else {
                        enemyHitPointCounter++;
                        continue;
                    }

                }
                if (object instanceof Unit && ((Unit) (object)).getActualHitPoints() <= 0) {
                    objectsToDelete.add(object);
                    if (((Unit) object).isEnemy()) {
                        model.getPlayer().addScore(50);
                        model.getPlayer().addCash(((Unit) object).getPrice() / 4);
                    } else {
                        model.getEnemy().addScore(50);
                        model.getEnemy().addCash(((Unit) object).getPrice() / 2);
                    }
                }
            }
            model.getActiveObjects().removeAll(objectsToDelete);
            model.getPlayer().minusHP(playerHitpointCounter);
            model.getEnemy().minusHP(enemyHitPointCounter);

            List<ModelObject> projectilesToDelete = new ArrayList<>();
            for (Projectile projectile : model.getProjectiles()) {
                if(projectile == null){
                    System.out.println(".handle()");
                }
                projectile.process();
                if(projectile.isTargetHit()){
                    projectilesToDelete.add(projectile);
                }
            }
            model.getProjectiles().removeAll(projectilesToDelete);

        }
    }

    public static boolean isRUNNING() {
        return RUNNING;
    }
}
