//import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;

public class AlienCreeps extends Application {
    static boolean START = false;
    static boolean ISPAUSED = false;
    private static int CURRENT_SECOND = 0;
    private static int CURRENT_HOUR = 0;
    private static int CURRENT_DAY = 0;
    private static boolean gameEnded = false;
    Thread gameTime;
    Thread gameInput;

    private Scanner scanner = new Scanner(System.in);
    private Hero hero = new Hero(new Dimension(400, 300));
    static GameMap gameMap = new GameMap();//hero);
    static AlienCreeps game = new AlienCreeps();
    private Text timeText = new Text(935,60,"");

    Runnable mainRunnable;
    static boolean restart;

    public static void main(String[] args) {
        launch(args);
    }

    private String requestWeapon;

    private void resetGame(){
        restart = true;
        hero = new Hero(new Dimension(400, 300));
        START = false;
        ISPAUSED = false;
        gameEnded = false;
        gameMap = new GameMap();
        game = new AlienCreeps();
        timeText.setText("00:00:00");
        CURRENT_DAY = 0;
        CURRENT_HOUR = 0;
        CURRENT_SECOND = 0;
        removeElementFromGameRoot(((Group) gameScene.getRoot()));

        gameScene.setRoot(createGameContent());
        stage.setScene(menuScene);
    }

    private Runnable makeMainRunnable(){
        return new Runnable() {
            private final Object lock = new Object();


            @Override
            public void run() {
                System.out.println("SETTING MAIN RUNNABLE");
                Thread heroLifeCycle = new Thread(hero);
                heroLifeCycle.start();


                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (restart){
                        System.out.println("quitting main runnable");
                        break;
                    }
                    if (START && !ISPAUSED){
                        if (CURRENT_SECOND == 0 && CURRENT_HOUR == 0 && CURRENT_DAY == 0) {
                            //      gameMap.randomWeather();
                        }
                        if (CURRENT_SECOND < 60) { //9
                            CURRENT_SECOND++;
                            //     gameMap.plague();
                        } else if (CURRENT_HOUR < 23) { //23
                            //      gameMap.superNaturalHelp();
                            //      gameMap.naturalDisaster();
                            CURRENT_HOUR++;
                            CURRENT_SECOND = 0;
                        } else {
                            gameMap.setCanUpgradeSoldiers();
                            //      gameMap.randomWeather();
                            CURRENT_DAY++;
                            CURRENT_HOUR = 0;
                            CURRENT_SECOND = 0;
                        }
                        updateTime(CURRENT_DAY,CURRENT_HOUR,CURRENT_SECOND);

                    /*System.out.println("------------------------");
                    System.out.println("Current second = " + CURRENT_SECOND);
                    System.out.println("Current hour = "  + CURRENT_HOUR);
                    System.out.println("Current day = " + CURRENT_DAY);*/
                        gameMap.nextSecond();
                        if (gameEnded){
                            System.out.println("GAME ENDED");
                            //System.exit(0);
                        /*try {
                            stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        }

                    /*synchronized (lock){
                        if (gameMap.nextSecond()) {
                            endGame(true);
                            System.out.println("GAME OVER");
                            gameEnded = true;
                            gameTime.stop();
                            System.exit(0);
                            return;
                        }
                    }*/
                    }
                }
            }
        };
    }

