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
public abstract class Weapon implements Mappable, Shootable{

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

    Weapon(Dimension dimension,String type){
        mapTo(dimension);
        setType(type);
        setName(type);
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

    public int getPowerOfBulletAir() {
        return powerOfBulletAir;
    }

    public int getType() {
        return type;
    }

    public int getSpeedReduction() {
        return speedReduction;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSpeedReduction(int speedReduction) {
        this.speedReduction = speedReduction;
    }
    public void setPowerOfBulletAir(int powerOfBulletAir) {
        this.powerOfBulletAir = powerOfBulletAir;
    }
    public boolean upgrade(Hero hero){
        if ((hero.getMoney() > (int)(getPrice() * 1.2)) && level<3){
            setPrice((int)(getPrice() * 1.2));
            hero.addMoney(-1 * getPrice());
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

    private void setType(String a){
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
                break;
            case "Rocket":
                setType(1);
                break;
            case "Laser":
                setType(2);
                break;
            case "Antiaircraft":
                setOnAirOnly(true);
                setType(3);
                break;
            case "Freezer":
                setType(4);
                break;

            default:
                System.out.println(a + "not Found");
                break;
        }

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
            default:
                System.out.println(a+ " not found in -getInitialRadius- ");
                return -1;
        }

    }

    @Override
    public String toString(){
        String str= "name: " + this.name + " place: " + " not defined in toString yet" + " level: " + this.level;
        
        return str;
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
}
