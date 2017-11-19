
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
public abstract class Weapon implements Mappable{
    
    private String name;
    private int type;
    private int powerOfBullet;
    private int powerOfBulletAir;
    private int speedOfBullet;
    private double radius;
    private int price;
    private Hero myHero;
    private int level;
    private boolean onAirOnly;
    private int speedReduction;
    private Dimension dimension;
    private  int InitialPrice;
    private  int InitialPowerOfBullet;
    private  int InitialPowerOfBulletAir;
    private  int InitialSpeedOfBullet;
    private  int InitialSpeedReduction;
    private  double InitialRadius;
    
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setMyHero(Hero hero){myHero=hero;}
    public Hero getMyHero(){return myHero;}
    public void setName(String a){name = a.trim();}
    public String getName(){return name;}
    public void setRadius(double a){radius=a;}
    public void setRadius(String a){radius=Double.parseDouble(a.trim());}
    public double getRadius(){return radius;}
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
    public boolean upgrade(){
        if ((getMyHero().getMoney()>(int)(getPrice()*1.2)) && level<3)
            {setPrice((int)(getPrice()*1.2));
            getMyHero().addMoney(-getPrice());
            level++;
            upgradePower();
            upgradeRadious();
            return true;}
        return false;
        }
    private void upgradePower(){
        setPowerOfBulletAir((int)(getPowerOfBulletAir()*1.1));
        setPowerOfBullet((int)(getPowerOfBullet()*1.1));
    }
    private void upgradeRadious(){
        setRadius(getRadius()*1.1);
    }
    public void setSpeedReduction(int a){speedReduction=a;}
    public void setSpeedReduction(String a){speedReduction=Integer.parseInt(a.trim());}
    public int getSpeedReduction(){return speedReduction;}
    public int getType(){return type;}
    private void setType(int a){type=a;}
    private void setType(String a){
        InitialPrice=Weapon.getInitialPrice(a);
        setPrice(InitialPrice);
        InitialSpeedOfBullet=getInitialSpeedOfBullet(a);
        setSpeedOfBullet(InitialSpeedOfBullet);
        InitialPowerOfBullet=getInitialPowerOfBullet(a);
        setPowerOfBullet(InitialPowerOfBullet);
        InitialPowerOfBulletAir=getInitialPowerOfBulletAir(a);
        setPowerOfBulletAir(InitialPowerOfBulletAir);
        InitialSpeedReduction=getInitialSpeedReduction(a);
        setSpeedReduction(InitialSpeedReduction);
        InitialRadius=getInitialRadius(a);
        setRadius(InitialRadius);
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
                setOnAirOnly();
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
    
    Weapon(Dimension dimension,String type_,Hero mine){
        setDimension(dimension);
        setType(type_);
        setName(type_);
        setMyHero(mine);
    }
    public static Weapon WeaponFactory(Dimension dimension,String type_,Hero mine){
         switch (type_){           
            case "Machine Gun":               
                return  new WeaponNearest(dimension,type_, mine); 
                
            case "Rocket":                
                return  new WeaponAll(dimension,type_, mine); 
            case "Laser":               
                return  new WeaponNearest(dimension,type_, mine); 
            case "Antiaircraft":              
                return  new WeaponNearest(dimension,type_, mine); 
            case "Freezer":
                return  new WeaponAll(dimension,type_, mine); 
            default:
                System.out.println(type_+ "not Found");
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
        
        String str="name: "+getName()+" place: "+" not defined in toString yet"+ " level: "+getLevel();
        
        return str;}
    public static Weapon search(List<Weapon> weapon,String nameSearch){
        int n=weapon.size();
        Weapon searching;
        for (int i=0;i<n;i++){
            searching=weapon.get(i);
            if (searching!=null && searching.getName().equalsIgnoreCase(nameSearch))
                {return searching;}
        
        }
        return null;}
    
    
    
    
    public static Weapon search(List<Weapon> weapon,int x,int y){
        int n=weapon.size();
        Weapon searching;
        for (int i=0;i<n;i++){

        
        }
        return null;}
    public abstract boolean applyWeapon(List<Alien> alien);
    
 
}
