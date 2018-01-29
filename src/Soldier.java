import javafx.application.Platform;
import javafx.print.PageLayout;

import java.util.List;

/**
 *
 * @author Tara
 */
public class Soldier extends Warrior {

    private WarriorView warriorView;
    private BulletView bulletView;
    private ProgressBar healthBar;


    @Override
    public ProgressBar getHealthBar() {
        return healthBar;
    }


    void increaseRadius(double percent) {
        setRadius(getRadius() * (1+percent));
    }

    void resetRadius() {
        setRadius(3);
    }

    void reduceRadius(double a) {
        setRadius(getRadius() * a);
    }

    Soldier() {
       // warriorView = new WarriorView("soldiers", String.valueOf(number) + "/")
        bulletView = new BulletView("soldier");;
        healthBar = new ProgressBar("health");
        healthBar.initBar();

        shouldShoot = false;
        setRadius(3);
        setEnergy(150);
        setMaximumEnergy(150);
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
    public BulletView getBulletView() {
        return this.bulletView;
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
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (AlienCreeps.restart){
                break;
            }

            if (!AlienCreeps.ISPAUSED){
                if (shouldShoot){
                    //    System.out.println("SOLDIER SHOULD SHOOT");
                    List<Alien> fuckthisshit =  shoot(toShoot);
                    if (fuckthisshit != null){
                        if (!fuckthisshit.isEmpty()){

                            killed.addAll(shoot(toShoot));
                        }
                    }
                }
            }

        }
    }
}
