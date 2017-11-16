/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phase1;

/**
 *
 * @author Tara
 */
public abstract class Warrior {
    private String name;
    private int money;
    private double radious;
    private int powerOfBullet;
    private int speedOfBullet;
    private int energy;
    private double x,y; //// double?!?!?!
    private Map = thisMap;
    
    public void setX(double a){x=a;}
    public void setY(double a){y=a;}
    public void setXY(double a,double b){x=a;y=b;}
    public void setX(String a){x=Integer.parseInt(a.trim());}
    public void setY(String a){y=Integer.parseInt(a.trim());}
    public void setXY(String a,String b){x=Integer.parseInt(a.trim());y=Integer.parseInt(b.trim());}    
    public double getX(){return x;}
    public double getY(){return y;}
    public void setName(String a){name = a.trim();}
    public String getName(){return name;}
    public void setMoney(int a){money=a;}
    public void setMoney(String a){money=Integer.parseInt(a.trim());}
    public int getMoney(){return money;}
    public void setRadious(double a){radious=a;}
    public void setRadious(){radious=0.5;}
    public void setRadious(String a){radious=Double.parseDouble(a.trim());}
    public double getRadious(){return radious;}
    public void setPowerOfBullet(int a){powerOfBullet=a;}
    public void setPowerOfBullet(String a){powerOfBullet=Integer.parseInt(a.trim());}
    public int getPowerOfBullet(){return powerOfBullet;}
    public void setSpeedOfBullet(int a){speedOfBullet=a;}
    public void setSpeedOfBullet(String a){speedOfBullet=Integer.parseInt(a.trim());}
    public int getSpeedOfBullet(){return speedOfBullet;} 
    public void setEnergy(int a){energy=a;}
    public void setEnergy(String a){energy=Integer.parseInt(a.trim());}
    public int getEnergy(){return energy;}
    public void reduceEnergy(int a){energy=energy-a;}
    public void setMap(Map a){thisMap=a;}
    public Map getMap(){return thisMap;}
    public double distance(int X,int Y){
        return Math.sqrt(Math.pow((X-getX()),2) +Math.pow((Y-getY()),2));
    }
    
    public abstract boolean applyWeapon(Alien[] alien);
    public abstract boolean isDead();
    public abstract boolean gotShot(Alien a);
    public abstract void addMoney(int a);
    public abstract void addMoney(String a);
    public abstract void move();
}
