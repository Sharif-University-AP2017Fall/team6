
/**
 *
 * @author Tara
 */
public class Soldier extends Warrior {

    void increaseRadius() {
        setRadius(getRadius() * 1.1);
    }

    void resetRadius() {
        //    System.out.println("previous reduced radius = " + getRadius());
        setRadius(0.5);
        //  System.out.println("normal radius = " + getRadius());
    }

    void reduceRadius() {
//        System.out.println("previous radius = " + getRadius());
        setRadius(getRadius() * 0.85);
        //      System.out.println("current radius = " + getRadius());
    }

    Soldier() {
        setRadius(0.5);
        setEnergy(150);
        setShootingSpeed(5);
        setPowerOfBullet(10);
    }

    void showStatus() {
        System.out.println("\tplace: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy());
    }

    @Override
    public String toString() {
        return "\tplace: " + super.getDimension() +
                "\tenergy left : " + super.getEnergy() +
                "\n";
    }

    @Override
    public void move(Dimension changeDimension) {
        Dimension newDimension = new Dimension(getShootingPoint().getX() + changeDimension.getX(),
                getShootingPoint().getY() + changeDimension.getY());

        setDimension(newDimension);
    //    System.out.println("soldier moved to " + newDimension);
    }

    @Override
    public void run() {

    }
}
