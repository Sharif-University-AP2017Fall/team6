
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;




/**
 *
 * @author Tara
 */
public class WeaponAll extends Weapon {

    
    WeaponAllView weaponView;
    
    WeaponAll(Dimension dimension, String type, int locationNum) {
        
        super(dimension, type, locationNum);
        weaponView=new WeaponAllView(type, dimension);
        super.setWeaponView(weaponView);
        
    }

    

    
    private Object lock = new Object();

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        List<Alien> canShoot = new ArrayList<>();
        List<Alien> deadAliens = new ArrayList<>();

        for (int i = 0; i < aliens.size(); i++) {
            
            if (!this.isOnAirOnly() || (this.isOnAirOnly() && aliens.get(i).isCanFly())) {
                
                canShoot.add(aliens.get(i));
                
            }
        }

        int numBullet = 0;
        int maxBullet = getSpeedOfBullet();
        for (int i = 0; i < canShoot.size(); i++) {
            
            Alien ai = canShoot.get(i);

            if (!ai.isDead()) {
                try {
                    
                    Thread.sleep(100);
                    
                } catch (InterruptedException e) {
                    
                    e.printStackTrace();
                    
                }
                
                synchronized (lock){
                    
                    ai.reduceSpeed(this.getSpeedReduction() / 100);
                    
                    if (ai.isCanFly()) {
                        
                        ai.reduceEnergy(this.getPowerOfBulletAir());
                        
                    } else {
                        
                        ai.reduceEnergy(this.getPowerOfBullet());
                        
                    }
                    if (ai.isDead()) {
                        
                        System.out.println(getName() + " killed " + ai.getName());
                        deadAliens.add(ai);
                        //TODO DELETE FROM SCRREN
                    }
                    
                    numBullet++;
                }
            }
            if (numBullet >= maxBullet) { 
                //no longer have any bullets
                break;
                
            } else if (numBullet < getSpeedOfBullet() && i == canShoot.size() - 1) {
                //still have bullets, start killing from top
                i = -1;
            }
            if (canShoot.size() == deadAliens.size()) { 
                //killed all aliens
                break;
            }
        }
        
        System.out.println("finished shooting");
        
        try {
            
            Thread.sleep(10);
            
        } catch (InterruptedException e) {
            
            e.printStackTrace();
            
        }
        synchronized (lock){
            
            stopShooting();
            
        }
        
        return deadAliens;
        
    }
}





class WeaponAllView extends WeaponView {

     public ImageView pic;

     public Dimension dim;
     
     public int index;

     public WeaponAllView(String name, Dimension dim_) {
         
         System.out.println("setting view for " + name);
         
         dim=dim_;
         
         
         String address = "res/weapons/" + name + "/";
         pic = new ImageView(new Image(getClass()
                 .getResource(address + "1.png").toExternalForm()));
         pic.setFitWidth(64);
         pic.setFitHeight(64);
         pic.setVisible(true);

         

         getChildren().addAll(pic);
         setTranslateX(dim_.getX());
         setTranslateY(dim_.getX());
     }

    @Override
     public void clear() {
         
         pic.setVisible(false);

     }

     @Override
     public  void shoot(Alien min){}
     
     @Override
     public void setPic(int i){}

     
 }





