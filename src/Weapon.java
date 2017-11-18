/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Tara
 */
public class Weapon {
    
    private String name;
    private int type;
    //private double x,y;
    private int powerOfBullet;
    private int powerOfBulletAir;
    private int speedOfBullet;
    private double radious;
    private int price;
    private Hero myHero;
    private int level;
    private boolean onAirOnly;
    private int speedReduction;
    private Dimension dimension;

    public Dimension getDimension() {
        return dimension;
    }
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setMyHero(Hero hero){myHero=hero;}
    public Hero getMyHero(){return myHero;}
    public void setName(String a){name = a.trim();}
    public String getName(){return name;}
    public void setRadious(double a){radious=a;}
    public void setRadious(String a){radious=Double.parseDouble(a.trim());}
    public double getRadious(){return radious;}
    public void setPowerOfBullet(int a){powerOfBullet=a;}
    public void setPowerOfBullet(String a){powerOfBullet=Integer.parseInt(a.trim());}
    public int getPowerOfBullet(){return powerOfBullet;}
    public void setPowerOfBulletAir(int a){powerOfBulletAir=a;}
    public void setPowerOfBulletAir(String a){powerOfBulletAir=Integer.parseInt(a.trim());}
    public int getPowerOfBulletAir(){return powerOfBulletAir;}
    public void setSpeedOfBullet(int a){speedOfBullet=a;}
    public void setSpeedOfBullet(String a){speedOfBullet=Integer.parseInt(a.trim());}
    public int getSpeedOfBullet(){return speedOfBullet;} 
    public void setPrice(int a){price=a;}
    public int getPrice(){return price;}
    public void setPrice(String a){price=Integer.parseInt(a.trim());}
    public void setOnAirOnly(){onAirOnly=true;}
    public boolean getOnAirOnly(){return onAirOnly;}

    public int getLevel(){return level;}
    public void setLevel(int a){level=a;}
    public boolean upgrade(int money){
        if ((money>(int)(getPrice()*1.2))&& level<3)
            {setPrice((int)(getPrice()*1.2));
            level++;
            upgradePower();
            upgradeRadious();
            return true;}
        return false;
        }
    public void upgradePower(){
        setPowerOfBulletAir((int)(getPowerOfBulletAir()*1.1));
        setPowerOfBullet((int)(getPowerOfBullet()*1.1));
    }
    public void upgradeRadious(){
        setRadious(getRadious()*1.1);
    }
    public void setSpeedReduction(int a){speedReduction=a;}
    public void setSpeedReduction(String a){speedReduction=Integer.parseInt(a.trim());}
    public int getSpeedReduction(){return speedReduction;} 
    public int getType(){return type;}
    public void setType(int a){type=a;}
    public void setType(String a){
        switch (a){
            case "Machine Gun":
                setSpeedOfBullet(10);
                setPowerOfBullet(10);
                setPowerOfBulletAir(5);
                setSpeedReduction(0);
                setRadious(1);
                setPrice(100);
                setType(0); 
                break; 
            case "Rocket":
                setSpeedOfBullet(3);
                setPowerOfBullet(20);
                setPowerOfBulletAir(10);
                setSpeedReduction(0);
                setRadious(2);
                setPrice(180);
                setType(1);
                break;
            case "Laser":
                setSpeedOfBullet(7);
                setPowerOfBullet(10);
                setPowerOfBulletAir(7);
                setSpeedReduction(40);
                setRadious(1);
                setPrice(150);
                setType(2);
                break;
            case "Antiaircraft":
                setSpeedOfBullet(15);
                setPowerOfBullet(0);
                setPowerOfBulletAir(12);
                setSpeedReduction(20);
                setOnAirOnly();
                setRadious(1.5);
                setPrice(180);
                setType(3);
                break;
            case "Freezer":
                setSpeedOfBullet(5);
                setPowerOfBullet(5);
                setPowerOfBulletAir(3);
                setSpeedReduction(60);
                setRadious(1);
                setPrice(170);
                setType(4);
                break;
               
            default:
                break;
                
        
        
        }
                
    
    
    
    }
    Weapon(Dimension dimension,String type_,Hero mine){
        setDimension(dimension);
        setType(type_);
        setName(type_);
        setMyHero(mine);
    }
    public static Weapon search(Weapon weapon,String nameSearch){return null;}
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
                return -1;
        }

    }
    public boolean applyWeapon(Alien[] alien){return false;}
    @Override
    public String toString(){return null;}



    
    
}
