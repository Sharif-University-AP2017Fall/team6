import java.util.ArrayList;
import java.util.HashMap;
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
public abstract class Weapon implements Mappable, Shooter, Comparable, Runnable {

    private String name;
    private Dimension dimension;
    private int locationNum;
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

    private boolean shouldShoot;
     List<Alien> toShoot;
     List<Alien> killed;

    static int NUM_USED_TESLA = 0;
    static int SECONDS_LEFT_TO_USE_TESLA = 10;
    static boolean TESLA_IN_USE = false;

    private WeaponView weaponView;


    public WeaponView getWeaponView() {
        return weaponView;
    }

    public void setWeaponView(WeaponView a) {
        weaponView = a;
    }


    public Dimension getDimension() {
        return dimension;
    }


    Weapon(Dimension dimension, String type, int locationNum) {
        this.shouldShoot = false;
        mapTo(dimension);
        setName(type);
        setType(type);
        this.locationNum = locationNum;
    }

    private void setType(String a) {
        setLevel(1);

        if (!a.equalsIgnoreCase("tesla")){
            initialPrice = InitialWeapon.getPrice(a.toLowerCase());//Weapon.getInitialPrice(a);
            setPrice(initialPrice);

            initialSpeedOfBullet = InitialWeapon.getBulletSpeed(a.toLowerCase());
            setSpeedOfBullet(initialSpeedOfBullet);

            initialPowerOfBullet = InitialWeapon.getBulletPower(a.toLowerCase());
            setPowerOfBullet(initialPowerOfBullet);

            initialPowerOfBulletAir = InitialWeapon.getBulletPowerAir(a.toLowerCase());
            setPowerOfBulletAir(initialPowerOfBulletAir);

            initialSpeedReduction = InitialWeapon.getSpeedReduction(a.toLowerCase());
            setSpeedReduction(initialSpeedReduction);

        }


        initialRadius = InitialWeapon.getRadius(a.toLowerCase());
        setRadius(initialRadius);

        switch (a.toLowerCase()) {
            case "machine gun":
                setType(0);
                setOnAirOnly(InitialWeapon.getIsOnAirOnly("machine gun"));
                break;
            case "rocket":
                setType(1);
                setOnAirOnly(InitialWeapon.getIsOnAirOnly("rocket"));
                break;
            case "laser":
                setType(2);
                setOnAirOnly(InitialWeapon.getIsOnAirOnly("laser"));
                break;
            case "antiaircraft":
                setType(3);
                setOnAirOnly(InitialWeapon.getIsOnAirOnly("antiaircraft"));
                break;
            case "freezer":
                setType(4);
                setOnAirOnly(InitialWeapon.getIsOnAirOnly("freezer"));
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

    void reduceRadius(double a) {
        setRadius(this.radius * a);
    }

    void resetRadius(double a) {
        setRadius(this.radius * (double) 1/a);
    }

    @Override
    public boolean isWithinRadius(Dimension dimension) {
        /*System.out.println(this.getName());
        System.out.println("*****************");
        System.out.println("checking location : " + this.dimension);
        System.out.println("distance : " + this.dimension.distanceFrom(dimension));
        System.out.println("radius : " + this.radius * GameMap.UNIT);
        System.out.println(this.dimension.distanceFrom(dimension) <= this.radius * GameMap.UNIT);*/
        System.out.println(this.dimension);
        System.out.println(dimension);
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

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (AlienCreeps.restart){
                break;
            }
            if (!AlienCreeps.ISPAUSED){
                if (shouldShoot) {
                    killed.addAll(shoot(toShoot));
                }
            }
        }
    }

    public List<Alien> getKilled() {
        return killed;
    }

    public void resetKilled() {
        killed = new ArrayList<>();
    }

    public void setShouldShoot(List<Alien> toShoot) {
        this.shouldShoot = true;
        this.toShoot = toShoot;
        resetKilled();
    }

    public void stopShooting() {
        this.shouldShoot = false;
    }

    public boolean isShouldShoot() {
        return shouldShoot;
    }

    static Weapon WeaponFactory(Dimension dimension, String type, int locationNum) {
        type = type.toLowerCase();
        if (InitialWeapon.weaponNames.contains(type)) {

            switch (type) {
                case "tesla":
                    NUM_USED_TESLA++;
                    SECONDS_LEFT_TO_USE_TESLA = 10;
                    TESLA_IN_USE = true;
                    return new WeaponAll(dimension, type, locationNum);
                case "machine gun":
                    return new WeaponNearest(dimension, type, locationNum);
                case "rocket":
                    return new WeaponAll(dimension, type, locationNum);
                case "laser":
                    return new WeaponNearest(dimension, type, locationNum);
                case "antiaircraft":
                    return new WeaponNearest(dimension, type, locationNum);
                case "freezer":
                    return new WeaponAll(dimension, type, locationNum);
                default:
                    System.out.println(type + " not Found");
                    break;
            }
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
                return 10/5;
            case "rocket":
                return 20/5;
            case "laser":
                return 10/5;
            case "antiaircraft":
                return 15/5;
            case "freezer":
                return 10/5;
            case "tesla":
                return 20/2;
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
        if (this.locationNum > 0) {
            return "weaponName: " + this.name + "" +
                    "\tplace: " + this.dimension + "" +
                    "\tlocation number: " + this.locationNum + "" +
                    "\tweaponLevel: " + this.level;
        }
        return "weaponName: " + this.name + "" +
                "\tplace: " + this.dimension + "" +
                "\tweaponLevel: " + this.level;
    }

    @Override
    public int compareTo(Object o) {
        Weapon otherWeapon = ((Weapon) o);
        return this.name.compareTo(otherWeapon.getName());
    }

    public abstract int getNumKilled();
}




