
import javafx.application.Platform;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//import sun.jvm.hotspot.gc_implementation.g1.G1HeapRegionTable;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

import java.util.*;

public class Alien implements Movable, Comparable, Runnable {

    /*** CLASS PARAMETERS ***/
    private static int NUM = 0;
    private static int MAXNUM = 5;//5;
    private static boolean START = false;
    private static ArrayList<Alien> deadAliens = new ArrayList<>();

    /**** PROPERTIES ****/
    private String name;
    private Dimension currentDim;
    private int energy;
    private int initialEnergy;
    private double speed;
    private int initialSpeed;
    private int shootingSpeed;
    private int strength;
    private int type;
    private boolean canFly;

    private long threadID;

    /**** STATE ****/
    private boolean shouldShoot;
    Warrior toShoot;

    private boolean shouldMove;
    Dimension moveTo;
    private int cycleNumLeft;
    private int cycleNum;

    /*** VIEW ***/
    private AlienView alienView;
    private ProgressBar progressBar;

    private static HashMap<String,InitialAlien> initialAlien = new HashMap<>();
    private static ArrayList<String> initialAlienName = new ArrayList<>();
    
    static{
    
    initialAlien.put("Albertonion".toLowerCase(), new InitialAlien("Albertonion".toLowerCase()));
    initialAlienName.add("Albertonion".toLowerCase());
    
    initialAlien.put("Algwasonion".toLowerCase(), new InitialAlien("Algwasonion".toLowerCase()));
    initialAlienName.add("Algwasonion".toLowerCase());
    
    initialAlien.put("Activionion".toLowerCase(), new InitialAlien("Activionion".toLowerCase()));
    initialAlienName.add("Activionion".toLowerCase());
    
    initialAlien.put("Aironion".toLowerCase(), new InitialAlien("Aironion".toLowerCase()));
    initialAlienName.add("Aironion".toLowerCase());
    }
    
    
    public static HashMap<String,InitialAlien> getHashMap(){
    
        return initialAlien;
    
    }
    
    public static ArrayList<String> getInitialAlienName(){
        return initialAlienName;
        
    }
    
    
    static void addNewDefinedAlien(String name,InitialAlien Taze){
        
        initialAlien.put(name.toLowerCase(), Taze);
        initialAlienName.add(name.toLowerCase());
        
        
    }
    /***** changing static Initial Alien  ******/
    static void changeInitialEnergy(String name,int changed){
        
        initialAlien.get(name.toLowerCase()).setInitialEnergy(changed);
    }
    
    static void changeInitialSpeed(String name,int changed){
        
        initialAlien.get(name.toLowerCase()).setInitialSpeed(changed);
    }
    
    static void changeInitialShootingSpeed(String name,int changed){
        
        initialAlien.get(name.toLowerCase()).setShootingSpeed(changed);
    
    }
    
    static void changeInitialStrength(String name,int changed){
        
        initialAlien.get(name.toLowerCase()).setStrength(changed);
    
    }
    
    static void changeCanFly(String name,boolean changed ){
        
        initialAlien.get(name.toLowerCase()).setCanFly(changed);
    
    }
    
    static void changeDeactivate(String name ){
        
        initialAlien.remove(name.toLowerCase());
        initialAlienName.remove(name.toLowerCase());
    
    }
    
    /***  Asking for initial fields of Alien  ***/
    
    static int getInitialEnergy(String name){
        
        return initialAlien.get(name.toLowerCase()).getInitialEnergy();
    }
    
    static int getInitialSpeed(String name){
        
        return initialAlien.get(name.toLowerCase()).getInitialSpeed();
    }
    
    static int getInitialShootingSpeed(String name){
        
        return initialAlien.get(name.toLowerCase()).getShootingSpeed();
    
    }
    
    static int getInitialStrength(String name){
        
        return initialAlien.get(name.toLowerCase()).getStrength();
    
    }
    
    static boolean getCanFly(String name ){
        
        return initialAlien.get(name.toLowerCase()).isCanFly();
    
    }
    

    
    
    
    
    
    
    
    
    /*** کد های قبلی ****/

    BulletView bulletView;
     
    
    public static boolean addDeadAliens(Alien rip){
        if (!deadAliens.contains(rip)){
            deadAliens.add(rip);
            reduceNum(1);
            if (Alien.NUM == 0){
                if (AlienCreeps.getCurrentHour() > 1){ //hour > 1
                    AlienCreeps.endGame(false);
                }
            }
            return true;
        }
        return false;
    }
    
