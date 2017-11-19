/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tara
 */
public class WeaponAll extends Weapon {

    public WeaponAll(Dimension dimension, String type_, Hero mine) {
        super(dimension, type_, mine);
    }
    @Override
    public  boolean applyWeapon(Alien[] alien){return true;}
    
}
