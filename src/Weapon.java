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
public abstract class Weapon implements Mappable, Shooter, Comparable {

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

    static int NUM_USED_TESLA = 0;
    static int SECONDS_LEFT_TO_USE_TESLA = 10;
    static boolean TESLA_IN_USE = false;

    Weapon(Dimension dimension, String type) {
        mapTo(dimension);
        setName(type);
        setType(type);

    }

    private void setType(String a) {
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

        switch (a.toLowerCase()) {
            case "machine gun":
                setType(0);
                setOnAirOnly(false);
                break;
            case "rocket":
                setType(1);
                setOnAirOnly(false);
                break;
            case "laser":
                setType(2);
                setOnAirOnly(false);
                break;
            case "antiaircraft":
                setType(3);
                setOnAirOnly(true);
                break;
            case "freezer":
                setType(4);
                setOnAirOnly(false);
                break;
            case "tesla":
                setType(5);
                setOnAirOnly(false);
                break;
            default:
                System.out.println(a + " not Found");
                break;
        }
    }

    String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    int getPrice() {
        return price;
    }

    private void setPrice(int price) {
        this.price = price;
    }

    void reducePrice(double percentage) {
        setPrice((int) (this.price * percentage));
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    boolean isOnAirOnly() {
        return onAirOnly;
    }

    private void setOnAirOnly(boolean onAirOnly) {
        this.onAirOnly = onAirOnly;
    }

    double getRadius() {
        return radius;
    }

    void setRadius(double radius) {
        this.radius = radius;
    }

    int getSpeedOfBullet() {
        return speedOfBullet;
    }

    private void setSpeedOfBullet(int speedOfBullet) {
        this.speedOfBullet = speedOfBullet;
    }

    int getPowerOfBullet() {
        return powerOfBullet;
    }

    private void setPowerOfBullet(int powerOfBullet) {
        this.powerOfBullet = powerOfBullet;
    }

    public int getType() {
        return type;
    }

    private void setType(int type) {
        this.type = type;
    }

    int getSpeedReduction() {
        return speedReduction;
    }

    private void setSpeedReduction(int speedReduction) {
        this.speedReduction = speedReduction;
    }

    int getPowerOfBulletAir() {
        return powerOfBulletAir;
    }

    private void setPowerOfBulletAir(int powerOfBulletAir) {
        this.powerOfBulletAir = powerOfBulletAir;
    }

    boolean upgrade(Hero hero) {
        if ((double) hero.getMoney() > (this.price * 1.2) && level < 3) {
            setPrice((int) (this.price * 1.2));
            hero.reduceMoney(this.price);
            level++;
            upgradePower();
            upgradeRadius();
            return true;
        }
        return false;
    }

    private void upgradePower() {
        setPowerOfBulletAir((int) (this.powerOfBulletAir * 1.1));
        setPowerOfBullet((int) (this.powerOfBullet * 1.1));
    }

    private void upgradeRadius() {
        setRadius(this.radius * 1.1);
    }

    void reduceRadius() {
        setRadius(this.radius * 0.85);
    }

    void resetRadius() {
        setRadius(this.radius * (double) 100 / 85);
    }

    @Override
    public boolean isWithinRadius(Dimension dimension) {
        /*System.out.println(this.getName());
        System.out.println("*****************");
        System.out.println("checking location : " + this.dimension);
        System.out.println("distance : " + this.dimension.distanceFrom(dimension));
        System.out.println("radius : " + this.radius * GameMap.UNIT);
        System.out.println(this.dimension.distanceFrom(dimension) <= this.radius * GameMap.UNIT);*/
        return this.dimension.distanceFrom(dimension) <= this.radius * GameMap.UNIT;
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

    static Weapon WeaponFactory(Dimension dimension, String type) {
        switch (type.toLowerCase()) {
            case "tesla":
                ;
                NUM_USED_TESLA++;
                SECONDS_LEFT_TO_USE_TESLA = 10;
                TESLA_IN_USE = true;
                return new WeaponAll(dimension, type);
            case "machine gun":
                return new WeaponNearest(dimension, type);
            case "rocket":
                return new WeaponAll(dimension, type);
            case "laser":
                return new WeaponNearest(dimension, type);
            case "antiaircraft":
                return new WeaponNearest(dimension, type);
            case "freezer":
                return new WeaponAll(dimension, type);
            default:
                System.out.println(type + " not Found");
                break;
        }
        return null;
    }

    static int getInitialPrice(String a) {
        switch (a.toLowerCase()) {
            case "machine gun":
                return 100;
            case "rocket":
                return 180;
            case "laser":
                return 150;
            case "antiaircraft":
                return 180;
            case "freezer":
                return 170;
            case "barrack":
                return 90;
            default:
                return -1;
        }
    }

    private int getInitialSpeedOfBullet(String a) {
        switch (a.toLowerCase()) {
            case "machine gun":
                return 10;
            case "rocket":
                return 3;
            case "laser":
                return 7;
            case "antiaircraft":
                return 15;
            case "freezer":
                return 5;
            default:
                return -1;
        }
    }

    private int getInitialPowerOfBullet(String a) {
        switch (a.toLowerCase()) {
            case "machine gun":
                return 10;
            case "rocket":
                return 20;
            case "laser":
                return 10;
            case "antiaircraft":
                return 0;
            case "freezer":
                return 5;
            default:
                return -1;
        }
    }

    private int getInitialPowerOfBulletAir(String a) {
        switch (a.toLowerCase()) {
            case "machine gun":
                return 5;
            case "rocket":
                return 10;
            case "laser":
                return 7;
            case "antiaircraft":
                return 12;
            case "freezer":
                return 3;
            default:
                return -1;
        }
    }

    private int getInitialSpeedReduction(String a) {
        switch (a.toLowerCase()) {
            case "machine gun":
                return 0;
            case "rocket":
                return 0;
            case "laser":
                return 40;
            case "antiaircraft":
                return 20;
            case "freezer":
                return 60;
            default:
                return -1;
        }
    }

    private double getInitialRadius(String a) {
        switch (a.toLowerCase()) {
            case "machine gun":
                return 1;
            case "rocket":
                return 2;
            case "laser":
                return 1;
            case "antiaircraft":
                return 1.5;
            case "freezer":
                return 1;
            case "tesla":
                return 2;
            default:
                return -1;
        }
    }

    boolean naturalDisasterWeapon() {

        setLevel(1);
        setPrice(initialPrice);
        setSpeedOfBullet(initialSpeedOfBullet);
        setPowerOfBullet(initialPowerOfBullet);
        setPowerOfBulletAir(initialPowerOfBulletAir);
        setSpeedReduction(initialSpeedReduction);
        setRadius(initialRadius);
        System.out.println(this.name + "weapon in " + this.dimension + " was hit by a natural disaster :(( ");

        return true;

    }

    static void showWeaponList() {
        System.out.println("Machine Gun\n" +
                "Rocket\n" +
                "Laser\n" +
                "Antiaircraft\n" +
                "Freezer\n" +
                "Barrack\n");
    }

    @Override
    public String toString() {
        return "name: " + this.name + "\tplace: " + dimension + "\tlevel: " + this.level;
    }

    @Override
    public int compareTo(Object o) {
        Weapon otherWeapon = ((Weapon) o);
        return this.name.compareTo(otherWeapon.getName());
    }
}
