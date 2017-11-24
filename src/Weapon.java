import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Tara
 */
public abstract class Weapon implements Mappable, Shooter, Comparable{

    private String name;
    private Dimension dimension;
    private int type; //not sure if this is necessary
    private int price;
    private int level;
    private boolean onAirOnly;

    private double radius; //شعاع اثر
    private int speedOfBullet; //آهنگ شلیک
    private int powerOfBullet; //میزان کاهش انرژی
    private int powerOfBulletAir; //میزان کاهش انرژی موجودات پرنده
    private int speedReduction; //میزان کاهش سرعت

    private int initialPrice;
    private int initialPowerOfBullet;
    private int initialPowerOfBulletAir;
    private int initialSpeedOfBullet;
    private int initialSpeedReduction;
    private double initialRadius;

    public static int NUM_USED_TESLA = 0;
    public static int SECONDS_LEFT_TO_USE_TESLA = 10;
    public static boolean TESLA_IN_USE = false;

    Weapon(Dimension dimension,String type){
        mapTo(dimension);
        setName(type);
        setType(type);
    }

    private void setType(String a){
        setLevel(1);

        initialPrice = Weapon.getInitialPrice(a);
        setPrice(initialPrice);
        initialSpeedOfBullet = getInitialSpeedOfBullet(a);
        setSpeedOfBullet(initialSpeedOfBullet);
        initialPowerOfBullet = getInitialPowerOfBullet(a);
        setPowerOfBullet(initialPowerOfBullet);
        initialPowerOfBulletAir = getInitialPowerOfBulletAir(a);
        setPowerOfBulletAir(initialPowerOfBulletAir);
        initialSpeedReduction = getInitialSpeedReduction(a);
        setSpeedReduction(initialSpeedReduction);
        initialRadius = getInitialRadius(a);
        setRadius(initialRadius);

        switch (a){
            case "Machine Gun":
                setType(0);
                setOnAirOnly(false);
                break;
            case "Rocket":
                setType(1);
                setOnAirOnly(false);
                break;
            case "Laser":
                setType(2);
                setOnAirOnly(false);
                break;
            case "Antiaircraft":
                setType(3);
                setOnAirOnly(true);
                break;
            case "Freezer":
                setType(4);
                setOnAirOnly(false);
                break;
            case "Tesla":
                setType(5);
                setOnAirOnly(false);
            default:
                System.out.println(a + "not Found");
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void reducePrice(double percentage){
        setPrice((int) (this.price * percentage));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isOnAirOnly() {
        return onAirOnly;
    }

    public void setOnAirOnly(boolean onAirOnly) {
        this.onAirOnly = onAirOnly;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getSpeedOfBullet() {
        return speedOfBullet;
    }

    public void setSpeedOfBullet(int speedOfBullet) {
        this.speedOfBullet = speedOfBullet;
    }

    public int getPowerOfBullet() {
        return powerOfBullet;
    }

    public void setPowerOfBullet(int powerOfBullet) {
        this.powerOfBullet = powerOfBullet;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpeedReduction() {
        return speedReduction;
    }

    public void setSpeedReduction(int speedReduction) {
        this.speedReduction = speedReduction;
    }

    public int getPowerOfBulletAir() {
        return powerOfBulletAir;
    }

    public void setPowerOfBulletAir(int powerOfBulletAir) {
        this.powerOfBulletAir = powerOfBulletAir;
    }

    public boolean upgrade(Hero hero){
        if ((double)hero.getMoney() > (this.price * 1.2) && level < 3){
            setPrice((int)(this.price * 1.2));
            hero.reduceMoney(this.price);
            level++;
            upgradePower();
            upgradeRadious();
            return true;
        }
        return false;
    }

    private void upgradePower(){
        setPowerOfBulletAir((int)(this.powerOfBulletAir * 1.1));
        setPowerOfBullet((int)(this.powerOfBullet * 1.1));
    }

    private void upgradeRadious(){
        setRadius(this.radius * 1.1);
    }

    public void showStatus(){
        System.out.println("place: " + dimension +
                "\tlevel: " + level +
                "\n");
    }

    @Override
    public boolean isWithinRadius(Dimension dimension){
        return this.dimension.distanceFrom(dimension) <= this.radius;
    }

    @Override
    public abstract List<Alien> shoot(List<Alien> aliens);

    @Override
    public Dimension getShootingPoint() {
        return this.dimension;
    }

    @Override
    public void mapTo(Dimension dimension) {
        this.dimension = dimension;
    }

    public static Weapon search(List<Weapon> weapon,String nameSearch){
        int n = weapon.size();
        Weapon searching;
        for (int i = 0; i < n; i++){
            searching = weapon.get(i);
            if (searching != null && searching.getName().equalsIgnoreCase(nameSearch)){
                return searching;
            }
        }
        return null;
    }

    public static Weapon search(List<Weapon> weapon,int x,int y){
        int n=weapon.size();
        Weapon searching;
        for (int i=0;i<n;i++){


        }
        return null;
    }

    public static Weapon WeaponFactory(Dimension dimension,String type){
         switch (type){
            case "Machine Gun":               
                return  new WeaponNearest(dimension,type);
            case "Rocket":                
                return  new WeaponAll(dimension,type);
            case "Laser":               
                return  new WeaponNearest(dimension,type);
            case "Antiaircraft":              
                return  new WeaponNearest(dimension,type);
            case "Freezer":
                return  new WeaponAll(dimension,type);
             case "Tesla":
                 NUM_USED_TESLA++;
                 TESLA_IN_USE = true;
                 return new WeaponAll(dimension, type);
            default:
                System.out.println(type + "not Found");
                break;
         }
    return null;
    }

    public static int getInitialPrice(String a){
    switch (a){
            case "Machine Gun":
                return 100;
            case "Rocket":
                return 180;
            case "Laser":
                return 150;
            case "Antiaircraft":
                return 180;
            case "Freezer":
                return 170;
            default:
                System.out.println(a+ " not found in -getInitialPrice- ");
                return -1;
        }
    }
    public  int getInitialSpeedOfBullet(String a){
    switch (a){
            case "Machine Gun":
                return 10;
            case "Rocket":
                return 3;
            case "Laser":
                return 7;
            case "Antiaircraft":
                return 15;
            case "Freezer":
                return 5;
            default:
                System.out.println(a+ " not found in -getInitialSpeedOfBullet- ");
                return -1;
        }
    }
    public  int getInitialPowerOfBullet(String a){
    switch (a){
            case "Machine Gun":
                return 10;
            case "Rocket":
                return 20;
            case "Laser":
                return 10;
            case "Antiaircraft":
                return 0;
            case "Freezer":
                return 5;
            default:
                System.out.println(a+ " not found in -getInitialSpeedOfBullet- ");
                return -1;
        }
    }
    public  int getInitialPowerOfBulletAir(String a){
    switch (a){
            case "Machine Gun":
                return 5;
            case "Rocket":
                return 10;
            case "Laser":
                return 7;
            case "Antiaircraft":
                return 12;
            case "Freezer":
                return 3;
            default:
                System.out.println(a+ " not found in -getInitialSpeedOfBulletAir- ");
                return -1;
        }
    }

    public  int getInitialSpeedReduction(String a){
    switch (a){
            case "Machine Gun":
                return 0;
            case "Rocket":
                return 0;
            case "Laser":
                return 40;
            case "Antiaircraft":
                return 20;
            case "Freezer":
                return 60;
            default:
                System.out.println(a+ " not found in -getInitialSpeedReduction- ");
                return -1;
        }
    }

    public  double getInitialRadius(String a){
    switch (a){
            case "Machine Gun":
                return 1;
            case "Rocket":
                return 2;
            case "Laser":
                return 1;
            case "Antiaircraft":
                return 1.5;
            case "Freezer":
                return 1;
        case "Tesla":
                return 2;
            default:
                System.out.println(a+ " not found in -getInitialRadius- ");
                return -1;
        }
    }

    public static void showWeaponList(){
        System.out.println("-------------------------");
        System.out.println("Machine Gun\n" +
                "Rocker\n" +
                "Laser\n" +
                "Antiaircraft\n" +
                "Freezer\n");
    }

    @Override
    public String toString(){
        return "name: " + this.name + " place: " + dimension + " level: " + this.level;
    }

    @Override
    public int compareTo(Object o) {
        Weapon otherWeapon = ((Weapon) o);
        return this.name.compareTo(otherWeapon.getName());
    }
}
