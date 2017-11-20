
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
    public  boolean applyWeapon(List<Alien> alien){
        int n = alien.size();
        int numBullet = 0;
        for (int i = 0; i < n; i++){
            alien.get(i).gotShot(this);
            numBullet++;
            if (numBullet > getSpeedOfBullet()){
                break;
            }
        }
        return true;
    }
}
