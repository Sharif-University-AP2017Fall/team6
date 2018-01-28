
import com.sun.deploy.util.DeployUIManager;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tara
 */
public abstract class Warrior implements Movable, Shooter, Runnable {
    private double radius;
    protected Dimension dimension;

    private int powerOfBullet;
    private int shootingSpeed;
    private int energy;

    private int numKilled;

    protected boolean shouldShoot;
    protected List<Alien> toShoot;
    protected List<Alien> killed;

    double getRadius() {
        return radius;
    }

    public abstract WarriorView getWarriorView();

    public void setShootingSpeed(int shootingSpeed) {
        this.shootingSpeed = shootingSpeed;
    }

    public void setPowerOfBullet(int powerOfBullet) {
        this.powerOfBullet = powerOfBullet;
    }

    public Dimension getDimension() {
        return dimension;
    }

    private int getShootingSpeed() {
        return shootingSpeed;
    }

    int getEnergy() {
        return energy;
    }

    void setRadius(double radius) {
        this.radius = radius;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        //System.out.println("moved to " + dimension);
    }

    void setEnergy(int energy) {
        this.energy = energy;
    }

    boolean isDead() {
        return energy < 0;
    }


    private Object lock2 = new Object();
    void reduceEnergy(int a) {
        //energy = energy - a;
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock2){
            energy -= a;
            /*System.out.println("••••••••••••");
            System.out.println(this.getClass().getName());
            System.out.println("••••••••••••");
            System.out.println("current energy : " + this.energy);
            System.out.println("reduction amount : " + a);
            System.out.println("new energy : " + (this.energy - a));*/
        }

    }

    void increaseBulletPower() {
        this.powerOfBullet = (int) (this.powerOfBullet * 1.1);
    }

    void increaseBulletSpeed() {
        this.shootingSpeed = (int) (this.shootingSpeed * 1.1);
    }

    void correctDim(){
        double x = Math.round(dimension.getX() * 10);
        double y = Math.round(dimension.getY() * 10);
        Dimension newDim = new Dimension(x / 10,
                y / 10);
        setDimension(newDim);
    }
    @Override
    public abstract void move(Dimension dimension);

    private Object lock = new Object();

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        List<Alien> deadAliens = new ArrayList<>();
        if (!aliens.isEmpty()) {
            Alien min = findClosest(aliens);
            int maxBullet = this.getShootingSpeed();

            for (int numBullet = 0; numBullet < maxBullet; numBullet++) {
                try {
                    Thread.sleep(1000 / maxBullet - 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock){
                    if (shouldShoot){
                        if (!min.isCanFly()) {
                            min.stop();
                            min.reduceEnergy(this.powerOfBullet);
                            min.setShoot(true);
                            min.setToShoot(this);
                            //     System.out.println(this.getClass().getName() + "has " + (maxBullet - numBullet) + " bullets left");

                            if (this.isDead()){
                            //    System.out.println(this.getClass().getName() + " died");

                                if (this.getClass().getName().toLowerCase().equals("hero")){
                                    AlienCreeps.gameMap.secondsLeftToResurrectHero = AlienCreeps.gameMap.getHero().getResurrectionTime();

                                } else{
                                    AlienCreeps.gameMap.getHero().removeSoldier(((Soldier) this));
                                    AlienCreeps.gameMap.barrack.requestSoldier(AlienCreeps.gameMap.getHero().getResurrectionTime(), ((Soldier) this));
                                }

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlienCreeps.removeElementFromGameRoot(getWarriorView());
                                    }
                                });

                                stopShooting();
                            /*if (min.isDead()){
                                List<Alien> dummy = new ArrayList<>();
                                dummy.add(min);
                                return dummy;
                            } else {
                                return null;
                            }*/
                            }

                            if (min.isDead()) {
                                min.setShoot(false);
                                min.setToShoot(null);
                                deadAliens.add(min);
                                aliens.remove(min);
                            //    System.out.println(this.getClass().getName() + " killed " + min.getName());

                                /*****/
                                toShoot.remove(min);


                                if (Alien.addDeadAliens(min)) {
                                    List<Alien> dummy = new ArrayList<>();
                                    dummy.add(min);
                                    /***** UPDATING HERO'S ACHIEVEMENTS***/
                                    AlienCreeps.gameMap.updateAchievements(dummy,
                                            this.getClass().getName().toLowerCase());
                                    /***REDUCING NUMBER OF TOTAL ALIENS*/

                                    /****ADDING MONEY AND EXPERIENCE LEVEL TO HERO***/
                                    if (this.getClass().getName().toLowerCase().equals("hero")){
                                        if (AlienCreeps.gameMap.getHero().addExperienceLevel(15)) {
                                            AlienCreeps.gameMap.reduceAllWeaponsPrice();
                                        }
                                        AlienCreeps.gameMap.getHero().addMoney(10);
                                    }else{
                                        if (AlienCreeps.gameMap.getHero().addExperienceLevel(5)) {
                                            AlienCreeps.gameMap.reduceAllWeaponsPrice();
                                        }
                                        AlienCreeps.gameMap.getHero().addMoney(10);
                                    }
                                    /***DELETING THE ALIEN FROM ROUTES***/
                                    for (int i = 0; i < AlienCreeps.gameMap.getRoutes().size(); i++){
                                        AlienCreeps.gameMap.removeAliensFromRoute(AlienCreeps.gameMap.getRoutes().get(i), dummy);
                                    }
                                }


                                /****/

                                numKilled++;
                                if (aliens.isEmpty()) {
                                    //               System.out.println("hero killed all and finished shooting");
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    synchronized (lock){
                                        stopShooting();
                                    }
                                    return deadAliens;
                                } else {
                                    min = findClosest(aliens);
                                }
                                //System.out.println("killed " + min.getName());
                            }
                        } else {
                            //      System.out.println("hero's bullet won't reach " + min.getName());
                            min = findClosest(aliens);
                        }
                    }
                }
            }
            //min.shoot(this);
        }
//        System.out.println("hero finished shooting");
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

    public List<Alien> getKilled() {
        return killed;
    }

    public void resetKilled(){
        killed = new ArrayList<>();
    }

    public void setShouldShoot(List<Alien> toShoot){
        this.shouldShoot = true;
        this.toShoot = toShoot;
        resetKilled();
    }

    public void stopShooting(){
        this.shouldShoot = false;
    }

    public boolean isShouldShoot() {
        return shouldShoot;
    }

    private Alien findClosest(List<Alien> aliens) {
        Alien min = aliens.get(0);
        Dimension shootingPoint = this.getShootingPoint();
        double distance = shootingPoint.distanceFrom(min.getCurrentDim());
        for (int i = 1; i < aliens.size(); i++) {
            double comparable = shootingPoint.distanceFrom(aliens.get(i).getCurrentDim());
            if (distance > comparable) {
                distance = comparable;
                min = aliens.get(i);
            }
        }
        return min;
    }

    @Override
    public boolean isWithinRadius(Dimension dimension) {
        /*System.out.println("hero");
        System.out.println("*****************");
        System.out.println("checking location : " + this.dimension);
        System.out.println("distance : " + this.dimension.distanceFrom(dimension));
        System.out.println("radius : " + this.radius * GameMap.UNIT);
        System.out.println(this.dimension.distanceFrom(dimension) <= this.radius * GameMap.UNIT);*/
        return this.dimension.distanceFrom(dimension) <= this.radius * GameMap.UNIT;
    }

    @Override
    public Dimension getShootingPoint() {
        return this.dimension;
    }
}
