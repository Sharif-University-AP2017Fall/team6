/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phase1;

import java.util.ArrayList;

/**
 *
 * @author Tara
 */
public class Hero extends Warrior{
    private Soldier[] soldiers=new Soldier[3]; // remember to initialize!
    private int resurrectionTime;
    private int powerLevel;
    private int experienceLevel;
    private int[] numOfKilledByWeapon=new int [4];
    private int[] numOfKilledBySoldier=new int [4];
    private int[] numOfKilledByHero=new int [4];
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    
    public int[] getNumOfKilledByWeopen(){return numOfKilledByWeapon;}
    public int[] getNumOfKilledBySoldier(){return numOfKilledBySoldier;}
    public int[] getNumOfKilledByHero(){return numOfKilledByHero;}
    public void addNumOfKilledByHero(String type){}
    public void addNumOfKilledBySoldier(String type){}
    public void addNumOfKilledByWeopen(String type){}
    //public void addNumOfKilledByHero(int a){numOfKilledByHero=numOfKilledByHero+a;}
    //public void addNumOfKilledBySoldier(int a){numOfKilledBySoldier=numOfKilledBySoldier+a;}
    //public void addNumOfKilledByWeapon(int a){numOfKilledByWeapon=numOfKilledByWeapon+a;}
    public void setPowerLevel(int a){}
    public void setExperienceLevel(int a){}
    public void setPowerLevel(){}
    public void setExperienceLevel(){}
    public int getPowerLevel(){return powerLevel;}
    public int getExperienceLevel(){return experienceLevel;}
    public int calculateResurrectionTime(){
        return 0;}
    public boolean applyWeapon(Alien[] alien){return true;};
    public boolean isDead(){
        if(getEnergy()>0)
            return false;
        return true;}
    public void soldierDied(Soldier dead){}
    public boolean gotShot(Alien a){return true;}
    public void addMoney(int a){setMoney(getMoney()+a);}
    public void addMoney(String a){
        int b=Integer.parseInt(a.trim());
        setMoney(getMoney()+b);}
    public void move(){}
    public void gotByWorm(int x,int y){setX(x);setY(y);moveSoldiers();}
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
    Hero(double x,double y){
        setXY(x,y);
        setMoney(1000);
        for (int i;i<3;i++){
            soldiers[i]=new Soldier(this,x,y);
                }
    
    
    
    }
    @Override
    public String toString(){return null;}
}
