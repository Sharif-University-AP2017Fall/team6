public class Alien implements Movable, Comparable {
    private static int NUM = 0;
    private static int MAXNUM = 25;
    private static boolean START = false;

    private String name;
    private Dimension dimension;
    private int energy;
    private int speed;
    private int initialSpeed;
    private int shootingSpeed;
    private int strength;
    private int type;

    private boolean canFly;

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    Alien(String name) {
        NUM++;
        //System.out.println("******* ****");
        //System.out.println("num = " + NUM);
        START = true;
        this.name = name;
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
    }

    void reduceSpeed(double reductionPercentage) {
        int reduceAmount = (int) (this.speed * reductionPercentage);
        this.speed -= reduceAmount;
    }

    void stop() {
        this.speed = 0;
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

    boolean isDead() {
        return energy <= 0;
    }

    String getName() {
        return name;
    }

    int getSpeed() {
        return speed;
    }

    boolean isCanFly() {
        return canFly;
    }

    static int getNUM() {
        return NUM;
    }

    static boolean isSTART() {
        return START;
    }

    static int getMAXNUM() {
        return MAXNUM;
    }

    @Override
    public boolean move(Dimension dimension) {
        //System.out.println(name + " moved to dimension " + dimension);
        setDimension(dimension);
        return true;
    }

    @Override
    public String toString() {
        return "name: " + name + "\tplace: " + dimension + "\tenergy left: " + energy;
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

    static void reduceNum(int NUM) {
        Alien.NUM -= NUM;
    }

    @Override
    public int compareTo(Object o) {
        Alien otherAlien = ((Alien) o);
        return this.name.compareTo(otherAlien.getName());
    }
}
