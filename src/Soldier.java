
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
    public boolean move(Dimension changeDimension) {
        Dimension newDimension = new Dimension(getShootingPoint().getX() + changeDimension.getX(),
                getShootingPoint().getY() + changeDimension.getY());
        if (newDimension.isWithinBounds(GameMap.XBOUND, 0, GameMap.YBOUND, 0)) {
            //System.out.println("Soldier moved to " + newDimension);
            setDimension(newDimension);
            return true;
        }
        return false;
    }
}
