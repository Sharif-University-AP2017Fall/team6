
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
        Alien min = aliens.get(0);
        double dist = this.getShootingPoint().distanceFrom(min.getDimension());
        int n = aliens.size();
        for (int i = 1; i < n; i++){
            if (!this.isOnAirOnly() || (this.isOnAirOnly() && aliens.get(i).isCanFly())){
                if (dist > this.getShootingPoint().distanceFrom(aliens.get(i).getDimension())){
                    min = aliens.get(i);
                }
            }
        }
        if (min != null){
            if (!this.isOnAirOnly() || (this.isOnAirOnly() && min.isCanFly())){
                for (int numBullet = 0; numBullet < getSpeedOfBullet(); numBullet++){
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
        }
        return null;
    }
}
