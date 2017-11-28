
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

    public void increaseRadius(){
        setRadius(getRadius() * 1.1);
    }

    public void resetRadious(){
            setRadius(0.5);
    }

    public void reduceRadius(){
        setRadius(getRadius() * 0.85);
    }

    Soldier(Dimension dimension){
        setEnergy(150);
        setRadius(0.5);
        setDimension(dimension);
    }

    Soldier(){
        setRadius(0.5);
        setEnergy(150);
    }

    public void showStatus(){
        System.out.println("place: " + super.getDimension() +
                "\tenergy left: " + super.getEnergy() +
                "\n");
    }

    @Override
    public String toString() {
        return "\tplace: " + super.getDimension() +
                "\tenergy left : " + super.getEnergy() +
                "\n";
    }

    @Override
    public boolean died() {
        return false;

    }

    public boolean resurrection(){return true;}

    @Override
    public boolean move(Dimension changeDimension) {
        Dimension newDimension = new Dimension(getShootingPoint().getX() + changeDimension.getX(),
                getShootingPoint().getY() + changeDimension.getY());
        if (newDimension.isWithinBounds(GameMap.XBOUND, 0, GameMap.YBOUND, 0)){
            System.out.println("Soldier moved to " + newDimension);
            setDimension(newDimension);
            return true;
        }
        return false;
    }
}
