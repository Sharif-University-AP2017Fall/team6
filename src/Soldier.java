
/**
 *
 * @author Tara
 */
public class Soldier extends Warrior {

    private WarriorView warriorView;

    void increaseRadius() {
        setRadius(getRadius() * 1.1);
    }

    void resetRadius() {
        setRadius(0.5);
    }

    void reduceRadius() {
        setRadius(getRadius() * 0.85);
    }

    Soldier() {
       // warriorView = new WarriorView("soldiers", String.valueOf(number) + "/")
        setRadius(0.5);
        setEnergy(150);
        setShootingSpeed(5);
        setPowerOfBullet(10);
    }

    void showStatus() {
        System.out.println("\tplace: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy());
    }

    public void setWarriorView(int number, Dimension dim) {
        this.warriorView = new WarriorView("soldiers", String.valueOf(number) + "/", dim);
    }

    public WarriorView getWarriorView() {
        return warriorView;
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

        Dimension.correctDim(newDimension);

        setDimension(newDimension);

        double deltaX = changeDimension.getX();
        double deltaY = changeDimension.getY();
        //  if (Double.compare(deltaX, 0.0) == 0) {
        if (deltaY > 0) {
            warriorView.moveDown(deltaY);
        }else if (deltaY < 0){
            warriorView.moveUp(-1 * deltaY);
        }
        //   }

        //    if (Double.compare(deltaY, 0.0) == 0){
        if (deltaX > 0){
            warriorView.moveRight(deltaX);
        }else if (deltaX < 0){
            warriorView.moveLeft(-1 * deltaX);
        }
        //  }
    //    System.out.println("soldier moved to " + newDimension);
    }

    @Override
    public void run() {
        while (true){
            if (shouldShoot){
                killed.addAll(shoot(toShoot));
            }
        }
    }
}
