
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

    public WeaponNearest(Dimension dimension, String type) {
        super(dimension, type);
    }
    @Override
    public List<Alien> shoot(List<Alien> aliens){
        List<Alien> canShoot = new ArrayList<>();
        if (this.isOnAirOnly()){
            for (int i = 0; i < aliens.size(); i++){
                if (aliens.get(i).isCanFly()){
                    canShoot.add(aliens.get(i));
                }
            }
        }else{
            canShoot.addAll(aliens);
        }
        if (!canShoot.isEmpty()){
            Alien min = canShoot.get(0);
            Dimension shootingPoint = this.getShootingPoint();
            double distance = shootingPoint.distanceFrom(min.getDimension());
            for (int i = 1; i < canShoot.size(); i++){
                if (Double.compare(distance, shootingPoint.distanceFrom(canShoot.get(i).getDimension())) > 0){
                    min = canShoot.get(i);
                }
            }
            int maxBullet = this.getSpeedOfBullet();
            for (int numBullet = 0; numBullet < maxBullet; numBullet++){
                min.reduceSpeed(this.getSpeedReduction() / 100);
                if (min.isCanFly()){
                    min.reduceEnergy(this.getPowerOfBulletAir());
                }else{
                    min.reduceEnergy(this.getPowerOfBullet());
                }
                if (min.isDead()){
                    List<Alien> deadAlien = new ArrayList<>();
                    deadAlien.add(min);
                    return deadAlien;
                }
            }
        }
        return null;
    }
}
