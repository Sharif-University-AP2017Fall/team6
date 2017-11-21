public class Alien implements Movable{

    private String name;
    private int energy;
    private int speed;
    private int initialSpeed;
    private int shootingSpeed;
    private int strength;
    private int type;
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

    public void reduceSpeed(double reductionPercentage){
        int reduceAmount = (int) (this.speed * reductionPercentage);
        this.speed -= reduceAmount;
    }

    public void stop(){
        this.speed = 0;
    }

    public void backToNormalSpeed(){
        this.speed = this.initialSpeed;
    }

    public void reduceEnergy(int amount){
        this.energy -= amount;
    }

    public boolean isDead(){
        return energy <= 0;
    }

    public void fire(Warrior warrior){}
    
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

    public int getType() {
        return type;
    }

    @Override
    public void move(Dimension dimension) {
        setDimension(dimension);
    }

    @Override
    public String toString() {
        return  "name: " + name + "\tplace: " + dimension + "\tenergy left: " + energy;
    }
}
