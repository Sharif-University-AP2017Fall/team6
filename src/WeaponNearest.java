


import java.util.ArrayList;

import java.util.List;
import javafx.animation.FadeTransition;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Platform;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.layout.StackPane;

import javafx.scene.shape.*;
import javafx.util.Duration;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tara
 */
public class WeaponNearest extends Weapon {

    WeaponNearestView weaponView;
    
    WeaponNearest(Dimension dimension, String type, int locationNum) {
        
        super(dimension, type, locationNum);
        weaponView=new WeaponNearestView(type,dimension);
        super.setWeaponView(weaponView);
        
    }

    
   
    
    
    
    private Object lock = new Object();

    @Override
    public List<Alien> shoot(List<Alien> aliens) {
        
        List<Alien> canShoot = new ArrayList<>();
        List<Alien> deadAliens = new ArrayList<>();

        if (this.isOnAirOnly()) {
            for (int i = 0; i < aliens.size(); i++) {
                if (aliens.get(i).isCanFly()) {
                    canShoot.add(aliens.get(i));
                }
            }
        } else {
            canShoot.addAll(aliens);
        }

        if (!canShoot.isEmpty()) {
            
            Alien min = findClosest(canShoot);
            int maxBullet = this.getSpeedOfBullet();
            
            for (int numBullet = 0; numBullet < maxBullet; numBullet++) {
                
                try {
                    Thread.sleep(100);
                    
                } catch (InterruptedException e) {
                    
                    e.printStackTrace();
                }
                synchronized (lock){
                    
                    weaponView.shoot(min);
                    
                    min.reduceSpeed(this.getSpeedReduction() / 100);
                    
                    if (min.isCanFly()) {
                        
                        min.reduceEnergy(this.getPowerOfBulletAir());
                        
                    } else {
                        
                        min.reduceEnergy(this.getPowerOfBullet());
                        
                    }
                    if (min.isDead()) {
                        

                        System.out.println(getName() + " killed " + min.getName());
                        //  System.out.println(min.getName() + " died.");
                        deadAliens.add(min);
                        canShoot.remove(min);

                        Alien finalMin = min;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AlienCreeps.removeElementFromGameRoot(finalMin.getAlienView());
                            }
                        });
                        if (!canShoot.isEmpty()) {
                            min = findClosest(canShoot);
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            synchronized (lock){
                                stopShooting();
                            }
                            System.out.println("finished shooting");
                            return deadAliens;
                        }
                    }
                }
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

    private Alien findClosest(List<Alien> aliens) {
        
        Alien min = aliens.get(0);
        
        Dimension shootingPoint = this.getShootingPoint();
        
        double distance = min.getCurrentDim().distanceFrom(shootingPoint);
        
        for (int i = 1; i < aliens.size(); i++) {
            
            double comparable = shootingPoint.distanceFrom(aliens.get(i).getCurrentDim());
            
            if (distance > comparable) {
                
                distance = comparable;
                min = aliens.get(i);
                
            }
        }
        
        return min;
        
    }
}






class WeaponNearestView extends WeaponView {

     public ImageView[] pic;

     public Dimension dim;
     
     public int index;

     public int indB;
     
     public ImageView bul;
     
     private String nameW; 
     
     public WeaponNearestView(String name, Dimension dim_){
         
         System.out.println("setting view for " + name);
         this.pic = new ImageView[8];
         index = 5;
         dim=dim_;
         nameW=name;
         
         String address = "res/weapons/" + name +"/";
         pic[4] = new ImageView(new Image(getClass()
                 .getResource(address + "5.png").toExternalForm()));
         pic[4].setFitWidth(64);
         pic[4].setFitHeight(64);
         pic[4].setVisible(true);

         for (int i=0;i<8;i++){
             
             if (i==4)
                 continue;
             
            pic[i] = new ImageView(new Image(getClass()
                    .getResource(address +String.valueOf(i+1)+".png").toExternalForm()));
            pic[i].setFitWidth(64);
            pic[i].setFitHeight(64);
            pic[i].setVisible(false);

         }
         

            bul = new ImageView(new Image(getClass()
                    .getResource(address+"10.png").toExternalForm()));
            bul.setFitWidth(10);
            bul.setFitHeight(10);
            bul.setVisible(true);

         
         
         
         pic[4].setVisible(true);
         getChildren().addAll(pic[0],
                 pic[1],
                 pic[2],
                 pic[3],
                 pic[4],
                 pic[5],
                 pic[6],
                 pic[7],
                 bul
                );
         
         setTranslateX(dim_.getX()-32);
         setTranslateY(dim_.getY()-32);
         
     }

