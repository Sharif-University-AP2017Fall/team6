
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
        List<Alien> canShoot = new ArrayList<>();
        List<Alien> deadAliens = new ArrayList<>();
        for (int i = 0; i < aliens.size(); i++){
            if (!this.isOnAirOnly() || (this.isOnAirOnly() && aliens.get(i).isCanFly())){
                canShoot.add(aliens.get(i));
            }
        }

        //int alienNum = aliens.size();
        int numBullet = 0;
        for (int i = 0; i < canShoot.size(); i++){
            //int numForEach = (int) Math.ceil(getSpeedOfBullet() / alienNum);
            Alien alienToShoot = canShoot.get(i);
            if (!alienToShoot.isDead()){
                alienToShoot.reduceSpeed(this.getSpeedReduction() / 100);
                if (alienToShoot.isCanFly()){
                    alienToShoot.reduceEnergy(this.getPowerOfBulletAir());
                }else{
                    alienToShoot.reduceEnergy(this.getPowerOfBullet());
                }
                if (alienToShoot.isDead()){
                //    System.out.println(alienToShoot.getName() + " died.");
                    deadAliens.add(alienToShoot);
                }
                numBullet++;
            }
            if (numBullet >= getSpeedOfBullet()){
                break;
            }else if (numBullet < getSpeedOfBullet() && i == canShoot.size() - 1){
                i = -1;
            }
            if (canShoot.size() == deadAliens.size()){
                break;
            }
        }
        return deadAliens;
    }
}
