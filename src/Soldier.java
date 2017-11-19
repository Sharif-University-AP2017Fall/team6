
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
public class Soldier extends Warrior {
    private Hero myHero;
    
    public void setMyHero(Hero hero){myHero=hero;}
    public Hero getMyHero(){return myHero;}
    public void increaseRadious(int payment){
            while(payment>9){
            setRadious(getRadius()*1.1);
            payment=payment-10;
            }            
    }
    public void addMoney(int a){
        increaseRadious(a);
    }
    public void addMoney(String a){
        int b=Integer.parseInt(a.trim());
        increaseRadious(b);
    }
    public void resetRadious(){
            setRadius();
    }
    Soldier(Hero a,Dimension dimension){
        setMyHero(a);
        setDimension(dimension);

    }
    public boolean gotShot(Alien a){
        setEnergy(getEnergy()-a.getStrength());
        if(isDead())
            died();
        return true;}   
    @Override
    public String toString(){return "Soldier: \n \t Radius: "+ getRadius()+" Power: "+getPowerOfBullet();}
    public boolean died(){
        getMyHero().soldierDied(this);
        return false;
    }
    public boolean applyWeapon(List<Alien> alien){
                int n=alien.size();
        int numBullet=0;
        for (int i=0;i<n;i++){
            if (this.getDimension().distance(alien.get(i).getDimension())<this.getRadius())
                {
                   alien.get(i).gotShot(this); 
                   numBullet++;
                   if(numBullet>getSpeedOfBullet())
                       break;
                }
        
        }
        
        return true;
    } 
    
    
    public void incresePowerOfBullet(int a){}
    public boolean resurrection(){return true;}

    @Override
    public void move(){}
    

    
}
