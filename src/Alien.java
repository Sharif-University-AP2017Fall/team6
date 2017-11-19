public class Alien implements Movable{
    private String name;
    private int energy;
    private int speed;
    private int shootingSpeed;
    private int strength;
    private boolean canFly;
    private Dimension dimension;


    public Dimension getDimension() {
        return dimension;
    }
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }


    public Alien(String name) {
        this.name = name;
        switch (name){
            case "Albertonion":
                this.energy = 250;
                this.speed = 8;
                this.shootingSpeed = 5;
                this.strength = 7;
                this.canFly = false;
                break;
            case "Algwasonion":
                this.energy = 150;
                this.speed = 4;
                this.shootingSpeed = 10;
                this.strength = 25;
                this.canFly = false;
                break;
            case "Activionion":
                this.energy = 400;
                this.speed = 2;
                this.shootingSpeed = 2;
                this.strength = 40;
                this.canFly = false;
                break;
            case "Aironion":
                this.energy = 200;
                this.speed = 5;
                this.shootingSpeed = 5;
                this.strength = 20;
                this.canFly = true;
                break;
        }
    }

    public Alien(String name, int energy, int initialSpeed, int shootingSpeed, int strength, boolean canFly) { //custom constructor?!
        this.name = name;
        this.energy = energy;
        this.speed = initialSpeed;
        this.shootingSpeed = shootingSpeed;
        this.strength = strength;
        this.canFly = canFly;
    }

    public boolean isDead(){
        return energy <= 0;
    }

    public void fire(Warrior warrior){}

    public void reduceEnergy(int amount){}
    
    public boolean gotShot(Warrior a){return false;}
    public boolean gotShot(Weapon a){return false;}
    
    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public int getSpeed() {
        return speed;
    }

    public int getShootingSpeed() {
        return shootingSpeed;
    }

    public int getStrength() {
        return strength;
    }

    public boolean isCanFly() {
        return canFly;
    }

    @Override
    public void move() {

    }

    
}
