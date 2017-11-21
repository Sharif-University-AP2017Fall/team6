
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
public class WeaponAll extends Weapon {

    public WeaponAll(Dimension dimension, String type) {
        super(dimension, type);
    }
    @Override
    public List<Alien> shoot(List<Alien> aliens){
        int alienNum = aliens.size();
        int numBullet = 0;
        List<Alien> deadAliens = new ArrayList<>();
        for (int i = 0; i < alienNum; i++){
            Alien alienToShoot = aliens.get(i);
            if (!alienToShoot.isDead()){
                if (!this.isOnAirOnly() || (this.isOnAirOnly() && alienToShoot.isCanFly())){
                    alienToShoot.reduceSpeed(this.getSpeedReduction() / 100);
                    if (alienToShoot.isCanFly()){
                        alienToShoot.reduceEnergy(this.getPowerOfBulletAir());
                    }else{
                        alienToShoot.reduceEnergy(this.getPowerOfBullet());
                    }
                    if (alienToShoot.isDead()){
                        deadAliens.add(alienToShoot);
                    }
                    numBullet++;
                }
            }
            if (numBullet > getSpeedOfBullet()){
                break;
            }
        }
        return deadAliens;
    }
}
