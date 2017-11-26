/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marek.thetowers.model.defenseTowers;

import android.graphics.PointF;

import com.marek.thetowers.model.GameUtil;
import com.marek.thetowers.model.enviromentLogic.ModelObject;
import com.marek.thetowers.model.projectiles.CannonBall;
import com.marek.thetowers.model.projectiles.Missile;
import com.marek.thetowers.model.projectiles.PhotonBall;
import com.marek.thetowers.model.projectiles.Projectile;
import com.marek.thetowers.model.projectiles.ProjectileType;
import com.marek.thetowers.model.units.ArmorType;
import com.marek.thetowers.model.units.Unit;

import java.util.List;

/**
 * @author marek
 */
public abstract class DefenseTowerWithProjectile extends DefenseTower {

    private List<Projectile> projectiles;
    private final ProjectileType projectileType;

    DefenseTowerWithProjectile(PointF position, PointF imageOffset, float direction, int radius, int price, List<ModelObject> activeObjects, int rateOfFire, int windowGapMillis, ProjectileType projectileType, List<Projectile> projectiles, boolean enemy, final int damage, final ArmorType armorCounter) {
        super(position, imageOffset, direction, radius, price, activeObjects, rateOfFire, windowGapMillis, enemy, damage, armorCounter);
        this.projectiles = projectiles;
        this.projectileType = projectileType;
    }

    /**
     * Podle daného projectilu věže vytvoří instanci aktivního projectilu.
     */
    private Projectile resolveProctileTypeAndCreateProjectile(ProjectileType type) {
        PointF toPoint = this.lockedEnemy.getPosition();
        PointF speedVector = new PointF(toPoint.x - this.getPosition().x, toPoint.y - this.getPosition().y);
        speedVector = GameUtil.normalizePoint(speedVector);
        speedVector = new PointF(speedVector.x * 5, speedVector.y * 5);
        PointF projectileStartPosition;
        projectileStartPosition = new PointF(this.getPosition().x + (speedVector.x * 7), this.getPosition().y + (speedVector.y * 7));
        if (null != type) switch (type) {
            case Cannon_Ball:
                CannonBall ball = new CannonBall(projectileStartPosition, new PointF(0, 0), (Unit) this.lockedEnemy, this.damage);
                ball.setDirectionToPoint(this.lockedEnemy.getPosition());
                return ball;
            case Missile:
                Missile missile = new Missile(projectileStartPosition, new PointF(0, 0), (Unit) this.lockedEnemy, this.damage);
                missile.setDirectionToPoint(this.lockedEnemy.getPosition());
                return missile;
            case Photon_Ball:
                projectileStartPosition = this.getPosition();
                PhotonBall photonBall = new PhotonBall(projectileStartPosition, new PointF(0, 0), (Unit) this.lockedEnemy, this.damage);
                photonBall.setDirectionToPoint(this.lockedEnemy.getPosition());
                return photonBall;
            default:
                break;
        }
        return null;
    }

    /**
     * Zkontroluje, zda má na dostřel nějakou nepřátelskou jednotku, pokud ano lockne ji a začne vytvářet instance projectilu (v definovan0m časovém intervalu podel kadence) které poletí na jednotku.
     */
    @Override
    public void process() {
        if (lockedEnemy == null || lockedEnemy.isOutOfSpace() || lockedEnemy.getActualHitPoints() <= 0) {
            for (ModelObject item : this.activeObjects) {
                if (item instanceof Unit) {
                    if ((!((Unit) item).isEnemy() && !this.enemy) || (((Unit) item).isEnemy() && this.enemy)) {
                        continue;
                    }
                    if (Math.sqrt(Math.pow(item.getPosition().x - this.getPosition().x, 2) + Math.pow(item.getPosition().y - this.getPosition().y, 2)) <= this.radius) {
                        this.lockedEnemy = (Unit) item;
                        this.setDirectionToPoint(item.getPosition());
                        break;
                    }
                }
            }
        } else {
            if (Math.sqrt(Math.pow(this.lockedEnemy.getPosition().x - this.getPosition().x, 2) + Math.pow(this.lockedEnemy.getPosition().y - this.getPosition().y, 2)) <= this.radius) {
                this.setDirectionToPoint(this.lockedEnemy.getPosition());

                if (this.rateOfFireCounter == this.numberOfCycleToFire) {
                    Projectile projectile = resolveProctileTypeAndCreateProjectile(projectileType);
                    this.projectiles.add(projectile);
                    if(getFireSound() != null){
                        getFireSound().seekTo(0);
                        getFireSound().start();
                    }

                    this.rateOfFireCounter = 0;
                }
            } else {
                this.lockedEnemy = null;
            }

        }
        if (this.rateOfFireCounter != this.numberOfCycleToFire) {
            this.rateOfFireCounter++;
        }
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    @Override
    public abstract int getViewIdentifikator();
}