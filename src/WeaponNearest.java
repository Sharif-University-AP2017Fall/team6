
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
public class WeaponNearest extends Weapon {

    WeaponNearest(Dimension dimension, String type, int locationNum) {
        super(dimension, type, locationNum);
    }

    private Object lock = new Object();

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        List<Alien> canShoot = new ArrayList<>();
        List<Alien> deadAliens = new ArrayList<>();

        if (this.isOnAirOnly()) {
            for (int i = 0; i < aliens.size(); i++) {
                if (aliens.get(i).isCanFly()) {
                    canShoot.add(aliens.get(i));
                }
            }
        } else {
            canShoot.addAll(aliens);
        }

        if (!canShoot.isEmpty()) {
            Alien min = findClosest(canShoot);
            int maxBullet = this.getSpeedOfBullet();
            for (int numBullet = 0; numBullet < maxBullet; numBullet++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock){
                    min.reduceSpeed(this.getSpeedReduction() / 100);
                    if (min.isCanFly()) {
                        min.reduceEnergy(this.getPowerOfBulletAir());
                    } else {
                        min.reduceEnergy(this.getPowerOfBullet());
                    }
                    if (min.isDead()) {
                        System.out.println(getName() + " killed " + min.getName());
                        //  System.out.println(min.getName() + " died.");
                        deadAliens.add(min);
                        canShoot.remove(min);
                        if (!canShoot.isEmpty()) {
                            min = findClosest(canShoot);
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            synchronized (lock){
                                stopShooting();
                            }
                            System.out.println("finished shooting");
                            return deadAliens;
                        }
                    }
                }
            }
        }
        System.out.println("finished shooting");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock){
            stopShooting();
        }
        return deadAliens;
    }

    private Alien findClosest(List<Alien> aliens) {
        Alien min = aliens.get(0);
        Dimension shootingPoint = this.getShootingPoint();
        double distance = min.getCurrentDim().distanceFrom(shootingPoint);
        for (int i = 1; i < aliens.size(); i++) {
            double comparable = shootingPoint.distanceFrom(aliens.get(i).getCurrentDim());
            if (distance > comparable) {
                distance = comparable;
                min = aliens.get(i);
            }
        }
        return min;
    }
}