     @Override
     public void clear() {
         
         pic[0].setVisible(false);
         pic[1].setVisible(false);
         pic[2].setVisible(false);
         pic[3].setVisible(false);
         pic[4].setVisible(false);
         pic[5].setVisible(false);
         pic[6].setVisible(false);
         pic[7].setVisible(false);
         
     }

     
     
    @Override
     public void setPic(int i){
         
         clear();
         pic[i].setVisible(true);
     
     }

     
     @Override
     public void shoot(Alien min) {

         Dimension prey=min.getCurrentDim();
         double deltaX=Dimension.deltaX(this.dim, prey);
         double deltaY=Dimension.deltaY(this.dim, prey);
         
         changePic(deltaX,deltaY);
         
         new ShootView(dim, prey,nameW );
         
         
     }
     
     
     
     public void changePic(double deltaX,double deltaY){
     
         
            if (deltaX==0){
                    if(deltaY>0){

                        setPic(6);

                    }
                    else{

                        setPic(2);  

                    }  
                    return;
                }

                double slope=-Math.atan(deltaY/deltaX);

                if(deltaX>0){
                    if(slope<0.4 && slope>-0.4)
                       {
                       setPic(0);
                       return;
                       }
                    if(slope>0.4 && slope<1.2)
                       {
                       setPic(1);
                       return;
                       }
                    if(slope<-0.4 && slope>-1.2)
                       {
                       setPic(7);
                       return;
                       }
                    if(slope<-1.2)
                       {
                       setPic(6);
                       return;
                       }
                    if(slope>1.2)
                       {
                       setPic(2);
                       return;
                       }
                }
                else{
                    if(slope<0.4 && slope>-0.4)
                       {
                       setPic(4);
                       return;
                       }
                    if(slope>0.4 && slope<1.2)
                       {
                       setPic(5);
                       return;
                       }
                    if(slope<-0.4 && slope>-1.2)
                       {
                       setPic(3);
                       return;
                       }
                    if(slope<-1.2)
                       {
                       setPic(2);
                       return;
                       }
                    if(slope>1.2)
                       {
                       setPic(6);
                       return;
                       } 
                }


     
     
     
     
     
     } 
     
     
     
 }



class ShootView extends StackPane {
       
    
    ShootView(Dimension dim1,Dimension dim2, String name ){
    
        
         double deltaX=Dimension.deltaX(dim1, dim2);
         double deltaY=Dimension.deltaY(dim1, dim2);
         
         
         ImageView bullet=new ImageView(new Image(getClass()
                    .getResource("res/weapons/" + name +"/10.png").toExternalForm()));;
         
         /*          
         bullet.setVisible(true);
         bullet.setFitWidth(10);
         bullet.setFitHeight(10);
         
         Path path = new Path();
         path.getElements().add (new MoveTo (dim1.getX(), dim1.getY()));
         
         LineTo lineTo=new LineTo(deltaX,deltaY);
         path.getElements().add (lineTo);
         
         PathTransition pathTransition=new PathTransition();
         pathTransition.setDuration(Duration.millis(100));
         pathTransition.setNode(bullet);
         pathTransition.setPath(path);
         pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
         pathTransition.setCycleCount(1);
         pathTransition.setAutoReverse(false);

         pathTransition.play();
         
          
         FadeTransition ft = new FadeTransition(Duration.millis(120), bullet);
         ft.setFromValue(1.0);
         ft.setToValue(0);
         ft.setCycleCount(1);
         ft.setAutoReverse(false);

         ft.play();
         */
          Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                bullet.setVisible(true);
                                bullet.setFitWidth(20);
                                bullet.setFitHeight(20);

                                Path path = new Path();
                                path.getElements().add (new MoveTo (dim1.getX(), dim1.getY()));

                                LineTo lineTo=new LineTo(deltaX,deltaY);
                                path.getElements().add (lineTo);

                                PathTransition pathTransition=new PathTransition();
                                pathTransition.setDuration(Duration.millis(150));
                                pathTransition.setNode(bullet);
                                pathTransition.setPath(path);
                                pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
                                pathTransition.setCycleCount(1);
                                pathTransition.setAutoReverse(false);

                                pathTransition.play();


                                FadeTransition ft = new FadeTransition(Duration.millis(150), bullet);
                                ft.setFromValue(1.0);
                                ft.setToValue(0);
                                ft.setCycleCount(1);
                                ft.setAutoReverse(false);

                                ft.play();
                                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot()
                                        .getChildrenUnmodifiable().size(),bullet);
                            }
                        });
         
    
    }
    



}































