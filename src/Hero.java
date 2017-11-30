/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Tara
 */
public class Hero extends Warrior {
    private Soldier[] soldiers = new Soldier[3];
    private Dimension soldierDims[] = new Dimension[3];
    private int resurrectionTime;
    private int powerLevel;
    private int experienceLevel;
    private int money;
    private Achievement achievement;

    Hero(Dimension dimension) {
        achievement = new Achievement();
        setDimension(dimension);
        setMoney(10000);
        setEnergy(300);
        setRadius(0.5);
        soldierDims[0] = new Dimension(15, 0);
        soldierDims[1] = new Dimension(-15, 0);
        soldierDims[2] = new Dimension(0, -15);
        soldiers[0] = null;
        soldiers[1] = null;
        soldiers[2] = null;
    }

    void setSoldiers(Soldier[] soldiers) {
        this.soldiers = soldiers;
    }

    int getResurrectionTime() {
        this.calculateResurrectionTime();
        return resurrectionTime;
    }

    Dimension[] getSoldierDims() {
        return soldierDims;
    }

    int getMoney() {
        return money;
    }

    private void setMoney(int money) {
        this.money = money;
    }

    void addMoney(int amount) {
        this.money += amount;
    }

    Achievement getAchievement() {
        return achievement;
    }

    void reduceMoney(int amount) {
        this.money -= amount;
    }

    private boolean addPowerLevel() {
        if (powerLevel < 3) {
            //System.out.println("added one power level");
            powerLevel++;
            this.increaseBulletPower();
            this.increaseBulletSpeed();
            for (int i = 0; i < 3; i++) {
                if (soldiers[i] != null) {
                    soldiers[i].increaseBulletPower();
                    soldiers[i].increaseBulletSpeed();
                }
            }
            return true;
        }
        return false;
    }

    Soldier[] getSoldiers() {
        return soldiers;
    }

    boolean addExperienceLevel(int amount) {
        if (amount > 0)
            //System.out.println("adding experience level amount : " + amount);
            experienceLevel += amount;
        if (experienceLevel - powerLevel * 50 >= 50) {
            if (addPowerLevel()) {
                return true;
            }
        }
        return false;
    }

    private void calculateResurrectionTime() {
        this.resurrectionTime = (int) ((5.0 - (((double) this.experienceLevel / 100) + (double) this.powerLevel)) * 0.99);
    }

    Weapon buyWeapon(String nameOfWeapon, Dimension dimension, int locationNum) {
        if (this.getMoney() >= Weapon.getInitialPrice(nameOfWeapon)) {
            Weapon bought = Weapon.WeaponFactory(dimension, nameOfWeapon, locationNum);
            if (bought != null) {
                reduceMoney(bought.getPrice());
            }
            return bought;
        }
        System.out.println("Not enough money");
        return null;
    }

    boolean upgradeWeapon(Weapon toUpgrade) {
        return toUpgrade.upgrade(this);
    }

    boolean upgradeSoldiers() {
        int numAlive = 0;
        for (int i = 0; i < 3; i++) {
            if (soldiers[i] != null) {
                numAlive++;
            }
        }
        if (numAlive == 0) {
            System.out.println("There are no soldiers to upgrade.");
        } else {
            if (this.getMoney() >= numAlive * 10) {
                this.reduceMoney(numAlive * 10);
                for (int i = 0; i < 3; i++) {
                    if (soldiers[i] != null) {
                        soldiers[i].increaseRadius();
                    }
                }
                return true;
            } else {
                System.out.println("Not enough money");
            }
        }
        return false;
    }

    void showStatus() {
        System.out.println("place: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy() +
                "\tnumber of aliens killed: " + achievement.getNumOfKilledByHero());
    }

