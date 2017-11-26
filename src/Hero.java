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
    private Soldier[] soldiers = new Soldier[3]; // remember to initialize!
    private Dimension soldierDims[] = new Dimension[3];
    private int resurrectionTime;
    private int powerLevel;
    private int experienceLevel;
    private int money;
    private Achievement achievement;
    //private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

    Hero (Dimension dimension){
        achievement = new Achievement();
        setDimension(dimension);
        setMoney(1000);
        setEnergy(300);
        soldierDims[0] = new Dimension(15, 0);
        soldierDims[1] = new Dimension(-15, 0);
        soldierDims[2] = new Dimension(0, -15);
        soldiers[0] = new Soldier(soldierDims[0].add(dimension));
        soldiers[1] = new Soldier(soldierDims[1].add(dimension));
        soldiers[2] = new Soldier(soldierDims[2].add(dimension));
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public int getResurrectionTime() {
        this.calculateResurrectionTime();
        return resurrectionTime;
    }

    public Dimension[] getSoldierDims() {
        return soldierDims;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int amount){
        this.money += amount;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void reduceMoney(int amount){
        this.money -= amount;
    }

    public boolean addPowerLevel(){
        if (powerLevel < 3){
            powerLevel++;
            this.increaseBulletPower();
            this.increaseBulletSpeed();
            for (int i = 0; i < 3; i++){
                if (soldiers[i] != null){
                    soldiers[i].increaseBulletPower();
                    soldiers[i].increaseBulletSpeed();
                }
            }
            return true;
        }
        return false;
    }

    public Soldier[] getSoldiers() {
        return soldiers;
    }

    public boolean addExperienceLevel(int amount){
        experienceLevel += amount;
        if (experienceLevel - powerLevel * 50 >= 50){
            if (addPowerLevel()) {
                return true;
            }
        }
        return false;
    }

    public void calculateResurrectionTime(){
        this.resurrectionTime = (int)((5.0 - (((double)this.experienceLevel / 100) + (double)this.powerLevel)) * 0.99);
    }

    public Weapon buyWeapon(String nameOfWeapon, Dimension dimension){
        if (this.getMoney() >= Weapon.getInitialPrice(nameOfWeapon)){
            Weapon bought = Weapon.WeaponFactory(dimension,nameOfWeapon);
            if (bought != null){
                reduceMoney(bought.getPrice());
            }
            return bought;
        }
        System.out.println("Not enough money");
        return null;
    }

    public boolean upgradeWeapon(Weapon toUpgrade){
        return toUpgrade.upgrade(this);
    }

    public boolean upgradeSoldiers(){
        int numAlive = 0;
        for (int i = 0; i < 3; i++){
            if (soldiers[i] != null){
                numAlive++;
            }
        }
        if (this.getMoney() >= numAlive * 10){
            this.reduceMoney(numAlive * 10);
            for (int i = 0; i < 3; i++){
                if (soldiers[i] != null){
                    soldiers[i].increaseRadious();
                }
            }
            return true;
        }
        return false;
    }

    public boolean died(){
       // System.out.println("GAME OVER");
        System.out.println(toString());
       // System.exit(0);
        return false;
    }

    public void showStatus(){
        System.out.println("place: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy() +
                "\tnumber of aliens killed: " + achievement.getNumOfKilledByHero() +
                "\n");
    }

    public void showKnightStatus(){
        for (int i = 0; i < 3; i++){
            if (soldiers[i] != null){
                soldiers[i].showStatus();
            }else{
                System.out.println("Soldier #" + (i + 1) + " is dead.");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Hero " +
                "\tname: " + super.getName() +
                "\tplace: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy() +
                "\n");
        for (int i = 0; i < 3; i++){
            if (soldiers[i] != null){
                string.append("Soldier #" + (i + 1) + soldiers[i].toString());
            }
        }
        return string.toString();
    }

    /**** Hero changes its dimension and his alive soldiers also change dimensions. ****/

    @Override
    public void move(Dimension changeDimension) {
        Dimension newDim = new Dimension(getShootingPoint().getX() + changeDimension.getX(),
                getShootingPoint().getY() + changeDimension.getY());
        if (newDim.isWithinBounds()){
            setDimension(newDim);
            for (int i = 0; i < 3; i++){
                if (soldiers[i] != null){
                    soldiers[i].move(changeDimension);
                }
            }
        }
    }
}


class Achievement {
    private int numTypeAlien=4;
    private int[] numOfKilledByWeapon = new int [numTypeAlien];
    private int[] numOfKilledBySoldier = new int [numTypeAlien];
    private int[] numOfKilledByHero = new int [numTypeAlien];
    private Map<String,Boolean> achieved = new HashMap<>();

    public Achievement() {
        achieved.put("Great Hunter",false);
        achieved.put("Good Gene",false);
        achieved.put("Greek Goddess",false);
        achieved.put("Eagle Eye",false);
        achieved.put("Restless Shooter",false);
        achieved.put("Brave Warrior",false);
        achieved.put("Butcher",false);
        achieved.put("Blood Sucker",false);
    }

    public void killedWeapon(Alien alien){
        switch (alien.getName()){
            case "Albertonion":
                numOfKilledByWeapon[0]++;
                if(numOfKilledByWeapon[0] > 9)
                    achieved.replace("Restless Shooter", true);
                break;
            case "Algwasonion":
                numOfKilledByWeapon[1]++;
                if(numOfKilledByWeapon[1] > 9)
                    achieved.replace("Brave Warrior", true);
                break;
            case "Activionion":
                numOfKilledByWeapon[2]++;
                if(numOfKilledByHero[2] > 9)
                    achieved.replace("Butcher", true);
                break;
            case "Aironion":
                numOfKilledByWeapon[3]++;
                if(numOfKilledByHero[3] > 9)
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
    public void killedHero(Alien alien){
        switch (alien.getName()){
            case "Albertonion":
                numOfKilledByHero[0]++;
                if(numOfKilledByHero[0] > 4)
                    achieved.replace("Great Hunter", true);
                break;
            case "Algwasonion":
                numOfKilledByHero[1]++;
                if(numOfKilledByHero[1] > 4)
                    achieved.replace("Good Gene", true);
                break;
            case "Activionion":
                numOfKilledByHero[2]++;
                if(numOfKilledByHero[2] > 4)
                    achieved.replace("Greek Goddess", true);
                if(numOfKilledByHero[2] > 9)
                    achieved.replace("Eagle Eye", true);
                break;
            case "Aironion":
                numOfKilledByHero[3]++;
                break;
        }
    }

    public int getNumOfKilledByHero(){
        int num = 0;
        for (int i = 0; i < numOfKilledByHero.length; i++){
            num += numOfKilledByHero[i];
        }
        return num;
    }
    public String toString(){
        StringBuilder str = new StringBuilder();
        for (String key : achieved.keySet()){
            if (achieved.get(key)){
                str.append(key).append("\n");
            }
        }
        return str.toString();
    }
}