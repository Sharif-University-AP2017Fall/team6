
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//import sun.jvm.hotspot.gc_implementation.g1.G1HeapRegionTable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;
public class Alien implements Movable, Comparable, Runnable {

    /*** CLASS PARAMETERS ***/
    private static int NUM = 0;
    private static int MAXNUM = 10;
    private static boolean START = false;

    /**** PROPERTIES ****/
    private String name;
    private Dimension currentDim;
    private int energy;
    private int speed;
    private int initialSpeed;
    private int shootingSpeed;
    private int strength;
    private int type;
    private boolean canFly;

    /**** STATE ****/
    private boolean shouldShoot;
    Warrior toShoot;

    private boolean shouldMove;
    Dimension moveTo;

    /*** VIEW ***/
    private AlienView alienView;
    
    Alien(String name) {
        currentDim = new Dimension(0.0, 0.0);
        alienView = new AlienView("aliens", name);
        NUM++;
        START = true;
        this.name = name;// + NUM; //TODO delete NUM from this
        switch (name) {
            case "Albertonion":
                this.energy = 250;
                this.speed = 8;
                this.initialSpeed = 8;
                this.shootingSpeed = 5;
                this.strength = 7;
                this.type = 0;
                this.canFly = false;
                break;
            case "Algwasonion":
                this.energy = 150;
                this.speed = 4;
                this.initialSpeed = 4;
                this.shootingSpeed = 10;
                this.strength = 25;
                this.type = 1;
                this.canFly = false;
                break;
            case "Activionion":
                this.energy = 400;
                this.speed = 2;
                this.initialSpeed = 2;
                this.shootingSpeed = 2;
                this.strength = 40;
                this.type = 2;
                this.canFly = false;
                break;
            case "Aironion":
                this.energy = 200;
                this.speed = 5;
                this.initialSpeed = 5;
                this.shootingSpeed = 5;
                this.strength = 20;
                this.type = 3;
                this.canFly = true;
                break;
        }
        this.shouldMove = false;
        this.shouldShoot = false;
    }

//    void setAlienView(Dimension dim){
    //    this.alienView = new AlienView("aliens", name, dim);
//
  //  }


    /***** METHODS ****/

    void reduceSpeed(double reductionPercentage) {
        int reduceAmount = (int) (this.speed * reductionPercentage);
        this.speed -= reduceAmount;
    }

    private Object lock3 = new Object();

