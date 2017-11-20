
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
    public  boolean applyWeapon(List<Alien> alien){
        Alien min = alien.get(0);
        double dist = this.getDimension().distanceFrom(min.getDimension());
        int n = alien.size();
        int numBullet = 0;
        for (int i = 1;i<n;i++){
            if (dist > this.getDimension().distanceFrom(alien.get(i).getDimension()))
                min = alien.get(i);
        }
        
        while(numBullet < this.getSpeedOfBullet()){
                   min.gotShot(this); 
                   numBullet++;
                   if(numBullet > getSpeedOfBullet())
                       break;
        }
        return true;
    }
}
