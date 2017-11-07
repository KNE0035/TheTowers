package com.marek.thetowers;

import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.units.Behemoth;
import com.marek.thetowers.model.units.HardenedTank;
import com.marek.thetowers.model.units.QuadBike;
import com.marek.thetowers.model.units.Tank;
import com.marek.thetowers.model.units.Unit;

public class GameViewActivity extends AppCompatActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        gameView = (GameView) findViewById (R.id.gameView);
    }

    public void tankButtonClicked(View v) {
        synchronized (gameView.getModel()) {
            if (gameView.getModel().getPlayerUnitGenerator().isReady() && gameView.getModel().getPlayer().getCash() > Tank.getPRICE()) {
                Tank tank = new Tank(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
                pushPlayerUnit(tank);
                Toast.makeText(v.getContext(),
                        "Tank pushed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void quadButtonClicked(View v) {
        synchronized (gameView.getModel()) {
            if (gameView.getModel().getPlayerUnitGenerator().isReady() && gameView.getModel().getPlayer().getCash() > Tank.getPRICE()) {
                QuadBike quadBike = new QuadBike(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
                pushPlayerUnit(quadBike);
                Toast.makeText(v.getContext(),
                        "QuadBike pushed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void hardenedTankButtonClicked(View v) {
        synchronized (gameView.getModel()) {
            if (gameView.getModel().getPlayerUnitGenerator().isReady() && gameView.getModel().getPlayer().getCash() > Tank.getPRICE()) {
                HardenedTank hardenedTank = new HardenedTank(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
                pushPlayerUnit(hardenedTank);
                Toast.makeText(v.getContext(),
                        "Hardened tank pushed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void behemothButtonClicked(View v) {
        synchronized (gameView.getModel()) {
            if (gameView.getModel().getPlayerUnitGenerator().isReady() && gameView.getModel().getPlayer().getCash() > Tank.getPRICE()) {
                Behemoth behemoth = new Behemoth(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
                pushPlayerUnit(behemoth);
                Toast.makeText(v.getContext(),
                        "Behemoth pushed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void pushPlayerUnit(Unit unit){
        gameView.getModel().getPlayer().minusCash(unit.getPrice());
        gameView.getModel().getPlayerUnitGenerator().setUnitToPop(unit);
    }
}
