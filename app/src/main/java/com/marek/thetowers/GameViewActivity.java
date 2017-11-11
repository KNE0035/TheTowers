package com.marek.thetowers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.Purchasable;
import com.marek.thetowers.model.defenseTowers.Cannon;
import com.marek.thetowers.model.defenseTowers.DefenseTower;
import com.marek.thetowers.model.defenseTowers.MachineGun;
import com.marek.thetowers.model.defenseTowers.MissileTower;
import com.marek.thetowers.model.defenseTowers.PhotonCannon;
import com.marek.thetowers.model.units.Behemoth;
import com.marek.thetowers.model.units.HardenedTank;
import com.marek.thetowers.model.units.QuadBike;
import com.marek.thetowers.model.units.Tank;
import com.marek.thetowers.model.units.Unit;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class GameViewActivity extends AppCompatActivity {

    private GameView gameView;

    public TextView moneyText = null;
    public TextView scoreText = null;
    public TextView hitPointText = null;
    public TextView infoText = null;
    private ConstraintLayout gameActivityLayout;
    private PopupWindow gameEndPopUp;
    private Button closeGameButton;
    private TextView endGameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        moneyText = (TextView) findViewById(R.id.moneyText);
        scoreText = (TextView) findViewById(R.id.scoreText);
        infoText = (TextView) findViewById(R.id.infoText);
        hitPointText = (TextView) findViewById(R.id.hitPointText);
        gameView = (GameView) findViewById(R.id.gameView);
        gameView.setGameViewActivity(this);
        gameActivityLayout = (ConstraintLayout) findViewById(R.id.gameActivity);


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.end_game_pop_up,null);
        gameEndPopUp = new PopupWindow(
                customView,
                600,
                400
        );

        closeGameButton = (Button) customView.findViewById(R.id.closeGameButton);
        closeGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int highScore = Integer.parseInt(getResources().getString(R.string.high_score));

                if(highScore < gameView.getModel().getPlayer().getScore()){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(getString(R.string.high_score), gameView.getModel().getPlayer().getScore());
                    editor.commit();
                }

                gameEndPopUp.dismiss();
                onBackPressed();
            }
        });

        endGameText = (TextView) customView.findViewById(R.id.endGameText);

    }

    public void tankButtonClicked(View v) {
        Purchasable tank = new Tank(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
        if(!checkPlayerMoney(tank, v)){
            return;
        }

        if (gameView.getModel().getPlayerUnitGenerator().isReady()) {
            pushPlayerUnit((Unit) tank);
            infoText.setText("Tank pushed");
        }
    }

    public void quadButtonClicked(View v) {
        Purchasable quadBike = new QuadBike(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
        if(!checkPlayerMoney(quadBike, v)){
            return;
        }

        if (gameView.getModel().getPlayerUnitGenerator().isReady()) {
            pushPlayerUnit((Unit) quadBike);
            infoText.setText("Quad bike pushed");
        }
    }

    public void hardenedTankButtonClicked(View v) {
        Purchasable hardenedTank = new HardenedTank(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
        if(!checkPlayerMoney(hardenedTank, v)){
            return;
        }

        if (gameView.getModel().getPlayerUnitGenerator().isReady()) {
            pushPlayerUnit((HardenedTank)hardenedTank);
            infoText.setText("Hardened tank pushed");
        }
    }

    public void behemothButtonClicked(View v) {
        Purchasable behemoth = new Behemoth(new PointF(GameUtil.dip2px(15), 0), gameView.getModel().getPlayerPath(), 0, false, gameView.getModel().getActiveObjects());
        if(!checkPlayerMoney(behemoth, v)){
            return;
        }

        if (gameView.getModel().getPlayerUnitGenerator().isReady()) {
            pushPlayerUnit((Unit)behemoth);
            infoText.setText("Behemoth pushed");
        }
    }

    private boolean checkPlayerMoney(Purchasable purchasable, View v){
        if (gameView.getModel().getPlayer().getCash() < purchasable.getPrice()) {
            infoText.setText("Not enugh money");
            return false;
        }
        return true;
    }

    public void cannonButtonClicked(View view) {
        PointF zeroPoint = new PointF(0,0);
        Purchasable defenseTower = new Cannon(zeroPoint, zeroPoint, 0, null, GameThread.WINDOW_GAP_MILLIS, null, false);
        if(!checkPlayerMoney(defenseTower , view)){
            return;
        }
        gameView.getModel().getPlayer().setSelectedTower((DefenseTower) defenseTower);
        infoText.setText("Cannon selected");
    }

    public void machineGunButtonClicked(View view) {
        PointF zeroPoint = new PointF(0,0);
        Purchasable defenseTower = new MachineGun(zeroPoint, zeroPoint, 0, null, GameThread.WINDOW_GAP_MILLIS, false);
        if(!checkPlayerMoney(defenseTower , view)){
            return;
        }
        gameView.getModel().getPlayer().setSelectedTower((DefenseTower) defenseTower);
        infoText.setText("Machine gun selected");
    }

    public void missleTowerButtonClicked(View view) {
        PointF zeroPoint = new PointF(0,0);
        Purchasable defenseTower = new MissileTower(zeroPoint, zeroPoint, 0, null, GameThread.WINDOW_GAP_MILLIS, null, false);
        if(!checkPlayerMoney(defenseTower , view)){
            return;
        }
        gameView.getModel().getPlayer().setSelectedTower((DefenseTower) defenseTower);
        infoText.setText("Missile tower selected");
    }

    public void photonCannonButtonClicked(View view) {
        PointF zeroPoint = new PointF(0,0);
        Purchasable defenseTower = new PhotonCannon(zeroPoint, zeroPoint, 0, null, GameThread.WINDOW_GAP_MILLIS, null, false);
        if(!checkPlayerMoney(defenseTower , view)){
            return;
        }
        gameView.getModel().getPlayer().setSelectedTower((DefenseTower) defenseTower);
        infoText.setText("Photon cannon selected");
    }

    public void pushPlayerUnit(Unit unit) {
        gameView.getModel().getPlayer().minusCash(unit.getPrice());
        gameView.getModel().getPlayerUnitGenerator().setUnitToPop(unit);
    }

    public void endGame(String endGameText) {
        gameEndPopUp.showAtLocation(gameActivityLayout, Gravity.NO_GRAVITY, getWindow().getDecorView().getWidth() / 2 - 350,getWindow().getDecorView().getHeight() / 2 - 100);
        this.endGameText.setText(endGameText);
        gameView.getGameThread().setRunning(false);
    }
}