    private void launchGame() {
        System.out.println("launching game");

        mainRunnable = makeMainRunnable();
        gameTime = new Thread(mainRunnable);
        gameTime.start();

        /*Runnable r2 = () -> {
            while (true) {
                String input = scanner.nextLine();
                String info[] = input.split(" ");
                if (input.matches("put [\\w]*[\\s]*[\\w]* in place [\\d]+")) {
                    String weaponName = info[1];
                    int locationNum;
                    if (!info[2].equals("in")) {
                        weaponName += " " + info[2];
                        locationNum = Integer.parseInt(info[5]);
                    } else {
                        locationNum = Integer.parseInt(info[4]);
                    }
                    gameMap.putWeaponInPlace(weaponName, locationNum);
                } else if (input.matches("upgrade [\\w]*[\\s]*[\\w]* in place [\\d]+")) {
                    String weaponName = info[1];
                    int locationNum;
                    if (!info[2].equals("in")) {
                        weaponName += " " + info[2];
                        locationNum = Integer.parseInt(info[5]);
                    } else {
                        locationNum = Integer.parseInt(info[4]);
                    }
                    gameMap.upgradeWeaponInPlace(weaponName, locationNum);
                } else if (input.matches("show details")) {
                    gameMap.showRemainingAliens();
                    System.out.println(hero);
                    System.out.println("*** Weapons ***");
                    System.out.println("----------");
                    gameMap.getWeapons().forEach(System.out::println);
                    System.out.print("\n\n");
                    gameMap.showReachedFlag();
                } else if (input.matches("move hero for \\(-*[\\d]+,[\\s]*-*[\\d]+\\)")) {
                    String dimInfo[] = input.substring(15, input.length() - 1).split(",[\\s]*");
                    Dimension change = new Dimension(Integer.parseInt(dimInfo[0]),
                            Integer.parseInt(dimInfo[1]));
                    gameMap.moveHero(change);
                } else if (input.matches("tesla in \\([\\d]+,[\\s]*[\\d]+\\)")) {
                    String dimInfo[] = input.substring(10, input.length() - 1).split(",[\\s]*");
                    Dimension dimension = new Dimension(Integer.parseInt(dimInfo[0]),
                            Integer.parseInt(dimInfo[1]));
                    gameMap.useTesla(dimension);
                } else if (input.matches("hero status")) {
                    hero.showStatus();
                } else if (input.matches("knights status")) {
                    hero.showKnightStatus();
                } else if (input.matches("weapons status")) {
                    gameMap.getWeapons().forEach(System.out::println);
                } else if (input.matches("status [\\w]*[\\s]*[\\w]* weapon")) {
                    String weaponName = info[1];
                    if (!info[2].equals("weapon")) {
                        weaponName += " " + info[2];
                    }
                    List<Weapon> weapons = gameMap.getWeapons();
                    if (!weapons.isEmpty()) {
                        gameMap.getWeapons(weaponName).forEach(System.out::println);
                    } else {
                        System.out.println("No " + weaponName + " found");
                    }
                } else if (input.matches("show achievements")) {
                    System.out.println(hero.getAchievement());
                } else if (input.matches("upgrade soldiers")) {
                    gameMap.upgradeSoldier();
                } else if (input.matches("show achievements")) {
                    System.out.print(hero.getAchievement().toString());
                } else if (input.matches("show money")) {
                    System.out.println(hero.getMoney());
                } else if (input.matches("burrow [\\d]+ from intergalactic bank")) {
                    gameMap.burrowMoney(Integer.parseInt(info[1]));
                } else if (input.matches("pay the intergalactic bank back")) {
                    gameMap.payBack();
                } else if (input.matches("show map")) {
                    System.out.println(gameMap);
                } else if (input.matches("show available locations")) {
                    gameMap.showAvailableLocations();
                } else {
                    System.out.println("invalid command");
                }
                if (gameEnded){
                    gameInput.stop();
                    return;
                }
            }
        };
        gameInput = new Thread(r2);
        gameInput.start();*/

        /*Runnable r3 = () -> */gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M){
                popupHeroDimStage.showAndWait();
            } else if (event.getCode() == KeyCode.B){
                stage.setScene(askWeaponScene);
                stage.show();
            } else if (event.getCode() == KeyCode.TAB){
                gameMap.focusWeapons();
            } else if (event.getCode() == KeyCode.SHIFT){
                gameMap.focusAliens();
            } else if (event.getCode() == KeyCode.W){
                gameMap.upgradeWeapon();
            } else if (event.getCode() == KeyCode.T){
                popupTeslaDimStage.showAndWait();
            } else if(event.getCode() == KeyCode.ESCAPE){
                gameMap.unFocusAll();
            } else if (event.getCode() == KeyCode.S){
                gameMap.upgradeSoldier();
            } else if (event.getCode() == KeyCode.BACK_SPACE){
                ISPAUSED = true;
                popupPauseStage.showAndWait();
                //TODO TRY SYNCHRONIZED
                //popupPauseScene.setRoot(createPauseSceneContent());
            }else{
                gameMap.moveHero(event);
            }
        });
        /*gameInput = new Thread(r3);
        gameInput.start();*/
    }

    static int getCurrentSecond() {
        return CURRENT_SECOND;
    }

    static int getCurrentHour() {
        return CURRENT_HOUR;
    }

    static int getCurrentDay() {
        return CURRENT_DAY;
    }


    static Scene menuScene = new Scene(new Group(),540, 1000);
    static Scene gameScene = new Scene(new Group(), GameMap.XBOUND + 200, GameMap.YBOUND);
    static Scene popupHeroDimScene = new Scene(new Group(), 450, 275);
    static Scene askWeaponScene = new Scene(new Group(), 500, 500);
    static Scene popupEndGameScene = new Scene(new Group(), 400, 250);
    static Scene popupLocationNumScene = new Scene(new Group(), 300, 600);
    static Scene popupTeslaDimScene  = new Scene(new Group(), 450, 275);
    static Scene popupPauseScene = new Scene(new Group(), 275, 300);

    static Stage stage;
    static Stage popupHeroDimStage = new Stage();
    static Stage popupEndGameStage = new Stage();
    static Stage popupLocationNUmStage = new Stage();
    static Stage popupTeslaDimStage = new Stage();
    static Stage popupPauseStage = new Stage();
    //static Stage statusStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        menuScene.setRoot(createMenuContent());
        stage.setScene(menuScene);

        gameScene.setRoot(createGameContent());
        askWeaponScene.setRoot(createAskWeaponContent());

        popupHeroDimScene.setRoot(createPopupAskDimContent(true));
        popupHeroDimStage.setScene(popupHeroDimScene);
        popupHeroDimStage.initModality(Modality.APPLICATION_MODAL);
        popupHeroDimStage.centerOnScreen();

        popupLocationNumScene.setRoot(createLocationNumContent());
        popupLocationNUmStage.setScene(popupLocationNumScene);
        popupLocationNUmStage.initModality(Modality.APPLICATION_MODAL);
        popupLocationNUmStage.centerOnScreen();

        popupEndGameStage.setScene(popupEndGameScene);
        popupEndGameStage.initModality(Modality.APPLICATION_MODAL);
        popupEndGameStage.setAlwaysOnTop(true);
        popupEndGameStage.centerOnScreen();

        popupTeslaDimScene.setRoot(createPopupAskDimContent(false));
        popupTeslaDimStage.setScene(popupTeslaDimScene);
        popupTeslaDimStage.initModality(Modality.APPLICATION_MODAL);
        popupTeslaDimStage.centerOnScreen();

        popupPauseScene.setRoot(createPauseSceneContent());
        popupPauseStage.setScene(popupPauseScene);
        popupPauseStage.initModality(Modality.APPLICATION_MODAL);
        popupPauseStage.setAlwaysOnTop(true);
        popupPauseStage.centerOnScreen();

        stage.show();
        stage.centerOnScreen();
    }

    private Parent createPauseSceneContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/pause/bg.png").toExternalForm()));

        MenuItem quit_to_main_menu = new MenuItem(new Image(getClass()
                .getResource("res/menu/pause/Quit to main menu.png").toExternalForm()), 220, 55);
        quit_to_main_menu.setDim(30, 45);
        quit_to_main_menu.setOnAction(new Runnable() {
            @Override
            public void run() {

                resetGame();
                //game = new AlienCreeps();
                popupPauseStage.close();

            }
        });


        MenuItem continue_ = new MenuItem(new Image(getClass()
                .getResource("res/menu/pause/Continue.png").toExternalForm()), 220, 55);
        continue_.setDim(30, 125);
        continue_.setOnAction(new Runnable() {
            @Override
            public void run() {
                ISPAUSED = false;
                popupPauseStage.close();
            }
        });

        MenuItem show_status = new MenuItem(new Image(getClass()
                .getResource("res/menu/pause/Show Status.png").toExternalForm()), 220, 55);
        show_status.setDim(30, 205);



        /*popupPauseScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    ISPAUSED = false;
                    popupPauseStage.close();
                }
            }
        });*/

        root.getChildren().addAll(background, quit_to_main_menu, continue_, show_status);

        return root;
    }

    static void endGame(boolean gameOver) {
        System.out.println("setting endgame scene");

         gameEnded = true;

         gameMap.stopWalking();

        boolean finalGameOver = gameOver;
        Platform.runLater(() -> {
            popupEndGameScene.setRoot(game.createEndGameContent(finalGameOver));
            popupEndGameStage.show();
        });

        Timeline end = new Timeline(new KeyFrame(Duration.millis(1500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               // Platform.exit();
                System.exit(0);
            }
        }));
        end.setCycleCount(1);
        end.play();

    }



    private Parent createGameContent(){
        Group root = new Group();
        Canvas canvas = new Canvas(GameMap.XBOUND + 300, GameMap.YBOUND);
        root.getChildren().add(canvas);
        MapView mapView = new MapView(canvas);
        gameMap = new GameMap();
        gameMap.setHero(this.hero);

        root.getChildren().addAll(hero.getAchievementView());
        root.getChildren().add(timeText);
        root.getChildren().add(hero.getWarriorView());
        root.getChildren().add(hero.getBulletView());
        root.getChildren().add(hero.getHealthBar());
        root.getChildren().add(hero.getMoneyBar());
        hero.getHealthBar().setDim(900, 140);
        hero.getMoneyBar().setDim(900, 100);
//        launchGame();

        return root;
    }

    private Parent createAskWeaponContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/ask_weapon/screen.png").toExternalForm()));

        background.setFitWidth(500);
        background.setFitHeight(500);

        GaussianBlur blur = new GaussianBlur(2);
        background.setEffect(blur);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 70);

        Text title = new Text("Choose a weapon");
        title.setFont(font);
        title.relocate(40, 20);
        title.setFill(Color.rgb(133, 171, 37));

        String address = "res/menu/ask_weapon/";

        Image barrack_btn = new Image(getClass()
                .getResource(address + "Barrack.png").toExternalForm());
        MenuItem barrack = new MenuItem(barrack_btn);
        barrack.setDim(50, 220);
        ImageView barrack_view = new ImageView(new Image(getClass()
                .getResource(address + "Barrack_view.png").toExternalForm()));
        barrack_view.relocate(67, 110);
        barrack.setOnAction(() -> {
            requestWeapon = "barrack";
            popupLocationNUmStage.show();
        });

        Image rocket_btn = new Image(getClass()
                .getResource(address + "Rocket.png").toExternalForm());
        MenuItem rocket = new MenuItem(rocket_btn);
        rocket.setDim(190, 220);
        ImageView rocket_view = new ImageView(new Image(getClass()
                .getResource(address + "Rocket_view.png").toExternalForm()));
        rocket_view.relocate(195, 110);
        rocket.setOnAction(() -> {
            requestWeapon = "rocket";
            popupLocationNUmStage.show();
        });


        Image freezer_btn = new Image(getClass()
                .getResource(address + "Freezer.png").toExternalForm());
        MenuItem freezer = new MenuItem(freezer_btn);
        freezer.setDim(340, 220);
        ImageView freezer_view = new ImageView(new Image(getClass()
                .getResource(address + "Freezer_view.png").toExternalForm()));
        freezer_view.relocate(345, 100);
        freezer.setOnAction(() -> {
            requestWeapon = "freezer";
            popupLocationNUmStage.show();
        });

        Image antiaircraft_btn = new Image(getClass()
                .getResource(address + "Antiaircraft.png").toExternalForm());
        MenuItem antiaircraft = new MenuItem(antiaircraft_btn);
        antiaircraft.setDim(50, 420);
        ImageView antiaircraft_view = new ImageView(new Image(getClass()
                .getResource(address + "Antiaircraft_view.png").toExternalForm()));
        antiaircraft_view.relocate(67, 265);
        antiaircraft.setOnAction(() -> {
            requestWeapon = "antiaircraft";
            popupLocationNUmStage.show();
        });

        Image machine_gun_btn = new Image(getClass()
                .getResource(address + "Machine Gun.png").toExternalForm());
        MenuItem machine_gun = new MenuItem(machine_gun_btn);
        machine_gun.setDim(190, 420);
        ImageView machine_gun_view = new ImageView(new Image(getClass()
                .getResource(address + "Machine Gun_view.png").toExternalForm()));
        machine_gun_view.relocate(207, 260);
        machine_gun.setOnAction(new Runnable() {
            @Override
            public void run() {
                requestWeapon = "machine gun";
                popupLocationNUmStage.show();
            }
        });


        Image laser_btn = new Image(getClass()
                .getResource(address + "Laser.png").toExternalForm());
        MenuItem laser = new MenuItem(laser_btn);
        laser.setDim(340, 420);
        ImageView laser_view = new ImageView(new Image(getClass()
                .getResource(address + "Laser_view.png").toExternalForm()));
        laser_view.relocate(360, 270);
        laser.setOnAction(() -> {
            requestWeapon = "laser";
            popupLocationNUmStage.show();
        });



        root.getChildren().addAll(background, title,
                barrack, barrack_view,
                rocket, rocket_view,
                freezer, freezer_view,
                antiaircraft, antiaircraft_view,
                machine_gun, machine_gun_view,
                laser, laser_view);

        askWeaponScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                stage.setScene(gameScene);
                stage.centerOnScreen();
                //stage.show();
                if (!START){
                  //  System.out.println("status = " + statusStage.focusedProperty());
                   // statusStage.show();
                    START = true;
                    launchGame();
                }
               // stage.requestFocus();
               // System.out.println("main = " + stage.focusedProperty());
                //stage.requestFocus();
                //stage.show();
            }
        });

        return root;
    }

    private Parent createMenuContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/bg/background.jpg").toExternalForm()));

        GaussianBlur blur = new GaussianBlur(2);
        background.setEffect(blur);

        background.setFitWidth(540);
        background.setFitHeight(810);

        MenuItem new_item = new MenuItem("NewGame");
        new_item.setOnAction(() -> {
//            gameScene.setRoot(createGameContent());
//            stage.setScene(gameScene);
            restart = false;
            stage.setScene(askWeaponScene);
            stage.show();
        });
        new_item.setDim(390, 300);

        MenuItem custom_item = new MenuItem("Custom");
        custom_item.setOnAction(new Runnable() {
            @Override
            public void run() {
                customizeGame();
            }
        });
        custom_item.setDim(300, 450);

        MenuItem exit_item = new MenuItem("Exit");
        exit_item.setOnAction(() -> System.exit(0));
        exit_item.setDim(390, 600);

        root.getChildren().addAll(background, new_item, custom_item, exit_item);

        Canvas canvas = new Canvas(560, 200);
        root.getChildren().add(canvas);

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.drawImage(new Image("res/menu/Title/AlienCreeps.png"), 5, 50);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200),
                new EventHandler<ActionEvent>() {
                    int imgNumber = 1;
                    double xDim = 50;
                    double deltaX = 50;
                    String address = "res/menu/spaceship/images/spaceship_";
                    @Override
                    public void handle(ActionEvent event) {
                        for (int i = 0; i < 10;i ++){
                            graphicsContext.clearRect(i * 50, 170, 48, 48);
                            graphicsContext.clearRect(i * 50, 15, 48, 48);
                        }
                        graphicsContext.drawImage(new Image(address + imgNumber + ".png"), xDim, 170);
                        graphicsContext.drawImage(new Image(address + imgNumber + ".png"), 500 - xDim, 15);
                        xDim += deltaX;
                        if (xDim == 450){
                            deltaX *= -1;
                        }
                        if (xDim == 50){
                            deltaX *= -1;
                        }
                        imgNumber++;
                        if (imgNumber == 25){
                            imgNumber = 1;
                        }
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return root;
    }


    private Parent createLocationNumContent(){
        Group root = new Group();
        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/ask_location/bg.png").toExternalForm()));

        background.setFitHeight(600);
        background.setFitWidth(300);


        ArrayList<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < 13; i++){
            items.add(new MenuItem(new Image("res/menu/ask_location/option.png"), String.valueOf(i + 1)));
            int finalI = i;
            items.get(i).setOnAction(new Runnable() {
                @Override
                public void run() {
                    popupLocationNUmStage.close();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gameMap.putWeaponInPlace(requestWeapon, finalI + 1);
                        }
                    });
                }
            });
        }
        items.get(0).setDim(62, 172);
        items.get(1).setDim(126, 124);
        items.get(2).setDim(190, 172);
        items.get(3).setDim(62, 272);
        items.get(4).setDim(126, 224);
        items.get(5).setDim(190, 272);
        items.get(6).setDim(62, 372);
        items.get(7).setDim(126, 324);
        items.get(8).setDim(190, 372);
        items.get(9).setDim(62, 472);
        items.get(10).setDim(126, 424);
        items.get(11).setDim(190, 472);
        items.get(12).setDim(126, 524);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 40);

        Text command = new Text("Choose a location\n for your weapon.");
        command.setFont(font);
        command.setFill(Color.rgb(208, 153, 69));
        command.relocate(24, 25);

        popupLocationNumScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                popupLocationNUmStage.close();
            }
        });
        root.getChildren().addAll(background, command);
        root.getChildren().addAll(items);
        return root;

    }

    Parent createEndGameContent(boolean gameOver){
        Group root = new Group();
        Font font1 = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 40);
        Font font2 = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 25);
        Font font3 = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);

        String address = "res/menu";
        if (gameOver){
            address += "/Lose";
        }else {
            address += "/Win";
        }

        ImageView background = new ImageView(new Image(getClass()
                .getResource(address + "/bg.png").toExternalForm()));

        background.setFitWidth(400);
        background.setFitHeight(250);

        Text msg1 = new Text("");
        Text msg2 = new Text("");
        Text msg3 = new Text("");
        msg1.setFont(font1);
        msg2.setFont(font2);
        msg3.setFont(font3);
        msg1.relocate(75, 50);
        msg2.relocate(40, 100);
        msg3.relocate(50, 180);

        if (gameOver){
            msg1.setText("GAME OVER");
            msg2.setText("The aliens have taken over the planet.");
            msg3.setText("You have failed this city");
            msg1.setFill(Color.rgb(194, 30, 30));
            msg2.setFill(Color.rgb(194, 30, 30));
            msg3.setFill(Color.rgb(194, 30, 30));

        } else{
            msg1.setText("CONGRATULATIONS! YOU WON!");
            msg2.setText("You have saved the planet!");
            msg3.setText("yaaaaaaaaaaay");
            msg1.setFill(Color.rgb(213, 163, 224));
            msg2.setFill(Color.rgb(213, 163, 224));
            msg3.setFill(Color.rgb(213, 163, 224));
        }

        root.getChildren().addAll(background, msg1, msg2, msg3);
        return root;
    }

    private Parent createPopupAskDimContent(boolean forHero){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/ask_dim/askdim.png").toExternalForm()));
        background.setFitWidth(450);
        background.setFitHeight(275);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 40);

        Text question = new Text("");
        question.setFont(font);
        question.setFill(Color.rgb(132, 171, 37));
        question.relocate(55, 30);
        if (forHero){
            question.setText("Enter hero's destination");
        } else{
            question.setText("Enter Tesla's dimension");
        }

        TextField x = new TextField();
        Label lblx = new Label("");
        lblx.setFont(font);
        x.relocate(200, 115);
        lblx.relocate(255, 110);
        x.setOpacity(0.0);
        x.setOnKeyPressed(new TextEventHandler(lblx));

        TextField y = new TextField();
        Label lbly = new Label("");
        lbly.setFont(font);
        y.relocate(200, 195);
        lbly.relocate(255, 187);
        y.opacityProperty().set(0.0);
        y.setOnKeyPressed(new TextEventHandler(lbly));

        if (forHero){
            popupHeroDimScene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    if (lblx.getText() != "" && lbly.getText() != ""){
                        Dimension change = new Dimension(Integer.parseInt(lblx.getText())- hero.getDimension().getX(),
                                Integer.parseInt(lbly.getText()) - hero.getDimension().getY());
                        hero.setShouldMove(change);
                    }
                    x.clear();
                    y.clear();
                    lblx.setText("");
                    lbly.setText("");
                    x.requestFocus();
                    popupHeroDimStage.close();
                }
            });
        } else {
            popupTeslaDimScene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    if (lblx.getText() != "" && lbly.getText() != ""){
                        gameMap.useTesla(new Dimension(Integer.parseInt(lblx.getText()),
                                Integer.parseInt(lbly.getText())));
                    }
                    x.clear();
                    y.clear();
                    lblx.setText("");
                    lbly.setText("");
                    x.requestFocus();
                    popupTeslaDimStage.close();
                }
            });
        }


        root.getChildren().addAll(background, question, x, y, lblx, lbly);

        return root;
    }

    static Scene customizeWeaponScene = new Scene(new Group(), 800, 500);
    static Scene customizeAliensScene = new Scene(new Group(), 800, 500);
    static Scene customizeHeroScene = new Scene(new Group(), 600, 500);

    /*static Stage customizeWeaponStage = new Stage();
    static Stage customizeAliensStage = new Stage();
    static Stage customizeHeroStage = new Stage();*/


    public void customizeGame(){
        customizeWeaponScene.setRoot(createCustomizeWeaponContent());
        //customizeWeaponStage.setScene(customizeWeaponScene);

        customizeAliensScene.setRoot(createCustomizeAliensContent());
        //customizeAliensStage.setScene(customizeAliensScene);

        customizeHeroScene.setRoot(createCustomizeHeroContent());
        //customizeHeroStage.setScene(customizeHeroScene);

        //customizeWeaponStage.show();
        stage.setScene(customizeWeaponScene);
        stage.show();
    }

    private Parent createCustomizeWeaponContent() {
        Group root = new Group();
        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(800);
        Image view = new Image(getClass()
                .getResource("res/menu/custom/option.png").toExternalForm());

        ArrayList<MenuItem> accepts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            accepts.add(new MenuItem(view, 90, 40, "Accept"));
            accepts.get(i).relocate(240 + i * 105, 380);
        }

        ArrayList<MenuItem> deletes = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            deletes.add(new MenuItem(view, 90, 40, "Delete"));
            deletes.get(i).relocate(240 + i * 105, 430);
        }

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);

        ArrayList<Text> fields = new ArrayList<>();
        fields.add(new Text("Price: "));
        fields.add(new Text("Radius: "));
        fields.add(new Text("Bullet\nSpeed: "));
        fields.add(new Text("Bullet\nPower: "));
        fields.add(new Text("Speed\nReduction: "));

        for (int i = 0; i < fields.size(); i++){
            fields.get(i).setFont(font);
         //   fields.get(i).relocate(110, i * 60 + 30);
        }

        VBox vBox = new VBox();
        vBox.getChildren().addAll(fields);
        vBox.setPadding(new Insets(15));
        //vBox1.setPadding(new Insets(5));
        vBox.setSpacing(10);
        vBox.setTranslateX(35);
        vBox.setTranslateY(0);
        vBox.setLayoutY(5);

        ArrayList<TextField> machinegun = new ArrayList<>();
        ArrayList<TextField> laser = new ArrayList<>();
        ArrayList<TextField> freezer = new ArrayList<>();
        ArrayList<TextField> anti = new ArrayList<>();
        ArrayList<TextField> rocket = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            machinegun.add(new TextField());
            machinegun.get(i).setPrefWidth(50);

            laser.add(new TextField());
            laser.get(i).setPrefWidth(50);

            freezer.add(new TextField());
            freezer.get(i).setPrefWidth(50);

            anti.add(new TextField());
            anti.get(i).setPrefWidth(50);

            rocket.add(new TextField());
            rocket.get(i).setPrefWidth(50);
        }

        VBox vBox1 = new VBox();
        vBox1.setSpacing(40);
        vBox1.setPadding(new Insets(15));
        vBox1.setTranslateX(35 + 135);
        vBox1.getChildren().addAll(machinegun);

        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(15));
        vBox2.setSpacing(40);
        vBox2.setTranslateX(35 + 2 * 135);
        vBox2.getChildren().addAll(rocket);

        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(15));
        vBox3.setSpacing(40);
        vBox3.setTranslateX(35 + 3 * 135);
        vBox3.getChildren().addAll(freezer);

        VBox vBox4 = new VBox();
        vBox4.setSpacing(40);
        vBox4.setPadding(new Insets(15));
        vBox4.setTranslateX(35 + 4 * 135);
        vBox4.getChildren().addAll(laser);


        VBox vBox5 = new VBox();
        vBox5.setSpacing(40);
        vBox5.setPadding(new Insets(15));
        vBox5.setTranslateX(35 + 5 * 135);
        vBox5.getChildren().addAll(anti);

        root.getChildren().add(background);
        root.getChildren().addAll(accepts);
        root.getChildren().addAll(deletes);
        root.getChildren().addAll(vBox, vBox1, vBox2, vBox3, vBox4, vBox5);

        machinegun.get(0).setText(String.valueOf(InitialWeapon.getPrice("machine gun")));
        machinegun.get(1).setText(String.valueOf(InitialWeapon.getRadius("machine gun")));
        machinegun.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("machine gun")));
        machinegun.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("machine gun")));
        machinegun.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("machine gun")));

        accepts.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("machine gun", Integer.parseInt(machinegun.get(0).getText()));
                InitialWeapon.setRadius("machine gun", Double.parseDouble(machinegun.get(1).getText()));
                InitialWeapon.setBulletSpeed("machine gun", Integer.parseInt(machinegun.get(2).getText()));
                InitialWeapon.setBulletPower("machine gun", Integer.parseInt(machinegun.get(3).getText()));
                InitialWeapon.setSpeedReduction("machine gun", Integer.parseInt(machinegun.get(4).getText()));
            }
        });


        rocket.get(0).setText(String.valueOf(InitialWeapon.getPrice("rocket")));
        rocket.get(1).setText(String.valueOf(InitialWeapon.getRadius("rocket")));
        rocket.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("rocket")));
        rocket.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("rocket")));
        rocket.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("rocket")));

        accepts.get(1).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("rocket", Integer.parseInt(machinegun.get(0).getText()));
                InitialWeapon.setRadius("rocket", Double.parseDouble(machinegun.get(1).getText()));
                InitialWeapon.setBulletSpeed("rocket", Integer.parseInt(machinegun.get(2).getText()));
                InitialWeapon.setBulletPower("rocket", Integer.parseInt(machinegun.get(3).getText()));
                InitialWeapon.setSpeedReduction("rocket", Integer.parseInt(machinegun.get(4).getText()));
            }
        });

        freezer.get(0).setText(String.valueOf(InitialWeapon.getPrice("freezer")));
        freezer.get(1).setText(String.valueOf(InitialWeapon.getRadius("freezer")));
        freezer.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("freezer")));
        freezer.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("freezer")));
        freezer.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("freezer")));

        accepts.get(2).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("freezer", Integer.parseInt(machinegun.get(0).getText()));
                InitialWeapon.setRadius("freezer", Double.parseDouble(machinegun.get(1).getText()));
                InitialWeapon.setBulletSpeed("freezer", Integer.parseInt(machinegun.get(2).getText()));
                InitialWeapon.setBulletPower("freezer", Integer.parseInt(machinegun.get(3).getText()));
                InitialWeapon.setSpeedReduction("freezer", Integer.parseInt(machinegun.get(4).getText()));
            }
        });

        laser.get(0).setText(String.valueOf(InitialWeapon.getPrice("laser")));
        laser.get(1).setText(String.valueOf(InitialWeapon.getRadius("laser")));
        laser.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("laser")));
        laser.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("laser")));
        laser.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("laser")));

        accepts.get(3).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("laser", Integer.parseInt(machinegun.get(0).getText()));
                InitialWeapon.setRadius("laser", Double.parseDouble(machinegun.get(1).getText()));
                InitialWeapon.setBulletSpeed("laser", Integer.parseInt(machinegun.get(2).getText()));
                InitialWeapon.setBulletPower("laser", Integer.parseInt(machinegun.get(3).getText()));
                InitialWeapon.setSpeedReduction("laser", Integer.parseInt(machinegun.get(4).getText()));
            }
        });

        anti.get(0).setText(String.valueOf(InitialWeapon.getPrice("antiaircraft")));
        anti.get(1).setText(String.valueOf(InitialWeapon.getRadius("antiaircraft")));
        anti.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("antiaircraft")));
        anti.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("antiaircraft")));
        anti.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("antiaircraft")));

        accepts.get(4).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("antiaircraft", Integer.parseInt(machinegun.get(0).getText()));
                InitialWeapon.setRadius("antiaircraft", Double.parseDouble(machinegun.get(1).getText()));
                InitialWeapon.setBulletSpeed("antiaircraft", Integer.parseInt(machinegun.get(2).getText()));
                InitialWeapon.setBulletPower("antiaircraft", Integer.parseInt(machinegun.get(3).getText()));
                InitialWeapon.setSpeedReduction("antiaircraft", Integer.parseInt(machinegun.get(4).getText()));

                //stage.setScene(customizeAliensScene);
                stage.setScene(customizeAliensScene);
            }
        });

        deletes.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.removeWeapon("machine gun");
            }
        });

        deletes.get(1).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.removeWeapon("rocket");
            }
        });

        deletes.get(2).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.removeWeapon("freezer");
            }
        });

        deletes.get(3).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.removeWeapon("laser");
            }
        });

        deletes.get(4).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.removeWeapon("antiaircraft");
                stage.setScene(customizeAliensScene);
            }
        });



        return root;
    }

    private Parent createCustomizeAliensContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(800);
        Image view = new Image(getClass()
                .getResource("res/menu/custom/option.png").toExternalForm());

        HBox acceptHbox = new HBox();
        acceptHbox.setSpacing(10);
        ArrayList<MenuItem> accepts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            accepts.add(new MenuItem(view, 90, 40, "Accept"));
        }
        acceptHbox.relocate(80, 380);
        acceptHbox.getChildren().addAll(accepts);
        //acceptHbox.setAlignment(Pos.BOTTOM_RIGHT);

        HBox deleteHbox = new HBox();
        deleteHbox.setSpacing(10);
        deleteHbox.setPadding(new Insets(10));
        deleteHbox.relocate(80, 430);
        //deleteHbox.setTranslateY(500);
        ArrayList<MenuItem> deletes = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            deletes.add(new MenuItem(view, 90, 40, "Delete"));
        }
        deleteHbox.getChildren().addAll(deletes);
        //deleteHbox.setAlignment(Pos.BASELINE_RIGHT);


        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);

        ArrayList<Text> fields = new ArrayList<>();
        fields.add(new Text("Energy: "));
        fields.add(new Text("Speed: "));
        fields.add(new Text("Bullet\nSpeed: "));
        fields.add(new Text("Bullet\nPower: "));
        //fields.add(new Text("Radius: "));

        for (int i = 0; i < fields.size(); i++){
            fields.get(i).setFont(font);
            //   fields.get(i).relocate(110, i * 60 + 30);
        }

        VBox vBox = new VBox();
        vBox.getChildren().addAll(fields);
        vBox.setPadding(new Insets(15));
        //vBox1.setPadding(new Insets(5));
        vBox.setSpacing(10);
        vBox.setTranslateX(35);
        vBox.setTranslateY(0);
        vBox.setLayoutY(5);

        ArrayList<TextField> albertonion = new ArrayList<>();
        ArrayList<TextField> aironion = new ArrayList<>();
        ArrayList<TextField> algwasonion = new ArrayList<>();
        ArrayList<TextField> activionion = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            albertonion.add(new TextField());
            albertonion.get(i).setPrefWidth(50);

            aironion.add(new TextField());
            aironion.get(i).setPrefWidth(50);

            algwasonion.add(new TextField());
            algwasonion.get(i).setPrefWidth(50);

            activionion.add(new TextField());
            activionion.get(i).setPrefWidth(50);
        }

        VBox vBox1 = new VBox();
        vBox1.setSpacing(40);
        vBox1.setPadding(new Insets(15));
        vBox1.setTranslateX(35 + 135);
        vBox1.getChildren().addAll(albertonion);

        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(15));
        vBox2.setSpacing(40);
        vBox2.setTranslateX(35 + 2 * 135);
        vBox2.getChildren().addAll(aironion);

        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(15));
        vBox3.setSpacing(40);
        vBox3.setTranslateX(35 + 3 * 135);
        vBox3.getChildren().addAll(algwasonion);

        VBox vBox4 = new VBox();
        vBox4.setSpacing(40);
        vBox4.setPadding(new Insets(15));
        vBox4.setTranslateX(35 + 4 * 135);
        vBox4.getChildren().addAll(activionion);

        albertonion.get(0).setText(String.valueOf(Alien.getInitialEnergy("albertonion")));
        albertonion.get(1).setText(String.valueOf(Alien.getInitialSpeed("albertonion")));
        albertonion.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("albertonion")));
        albertonion.get(3).setText(String.valueOf(Alien.getInitialStrength("albertonion")));

        aironion.get(0).setText(String.valueOf(Alien.getInitialEnergy("aironion")));
        aironion.get(1).setText(String.valueOf(Alien.getInitialSpeed("aironion")));
        aironion.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("aironion")));
        aironion.get(3).setText(String.valueOf(Alien.getInitialStrength("aironion")));

        algwasonion.get(0).setText(String.valueOf(Alien.getInitialEnergy("algwasonion")));
        algwasonion.get(1).setText(String.valueOf(Alien.getInitialSpeed("algwasonion")));
        algwasonion.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("algwasonion")));
        algwasonion.get(3).setText(String.valueOf(Alien.getInitialStrength("algwasonion")));

        activionion.get(0).setText(String.valueOf(Alien.getInitialEnergy("activionion")));
        activionion.get(1).setText(String.valueOf(Alien.getInitialSpeed("activionion")));
        activionion.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("activionion")));
        activionion.get(3).setText(String.valueOf(Alien.getInitialStrength("activionion")));

        accepts.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("albertonion", Integer.parseInt(albertonion.get(0).getText()));
                Alien.changeInitialSpeed("albertonion", Integer.parseInt(albertonion.get(1).getText()));
                Alien.changeInitialShootingSpeed("albertonion", Integer.parseInt(albertonion.get(2).getText()));
                Alien.changeInitialStrength("albertonion", Integer.parseInt(albertonion.get(3).getText()));
            }
        });

        accepts.get(1).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("aironion", Integer.parseInt(aironion.get(0).getText()));
                Alien.changeInitialSpeed("aironion", Integer.parseInt(aironion.get(1).getText()));
                Alien.changeInitialShootingSpeed("aironion", Integer.parseInt(aironion.get(2).getText()));
                Alien.changeInitialStrength("aironion", Integer.parseInt(aironion.get(3).getText()));
            }
        });

        accepts.get(2).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("algwasonion", Integer.parseInt(algwasonion.get(0).getText()));
                Alien.changeInitialSpeed("algwasonion", Integer.parseInt(algwasonion.get(1).getText()));
                Alien.changeInitialShootingSpeed("algwasonion", Integer.parseInt(algwasonion.get(2).getText()));
                Alien.changeInitialStrength("algwasonion", Integer.parseInt(algwasonion.get(3).getText()));
            }
        });

        accepts.get(3).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("activionion", Integer.parseInt(activionion.get(0).getText()));
                Alien.changeInitialSpeed("activionion", Integer.parseInt(activionion.get(1).getText()));
                Alien.changeInitialShootingSpeed("activionion", Integer.parseInt(activionion.get(2).getText()));
                Alien.changeInitialStrength("activionion", Integer.parseInt(activionion.get(3).getText()));

                stage.setScene(customizeHeroScene);
            }
        });

        deletes.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("albertonion");
            }
        });

        deletes.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("aironion");
            }
        });

        deletes.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("algwasonion");
            }
        });

        deletes.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("activionion");
                stage.setScene(customizeHeroScene);
            }
        });

        root.getChildren().addAll(background, acceptHbox, deleteHbox, vBox, vBox1, vBox2, vBox3, vBox4);
        return root;
    }

    private Parent createCustomizeHeroContent(){
        Group root = new Group();

        return root;
    }




    public static void addElementToGameRoot(int index, Node ... node){
        for (int i = 0; i < node.length; i++){
            if (!((Group) gameScene.getRoot()).getChildren().contains(node[i]))
                ((Group) gameScene.getRoot()).getChildren().add(index, node[i]);
        }
    }

    public static void removeElementFromGameRoot(Node ... node){
        ((Group) gameScene.getRoot()).getChildren().removeAll(node);
    }

    public void updateTime(int day,int hour,int sec){

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 40);
        String daystring = String.valueOf(day);
        String hourString = String.valueOf(hour);
        String secondString = String.valueOf(sec);
        if (day < 10){
            daystring = "0" + day;
        }

        if (hour < 10){
            hourString = "0" + hour;
        }

        if (sec < 10){
            secondString = "0" + sec;
        }

           timeText.setText(""+daystring+":"+hourString+":"+secondString);
           timeText.setFont(font);
           timeText.setFill(Color.rgb(50, 20, 15));
           
    }
    
}

class TextEventHandler implements EventHandler<KeyEvent>{


    private Label lbl;

    public TextEventHandler(Label label) {
        this.lbl = label;
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()){
            case DIGIT0:
                lbl.setText(lbl.getText() + 0);
                break;
            case DIGIT1:
                lbl.setText(lbl.getText() + 1);
                break;
            case DIGIT2:
                lbl.setText(lbl.getText() + 2);
                break;
            case DIGIT3:
                lbl.setText(lbl.getText() + 3);
                break;
            case DIGIT4:
                lbl.setText(lbl.getText() + 4);
                break;
            case DIGIT5:
                lbl.setText(lbl.getText() + 5);
                break;
            case DIGIT6:
                lbl.setText(lbl.getText() + 6);
                break;
            case DIGIT7:
                lbl.setText(lbl.getText() + 7);
                break;
            case DIGIT8:
                lbl.setText(lbl.getText() + 8);
                break;
            case DIGIT9:
                lbl.setText(lbl.getText() + 9);
                break;
            case BACK_SPACE:
                if (lbl.getText().length() > 0){
                    lbl.setText(lbl.getText().substring(0, lbl.getText().length() - 1));
                }
        }
    }
}
