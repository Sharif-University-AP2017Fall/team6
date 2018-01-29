import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MapView {
    private Canvas canvas;
    private GraphicsContext gc;
    String[] tilesInput;
    String Tara= "";//"/Users/Apple/Documents/TaraFiles/University/term 7/JAVA/project4/team6/";
    String Tara1= "";//"/Users/Apple/Documents/TaraFiles/University/term 7/JAVA/project4/team6/src";
                    
            
    public MapView(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();


        drawGrass();
        drawGrassDetails();

        drawRoute1();
        drawRoute2();

        Tile flag1 = new Tile(new Image("res/bg/flag/flag_1.png"));
        flag1.draw(gc, 27 * 32, 10 * 32);
        Tile flag2 = new Tile(new Image("res/bg/flag/flag_2.png"));
        flag2.draw(gc, 27 * 32, 11 * 32);

        drawWeapons();

        drawTrees(false);
        drawTrees(true);
        drawBushes();

        drawStatus();
    }

    private void drawStatus(){
        /*** BACKGROUND BOARDS ***/
        gc.drawImage(new Image("res/menu/status/bg.png"), GameMap.XBOUND, 0, 200, 100);
        gc.drawImage(new Image("res/menu/status/bg.png"), GameMap.XBOUND, 100, 200, 140);
        gc.drawImage(new Image("res/menu/status/bg.png"), GameMap.XBOUND, 240, 200, 110);
        gc.drawImage(new Image("res/menu/status/bg.png"), GameMap.XBOUND, 350, 200, GameMap.YBOUND - (110 + 140 + 100));

        /** TIME BOARD ***/
        drawBoard();

        heroStatus();
        knightStatus();
        weaponStatus();
        alienStatus();

    }

    private void heroStatus(){
        gc.drawImage(new Image("res/menu/item/name.png"), 1060, 100, 27, 140);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 30);
        Text heroText = new Text("Hero");
        heroText.relocate(1045, 160);
        //heroText.relocate(500, 500);
        heroText.setFill(Color.rgb(50, 20, 15));
        heroText.setFont(font);
        heroText.setRotate(90);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        heroText);
            }
        });
    }

    private void knightStatus(){
        gc.drawImage(new Image("res/menu/item/name.png"), 1060, 100 + 130, 27, 120);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 30);
        Text soldier = new Text("Soldiers");
        soldier.relocate(1032, 160 + 130);
        //heroText.relocate(500, 500);
        soldier.setFill(Color.rgb(50, 20, 15));
        soldier.setFont(font);
        soldier.setRotate(90);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        soldier);
            }
        });
    }

    private void weaponStatus(){
        gc.drawImage(new Image("res/menu/item/name.png"), 1060, 350, 27, 575 - 350);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 30);
        Text weapons = new Text("Weapon");
        weapons.relocate(1030, 160 + 75 + (575 - 350));
        //heroText.relocate(500, 500);
        weapons.setFill(Color.rgb(50, 20, 15));
        weapons.setFont(font);
        weapons.setRotate(90);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        weapons);
            }
        });
    }

    private void alienStatus(){

        gc.drawImage(new Image("res/menu/item/name.png"), 1060, 575, 27, 700 - 575);
        gc.drawImage(new Image("res/bg/flag/flag_1.png"), GameMap.XBOUND+20,GameMap.YBOUND-100 , 20, 20);
        gc.drawImage(new Image("res/bg/flag/flag_2.png"),GameMap.XBOUND+20,GameMap.YBOUND-100+20, 20, 20);
     //   gc.drawImage(new Image("res/menu/item/board3.png"),GameMap.XBOUND+30,GameMap.YBOUND-105, 120, 60);
        
        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 30);
        Text alien = new Text("Alien");
        alien.relocate(1045, 160 + 120 + (575 - 350) + (700 - 575));
        //heroText.relocate(500, 500);
        alien.setFill(Color.rgb(50, 20, 15));
        alien.setFont(font);
        alien.setRotate(90);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        alien);
            }
        });

    }

    public void drawGrass(){
        try {
            tilesInput = readStringFromFile(Tara+"src/res/bg/base_grass/grass_map.txt").split(",");
        } catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]) - 934;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("/res/bg/base_grass/grass_" + String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawGrassDetails(){
        try {
            tilesInput = readStringFromFile(Tara+"src/res/bg/details/details.txt").split(",");
        } catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                k++;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("/res/bg/details/detail_" + String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }

            }
        }
    }

    public void drawRoute2(){
        try {
            tilesInput = readStringFromFile(Tara+"src/res/bg/Route2/Route2.txt").split(",");
        }catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                switch (whichTile){
                    case 837:
                        whichTile = 2;
                        break;
                    case 966:
                        whichTile = 15;
                        break;
                    case 997:
                        whichTile = 17;
                        break;
                    case 870:
                        whichTile = 6;
                        break;
                    case 900:
                        whichTile = 7;
                        break;
                    case 933:
                        whichTile = 11;
                        break;
                    case 965:
                        whichTile = 14;
                        break;
                    case 964:
                        whichTile = 13;
                        break;
                    case 901:
                        whichTile = 8;
                        break;
                    case 902:
                        whichTile = 9;
                        break;
                    case 869:
                        whichTile = 5;
                        break;
                    case 838:
                        whichTile = 3;
                        break;
                }

                k++;
                if (whichTile != 0 && whichTile < 100){
                    Tile tile = new Tile(new Image("res/bg/Route2/Route2_" +String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawRoute1(){
        try {
            tilesInput = readStringFromFile(Tara+"src/res/bg/Route1/Route1.txt").split(",");
        }catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                switch (whichTile){
                    case 49:
                        whichTile = 5;
                        break;
                    case 82:
                        whichTile = 9;
                        break;
                    case 177:
                        whichTile = 17;
                        break;
                    case 113:
                        whichTile = 11;
                        break;
                    case 18:
                        whichTile = 3;
                        break;
                    case 144:
                        whichTile = 13;
                        break;
                    case 145:
                        whichTile = 14;
                        break;
                    case 81:
                        whichTile = 8;
                        break;
                    case 146:
                        whichTile = 15;
                        break;
                    case 17:
                        whichTile = 2;
                        break;
                    case 80:
                        whichTile = 7;
                        break;
                    case 50:
                        whichTile = 6;
                        break;
                }
                k++;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("/res/bg/Route1/Route1_" + String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawWeapons(){
        try {
            tilesInput = readStringFromFile(Tara+"src/res/bg/Weapons/Weapons.txt").split(",");
        }catch (IOException e){
            e.printStackTrace();
        }
        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                k++;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("res/bg/Weapons/" + String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawTrees(boolean isTop){
        try {
            if (isTop){
                tilesInput = readStringFromFile(Tara+"src/res/bg/trees/big/top.txt").split(",");
            }else{
                tilesInput = readStringFromFile(Tara+"src/res/bg/trees/big/bottom.txt").split(",");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                k++;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("res/bg/trees/big/tree_" + String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawBushes(){
        try {
            tilesInput = readStringFromFile(Tara+"src/res/bg/trees/small/bush.txt").split(",");
        }catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                k++;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("res/bg/trees/small/bush_" + String.valueOf(whichTile) + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    private String readStringFromFile(String address) throws IOException {

        FileReader fileReader = new FileReader(address);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        fileReader.close();
        return stringBuffer.toString();

    }
    
    
    
    void drawBoard(){
    
        
        String address = "/res/menu/item/board1.png";
        
       
        gc.drawImage(new Image(getClass()
                 .getResource(address).toExternalForm())
                ,920 ,20, 160,80);
/*
        gc.drawImage(new Image(getClass()
                 .getResource(address).toExternalForm())
                ,290 ,-20, 4*32,3*32 );
        
        gc.drawImage(new Image(getClass()
                 .getResource(address).toExternalForm())
                ,390 ,-20, 4*32,3*32 );
        
        gc.drawImage(new Image(getClass()
                 .getResource(address).toExternalForm())
                ,500 ,-20, 5*32,3*32 );
        
        gc.drawImage(new Image(getClass()
                 .getResource(address).toExternalForm())
                ,625 ,-20, 5*32,3*32+2 );*/

    }
    
    
    
    
    
    
    
    
    
    
    
    
}
