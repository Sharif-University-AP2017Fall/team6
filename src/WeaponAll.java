
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;




/**
 *
 * @author Tara
 */
public class WeaponAll extends Weapon {

    private Dimension dim;
    private WeaponAllView weaponView;
    private String name;
    WeaponAll(Dimension dimension, String type, int locationNum) {
        
        super(dimension, type, locationNum);
        weaponView = new WeaponAllView(type, dimension);
        super.setWeaponView(weaponView);
        dim = dimension;
        name=type;
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
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AlienCreeps.removeElementFromGameRoot(ai.getAlienView());
                            }
                        });
                    }
                    
                    numBullet++;
                    
                    Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                        AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                            .getChildrenUnmodifiable().size(),new ShootViewAll(dim, name ));
                                       }

                        });
                    
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
        
       // System.out.println("finished shooting");
        
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
         setTranslateX(dim_.getX()-32);
         setTranslateY(dim_.getY()-32);
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











class ShootViewAll extends StackPane {
       
    
    int ind=0; 
    ImageView[] bullet=new ImageView[6];
    ShootViewAll(Dimension dim1, String name ){
    
          
         
         
         
                    
           for (int i=0;i<6;i++){
               
               bullet[i]=new ImageView(new Image(getClass()
                         .getResource("res/weapons/" + name +"/"+String.valueOf(i+10)+".png").toExternalForm()));
               bullet[i].setFitWidth(32*(i+1));
               bullet[i].setFitHeight(32*(i+1));
               bullet[i].setVisible(false);
               
           }         

           Platform.runLater(new Runnable() {
               
                            @Override
                            public void run() {
                                /*
                                        AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                                     .getChildrenUnmodifiable().size(),bullet[0]);          
                                         AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                                     .getChildrenUnmodifiable().size(),bullet[1]);
                                         AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                                     .getChildrenUnmodifiable().size(),bullet[2]);
                                         AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                                     .getChildrenUnmodifiable().size(),bullet[3]);
                                         AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                                     .getChildrenUnmodifiable().size(),bullet[4]);
                                         AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                                                     .getChildrenUnmodifiable().size(),bullet[5]);
                                         
*/

                                getChildren().addAll(bullet);
                                setTranslateX(dim1.getX()-96);
                                setTranslateY(dim1.getY()-96);
                                
                            }
                            
                           
                        });
        
          Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                
                                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {


                                            @Override
                                            public void handle(ActionEvent event) {
                                                 clear();
                                                 if(ind<5)
                                                    {
                                                    bullet[ind].setVisible(true);
                                                    }
                                                    
                                                 ind++;
                                                }
                                }));
                                
                                
                                
                                
                                timeline.setCycleCount(6);
                                timeline.play();
                                
 
                            }
                            
                           
                        });
        
          
          
          
          
    
    }
    
    
    
    public void clear(){
    
         bullet[0].setVisible(false);
         bullet[1].setVisible(false);
         bullet[2].setVisible(false);
         bullet[3].setVisible(false);
         bullet[4].setVisible(false);
         bullet[5].setVisible(false);
         
    }



}



