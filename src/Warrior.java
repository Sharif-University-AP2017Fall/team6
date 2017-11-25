
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Tara
 */
public abstract class Warrior implements Movable, Shooter {
    private String name;
    private double radius;
    private Dimension dimension;

    private int powerOfBullet;
    private int shootingSpeed;
    private int energy;

    private int numKilled;

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getPowerOfBullet() {
        return powerOfBullet;
    }

    public int getShootingSpeed() {
        return shootingSpeed;
    }

    public int getEnergy() {
        return energy;
    }

    public int getNumKilled() {
        return numKilled;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setPowerOfBullet(int powerOfBullet) {
        this.powerOfBullet = powerOfBullet;
    }

    public void setShootingSpeed(int shootingSpeed) {
        this.shootingSpeed = shootingSpeed;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isDead() {
        return energy < 0;
    }

    public void reduceEnergy(int a){
        energy = energy-a;
        if (isDead())
            died();
    }

    public void increaseBulletPower(){
        this.powerOfBullet = (int)(this.powerOfBullet * 1.1);
    }

    public void increaseBulletSpeed(){
        this.shootingSpeed = (int)(this.shootingSpeed * 1.1);
    }

    public abstract boolean died();

    @Override
    public abstract void move(Dimension dimension);

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        List<Alien> canShoot = new ArrayList<>();
        for (int i = 0; i < aliens.size(); i++){
            if (!aliens.get(i).isCanFly()){
                canShoot.add(aliens.get(i));
            }
        }
        if (!canShoot.isEmpty()){
            Alien min = canShoot.get(0);
            Dimension shootingPoint = this.getShootingPoint();
            double distance = shootingPoint.distanceFrom(min.getDimension());
            int n = canShoot.size();
            for (int i = 1; i < n; i++){
                if (distance > shootingPoint.distanceFrom(canShoot.get(i).getDimension())){
                    min = canShoot.get(i);
                }
            }
            int maxBullet = this.getShootingSpeed();
            for (int numBullet = 0; numBullet < maxBullet; numBullet++){
                min.stop();
                min.reduceEnergy(this.powerOfBullet);
                if (min.isDead()){
                    List<Alien> deadAlien = new ArrayList<>();
                    deadAlien.add(min);
                    numKilled++;
                    return deadAlien;
                }
            }
            min.shoot(this);
        }
        return null;
    }

    @Override
    public boolean isWithinRadius(Dimension dimension) {
        return this.dimension.distanceFrom(dimension) <= this.radius * GameMap.UNIT;
    }

    @Override
    public Dimension getShootingPoint() {
        return this.dimension;
    }
}
