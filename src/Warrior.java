
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

    double getRadius() {
        return radius;
    }

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

    void reduceEnergy(int a) {
        energy = energy - a;
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

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        List<Alien> deadAliens = new ArrayList<>();
        if (!aliens.isEmpty()) {
            Alien min = findClosest(aliens);
            int maxBullet = this.getShootingSpeed();
            for (int numBullet = 0; numBullet < maxBullet; numBullet++) {
                if (!min.isCanFly()) {
                    min.stop();
                    min.reduceEnergy(this.powerOfBullet);
                    if (min.isDead()) {
                        deadAliens.add(min);
                        aliens.remove(min);
                        numKilled++;
                        if (aliens.isEmpty()) {
                            return deadAliens;
                        } else {
                            min = findClosest(aliens);
                        }
                        //System.out.println("killed " + min.getName());
                    }
                }
            }
            min.shoot(this);
        }
        return deadAliens;
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
