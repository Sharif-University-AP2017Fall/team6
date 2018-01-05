public class Alien implements Movable, Comparable, Runnable {

    /*** CLASS PARAMETERS ***/
    private static int NUM = 0;
    private static int MAXNUM = 25;
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

    Alien(String name) {
        NUM++;
        //System.out.println("******* ****");
        //System.out.println("num = " + NUM);
        START = true;
        this.name = name + NUM; //TODO delete NUM from this
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


    /***** METHODS ****/

    void reduceSpeed(double reductionPercentage) {
        int reduceAmount = (int) (this.speed * reductionPercentage);
        this.speed -= reduceAmount;
    }

    void stop() {
        this.speed = 0;
        this.moveTo = this.currentDim;
        this.shouldMove = false;
    }

    void backToNormalSpeed() {
        this.speed = this.initialSpeed;
    }

    void reduceEnergy(int amount) {
        System.out.println(this.name);
        System.out.println("*************");
        System.out.println("current energy : "  + this.energy);
        System.out.println("reduction amount : " + amount);
        System.out.println("new energy : " + (this.energy - amount));
        this.energy -= amount;
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
        setCurrentDim(dimension);
        System.out.println(name + " moved to " + currentDim);
    }

    @Override
    public String toString() {
        return "name: " + name + "\tplace: " + currentDim + "\tenergy left: " + energy;
    }

    boolean shoot(Warrior warrior) {
        int maxBullet = shootingSpeed;
        for (int i = 0; i < maxBullet; i++) {
            warrior.reduceEnergy(strength);
            System.out.println(name + " reduced hero's energy");
            if (warrior.isDead()) {
                return true;
            }
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

    public void setShoot(boolean shoot) {
        this.shouldShoot = shoot;
    }

    public void setToShoot(Warrior toShoot) {
        this.toShoot = toShoot;
    }

    public void setMove(boolean move) {
        //System.out.println("should move " + name);
        this.shouldMove = move;
    }

    public void setMoveTo(Dimension moveTo) {
        if (speed > 0){
            this.shouldMove = true;
            this.moveTo = moveTo;
        }
      //  System.out.println(name + " should move to " + moveTo);
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

    private Object lock = new Object();

    @Override
    public void run() {
        System.out.println("Created " + name);

        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldShoot){
                int sleepMilliseconds = 5000 / shootingSpeed; //TODO change 5000 later
                try {
                    Thread.sleep(sleepMilliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                shoot(toShoot);

            }if (shouldMove){
              //  System.out.println(name + " started moving to " + moveTo);
                Dimension moveFrom = currentDim;
                double deltaX = (moveTo.getX() - moveFrom.getX()) / 10;
                double deltaY = (moveTo.getY() - moveFrom.getY()) / 10;
                for (int i = 0; i < 10; i++){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock){
                        Dimension newDim = new Dimension(currentDim.getX() + deltaX,
                                currentDim.getY() + deltaY);
                     //   System.out.println("$$$$$$$$");
                      //  System.out.println(name + " has to move to " + moveTo);
                        move(newDim);
                      //  System.out.println("$$$$$$$$");
                    }
                    if (!shouldMove){
                        break;
                    }
                }
                if (currentDim.equals(moveTo)){
              //      System.out.println(name + " reached destination.");
                    shouldMove = false;
                }
            }
        }

    }

    public Dimension getMoveTo() {
        return moveTo;
    }
}

