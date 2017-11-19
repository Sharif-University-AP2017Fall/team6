/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

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
    private Achivement achieved;
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    

    public void setMoney(int a){money=a;}
    public void setMoney(String a){money=Integer.parseInt(a.trim());}
    public int getMoney(){return money;}
    public void setPowerLevel(int a){}
    public void setExperienceLevel(int a){}
    public void setPowerLevel(){}
    public void setExperienceLevel(){}
    public int getPowerLevel(){return powerLevel;}
    public int getExperienceLevel(){return experienceLevel;}
    public int calculateResurrectionTime(){
        return 0;}
    public boolean applyWeapon(Alien[] alien){return true;};
    public void soldierDied(Soldier dead){}
    public boolean gotShot(Alien a){return true;}
    public void addMoney(int a){setMoney(getMoney()+a);}
    public void addMoney(String a){
        int b=Integer.parseInt(a.trim());
        setMoney(getMoney()+b);}

    public void moveSoldiers(){
        for (int i=0;i<3;i++){
            soldiers[i].move();}
    }

    public Weapon buyWeapon(String nameOfWeapon){
        if (this.getMoney()<Weapon.getInitialPrice(nameOfWeapon))
            return null;
  
        /// buy the new weapon!
    return null;
    }


    public boolean upgradeWeapon(Weapon toUpgrade){return false;}

    Hero (Dimension dimension){
        setDimension(dimension);
        setMoney(1000);

        for (int i=0;i<3;i++){
            soldiers[i]=new Soldier(this,dimension);
                }
    }
    public boolean died(){return false;}
    @Override
    public String toString(){return null;}
    @Override
    public void move(){}
}


class Achivement {
    //static int numTypeAlien;
    int numTypeAlien=4;
    private int[] numOfKilledByWeapon=new int [numTypeAlien];
    private int[] numOfKilledBySoldier=new int [numTypeAlien];
    private int[] numOfKilledByHero=new int [numTypeAlien];
    private boolean GreatHunter;
    private boolean goodGene;
    private boolean greekGodess;
    private boolean eagleEye;
    private boolean restlessShooter;
    private boolean braveWarrior;
    private boolean butcher;
    private boolean bloodSucker;
    
    public void killedWeapon(Alien alien){
        switch (alien.getName()){
            case "Albertonion":
                numOfKilledByWeapon[0]++;
                if(numOfKilledByWeapon[0]>9)
                    restlessShooter=true;
                break;
            case "Algwasonion":
                numOfKilledByWeapon[1]++;
                if(numOfKilledByWeapon[1]>9)
                    braveWarrior=true;
                break;
            case "Activionion":
                numOfKilledByWeapon[2]++; 
                if(numOfKilledByHero[2]>9)
                    butcher=true;
                break;
            case "Aironion":
                numOfKilledByWeapon[3]++;
                if(numOfKilledByHero[3]>9)
                    bloodSucker=true;
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
                    GreatHunter=true;
                break;
            case "Algwasonion":
                numOfKilledByHero[1]++;
                if(numOfKilledByHero[1]>4)
                    goodGene=true;
                break;
            case "Activionion":
                numOfKilledByHero[2]++; 
                if(numOfKilledByHero[2]>4)
                    greekGodess=true;
                if(numOfKilledByHero[2]>9)
                    eagleEye=true;
                break;
            case "Aironion":
                numOfKilledByHero[3]++;
                break;
        }
    }

    
}