    void showKnightStatus() {
        for (int i = 0; i < 3; i++) {
            if (soldiers[i] != null) {
                System.out.print("Soldier #" + (i + 1));
                soldiers[i].showStatus();
            } else {
                System.out.println("Soldier #" + (i + 1) + " not found.");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("*** Hero ***\n----------\n");
        string.append("place: ").append(super.getDimension()).append("\tenergy left: ").append(super.getEnergy()).append("\n\n");

        string.append("*** Soldiers ***\n----------\n");
        boolean hasSoldiers = false;
        for (int i = 0; i < 3; i++) {
            if (soldiers[i] != null) {
                string.append("Soldier #" + (i + 1) + soldiers[i].toString());
                hasSoldiers = true;
            }
        }
        if (!hasSoldiers) {
            string.append("No soldiers found");
        }

        string.append("\n\n");
        return string.toString();
    }

    /**** Hero changes its dimension and his alive soldiers also change dimensions. ****/

    @Override
    public boolean move(Dimension changeDimension) {
        Dimension newDim = new Dimension(getShootingPoint().getX() + changeDimension.getX(),
                getShootingPoint().getY() + changeDimension.getY());
        if (newDim.isWithinBounds(GameMap.XBOUND - 20,
                20,
                GameMap.YBOUND - 20,
                20)
                ) {
            //System.out.println("moved hero to " + newDim);
            setDimension(newDim);
            for (int i = 0; i < 3; i++) {
                if (soldiers[i] != null) {
                    soldiers[i].move(changeDimension);
                }
            }
            return true;
        } else {
            System.out.println("Can't move outside of 20 < x < 780 & 20 < y < 580.");
            return false;
        }
    }
}


class Achievement {
    private int numTypeAlien = 4;
    private int[] numOfKilledByWeapon = new int[numTypeAlien];
    private int[] numOfKilledBySoldier = new int[numTypeAlien];
    private int[] numOfKilledByHero = new int[numTypeAlien];
    private Map<String, Boolean> achieved = new HashMap<>();

    Achievement() {
        achieved.put("Great Hunter", false);
        achieved.put("Good Gene", false);
        achieved.put("Greek Goddess", false);
        achieved.put("Eagle Eye", false);
        achieved.put("Restless Shooter", false);
        achieved.put("Brave Warrior", false);
        achieved.put("Butcher", false);
        achieved.put("Blood Sucker", false);
    }

    void killedWeapon(Alien alien) {
        switch (alien.getName()) {
            case "Albertonion":
                numOfKilledByWeapon[0]++;
                if (numOfKilledByWeapon[0] > 9)
                    achieved.replace("Restless Shooter", true);
                break;
            case "Algwasonion":
                numOfKilledByWeapon[1]++;
                if (numOfKilledByWeapon[1] > 9)
                    achieved.replace("Brave Warrior", true);
                break;
            case "Activionion":
                numOfKilledByWeapon[2]++;
                if (numOfKilledByHero[2] > 9)
                    achieved.replace("Butcher", true);
                break;
            case "Aironion":
                numOfKilledByWeapon[3]++;
                if (numOfKilledByHero[3] > 9)
                    achieved.replace("Blood Sucker", true);
                break;
        }
    }

    /*public void killedSoldier(Alien alien){
        switch (alien.getName()){
            case "Albertonion":
                numOfKilledBySoldier[0]++;
                break;
            case "Algwasonion":
                numOfKilledBySoldier[1]++;
                break;
            case "Activionion":
                numOfKilledBySoldier[2]++;
                break;
            case "Aironion":
                numOfKilledBySoldier[3]++;
                break;
        }
    }  */
    void killedHero(Alien alien) {
        switch (alien.getName()) {
            case "Albertonion":
                numOfKilledByHero[0]++;
                if (numOfKilledByHero[0] > 4)
                    achieved.replace("Great Hunter", true);
                break;
            case "Algwasonion":
                numOfKilledByHero[1]++;
                if (numOfKilledByHero[1] > 4)
                    achieved.replace("Good Gene", true);
                break;
            case "Activionion":
                numOfKilledByHero[2]++;
                if (numOfKilledByHero[2] > 4)
                    achieved.replace("Greek Goddess", true);
                if (numOfKilledByHero[2] > 9)
                    achieved.replace("Eagle Eye", true);
                break;
            case "Aironion":
                numOfKilledByHero[3]++;
                break;
        }
    }

    int getNumOfKilledByHero() {
        int num = 0;
        for (int i = 0; i < numOfKilledByHero.length; i++) {
            num += numOfKilledByHero[i];
        }
        return num;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : achieved.keySet()) {
            if (achieved.get(key)) {
                str.append(key).append("\n");
            }
        }
        return str.toString();
    }
}