/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Tara
 */
public class Hero extends Warrior {
    private Soldier[] soldiers=new Soldier[3]; // remember to initialize!
    private int resurrectionTime;
    private int powerLevel;
    private int experienceLevel;
    private int money;
    private Achivement achieved; //not necessary
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

    public void setMoney(int a){money=a;}
    public int getMoney(){return money;}
    public int getPowerLevel(){return powerLevel;}
    public int getExperienceLevel(){return experienceLevel;}
    public void addMoney(int a){setMoney(getMoney()+a);}
    public void setPowerLevel(int a){powerLevel=a;}
    public void setExperienceLevel(int a){experienceLevel=a;}
    public void addPowerLevel(){powerLevel++;}
    public void calculatePowerLevel(){
        while (getExperienceLevel()>49)
            {addExperienceLevel(-50);
            addPowerLevel();}
    }
    public void addExperienceLevel(int a){experienceLevel=a+experienceLevel;}
    public int calculateResurrectionTime(){
        double a1 = getExperienceLevel();
        double a2 = getPowerLevel();
        double b = 5 - (a1*+50*a2)*0.01-a2;
        b = b * 0.99;
        resurrectionTime = (int) b;
        return resurrectionTime;
    }
    public boolean gotShot(Alien a){
        setEnergy(getEnergy() - a.getStrength());
        if(isDead())
            died();
        return true;
    }
    public Weapon buyWeapon(String nameOfWeapon,Dimension dimension){
        if (this.getMoney()<Weapon.getInitialPrice(nameOfWeapon))
            {Weapon bought=Weapon.WeaponFactory(dimension,nameOfWeapon);
            if (bought!=null)
                setMoney(getMoney()-bought.getPrice());
            return bought;}   
        return null;
    }
    public boolean died(){
       // System.out.println("GAME OVER");
        System.out.println(toString());
       // System.exit(0);
        return false;
    }
    public boolean upgradeWeapon(Weapon toUpgrade){
            boolean condit=toUpgrade.upgrade();
            return condit;  
       }
    Hero (Dimension dimension){
        setDimension(dimension);
        setMoney(1000);

        for (int i=0;i<3;i++){
            soldiers[i]=new Soldier(this,dimension);
                }
    }
    @Override
    public String toString(){
        String str="";
        str=str+" Power level: "+getPowerLevel()+"\n Experience Level: "+ (getPowerLevel()*50+getExperienceLevel())
            + "\n Money: "+getMoney()+"\n"+ achieved.toString();
            
        
        return null;
    }
    
    public boolean applyWeapon(List<Alien> alien){
        int n=alien.size();
        int numBullet=0;
        for (int i=0;i<n;i++){
            if (this.getDimension().distanceFrom(alien.get(i).getDimension())<this.getRadius())
                {
                   alien.get(i).gotShot(this); 
                   numBullet++;
                   if(numBullet>getSpeedOfBullet())
                       break;
                }
        
        }
        return true;
    }
    
    
    
    public void soldierDied(Soldier dead){}

    /**** Hero changes its dimension and his alive soldiers also change dimensions. ****/

    @Override
    public void move(Dimension changeDimension) {
        Dimension newDim = new Dimension(getDimension().getX() + changeDimension.getX(),
                getDimension().getY() + changeDimension.getY());
        setDimension(newDim);
        for (int i = 0; i < 3; i++){
            if (soldiers[i] != null){
                soldiers[i].move(changeDimension);
            }
        }
    }
}


class Achivement {
    private int numTypeAlien=4;
    private int[] numOfKilledByWeapon=new int [numTypeAlien];
    private int[] numOfKilledBySoldier=new int [numTypeAlien];
    private int[] numOfKilledByHero=new int [numTypeAlien];
    private Map<String,Boolean> achieved=new HashMap<>();
    {
        achieved.put("Great Hunter",false);
        achieved.put("Good Gene",false);
        achieved.put("Greek Godess",false);
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
                if(numOfKilledByWeapon[0]>9)
                    achieved.replace("Restless Shooter", true);
                    
                break;
            case "Algwasonion":
                numOfKilledByWeapon[1]++;
                if(numOfKilledByWeapon[1]>9)
                    achieved.replace("Brave Warrior", true);
                break;
            case "Activionion":
                numOfKilledByWeapon[2]++; 
                if(numOfKilledByHero[2]>9)
                    achieved.replace("Butcher", true);
                break;
            case "Aironion":
                numOfKilledByWeapon[3]++;
                if(numOfKilledByHero[3]>9)
                    achieved.replace("Blood Sucker", true);
                break;
        }
    }
    public void killedSoldier(Alien alien){
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
    }    
    public void killedHero(Alien alien){
        switch (alien.getName()){
            case "Albertonion":
                numOfKilledByHero[0]++;
                if(numOfKilledByHero[0]>4)
                    achieved.replace("Great Hunter", true);
                break;
            case "Algwasonion":
                numOfKilledByHero[1]++;
                if(numOfKilledByHero[1]>4)
                    achieved.replace("Good Gene", true);
                break;
            case "Activionion":
                numOfKilledByHero[2]++; 
                if(numOfKilledByHero[2]>4)
                    achieved.replace("Greek Godess", true);
                if(numOfKilledByHero[2]>9)
                    achieved.replace("Eagle Eye", true);
                break;
            case "Aironion":
                numOfKilledByHero[3]++;
                break;
        }
    }
    public String toString(){
        StringBuilder str= new StringBuilder();
        for (String key : achieved.keySet()){
            if (achieved.get(key)){
                str.append(" ").append(key);
            }
        }
        return str.toString();
    }
    
}