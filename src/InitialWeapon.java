import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InitialWeapon{
    static ArrayList<String> weaponNames = new ArrayList<>();
    static HashMap<String, Boolean> isOnAirOnly = new HashMap<>();
    static HashMap<String, Double> radius = new HashMap<>();
    static HashMap<String, Integer> bulletSpeed = new HashMap<>();
    static HashMap<String, Integer> bulletPower = new HashMap<>();
    static HashMap<String, Integer> bulletPowerAir = new HashMap<>();
    static HashMap<String, Integer> speedReduction = new HashMap<>();
    static HashMap<String, Integer> price = new HashMap<>();

    static {
        weaponNames.add("Machine Gun".toLowerCase());
        weaponNames.add("Rocket".toLowerCase());
        weaponNames.add("Laser".toLowerCase());
        weaponNames.add("Antiaircraft".toLowerCase());
        weaponNames.add("Freezer".toLowerCase());
        weaponNames.add("Barrack".toLowerCase());

        isOnAirOnly.put("Machine Gun".toLowerCase(), false);
        radius.put("Machine Gun".toLowerCase(), 2.);
        bulletSpeed.put("Machine Gun".toLowerCase(), 10);
        bulletPower.put("Machine Gun".toLowerCase(), 10);
        bulletPowerAir.put("Machine Gun".toLowerCase(), 5);
        speedReduction.put("Machine Gun".toLowerCase(), 0);
        price.put("Machine Gun".toLowerCase(), 100);


        isOnAirOnly.put("Rocket".toLowerCase(), false);
        radius.put("Rocket".toLowerCase(), 4.);
        bulletSpeed.put("Rocket".toLowerCase(), 3);
        bulletPower.put("Rocket".toLowerCase(), 20);
        bulletPowerAir.put("Rocket".toLowerCase(), 10);
        speedReduction.put("Rocket".toLowerCase(), 0);
        price.put("Rocket".toLowerCase(), 180);

        isOnAirOnly.put("Laser".toLowerCase(), false);
        radius.put("Laser".toLowerCase(), 2.);
        bulletSpeed.put("Laser".toLowerCase(), 7);
        bulletPower.put("Laser".toLowerCase(), 10);
        bulletPowerAir.put("Laser".toLowerCase(), 7);
        speedReduction.put("Laser".toLowerCase(), 40);
        price.put("Laser".toLowerCase(), 150);

        isOnAirOnly.put("Antiaircraft".toLowerCase(), true);
        radius.put("Antiaircraft".toLowerCase(), 3.);
        bulletSpeed.put("Antiaircraft".toLowerCase(), 15);
        bulletPower.put("Antiaircraft".toLowerCase(), 0);
        bulletPowerAir.put("Antiaircraft".toLowerCase(), 12);
        speedReduction.put("Antiaircraft".toLowerCase(), 20);
        price.put("antiaircraft".toLowerCase(), 180);

        isOnAirOnly.put("Freezer".toLowerCase(), false);
        radius.put("Freezer".toLowerCase(), 2.);
        bulletSpeed.put("Freezer".toLowerCase(), 5);
        bulletPower.put("Freezer".toLowerCase(), 5);
        bulletPowerAir.put("Freezer".toLowerCase(), 3);
        speedReduction.put("Freezer".toLowerCase(), 60);
        price.put("freezer".toLowerCase(), 170);
    }

    public static boolean getIsOnAirOnly(String name) {
        return isOnAirOnly.get(name);
    }

    public static double getRadius(String name) {
        return radius.get(name);
    }

    public static int getBulletSpeed(String name) {
        return bulletSpeed.get(name);
    }

    public static int getBulletPower(String name) {
        return bulletPower.get(name);
    }

    public static int getBulletPowerAir(String name) {
        return bulletPowerAir.get(name);
    }

    public static int getSpeedReduction(String name) {
        return speedReduction.get(name);
    }

    public static int getPrice(String name) {
        return price.get(name);
    }



    public static void setIsOnAirOnly(String name, Boolean state) {
        isOnAirOnly.replace(name, state);
    }

    public static void getRadius(String name, double r) {
        radius.replace(name, r);
    }

    public static void getBulletSpeed(String name, int speed) {
        bulletSpeed.replace(name, speed);
    }

    public static void getBulletPower(String name, int power) {
        bulletPower.replace(name, power);
    }

    public static void getBulletPowerAir(String name, int power) {
        bulletPowerAir.replace(name, power);
    }

    public static void getSpeedReduction(String name, int amount) {
        speedReduction.replace(name, amount);
    }

    public static void getPrice(String name, int p) {
        price.replace(name, p);
    }

    public static void removeWeapon(String name){
        weaponNames.remove(name);
        isOnAirOnly.remove(name);
        radius.remove(name);
        bulletSpeed.remove(name);
        bulletPowerAir.remove(name);
        bulletPower.remove(name);
        speedReduction.remove(name);
        price.remove(name);
    }
}