    Alien(String name) {
        currentDim = new Dimension(0.0, 0.0);
        alienView = new AlienView("aliens", name);
        bulletView = new BulletView();

        progressBar = new ProgressBar("health");
        progressBar.setDim(GameMap.XBOUND + 15, GameMap.YBOUND - 55);

        this.cycleNumLeft = 10;
        this.cycleNum = 10;
        NUM++;
        START = true;
        this.name = name.toLowerCase();
        
        this.energy =  initialAlien.get(name).getInitialEnergy();
        this.initialEnergy = initialAlien.get(name).getInitialEnergy();
        this.speed = initialAlien.get(name).getInitialSpeed(); //8
        this.initialSpeed = initialAlien.get(name).getInitialSpeed(); //8
        this.shootingSpeed = initialAlien.get(name).getShootingSpeed();
        this.strength = initialAlien.get(name).getStrength();
        this.type = initialAlien.get(name).getType();
        this.canFly = initialAlien.get(name).isCanFly();
        

        this.shouldMove = false;
        this.shouldShoot = false;
    }



    /***** METHODS ****/

    void reduceSpeed(double reductionPercentage) {
        double reduceAmount = (this.speed * reductionPercentage);
        System.out.println("reducnig. prev Speed = " + this.speed);
        this.speed -= reduceAmount;
        System.out.println("new speed = " + this.speed);
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
            /*System.out.println("••••••••••••");
            System.out.println(this.weaponName);
            System.out.println("••••••••••••");
            System.out.println("current energy : " + this.energy);
            System.out.println("reduction amount : " + amount);
            System.out.println("new energy : " + (this.energy - amount));*/
            this.energy -= amount;
            getProgressBar();
        }
    }


    public Dimension getCurrentDim() {
        return currentDim;
    }

    public void setCurrentDim(Dimension currentDim) {
        this.currentDim = currentDim;
    }

    boolean isDead() {
        if (energy<=0){
            
            //alienView.dead();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    AlienCreeps.removeElementFromGameRoot(alienView);
                    AlienCreeps.removeElementFromGameRoot(progressBar);
                    AlienCreeps.removeElementFromGameRoot(bulletView);
                }
            });
            return true;
            
        }
        
        return false;
        
    }

    /*** IMPLEMENTED METHODS ***/

    @Override
    public void move(Dimension dimension) {
    //    System.out.println(weaponName);
//        System.out.println(System.currentTimeMillis());

        Dimension.correctDim(dimension);
        alienView.move(Dimension.deltaX(currentDim, dimension),
                        Dimension.deltaY(currentDim, dimension));

        setCurrentDim(dimension);

  //      System.out.println("moved to" + dimension);

   //     System.out.println("*********************");
    }

    @Override
    public String toString() {
        return "weaponName: " + name + "\tplace: " + currentDim + "\tenergy left: " + energy;
    }

    private Object lock2 = new Object();

    boolean shoot(Warrior warrior) {
        int maxBullet = shootingSpeed;
        for (int i = 0; i < maxBullet; i++) {
            try {
                Thread.sleep(1000 / maxBullet - 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                if (warrior != null){
                    bulletView.shoot(currentDim, warrior.getShootingPoint(), 1000 / maxBullet - 55, maxBullet);
                    if (energy > 0) {
                        warrior.reduceEnergy(strength);
                        //       System.out.println(weaponName + " reduced hero's energy");
                    } else {
                        setShoot(false);
                        setToShoot(null);
                        return false;
                    }
                    if (warrior.isDead()) {
                        backToNormalSpeed();
                        setShoot(false);
                        setToShoot(null);
                        return true;
                    }
                }
            }
     //            System.out.println(weaponName + " has " + (maxBullet - i) + " bullets left");
        }
    //     System.out.println(weaponName + " finished shooting");
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

    double getSpeed() {
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

    private final Object lock7 = new Object();

    public void setMoveTo(Dimension moveTo) {
        /*try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
      //  synchronized (lock7){
            if (speed > 0) {
               // System.out.println(weaponName + " SHOULD MOVE TO " + moveTo);
                this.shouldMove = true;
                this.moveTo = moveTo;
            }
       // }
    }

    public boolean isShouldMove() {
        return shouldMove;
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
    
    
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    static void reduceNum(int NUM) {
        Alien.NUM -= NUM;

       // System.out.println("NUM = " + Alien.NUM);
    }

    private Object lock1 = new Object();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
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
         //       deltaX *= (speed / 2.5);
           //     deltaY *= (speed / 2.5);

                int signX = Double.compare(deltaX, 0.0);
                int signY = Double.compare(deltaY, 0.0);

                deltaX = (this.speed * GameMap.UNIT) / 10.0 * signX;
                deltaY = (this.speed * GameMap.UNIT) / 10.0 * signY;
                for (int i = 0; i < cycleNum; i++) {
                    try {
                        Thread.sleep(90); //250
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
                    if (!shouldMove || currentDim.equals(moveTo)) {
                        this.cycleNumLeft--;
                        break;
                    }
                }
                cycleNumLeft = 10;
                shouldMove = false;
            }
            if(isDead()){
                return;
            }
        }
    }

    public int getCycleNumLeft() {
        return cycleNumLeft;
    }

    public AlienView getAlienView() {
        return alienView;
    }

    public long getThreadID() {
        return threadID;
    }

    public void setThreadID(long threadID) {
        this.threadID = threadID;
    }

    public ProgressBar getProgressBar() {
        //System.out.println("getting alien life bar");
        //System.out.println("updating progress bar, initial energy = " + initialEnergy);
        progressBar.setProgress(energy, initialEnergy);
        return progressBar;
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

     private boolean isFocus;

     public AlienView(String name, String alienName){//}, Dimension dim) {
     //    System.out.println("setting view for " + weaponName);

         isFocus = false;

         this.move_down = new ImageView[3];
         this.move_up = new ImageView[3];
         this.move_right = new ImageView[3];
         this.move_left = new ImageView[3];

         move_down_index = 0;
         move_up_index = 0;
         move_right_index = 0;
         move_left_index = 0;

         String FileName=Alien.getHashMap().get(alienName).getFileName();
         
         String address = "res/" + name + "/movement/" + FileName + "/";
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
         /*setOnMouseEntered(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent event) {
                 onSelect();
             }
         });

         setOnMouseExited(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent event) {
                 onDeselect();
             }
         });*/
     }


     private void onSelect(){
         for (int i = 0; i < 3; i ++){
             move_down[i].setEffect(new Glow(10));
             move_up[i].setEffect(new Glow(10));
             move_right[i].setEffect(new Glow(10));
             move_left[i].setEffect(new Glow(10));
         }
     }

     private void onDeselect(){
         for (int i = 0; i < 3; i ++){
             move_down[i].setEffect(new Glow(0));
             move_up[i].setEffect(new Glow(0));
             move_right[i].setEffect(new Glow(0));
             move_left[i].setEffect(new Glow(0));
         }
     }

     public boolean isFocus() {

         return isFocus;
     }

     public void setFocus() {
         isFocus = true;
         onSelect();
     }

     public void setUnfocus() {
         isFocus = false;
         onDeselect();
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


     public void dead(){

         Platform.runLater(new Runnable() {
             @Override
             public void run() {
                 clear();
             }
         });
     }
     
     public void moveRight(double delta) {
    //     System.out.println("RIGHT");
         clear();
         move_right_index++;
         move_right_index %= 3;

         setTranslateX(getTranslateX() + delta);
         move_right[move_right_index].setVisible(true);
     }


     public void moveLeft(double delta) {
    //     System.out.println("LEFT");
         clear();
         move_left_index++;
         move_left_index %= 3;

         setTranslateX(getTranslateX() - delta);
         move_left[move_left_index].setVisible(true);
     }


     public void moveUp(double deltax, double deltay) {
    //     System.out.println("UP");
         clear();
         move_up_index++;
         move_up_index %= 3;
         setTranslateX(getTranslateX() + deltax);
         setTranslateY(getTranslateY() + deltay);
         move_up[move_up_index].setVisible(true);
     }


     public void moveDown(double deltax, double deltay) {
   //      System.out.println("DOWN");
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

    //    System.out.println("DELTAX = " + deltaX + " DELTAY = " + deltaY);

         double finalDeltaY = deltaY;
         double finalDeltaX = deltaX;
         Platform.runLater(() -> {
             if (Double.compare(finalDeltaY, 0) == 0) {
                 if (Double.compare(finalDeltaX, 0) != 0){
                     if (finalDeltaX > 0) {
                         moveRight(finalDeltaX);
                         return;
                     } else {
                         moveLeft(finalDeltaX);
                     }
                 }
             }
             else if (finalDeltaY > 0) {
                 moveDown(finalDeltaX, finalDeltaY);
             }
             else {
                 moveUp(finalDeltaX, finalDeltaY);
             }
         });
     }

     public ImageView[] getMove_down() {
         return move_down;
     }
 }

class ProgressBar extends StackPane{

    ImageView[] progress = new ImageView[9];

    ProgressBar(String kind){
        for (int i = 0; i < 9; i++){
            progress[i] = new ImageView(new Image(getClass()
                    .getResource("res/progressbar/" + kind + "/" + i + ".png").toExternalForm()));
            progress[i].setVisible(false);
            progress[i].setFitWidth(180 - 30);
            progress[i].setFitHeight(50 - 5);
        }
      //  progress[0].setVisible(true);


        getChildren().addAll(progress);
        //relocate(GameMap.XBOUND + 15, GameMap.YBOUND - 55);
    }

    public void setProgress(int current, int max){

        double percent = (((double) current) / max) * 100;
        //System.out.println(percent + " = " + current + " / " + max);
        //System.out.println("energy percent = " + percent);
        for (int i = 9; i >= 1; i--){
            if (percent >= i * 10){
                //System.out.println("energy is higher than " + (i * 10));
               // System.out.println("setting picture " + (i - 1) + " visible");
                clearRest(i - 1);
                progress[i - 1].setVisible(true);
                return;
            }
        }
        progress[0].setVisible(true);
        clearRest(0);
    }

    private void clearRest(int visible){
        for (int i = 0; i < 9; i++){
            if (i != visible){
               // System.out.println("setting picture " + i + " invisible");
                progress[i].setVisible(false);
            }
        }
    }

    public void setDim(double x, double y){
        relocate(x, y);
    }

    public void initBar(){
        progress[8].setVisible(true);
    }



}




class InitialAlien {

    
    private int initialEnergy;
    private int initialSpeed;
    private int shootingSpeed;
    private int strength;
    private int type;
    private boolean canFly;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    InitialAlien(int initialEnergy,int initialSpeed,int shootingSpeed,int strength,boolean canFly,String fileName_){
        
        
        setInitialEnergy(initialEnergy);
        setInitialSpeed(initialSpeed);
        setShootingSpeed(shootingSpeed);
        setStrength(strength);
        setCanFly(canFly);
        fileName=fileName_;
    
    }
    
    InitialAlien(int initialEnergy,int initialSpeed,int shootingSpeed,int strength,String fileName_){
        
        
        setInitialEnergy(initialEnergy);
        setInitialSpeed(initialSpeed);
        setShootingSpeed(shootingSpeed);
        setStrength(strength);
        setCanFly(false);
        fileName=fileName_;
    
    }
    
    public int getInitialEnergy() {
        return initialEnergy;
    }

    public void setInitialEnergy(int initialEnergy) {
        this.initialEnergy = initialEnergy;
    }

    public int getInitialSpeed() {
        return initialSpeed;
    }

    public void setInitialSpeed(int initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public int getShootingSpeed() {
        return shootingSpeed;
    }

    public void setShootingSpeed(int shootingSpeed) {
        this.shootingSpeed = shootingSpeed;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCanFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }
    
    
    
    InitialAlien(String name){
        
        
    switch (name.toLowerCase()) {
            case "albertonion":
                //this.energy = 250;
                this.initialEnergy = 250;
                //this.speed = 4; //8
                this.initialSpeed = 5; //8
                this.shootingSpeed = 5;
                this.strength = 7;
                this.type = 0;
                this.canFly = false;
                break;
            case "algwasonion":
                //this.energy = 150;
                this.initialEnergy = 150;
                //this.speed = 3; //4
                this.initialSpeed = 3; //4
                this.shootingSpeed = 10;
                this.strength = 25;
                this.type = 1;
                this.canFly = false;
                break;
            case "activionion":
                //this.energy = 400;
                this.initialEnergy = 400;
                this.initialSpeed = 2; //2
                this.shootingSpeed = 2;
                this.strength = 40;
                this.type = 2;
                this.canFly = false;
                break;
            case "aironion":
                //this.energy = 200;
                this.initialEnergy = 200;
                //this.speed = 2; //5
                this.initialSpeed = 4; //5
                this.shootingSpeed = 5;
                this.strength = 20;
                this.type = 3;
                this.canFly = true;
                break;
        }
    
    fileName=name;
    
    }


    
}






























































