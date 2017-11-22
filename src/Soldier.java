
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

    public void increaseRadious(){
        setRadius(getRadius() * 1.1);
    }

    public void resetRadious(){
            setRadius(0.5);
    }

    Soldier(Hero a,Dimension dimension){
        setDimension(dimension);

    }
    public boolean gotShot(Alien a){
        setEnergy(getEnergy()-a.getStrength());
        if(isDead())
            died();
        return true;}   
    /*@Override
    public String toString(){return "Soldier: \n \t Radius: "+ getRadius()+" Power: "+getPowerOfBullet();}*/

    public void showStatus(){
        System.out.println("place: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy() +
                "\n");
    }

    @Override
    public String toString() {
        return "\tname: " + super.getName() +
                "\nplace: " + super.getDimension() +
                "\tenergy left : " + super.getEnergy() +
                "\n";
    }

    @Override
    public boolean died() {
        return false;
    }

    public boolean resurrection(){return true;}

    @Override
    public void move(Dimension changeDimension) {
        Dimension newDimension = new Dimension(getShootingPoint().getX() + changeDimension.getX(),
                getShootingPoint().getY() + changeDimension.getY());
        if (newDimension.isWithinBounds()){
            setDimension(newDimension);
        }
    }
}
