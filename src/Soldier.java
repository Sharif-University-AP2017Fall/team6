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

    public void incresePowerOfBullet(int a){}
    public boolean applyWeapon(Alien[] alien){return true;};
    public boolean resurrection(){return true;}
    
    public void increaseRadious(int payment){
            while(payment>9){
            setRadious(getRadious()*1.1);
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
            setRadious();
    }
    public void move(){}

    Soldier(Hero a,Dimension dimension){
        setMyHero(a);
        setDimension(dimension);

    }
    
    public boolean gotShot(Alien a){return false;}
    public boolean died(){return false;}
    @Override
    public String toString(){return null;}
}
