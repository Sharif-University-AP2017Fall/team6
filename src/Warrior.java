
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
public abstract class Warrior implements Movable, Shootable {
    private String name;
    private double radius;
    private Dimension dimension;

    private int powerOfBullet;
    private int speedOfBullet;
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

    public int getSpeedOfBullet() {
        return speedOfBullet;
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

    public void setSpeedOfBullet(int speedOfBullet) {
        this.speedOfBullet = speedOfBullet;
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

    public abstract boolean died();
    public abstract boolean gotShot(Alien a);

    @Override
    public abstract void move(Dimension dimension);

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        Alien min = aliens.get(0);
        double dist = this.getShootingPoint().distanceFrom(min.getDimension());
        int n = aliens.size();
        for (int i = 1;i<n;i++){
            if (dist > this.getShootingPoint().distanceFrom(aliens.get(i).getDimension()))
                min = aliens.get(i);
        }
        for (int numBullet = 0; numBullet < getSpeedOfBullet(); numBullet++){
            min.stop();
            min.reduceEnergy(this.powerOfBullet);
            if (min.isDead()){
                List<Alien> deadAlien = new ArrayList<>();
                deadAlien.add(min);
                numKilled++;
                return deadAlien;
            }
        }
        return null;
    }

    @Override
    public boolean isWithinRadius(Dimension dimension) {
        return this.dimension.distanceFrom(dimension) <= this.radius;
    }

    @Override
    public Dimension getShootingPoint() {
        return this.dimension;
    }
}
