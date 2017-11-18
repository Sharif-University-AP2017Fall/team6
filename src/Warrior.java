/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Tara
 */
public abstract class Warrior {
    private String name;
    private double radious;
    private int powerOfBullet;
    private int speedOfBullet;
    private int energy;
    private GameMap gameMap;
    private Dimension dimension;


    public Dimension getDimension() {
        return dimension;
    }
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setName(String a){name = a.trim();}
    public String getName(){return name;}
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
    public boolean isDead(){
        if ( 0<getEnergy())
            return false;
        return true;}
    public void reduceEnergy(int a){
        energy=energy-a;
        if (isDead())
            died();

    }


    public void setGameMap(GameMap a){gameMap=a;}
    public GameMap getGameMap(){return gameMap;}

    
    public abstract boolean applyWeapon(Alien[] alien);
    public abstract boolean died();
    public abstract boolean gotShot(Alien a);
    public abstract void move();
}
