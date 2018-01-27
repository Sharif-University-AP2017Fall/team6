
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tara
 */
public abstract class WeaponView extends StackPane {

     public abstract void clear();

     public abstract void setPic(int i);

     public abstract void shoot(Alien min);

     public abstract boolean isFocus();

     public abstract void setFocus();

     public abstract void setUnfocus();

}
     
    
    