    void stop() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock3) {
            this.speed = 0;
            this.moveTo = this.currentDim;
            this.shouldMove = false;
        }
    }

    void backToNormalSpeed() {
        this.speed = this.initialSpeed;
    }

    private Object lock4 = new Object();

    void reduceEnergy(int amount) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock4) {
            /*System.out.println(this.name);
            System.out.println("*************");
            System.out.println("current energy : " + this.energy);
            System.out.println("reduction amount : " + amount);
            System.out.println("new energy : " + (this.energy - amount));*/
            this.energy -= amount;
        }
    }


    public Dimension getCurrentDim() {
        return currentDim;
    }

    public void setCurrentDim(Dimension currentDim) {
        this.currentDim = currentDim;
    }

    boolean isDead() {
        return energy <= 0;
    }

    /*** IMPLEMENTED METHODS ***/

    @Override
    public void move(Dimension dimension) {
        System.out.println(name);

        Dimension.correctDim(dimension);
            alienView.move(Dimension.deltaX(currentDim, dimension),
                    Dimension.deltaY(currentDim, dimension));
        setCurrentDim(dimension);

        System.out.println(name + " moved to " + currentDim);
        System.out.println("********************••••••••••*****************");
    }

    @Override
    public String toString() {
        return "name: " + name + "\tplace: " + currentDim + "\tenergy left: " + energy;
    }

    private Object lock2 = new Object();

    boolean shoot(Warrior warrior) {
        int maxBullet = shootingSpeed;
        for (int i = 0; i < maxBullet; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                if (energy > 0) {
                    warrior.reduceEnergy(strength);
                    //       System.out.println(name + " reduced hero's energy");
                } else {
                    setShoot(false);
                    setToShoot(null);
                    return false;
                }
                if (warrior.isDead()) {
                    setShoot(false);
                    setToShoot(null);
                    return true;
                }
            }
            //     System.out.println(name + " has " + (maxBullet - i) + " bullets left");
        }
        // System.out.println(name + " finished shooting");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock2) {
            setShoot(false);
            setToShoot(null);
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        Alien otherAlien = ((Alien) o);
        return this.name.compareTo(otherAlien.getName());
    }

    /**** GETTER AND SETTER ****/

    String getName() {
        return name;
    }

    int getSpeed() {
        return speed;
    }

    boolean isCanFly() {
        return canFly;
    }

    public int getShootingSpeed() {
        return shootingSpeed;
    }

    private Object lock5 = new Object();

    public void setShoot(boolean shoot) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock5) {
            this.shouldShoot = shoot;
        }
    }

    private Object lock6 = new Object();

    public void setToShoot(Warrior toShoot) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock6) {
            this.toShoot = toShoot;
        }
    }

    public void setMove(boolean move) {
        this.shouldMove = move;
    }

    public void setMoveTo(Dimension moveTo) {
        if (speed > 0) {
            System.out.println(name + " should move to " + moveTo);
            this.shouldMove = true;
            this.moveTo = moveTo;
        }
    }

    public Dimension getMoveTo() {
        return moveTo;
    }

    /**** STATIC METHODS ****/

    static int getNUM() {
        return NUM;
    }

    static boolean isSTART() {
        return START;
    }

    static int getMAXNUM() {
        return MAXNUM;
    }

    static void reduceNum(int NUM) {
        Alien.NUM -= NUM;
    }

    private Object lock1 = new Object();

    @Override
    public void run() {
        //     System.out.println("Created " + name);

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldShoot) {
                shoot(toShoot);

            }
            if (shouldMove) {
                Dimension moveFrom = currentDim;
                double deltaX = (moveTo.getX() - moveFrom.getX()) / 10;
                double deltaY = (moveTo.getY() - moveFrom.getY()) / 10;

                int signX = Double.compare(deltaX, 0.0);
                int signY = Double.compare(deltaY, 0.0);

                deltaX = (this.speed * GameMap.UNIT) / 10.0 * signX;
                deltaY = (this.speed * GameMap.UNIT) / 10.0 * signY;
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(150); //250
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock1) {
                        double xRemain = moveTo.getX() - currentDim.getX();
                        if (Double.compare(Math.abs(xRemain), Math.abs(deltaX)) < 0){
                            deltaX = xRemain;
                        }

                        double yRemain = (moveTo.getY() - currentDim.getY());
                        if (Double.compare(Math.abs(yRemain), Math.abs(deltaY)) < 0){
                            deltaY = yRemain;
                        }

                        Dimension newDim = new Dimension(currentDim.getX() + deltaX,
                                currentDim.getY() + deltaY);
                        Dimension.correctDim(newDim);

                        if (speed > 0) {
                            move(newDim);
                        }
                    }
                    if (!shouldMove) {
                        System.out.println("reached destination1");
                        break;
                    }

                    if (currentDim.equals(moveTo)){
                        System.out.println("reached destination2");
                        break;
                    }
                }
                if (currentDim.equals(moveTo)) {
                    //      System.out.println(name + " reached destination.");
                    shouldMove = false;
                }
            }
        }
    }

    public AlienView getAlienView() {
        return alienView;
    }
}




 class AlienView extends StackPane {

     private ImageView[] move_down;
     private ImageView[] move_up;
     private ImageView[] move_left;
     private ImageView[] move_right;

     private int move_down_index;
     private int move_up_index;
     private int move_left_index;
     private int move_right_index;

     public AlienView(String name, String alienName){//}, Dimension dim) {
         System.out.println("setting view for " + name);
         this.move_down = new ImageView[3];
         this.move_up = new ImageView[3];
         this.move_right = new ImageView[3];
         this.move_left = new ImageView[3];

         move_down_index = 0;
         move_up_index = 0;
         move_right_index = 0;
         move_left_index = 0;

         String address = "res/" + name + "/movement/" + alienName + "/";
         move_down[0] = new ImageView(new Image(getClass()
                 .getResource(address + "down1.png").toExternalForm()));
         move_down[0].setFitWidth(30);
         move_down[0].setFitHeight(35);
         move_down[0].setVisible(true);

         move_down[1] = new ImageView(new Image(getClass()
                 .getResource(address + "down2.png").toExternalForm()));
         move_down[1].setFitWidth(30);
         move_down[1].setFitHeight(35);
         move_down[1].setVisible(false);

         move_down[2] = new ImageView(new Image(getClass()
                 .getResource(address + "down3.png").toExternalForm()));
         move_down[2].setFitWidth(30);
         move_down[2].setFitHeight(35);
         move_down[2].setVisible(false);

         move_up[0] = new ImageView(new Image(getClass()
                 .getResource(address + "up1.png").toExternalForm()));
         move_up[0].setFitWidth(30);
         move_up[0].setFitHeight(35);
         move_up[0].setVisible(false);

         move_up[1] = new ImageView(new Image(getClass()
                 .getResource(address + "up2.png").toExternalForm()));
         move_up[1].setFitWidth(30);
         move_up[1].setFitHeight(35);
         move_up[1].setVisible(false);

         move_up[2] = new ImageView(new Image(getClass()
                 .getResource(address + "up3.png").toExternalForm()));
         move_up[2].setFitWidth(30);
         move_up[2].setFitHeight(35);
         move_up[2].setVisible(false);

         move_left[0] = new ImageView(new Image(getClass()
                 .getResource(address + "left1.png").toExternalForm()));
         move_left[0].setFitWidth(30);
         move_left[0].setFitHeight(35);
         move_left[0].setVisible(false);

         move_left[1] = new ImageView(new Image(getClass()
                 .getResource(address + "left2.png").toExternalForm()));
         move_left[1].setFitWidth(30);
         move_left[1].setFitHeight(35);
         move_left[1].setVisible(false);

         move_left[2] = new ImageView(new Image(getClass()
                 .getResource(address + "left3.png").toExternalForm()));
         move_left[2].setFitWidth(30);
         move_left[2].setFitHeight(35);
         move_left[2].setVisible(false);

         move_right[0] = new ImageView(new Image(getClass()
                 .getResource(address + "right1.png").toExternalForm()));
         move_right[0].setFitWidth(30);
         move_right[0].setFitHeight(35);
         move_right[0].setVisible(false);

         move_right[1] = new ImageView(new Image(getClass()
                 .getResource(address + "right2.png").toExternalForm()));
         move_right[1].setFitWidth(30);
         move_right[1].setFitHeight(35);
         move_right[1].setVisible(false);

         move_right[2] = new ImageView(new Image(getClass()
                 .getResource(address + "right3.png").toExternalForm()));
         move_right[2].setFitWidth(30);
         move_right[2].setFitHeight(35);
         move_right[2].setVisible(false);

         getChildren().addAll(move_down[0],
                 move_down[1],
                 move_down[2],
                 move_up[0],
                 move_up[1],
                 move_up[2],
                 move_left[0],
                 move_left[1],
                 move_left[2],
                 move_right[0],
                 move_right[1],
                 move_right[2]);
     //    setTranslateX(dim.getX());
       //  setTranslateY(dim.getY());
     }


     private void clear() {
         move_right[0].setVisible(false);
         move_right[1].setVisible(false);
         move_right[2].setVisible(false);
         move_left[0].setVisible(false);
         move_left[1].setVisible(false);
         move_left[2].setVisible(false);
         move_up[0].setVisible(false);
         move_up[1].setVisible(false);
         move_up[2].setVisible(false);
         move_down[0].setVisible(false);
         move_down[1].setVisible(false);
         move_down[2].setVisible(false);
     }


     public void moveRight(double delta) {
         System.out.println("ALIEN IS MOVING RIGHT");
         clear();
         move_right_index++;
         move_right_index %= 3;

         setTranslateX(getTranslateX() + delta);
         move_right[move_right_index].setVisible(true);
        // move_right[move_right_index].setVisible(true);
     }


     public void moveLeft(double delta) {
         System.out.println("ALIEN IS MOVING LEFT");
         clear();
         move_left_index++;
         move_left_index %= 3;

         setTranslateX(getTranslateX() - delta);
         move_left[move_left_index].setVisible(true);
     }


     public void moveUp(double deltax, double deltay) {
         System.out.println("ALIEN IS MOVING UP");
         clear();
         move_up_index++;
         move_up_index %= 3;
         setTranslateX(getTranslateX() + deltax);
         setTranslateY(getTranslateY() + deltay);
         move_up[move_up_index].setVisible(true);
     }


     public void moveDown(double deltax, double deltay) {
         System.out.println("ALIEN IS MOVING DOWN");
         clear();
         move_down_index++;
         move_down_index %= 3;
         setTranslateX(getTranslateX() + deltax);
         setTranslateY(getTranslateY() + deltay);
         move_down[move_down_index].setVisible(true);
     }


     public void move(double deltaX, double deltaY) {

         double dummyX = Math.round(deltaX * 10);
         deltaX = dummyX / 10;

         double dummyY = Math.round(deltaY * 10);
         deltaY = dummyY / 10;
         System.out.println("DELTAX = " + deltaX + "DELTAY = " + deltaY);

         if (Double.compare(deltaY, 0) == 0) {
             if (Double.compare(deltaX, 0) != 0){
                 if (deltaX > 0) {
                     moveRight(deltaX);
                     return;
                 } else {
                     moveLeft(deltaX);
                 }
             }
         }
         else if (deltaY > 0) {
             moveDown(deltaX, deltaY);
         } 
         else {
             moveUp(deltaX, deltaY);
         }
     }
 }





