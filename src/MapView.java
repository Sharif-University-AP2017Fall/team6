import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sun.net.www.content.image.png;

import java.io.*;
import java.util.ArrayList;

public class MapView {
    private Canvas canvas;
    private GraphicsContext gc;
    String[] tilesInput;

    public MapView(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        drawGrass();
        drawRoute1();
        drawRoute2();

        Tile flag1 = new Tile(new Image("res/bg/flag/flag_1.png"));
        flag1.draw(gc, 27 * 32, 10 * 32);
        Tile flag2 = new Tile(new Image("res/bg/flag/flag_2.png"));
        flag2.draw(gc, 27 * 32, 11 * 32);

        drawWeapons();
    }


    public void drawGrass(){
        try {
            tilesInput = readStringFromFile("src/res/bg/base_grass/grass_map.txt").split(",");
        } catch (IOException e){
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]) - 934;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("/res/bg/base_grass/grass_" + whichTile + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawRoute2(){
        try {
            tilesInput = readStringFromFile("src/res/bg/Route2/Route2.txt").split(",");
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
                    Tile tile = new Tile(new Image("res/bg/Route2/Route2_" + whichTile + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawRoute1(){
        try {
            tilesInput = readStringFromFile("src/res/bg/Route1/Route1.txt").split(",");
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
                    Tile tile = new Tile(new Image("/res/bg/Route1/Route1_" + whichTile + ".png"));
                    tile.draw(gc, j * 32, i * 32);
                }
            }
        }
    }

    public void drawWeapons(){
        try {
            tilesInput = readStringFromFile("src/res/bg/Weapons/Weapons.txt").split(",");
        }catch (IOException e){
            e.printStackTrace();
        }
        int k = 0;
        for (int i = 0; i < 22; i++){
            for (int j = 0; j < 28; j++){
                int whichTile = Integer.parseInt(tilesInput[k]);
                k++;
                if (whichTile != 0){
                    Tile tile = new Tile(new Image("res/bg/Weapons/" + whichTile + ".png"));
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
}
