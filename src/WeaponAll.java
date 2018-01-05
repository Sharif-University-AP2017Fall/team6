
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

    WeaponAll(Dimension dimension, String type, int locationNum) {
        super(dimension, type, locationNum);
    }

    private Object lock = new Object();

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        List<Alien> canShoot = new ArrayList<>();
        List<Alien> deadAliens = new ArrayList<>();

        for (int i = 0; i < aliens.size(); i++) {
            if (!this.isOnAirOnly() || (this.isOnAirOnly() && aliens.get(i).isCanFly())) {
                canShoot.add(aliens.get(i));
            }
        }

        int numBullet = 0;
        int maxBullet = getSpeedOfBullet();
        for (int i = 0; i < canShoot.size(); i++) {
            Alien ai = canShoot.get(i);

            if (!ai.isDead()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock){
                    ai.reduceSpeed(this.getSpeedReduction() / 100);
                    if (ai.isCanFly()) {
                        ai.reduceEnergy(this.getPowerOfBulletAir());
                    } else {
                        ai.reduceEnergy(this.getPowerOfBullet());
                    }
                    if (ai.isDead()) {
                        System.out.println(getName() + " killed " + ai.getName());
                        deadAliens.add(ai);
                    }
                    numBullet++;
                }
            }
            if (numBullet >= maxBullet) { //no longer have any bullets
                break;
            } else if (numBullet < getSpeedOfBullet() && i == canShoot.size() - 1) { //still have bullets, start killing from top
                i = -1;
            }
            if (canShoot.size() == deadAliens.size()) { //killed all aliens
                break;
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
}
