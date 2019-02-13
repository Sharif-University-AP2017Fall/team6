//import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

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

    static MediaPlayer bg_sound;

    Runnable mainRunnable;
    static boolean restart;
    static boolean pressedSpace = false;

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

                gameMap.initialNumFlag();
                gameMap.initalWeaponStatus();
                gameMap.initBank();

                Media sound=new Media(getClass().getResource("res/sound/videoplayback.mp3").toExternalForm());
                bg_sound=new MediaPlayer(sound);
                bg_sound.play();
                bg_sound.setCycleCount(5);


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
                                 gameMap.plague();
                              //   gameMap.updateNumFlag();
                                 status.setText(gameMap.getHero().toString());
                        } else if (CURRENT_HOUR < 23) { //23
                                  gameMap.superNaturalHelp();
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


                         me(true);
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
            if (event.getCode() == KeyCode.M){ //OKAY
                popupHeroDimStage.showAndWait();
            } else if (event.getCode() == KeyCode.B){
                stage.setScene(askWeaponScene);
                stage.show();
            } else if (event.getCode() == KeyCode.TAB){
                gameMap.focusWeapons();
            } else if (event.getCode() == KeyCode.SHIFT){
                gameMap.focusAliens();
            } else if (event.getCode() == KeyCode.W){ /****testing the warnings****/
                gameMap.upgradeWeapon();
            } else if (event.getCode() == KeyCode.T){
                popupTeslaDimStage.showAndWait();
            } else if(event.getCode() == KeyCode.ESCAPE){
                gameMap.unFocusAll();
            } else if (event.getCode() == KeyCode.S){
                gameMap.upgradeSoldier();
            } else if (event.getCode() == KeyCode.BACK_SPACE){
                ISPAUSED = true;
                bg_sound.setMute(true);
                popupPauseStage.showAndWait();
            } else if (event.getCode() == KeyCode.SPACE){
                pressedSpace = true;
                Hero.runningTime = 20;
            }else{
                gameMap.moveHero(event);
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE){
                    pressedSpace = false;
                }
            }
        });
    //    gameInput = new Thread(r3);
      //  gameInput.start();
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
    static Scene popupWarningScene = new Scene(new Group(), 450, 275);
    static Scene popupStatusScene = new Scene(new Group(), 275, 300);
    
    static Stage stage;
    static Stage popupHeroDimStage = new Stage();
    static Stage popupEndGameStage = new Stage();
    static Stage popupLocationNUmStage = new Stage();
    static Stage popupTeslaDimStage = new Stage();
    static Stage popupPauseStage = new Stage();
    static Stage popupWarningStage = new Stage();
    static Stage popupStatusStage = new Stage();
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
        
        popupStatusScene.setRoot(createStatusSceneContent());
        popupStatusStage.setScene(popupStatusScene);
        popupStatusStage.initModality(Modality.APPLICATION_MODAL);
        popupStatusStage.setAlwaysOnTop(true);
        

        popupWarningStage.setScene(popupWarningScene);
        popupWarningStage.centerOnScreen();
        popupWarningStage.setAlwaysOnTop(true);
        popupWarningStage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
        stage.centerOnScreen();
    }

    Text status;
   private Parent createStatusSceneContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/pause/bg.png").toExternalForm()));

        MenuItem quit_ = new MenuItem(new Image(getClass()
                .getResource("res/menu/pause/Continue.png").toExternalForm()), 220, 55);
        quit_.setDim(30, 45);
        quit_.setOnAction(new Runnable() {
            @Override
            public void run() {

                popupStatusStage.close();

            }
        });

        ImageView textBg = new ImageView(new Image(getClass()
                .getResource("res/menu/item/board3.png").toExternalForm()));
        textBg.setFitWidth(250);
        textBg.setFitHeight(310);
        textBg.setX(0);
        textBg.setY(80);


        status=new Text(70,160, gameMap.getHero().toString());
        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 20);
        status.setFont(font);
        status.setFill(Color.rgb(50, 20, 15));

        root.getChildren().addAll(background, quit_,textBg,status);

        return root;
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
                bg_sound.stop();
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
                bg_sound.setMute(false);
                ISPAUSED = false;
                popupPauseStage.close();
            }
        });

        MenuItem show_status = new MenuItem(new Image(getClass()
                .getResource("res/menu/pause/Show Status.png").toExternalForm()), 220, 55);
        show_status.setDim(30, 205);
 //

        show_status.setOnAction(new Runnable() {
            @Override
            public void run() {
                popupStatusStage.showAndWait();
                //popupPauseStage.close();
            }
        });

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
        bg_sound.stop();
        ISPAUSED = true;
        System.out.println("setting endgame scene");

         gameEnded = true;

         gameMap.stopWalking();

        boolean finalGameOver = gameOver;
        Platform.runLater(() -> {
            popupEndGameScene.setRoot(game.createEndGameContent(finalGameOver));
            popupEndGameStage.show();
        });

        Timeline end = new Timeline(new KeyFrame(Duration.millis(4000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
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
                if (!START){
                    START = true;
                    launchGame();
                }
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
        custom_item.setPrefHeight(175);
        custom_item.setDim(290, 450);

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

        if (gameOver){
            Media sound=new Media(getClass().getResource("res/sound/gameover.mp3").toExternalForm());
            MediaPlayer player=new MediaPlayer(sound);
            player.play();
            msg1.setText("GAME OVER");
            msg2.setText("The aliens have taken over the planet.");
            msg3.setText("You have failed this city");
            msg1.setFill(Color.rgb(194, 30, 30));
            msg2.setFill(Color.rgb(194, 30, 30));
            msg3.setFill(Color.rgb(194, 30, 30));
            msg1.relocate(75, 50);
            msg2.relocate(40, 100);
            msg3.relocate(50, 180);

        } else{
            Media sound=new Media(getClass().getResource("res/sound/win.mp3").toExternalForm());
            MediaPlayer player=new MediaPlayer(sound);
            player.play();
            msg1.setText("CONGRATULATIONS! YOU WON!");
            msg2.setText("You have saved the planet!");
            msg3.setText("yaaaaaaaaaaay");
            msg1.setFill(Color.rgb(213, 163, 224));
            msg2.setFill(Color.rgb(213, 163, 224));
            msg3.setFill(Color.rgb(213, 163, 224));
            msg1.relocate(75 - 35 - 10 - 5, 50);
            msg2.relocate(40 + 35, 100);
            msg3.relocate(50 + 40 + 10, 130);

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
                    if (!Objects.equals(lblx.getText(), "") && !Objects.equals(lbly.getText(), "")){
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
                    if (!Objects.equals(lblx.getText(), "") && !Objects.equals(lbly.getText(), "")){
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

    static void showPopupWarning(String msg, double x, double y){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                popupWarningScene.setRoot(game.createPopupWarningContent(msg, x, y));
                popupWarningStage.show();

                Timeline end = new Timeline(new KeyFrame(Duration.millis(2000),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                popupWarningStage.close();

                            }
                        }));
                end.setCycleCount(1);
                end.play();
            }
        });

    }

    private Parent createPopupWarningContent(String msg, double x, double y){

        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/warning/bg.png").toExternalForm()));
        background.setFitWidth(450);
        background.setFitHeight(275);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 55);
        Text warning = new Text(msg);
        warning.setFont(font);
        warning.setFill(Color.rgb(190, 185, 40));
        warning.relocate(x, y);

        root.getChildren().addAll(background,  warning);

        return root;

    }

    static Scene customizeWeaponScene = new Scene(new Group(), 800, 550);
    static Scene customizeAliensScene = new Scene(new Group(), 700, 500);
    static Scene customizeHeroScene = new Scene(new Group(), 700, 500);
    static Scene customizeGameMapScene = new Scene(new Group(), 800, 500);
    static Scene newAliensScene = new Scene(new Group(), 800, 500);


    public void customizeGame(){
        customizeWeaponScene.setRoot(createCustomizeWeaponContent()); //OKAY

        customizeAliensScene.setRoot(createCustomizeAliensContent()); //OKAY
        newAliensScene.setRoot(createNewAliensContent());

        customizeHeroScene.setRoot(createCustomizeHeroContent()); //OKAY

        customizeGameMapScene.setRoot(createCustomizeGameMapContent());

        stage.setScene(customizeWeaponScene); //customizeWeapons
        stage.show();
    }

    private Parent createCustomizeWeaponContent() {
        Group root = new Group();
        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(800);
        background.setFitHeight(550);
        Image view = new Image(getClass()
                .getResource("res/menu/custom/option.png").toExternalForm());


        ArrayList<MenuItem> accepts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            accepts.add(new MenuItem(view, 90, 40, "Accept"));
            accepts.get(i).relocate(190 + i * 120, 380);
        }
        Tooltip machinegun_ta = new Tooltip("update Machine Gun\nor keep default.");
        Tooltip laser_ta = new Tooltip("update Laser\nor keep default.");
        Tooltip freezer_ta = new Tooltip("update Freezer\nor keep default.");
        Tooltip anti_ta = new Tooltip("update Antiaircraft\nor keep default.");
        Tooltip rocket_ta = new Tooltip("update Rocket\nor keep default.");
        Tooltip.install(accepts.get(0), machinegun_ta);
        Tooltip.install(accepts.get(1), laser_ta);
        Tooltip.install(accepts.get(2), freezer_ta);
        Tooltip.install(accepts.get(3), anti_ta);
        Tooltip.install(accepts.get(4), rocket_ta);


        ArrayList<MenuItem> deletes = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            deletes.add(new MenuItem(view, 90, 40, "Delete"));
            deletes.get(i).relocate(190 + i * 120, 430);
        }

        Tooltip machinegun_t = new Tooltip("select to delete Machine Gun");
        Tooltip laser_t = new Tooltip("select to delete Laser");
        Tooltip freezer_t = new Tooltip("select to delete Freezer");
        Tooltip anti_t = new Tooltip("select to delete Antiaircraft");
        Tooltip rocket_t = new Tooltip("select to delete Rocket");
        Tooltip.install(deletes.get(0), machinegun_t);
        Tooltip.install(deletes.get(1), laser_t);
        Tooltip.install(deletes.get(2), freezer_t);
        Tooltip.install(deletes.get(3), anti_t);
        Tooltip.install(deletes.get(4), rocket_t);


        ArrayList<ImageView> names = new ArrayList<>();
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/names/Machine Gun.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/names/Laser.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/names/Freezer.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/names/Antiaircraft.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/names/Rocket.png").toExternalForm())));

        for (int i = 0; i < 5; i++){
            names.get(i).relocate(190 + i * 120, 490);
            names.get(i).setFitHeight(40);
            names.get(i).setFitWidth(90);
        }

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 32);

        ArrayList<ImageView> fields = new ArrayList<>();
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/Price.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/Radius.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/Bullet Speed.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/Bullet Power.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/weapons/Speed Reduction.png").toExternalForm())));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(fields);
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(15);
        vBox.setTranslateX(25);
        vBox.setTranslateY(0);
        vBox.setLayoutY(5);

        ArrayList<TextField> machinegun = new ArrayList<>();
        ArrayList<Label> machinegun_l = new ArrayList<>();
        ArrayList<ImageView> machinegun_i = new ArrayList<>();

        ArrayList<TextField> laser = new ArrayList<>();
        ArrayList<Label> laser_l = new ArrayList<>();
        ArrayList<ImageView> laser_i = new ArrayList<>();

        ArrayList<TextField> freezer = new ArrayList<>();
        ArrayList<Label> freezer_l = new ArrayList<>();
        ArrayList<ImageView> freezer_i = new ArrayList<>();

        ArrayList<TextField> anti = new ArrayList<>();
        ArrayList<Label> anti_l = new ArrayList<>();
        ArrayList<ImageView> anti_i = new ArrayList<>();


        ArrayList<TextField> rocket = new ArrayList<>();
        ArrayList<Label> rocket_l = new ArrayList<>();
        ArrayList<ImageView> rocket_i = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            machinegun.add(new TextField());
            machinegun_l.add(new Label(""));
            machinegun_l.get(i).setFont(font);
            machinegun.get(i).setOpacity(0.0);
            machinegun.get(i).setOnKeyPressed(new TextEventHandler(machinegun_l.get(i)));
            machinegun_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            machinegun_i.get(i).setFitHeight(50);
            machinegun_i.get(i).setFitWidth(60);

            laser.add(new TextField());
            laser_l.add(new Label(""));
            laser_l.get(i).setFont(font);
            laser.get(i).setOpacity(0.0);
            laser.get(i).setOnKeyPressed(new TextEventHandler(laser_l.get(i)));
            laser_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            laser_i.get(i).setFitHeight(50);
            laser_i.get(i).setFitWidth(60);

            freezer.add(new TextField());
            freezer_l.add(new Label(""));
            freezer_l.get(i).setFont(font);
            freezer.get(i).setOpacity(0.0);
            freezer.get(i).setOnKeyPressed(new TextEventHandler(freezer_l.get(i)));
            freezer_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            freezer_i.get(i).setFitHeight(50);
            freezer_i.get(i).setFitWidth(60);

            anti.add(new TextField());
            anti_l.add(new Label(""));
            anti_l.get(i).setFont(font);
            anti.get(i).setOpacity(0.0);
            anti.get(i).setOnKeyPressed(new TextEventHandler(anti_l.get(i)));
            anti_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            anti_i.get(i).setFitHeight(50);
            anti_i.get(i).setFitWidth(60);

            rocket.add(new TextField());
            rocket_l.add(new Label(""));
            rocket_l.get(i).setFont(font);
            rocket.get(i).setOpacity(0.0);
            rocket.get(i).setOnKeyPressed(new TextEventHandler(rocket_l.get(i)));
            rocket_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            rocket_i.get(i).setFitHeight(50);
            rocket_i.get(i).setFitWidth(60);
        }

        VBox vBox1 = new VBox();
        vBox1.setSpacing(40);
        vBox1.setPadding(new Insets(15));
        vBox1.setTranslateX(35 + 135);
        vBox1.getChildren().addAll(machinegun);

        VBox vBox1_l = new VBox();
        vBox1_l.setSpacing(40);
        vBox1_l.setPadding(new Insets(15));
        vBox1_l.setTranslateX(50 + 120  + 30);
        vBox1_l.getChildren().addAll(machinegun_l);
        //vBox1_l.setAlignment(Pos.CENTER);

        VBox vBox1_i = new VBox();
        vBox1_i.setSpacing(25);
        vBox1_i.setPadding(new Insets(15));
        vBox1_i.setTranslateX(70 + 120);
        vBox1_i.getChildren().addAll(machinegun_i);
        //vBox1_i.setAlignment(Pos.CENTER);


        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(15));
        vBox2.setSpacing(40);
        vBox2.setTranslateX(35 + 2 * 135);
        vBox2.getChildren().addAll(rocket);

        VBox vBox2_l = new VBox();
        vBox2_l.setPadding(new Insets(15));
        vBox2_l.setSpacing(40);
        vBox2_l.setTranslateX(50 + 2 * 120  + 30);
        vBox2_l.getChildren().addAll(rocket_l);

        VBox vBox2_i = new VBox();
        vBox2_i.setPadding(new Insets(15));
        vBox2_i.setSpacing(25);
        vBox2_i.setTranslateX(70 + 2 * 120);
        vBox2_i.getChildren().addAll(rocket_i);


        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(15));
        vBox3.setSpacing(40);
        vBox3.setTranslateX(35 + 3 * 135);
        vBox3.getChildren().addAll(freezer);

        VBox vBox3_l = new VBox();
        vBox3_l.setPadding(new Insets(15));
        vBox3_l.setSpacing(40);
        vBox3_l.setTranslateX(50 + 3 * 120  + 30);
        vBox3_l.getChildren().addAll(freezer_l);

        VBox vBox3_i = new VBox();
        vBox3_i.setPadding(new Insets(15));
        vBox3_i.setSpacing(25);
        vBox3_i.setTranslateX(70 + 3 * 120);
        vBox3_i.getChildren().addAll(freezer_i);


        VBox vBox4 = new VBox();
        vBox4.setSpacing(40);
        vBox4.setPadding(new Insets(15));
        vBox4.setTranslateX(35 + 4 * 135);
        vBox4.getChildren().addAll(laser);

        VBox vBox4_l = new VBox();
        vBox4_l.setSpacing(40);
        vBox4_l.setPadding(new Insets(15));
        vBox4_l.setTranslateX(50 + 4 * 120  + 30);
        vBox4_l.getChildren().addAll(laser_l);

        VBox vBox4_i = new VBox();
        vBox4_i.setSpacing(25);
        vBox4_i.setPadding(new Insets(15));
        vBox4_i.setTranslateX(70 + 4 * 120);
        vBox4_i.getChildren().addAll(laser_i);



        VBox vBox5 = new VBox();
        vBox5.setSpacing(40);
        vBox5.setPadding(new Insets(15));
        vBox5.setTranslateX(35 + 5 * 135);
        vBox5.getChildren().addAll(anti);

        VBox vBox5_l = new VBox();
        vBox5_l.setSpacing(40);
        vBox5_l.setPadding(new Insets(15));
        vBox5_l.setTranslateX(50 + 5 * 120 + 30);
        vBox5_l.getChildren().addAll(anti_l);

        VBox vBox5_i = new VBox();
        vBox5_i.setSpacing(25);
        vBox5_i.setPadding(new Insets(15));
        vBox5_i.setTranslateX(70 + 5 * 120);
        vBox5_i.getChildren().addAll(anti_i);


        root.getChildren().add(background);
        root.getChildren().addAll(accepts);
        root.getChildren().addAll(deletes);
        root.getChildren().addAll(names);
        root.getChildren().addAll(vBox, vBox1_i, vBox2_i, vBox3_i, vBox4_i, vBox5_i);
        root.getChildren().addAll(vBox1, vBox2, vBox3, vBox4, vBox5);
        root.getChildren().addAll(vBox1_l, vBox2_l, vBox3_l, vBox4_l, vBox5_l);

        machinegun_l.get(0).setText(String.valueOf(InitialWeapon.getPrice("machine gun")));
        machinegun_l.get(1).setText(String.valueOf(InitialWeapon.getRadius("machine gun")));
        machinegun_l.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("machine gun")));
        machinegun_l.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("machine gun")));
        machinegun_l.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("machine gun")));

        accepts.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("machine gun", Integer.parseInt(machinegun_l.get(0).getText()));
                InitialWeapon.setRadius("machine gun", Double.parseDouble(machinegun_l.get(1).getText()));
                InitialWeapon.setBulletSpeed("machine gun", Integer.parseInt(machinegun_l.get(2).getText()));
                InitialWeapon.setBulletPower("machine gun", Integer.parseInt(machinegun_l.get(3).getText()));
                InitialWeapon.setSpeedReduction("machine gun", Integer.parseInt(machinegun_l.get(4).getText()));
            }
        });


        rocket_l.get(0).setText(String.valueOf(InitialWeapon.getPrice("rocket")));
        rocket_l.get(1).setText(String.valueOf(InitialWeapon.getRadius("rocket")));
        rocket_l.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("rocket")));
        rocket_l.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("rocket")));
        rocket_l.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("rocket")));

        accepts.get(1).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("rocket", Integer.parseInt(rocket_l.get(0).getText()));
                InitialWeapon.setRadius("rocket", Double.parseDouble(rocket_l.get(1).getText()));
                InitialWeapon.setBulletSpeed("rocket", Integer.parseInt(rocket_l.get(2).getText()));
                InitialWeapon.setBulletPower("rocket", Integer.parseInt(rocket_l.get(3).getText()));
                InitialWeapon.setSpeedReduction("rocket", Integer.parseInt(rocket_l.get(4).getText()));
            }
        });

        freezer_l.get(0).setText(String.valueOf(InitialWeapon.getPrice("freezer")));
        freezer_l.get(1).setText(String.valueOf(InitialWeapon.getRadius("freezer")));
        freezer_l.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("freezer")));
        freezer_l.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("freezer")));
        freezer_l.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("freezer")));

        accepts.get(2).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("freezer", Integer.parseInt(freezer_l.get(0).getText()));
                InitialWeapon.setRadius("freezer", Double.parseDouble(freezer_l.get(1).getText()));
                InitialWeapon.setBulletSpeed("freezer", Integer.parseInt(freezer_l.get(2).getText()));
                InitialWeapon.setBulletPower("freezer", Integer.parseInt(freezer_l.get(3).getText()));
                InitialWeapon.setSpeedReduction("freezer", Integer.parseInt(freezer_l.get(4).getText()));
            }
        });

        laser_l.get(0).setText(String.valueOf(InitialWeapon.getPrice("laser")));
        laser_l.get(1).setText(String.valueOf(InitialWeapon.getRadius("laser")));
        laser_l.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("laser")));
        laser_l.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("laser")));
        laser_l.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("laser")));

        accepts.get(3).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("laser", Integer.parseInt(laser_l.get(0).getText()));
                InitialWeapon.setRadius("laser", Double.parseDouble(laser_l.get(1).getText()));
                InitialWeapon.setBulletSpeed("laser", Integer.parseInt(laser_l.get(2).getText()));
                InitialWeapon.setBulletPower("laser", Integer.parseInt(laser_l.get(3).getText()));
                InitialWeapon.setSpeedReduction("laser", Integer.parseInt(laser_l.get(4).getText()));
            }
        });

        anti_l.get(0).setText(String.valueOf(InitialWeapon.getPrice("antiaircraft")));
        anti_l.get(1).setText(String.valueOf(InitialWeapon.getRadius("antiaircraft")));
        anti_l.get(2).setText(String.valueOf(InitialWeapon.getBulletSpeed("antiaircraft")));
        anti_l.get(3).setText(String.valueOf(InitialWeapon.getBulletPower("antiaircraft")));
        anti_l.get(4).setText(String.valueOf(InitialWeapon.getSpeedReduction("antiaircraft")));

        accepts.get(4).setOnAction(new Runnable() {
            @Override
            public void run() {
                InitialWeapon.setPrice("antiaircraft", Integer.parseInt(anti_l.get(0).getText()));
                InitialWeapon.setRadius("antiaircraft", Double.parseDouble(anti_l.get(1).getText()));
                InitialWeapon.setBulletSpeed("antiaircraft", Integer.parseInt(anti_l.get(2).getText()));
                InitialWeapon.setBulletPower("antiaircraft", Integer.parseInt(anti_l.get(3).getText()));
                InitialWeapon.setSpeedReduction("antiaircraft", Integer.parseInt(anti_l.get(4).getText()));

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

    private Parent createCustomizeAliensContent() {
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(customizeAliensScene.getWidth());
        background.setFitHeight(customizeAliensScene.getHeight());
        Image view = new Image(getClass()
                .getResource("res/menu/custom/option.png").toExternalForm());

        ArrayList<MenuItem> accepts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            accepts.add(new MenuItem(view, 90, 40, "Accept"));
            accepts.get(i).relocate(190 + i * 120, 310);
        }

        Tooltip albertonion_ta = new Tooltip("update Albertonion\nor keep default.");
        Tooltip aironion_ta = new Tooltip("update Aironion\nor keep default.");
        Tooltip algwasonion_ta = new Tooltip("update Algwasonion\nor keep default.");
        Tooltip activinion_ta = new Tooltip("update Activinion\nor keep default.");
        Tooltip.install(accepts.get(0), albertonion_ta);
        Tooltip.install(accepts.get(1), aironion_ta);
        Tooltip.install(accepts.get(2), algwasonion_ta);
        Tooltip.install(accepts.get(3), activinion_ta);


        ArrayList<MenuItem> deletes = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            deletes.add(new MenuItem(view, 90, 40, "Delete"));
            deletes.get(i).relocate(190 + i * 120, 360);
        }

        Tooltip albertonion_t = new Tooltip("select to delete Albertonion");
        Tooltip aironion_t = new Tooltip("select to delete Aironion");
        Tooltip algwasonion_t = new Tooltip("select to delete Algwasonion");
        Tooltip activionion_t = new Tooltip("select to delete Activinion");
        Tooltip.install(deletes.get(0), albertonion_t);
        Tooltip.install(deletes.get(1), aironion_t);
        Tooltip.install(deletes.get(2), algwasonion_t);
        Tooltip.install(deletes.get(3), activionion_t);

        ArrayList<ImageView> names = new ArrayList<>();
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/names/Albertonion.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/names/Aironion.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/names/Algwasonion.png").toExternalForm())));
        names.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/names/Activinion.png").toExternalForm())));

        for (int i = 0; i < 4; i++){
            names.get(i).relocate(190 + i * 120, 420);
            names.get(i).setFitHeight(40);
            names.get(i).setFitWidth(90);
        }
        ArrayList<ImageView> fields = new ArrayList<>();
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Energy.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Speed.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Bullet Speed.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Bullet Power.png").toExternalForm())));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(fields);
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(15);
        vBox.setTranslateX(25);
        vBox.setTranslateY(0);
        vBox.setLayoutY(5);

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 32);


        ArrayList<TextField> albertonion = new ArrayList<>();
        ArrayList<Label> albertonion_l = new ArrayList<>();
        ArrayList<ImageView> albertonion_i = new ArrayList<>();

        ArrayList<TextField> aironion = new ArrayList<>();
        ArrayList<Label> aironion_l = new ArrayList<>();
        ArrayList<ImageView> aironion_i = new ArrayList<>();

        ArrayList<TextField> algwasonion = new ArrayList<>();
        ArrayList<Label> algwasonion_l = new ArrayList<>();
        ArrayList<ImageView> algwasonion_i = new ArrayList<>();

        ArrayList<TextField> activionion = new ArrayList<>();
        ArrayList<Label> activionion_l = new ArrayList<>();
        ArrayList<ImageView> activionion_i = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            albertonion.add(new TextField());
            albertonion_l.add(new Label(""));
            albertonion_l.get(i).setFont(font);
            albertonion.get(i).setOpacity(0.0);
            albertonion.get(i).setOnKeyPressed(new TextEventHandler(albertonion_l.get(i)));
            albertonion_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            albertonion_i.get(i).setFitHeight(50);
            albertonion_i.get(i).setFitWidth(60);

            aironion.add(new TextField());
            aironion_l.add(new Label(""));
            aironion_l.get(i).setFont(font);
            aironion.get(i).setOpacity(0.0);
            aironion.get(i).setOnKeyPressed(new TextEventHandler(aironion_l.get(i)));
            aironion_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            aironion_i.get(i).setFitHeight(50);
            aironion_i.get(i).setFitWidth(60);

            algwasonion.add(new TextField());
            algwasonion_l.add(new Label(""));
            algwasonion_l.get(i).setFont(font);
            algwasonion.get(i).setOpacity(0.0);
            algwasonion.get(i).setOnKeyPressed(new TextEventHandler(algwasonion_l.get(i)));
            algwasonion_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            algwasonion_i.get(i).setFitHeight(50);
            algwasonion_i.get(i).setFitWidth(60);

            activionion.add(new TextField());
            activionion_l.add(new Label(""));
            activionion_l.get(i).setFont(font);
            activionion.get(i).setOpacity(0.0);
            activionion.get(i).setOnKeyPressed(new TextEventHandler(activionion_l.get(i)));
            activionion_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            activionion_i.get(i).setFitHeight(50);
            activionion_i.get(i).setFitWidth(60);
        }

        VBox vBox1 = new VBox();
        vBox1.setSpacing(40);
        vBox1.setPadding(new Insets(15));
        vBox1.setTranslateX(35 + 135);
        vBox1.getChildren().addAll(albertonion);

        VBox vBox1_l = new VBox();
        vBox1_l.setSpacing(40);
        vBox1_l.setPadding(new Insets(15));
        vBox1_l.setTranslateX(50 + 120  + 30);
        vBox1_l.getChildren().addAll(albertonion_l);

        VBox vBox1_i = new VBox();
        vBox1_i.setSpacing(25);
        vBox1_i.setPadding(new Insets(15));
        vBox1_i.setTranslateX(70 + 120);
        vBox1_i.getChildren().addAll(albertonion_i);



        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(15));
        vBox3.setSpacing(40);
        vBox3.setTranslateX(35 + 2 * 135);
        vBox3.getChildren().addAll(algwasonion);

        VBox vBox3_l = new VBox();
        vBox3_l.setPadding(new Insets(15));
        vBox3_l.setSpacing(40);
        vBox3_l.setTranslateX(50 + 2 * 120  + 30);
        vBox3_l.getChildren().addAll(algwasonion_l);

        VBox vBox3_i = new VBox();
        vBox3_i.setPadding(new Insets(15));
        vBox3_i.setSpacing(25);
        vBox3_i.setTranslateX(70 + 2 * 120);
        vBox3_i.getChildren().addAll(algwasonion_i);


        VBox vBox4 = new VBox();
        vBox4.setSpacing(40);
        vBox4.setPadding(new Insets(15));
        vBox4.setTranslateX(35 + 3 * 135);
        vBox4.getChildren().addAll(aironion);

        VBox vBox4_l = new VBox();
        vBox4_l.setSpacing(40);
        vBox4_l.setPadding(new Insets(15));
        vBox4_l.setTranslateX(50 + 3 * 120  + 30);
        vBox4_l.getChildren().addAll(aironion_l);

        VBox vBox4_i = new VBox();
        vBox4_i.setSpacing(25);
        vBox4_i.setPadding(new Insets(15));
        vBox4_i.setTranslateX(70 + 3 * 120);
        vBox4_i.getChildren().addAll(aironion_i);



        VBox vBox5 = new VBox();
        vBox5.setSpacing(40);
        vBox5.setPadding(new Insets(15));
        vBox5.setTranslateX(35 + 4 * 135);
        vBox5.getChildren().addAll(activionion);

        VBox vBox5_l = new VBox();
        vBox5_l.setSpacing(40);
        vBox5_l.setPadding(new Insets(15));
        vBox5_l.setTranslateX(50 + 4 * 120 + 30);
        vBox5_l.getChildren().addAll(activionion_l);

        VBox vBox5_i = new VBox();
        vBox5_i.setSpacing(25);
        vBox5_i.setPadding(new Insets(15));
        vBox5_i.setTranslateX(70 + 4 * 120);
        vBox5_i.getChildren().addAll(activionion_i);

        root.getChildren().add(background);
        root.getChildren().addAll(accepts);
        root.getChildren().addAll(deletes);
        root.getChildren().addAll(names);
        root.getChildren().addAll(vBox, vBox1_i, vBox4_i, vBox3_i, vBox5_i);
        root.getChildren().addAll(vBox1, vBox4, vBox3, vBox5);
        root.getChildren().addAll(vBox1_l, vBox4_l,vBox3_l, vBox5_l);


        albertonion_l.get(0).setText(String.valueOf(Alien.getInitialEnergy("albertonion")));
        albertonion_l.get(1).setText(String.valueOf(Alien.getInitialSpeed("albertonion")));
        albertonion_l.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("albertonion")));
        albertonion_l.get(3).setText(String.valueOf(Alien.getInitialStrength("albertonion")));

        aironion_l.get(0).setText(String.valueOf(Alien.getInitialEnergy("aironion")));
        aironion_l.get(1).setText(String.valueOf(Alien.getInitialSpeed("aironion")));
        aironion_l.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("aironion")));
        aironion_l.get(3).setText(String.valueOf(Alien.getInitialStrength("aironion")));

        algwasonion_l.get(0).setText(String.valueOf(Alien.getInitialEnergy("algwasonion")));
        algwasonion_l.get(1).setText(String.valueOf(Alien.getInitialSpeed("algwasonion")));
        algwasonion_l.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("algwasonion")));
        algwasonion_l.get(3).setText(String.valueOf(Alien.getInitialStrength("algwasonion")));

        activionion_l.get(0).setText(String.valueOf(Alien.getInitialEnergy("activionion")));
        activionion_l.get(1).setText(String.valueOf(Alien.getInitialSpeed("activionion")));
        activionion_l.get(2).setText(String.valueOf(Alien.getInitialShootingSpeed("activionion")));
        activionion_l.get(3).setText(String.valueOf(Alien.getInitialStrength("activionion")));

        accepts.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("albertonion", Integer.parseInt(albertonion_l.get(0).getText()));
                Alien.changeInitialSpeed("albertonion", Integer.parseInt(albertonion_l.get(1).getText()));
                Alien.changeInitialShootingSpeed("albertonion", Integer.parseInt(albertonion_l.get(2).getText()));
                Alien.changeInitialStrength("albertonion", Integer.parseInt(albertonion_l.get(3).getText()));
            }
        });

        accepts.get(1).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("aironion", Integer.parseInt(aironion_l.get(0).getText()));
                Alien.changeInitialSpeed("aironion", Integer.parseInt(aironion_l.get(1).getText()));
                Alien.changeInitialShootingSpeed("aironion", Integer.parseInt(aironion_l.get(2).getText()));
                Alien.changeInitialStrength("aironion", Integer.parseInt(aironion_l.get(3).getText()));
            }
        });

        accepts.get(2).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("algwasonion", Integer.parseInt(algwasonion_l.get(0).getText()));
                Alien.changeInitialSpeed("algwasonion", Integer.parseInt(algwasonion_l.get(1).getText()));
                Alien.changeInitialShootingSpeed("algwasonion", Integer.parseInt(algwasonion_l.get(2).getText()));
                Alien.changeInitialStrength("algwasonion", Integer.parseInt(algwasonion_l.get(3).getText()));
            }
        });

        accepts.get(3).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeInitialEnergy("activionion", Integer.parseInt(activionion_l.get(0).getText()));
                Alien.changeInitialSpeed("activionion", Integer.parseInt(activionion_l.get(1).getText()));
                Alien.changeInitialShootingSpeed("activionion", Integer.parseInt(activionion_l.get(2).getText()));
                Alien.changeInitialStrength("activionion", Integer.parseInt(activionion_l.get(3).getText()));

                stage.setScene(customizeHeroScene);
            }
        });

        deletes.get(0).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("albertonion");
            }
        });

        deletes.get(1).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("aironion");
            }
        });

        deletes.get(2).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("algwasonion");
            }
        });

        deletes.get(3).setOnAction(new Runnable() {
            @Override
            public void run() {
                Alien.changeDeactivate("activionion");
                stage.setScene(customizeHeroScene);
            }
        });

        return root;
    }

    private Parent createNewAliensContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(800);
        Image view = new Image(getClass()
                .getResource("res/menu/custom/option.png").toExternalForm());

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);


        ArrayList<ImageView> fields = new ArrayList<>();
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Energy.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Speed.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Bullet Speed.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Bullet Power.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/CanFly.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/aliens/Name.png").toExternalForm())));
        for (int i = 0; i < fields.size(); i++){
            fields.get(i).setFitWidth(120);
        }


        HBox hBox = new HBox();
        hBox.setSpacing(1);
        hBox.getChildren().addAll(fields);
        hBox.relocate(40, 50);

        ArrayList<TextField> newAlien = new ArrayList<>();
        ArrayList<Label> newAlien_l = new ArrayList<>();
        ArrayList<ImageView> newAlien_i = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            newAlien_i.add(new ImageView(new Image(getClass()
                    .getResource("res/menu/custom/input.png").toExternalForm())));
            newAlien_i.get(i).setFitWidth(110);
            newAlien_i.get(i).setFitHeight(50);

            newAlien_l.add(new Label(""));
            newAlien_l.get(i).setFont(font);

            newAlien.add(new TextField());
            newAlien.get(i).setOpacity(0);
            newAlien.get(i).setOnKeyPressed(new TextEventHandler(newAlien_l.get(i)));
        }

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(newAlien);
        hBox1.setSpacing(1);
        hBox1.relocate(50, 110);

        HBox hBox1_i = new HBox();
        hBox1_i.getChildren().addAll(newAlien_i);
        hBox1_i.setSpacing(10);
        hBox1_i.relocate(50, 120);

        HBox hBox1_l = new HBox();
        hBox1_l.getChildren().addAll(newAlien_l);
        hBox1_l.setSpacing(100);
        hBox1_l.setAlignment(Pos.CENTER);
        hBox1_l.relocate(75, 125);

        MenuItem choose_view4 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view8 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view15 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view22 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view12 = new MenuItem(view, 90, 40, "Choose");
        HBox choose_btns = new HBox();
        choose_btns.setSpacing(5);
        choose_btns.getChildren().addAll(choose_view4, choose_view8, choose_view15, choose_view22, choose_view12);
        choose_btns.relocate(70, 400);

        final String[] filename = {""};

        choose_view4.setOnAction(new Runnable() {
            @Override
            public void run() {
                /*InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                        Integer.parseInt(newAlien.get(1).getText()),
                        Integer.parseInt(newAlien.get(2).getText()),
                        Integer.parseInt(newAlien.get(3).getText()),
                        Boolean.parseBoolean(newAlien.get(4).getText()),
                        String.valueOf(4));
                Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);*/
                filename[0] = String.valueOf(4);

                //stage.setScene(customizeGameMapScene);
            }
        });

        choose_view8.setOnAction(new Runnable() {
            @Override
            public void run() {
                /*InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                        Integer.parseInt(newAlien.get(1).getText()),
                        Integer.parseInt(newAlien.get(2).getText()),
                        Integer.parseInt(newAlien.get(3).getText()),
                        Boolean.parseBoolean(newAlien.get(4).getText()),
                        String.valueOf(8));
                Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);*/

                filename[0] = String.valueOf(8);

                /*stage.setScene(customizeGameMapScene);*/
            }
        });

        choose_view15.setOnAction(new Runnable() {
            @Override
            public void run() {
                /*InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                        Integer.parseInt(newAlien.get(1).getText()),
                        Integer.parseInt(newAlien.get(2).getText()),
                        Integer.parseInt(newAlien.get(3).getText()),
                        Boolean.parseBoolean(newAlien.get(4).getText()),
                        String.valueOf(15));
                Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);

                stage.setScene(customizeGameMapScene);*/
                filename[0] = String.valueOf(15);
            }
        });

        choose_view22.setOnAction(new Runnable() {
            @Override
            public void run() {
                /*InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                        Integer.parseInt(newAlien.get(1).getText()),
                        Integer.parseInt(newAlien.get(2).getText()),
                        Integer.parseInt(newAlien.get(3).getText()),
                        Boolean.parseBoolean(newAlien.get(4).getText()),
                        String.valueOf(22));
                Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);
                stage.setScene(customizeGameMapScene);*/

                filename[0] = String.valueOf(22);
            }
        });

        choose_view12.setOnAction(new Runnable() {
            @Override
            public void run() {
                /*InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                        Integer.parseInt(newAlien.get(1).getText()),
                        Integer.parseInt(newAlien.get(2).getText()),
                        Integer.parseInt(newAlien.get(3).getText()),
                        Boolean.parseBoolean(newAlien.get(4).getText()),
                        String.valueOf(22));
                Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);
                stage.setScene(customizeGameMapScene);*/

                filename[0] = String.valueOf(12);
            }
        });

        MenuItem create = new MenuItem(view, 100, 100, "Create!");
        create.relocate(625, 200);
        create.setOnAction(new Runnable() {
            @Override
            public void run() {
                if (!filename[0].equals("")){
                    if (newAlien.get(4).getText().equalsIgnoreCase("F")){
                        InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                                Integer.parseInt(newAlien.get(1).getText()),
                                Integer.parseInt(newAlien.get(2).getText()),
                                Integer.parseInt(newAlien.get(3).getText()),
                                false,
                                filename[0]);
                        Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);
                    } else {
                        InitialAlien mine = new InitialAlien(Integer.parseInt(newAlien.get(0).getText()),
                                Integer.parseInt(newAlien.get(1).getText()),
                                Integer.parseInt(newAlien.get(2).getText()),
                                Integer.parseInt(newAlien.get(3).getText()),
                                true,
                                filename[0]);
                        Alien.addNewDefinedAlien(newAlien.get(5).getText(), mine);
                    }
                    for (int i = 0; i < newAlien_l.size(); i++){
                        newAlien_l.get(i).setText("");
                        //System.out.println("alskdjfcn");
                    }
                    newAlien_l.get(0).requestFocus();
                    newAlien.get(0).requestFocus();
                }
            }
        });
        MenuItem Done = new MenuItem(view, 100, 100, "Done!");
        Done.relocate(625, 320);
        Done.setOnAction(new Runnable() {
            @Override
            public void run() {
                stage.setScene(customizeGameMapScene);
            }
        });

            /***** Animation  ****/

        String address = "res/aliens/movement/";

        ImageView[] view4 = new ImageView[12];
        view4[0] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/down1.png").toExternalForm()));
        view4[1] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/down1.png").toExternalForm()));
        view4[2] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/down3.png").toExternalForm()));
        view4[3] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/left1.png").toExternalForm()));
        view4[4] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/left2.png").toExternalForm()));
        view4[5] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/left3.png").toExternalForm()));
        view4[6] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/up1.png").toExternalForm()));
        view4[7] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/up2.png").toExternalForm()));
        view4[8] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/up3.png").toExternalForm()));
        view4[9] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/right1.png").toExternalForm()));
        view4[10] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/right2.png").toExternalForm()));
        view4[11] = new ImageView(new Image(getClass()
                .getResource(address + 4 + "/right3.png").toExternalForm()));

        ImageView[] view8 = new ImageView[12];
        view8[0] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/down1.png").toExternalForm()));
        view8[1] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/down1.png").toExternalForm()));
        view8[2] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/down3.png").toExternalForm()));
        view8[3] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/left1.png").toExternalForm()));
        view8[4] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/left2.png").toExternalForm()));
        view8[5] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/left3.png").toExternalForm()));
        view8[6] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/up1.png").toExternalForm()));
        view8[7] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/up2.png").toExternalForm()));
        view8[8] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/up3.png").toExternalForm()));
        view8[9] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/right1.png").toExternalForm()));
        view8[10] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/right2.png").toExternalForm()));
        view8[11] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/right3.png").toExternalForm()));

        ImageView[] view15 = new ImageView[12];
        view15[0] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/down1.png").toExternalForm()));
        view15[1] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/down1.png").toExternalForm()));
        view15[2] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/down3.png").toExternalForm()));
        view15[3] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/left1.png").toExternalForm()));
        view15[4] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/left2.png").toExternalForm()));
        view15[5] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/left3.png").toExternalForm()));
        view15[6] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/up1.png").toExternalForm()));
        view15[7] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/up2.png").toExternalForm()));
        view15[8] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/up3.png").toExternalForm()));
        view15[9] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/right1.png").toExternalForm()));
        view15[10] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/right2.png").toExternalForm()));
        view15[11] = new ImageView(new Image(getClass()
                .getResource(address + 15 + "/right3.png").toExternalForm()));

        ImageView[] view22 = new ImageView[12];
        view22[0] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/down1.png").toExternalForm()));
        view22[1] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/down1.png").toExternalForm()));
        view22[2] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/down3.png").toExternalForm()));
        view22[3] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/left1.png").toExternalForm()));
        view22[4] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/left2.png").toExternalForm()));
        view22[5] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/left3.png").toExternalForm()));
        view22[6] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/up1.png").toExternalForm()));
        view22[7] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/up2.png").toExternalForm()));
        view22[8] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/up3.png").toExternalForm()));
        view22[9] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/right1.png").toExternalForm()));
        view22[10] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/right2.png").toExternalForm()));
        view22[11] = new ImageView(new Image(getClass()
                .getResource(address + 22 + "/right3.png").toExternalForm()));

        ImageView[] view12 = new ImageView[12];
        view12[0] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/down1.png").toExternalForm()));
        view12[1] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/down1.png").toExternalForm()));
        view12[2] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/down3.png").toExternalForm()));
        view12[3] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/left1.png").toExternalForm()));
        view12[4] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/left2.png").toExternalForm()));
        view12[5] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/left3.png").toExternalForm()));
        view12[6] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/up1.png").toExternalForm()));
        view12[7] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/up2.png").toExternalForm()));
        view12[8] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/up3.png").toExternalForm()));
        view12[9] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/right1.png").toExternalForm()));
        view12[10] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/right2.png").toExternalForm()));
        view12[11] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/right3.png").toExternalForm()));

        /*Text sprite = new Text("Alien's View");
        sprite.setFont(font);
        sprite.relocate(150, 250);*/

        for (int i = 0; i < 12; i++){
            view4[i].setFitWidth(50);
            view4[i].setFitHeight(50);
            view4[i].setVisible(false);
            view4[i].relocate(100 - 25, 300);

            view15[i].setFitWidth(50);
            view15[i].setFitHeight(50);
            view15[i].setVisible(false);
            view15[i].relocate(200 - 25, 300);

            view8[i].setFitWidth(50);
            view8[i].setFitHeight(50);
            view8[i].setVisible(false);
            view8[i].relocate(300 - 25, 300);

            view22[i].setFitWidth(50);
            view22[i].setFitHeight(50);
            view22[i].setVisible(false);
            view22[i].relocate(400 - 25, 300);

            view12[i].setFitWidth(50);
            view12[i].setFitHeight(50);
            view12[i].setVisible(false);
            view12[i].relocate(500 - 25, 300);
        }
        view4[0].setVisible(true);
        view15[0].setVisible(true);
        view8[0].setVisible(true);
        view22[0].setVisible(true);
        view12[0].setVisible(true);

        Timeline view_heros = new Timeline(new KeyFrame(Duration.millis(200),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        boolean flag = false;
                        for (int i = 0; i < 12; i++){
                            if (view15[i].isVisible()){
                                flag = true;
                                view4[i].setVisible(false);
                                view15[i].setVisible(false);
                                view8[i].setVisible(false);
                                view22[i].setVisible(false);
                                view12[i].setVisible(false);
                                if (i + 1 < 12){
                                    view4[i + 1].setVisible(true);
                                    view15[i + 1].setVisible(true);
                                    view8[i + 1].setVisible(true);
                                    view22[i + 1].setVisible(true);
                                    view12[i + 1].setVisible(true);
                                } else {
                                    view4[0].setVisible(true);
                                    view15[0].setVisible(true);
                                    view8[0].setVisible(true);
                                    view22[0].setVisible(true);
                                    view12[0].setVisible(true);
                                }
                                break;
                            }
                        }
                        if (!flag){
                            view4[0].setVisible(true);
                            view15[0].setVisible(true);
                            view8[0].setVisible(true);
                            view22[0].setVisible(true);
                            view12[0].setVisible(true);
                        }
                    }
                }));
        view_heros.setCycleCount(Animation.INDEFINITE);
        view_heros.play();




        root.getChildren().addAll(background, create, Done, hBox, hBox1_i, hBox1, hBox1_l, choose_btns);
        root.getChildren().addAll(view4);
        root.getChildren().addAll(view8);
        root.getChildren().addAll(view15);
        root.getChildren().addAll(view22);
        root.getChildren().addAll(view12);

        return root;
    }

    private Parent createCustomizeHeroContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(700);
        Image view = new Image(getClass()
                .getResource("res/menu/custom/option.png").toExternalForm());

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);


        ArrayList<ImageView> fields = new ArrayList<>();
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/hero/Money .png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/hero/Soldier Coin.png").toExternalForm())));
        fields.add(new ImageView(new Image(getClass()
                .getResource("res/menu/custom/hero/Coin Effect.png").toExternalForm())));

        HBox hBox = new HBox();
        hBox.setSpacing(50);
        hBox.getChildren().addAll(fields);
        hBox.relocate(70, 50);



        TextField moneyChoice = new TextField();
        Label moneyChoice_l = new Label(String.valueOf(Hero.getInitialMoney()));
        moneyChoice_l.setFont(font);
        moneyChoice.setOpacity(0.0);
        moneyChoice.setOnKeyPressed(new TextEventHandler(moneyChoice_l));
        ImageView moneyChoice_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        moneyChoice_i.setFitHeight(50);
        moneyChoice_i.setFitWidth(85);
        /*moneyChoice_i.relocate(190, 430);*/

        TextField soldierCoinChoice = new TextField();
        Label soldierCoinChoice_l = new Label(String.valueOf(String.valueOf(Hero.getCoinForEachYarane())));
        soldierCoinChoice_l.setFont(font);
        soldierCoinChoice.setOpacity(0.0);
        soldierCoinChoice.setOnKeyPressed(new TextEventHandler(soldierCoinChoice_l));
        ImageView soldierCoinChoice_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        soldierCoinChoice_i.setFitHeight(50);
        soldierCoinChoice_i.setFitWidth(85);
        /*soldierCoinChoice_i.relocate(190 + 1 * 120, 430);*/

        TextField coinEffectChoice = new TextField();
        Label coinEffectChoice_l = new Label(String.valueOf(String.valueOf(Hero.getYaranePercent())));
        coinEffectChoice_l.setFont(font);
        coinEffectChoice.setOpacity(0.0);
        coinEffectChoice.setOnKeyPressed(new TextEventHandler(coinEffectChoice_l));
        ImageView coinEffectChoice_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        coinEffectChoice_i.setFitHeight(50);
        coinEffectChoice_i.setFitWidth(85);
        /*coinEffectChoice_i.relocate(190 + 2 * 120, 430);*/

        HBox hBox1 = new HBox();
        hBox1.setSpacing(100);
        hBox1.relocate(70, 150);
        hBox1.getChildren().addAll(moneyChoice, soldierCoinChoice, coinEffectChoice);

        HBox hBox1_l = new HBox();
        hBox1_l.setSpacing(170);
        hBox1_l.relocate(105, 120);
        hBox1_l.getChildren().addAll(moneyChoice_l, soldierCoinChoice_l, coinEffectChoice_l);

        HBox hBox1_i = new HBox();
        hBox1_i.setSpacing(120);
        hBox1_i.relocate(100, 120);
        hBox1_i.getChildren().addAll(moneyChoice_i, soldierCoinChoice_i, coinEffectChoice_i);

        HBox buttons = new HBox();
        MenuItem money_btn = new MenuItem(view, 50, 50, "OK");
        money_btn.setOnAction(new Runnable() {
            @Override
            public void run() {
                Hero.setInitialMoney(Integer.parseInt(moneyChoice_l.getText()));
            }
        });

        MenuItem coin_btn = new MenuItem(view, 50, 50, "OK");
        coin_btn.setOnAction(new Runnable() {
            @Override
            public void run() {
                Hero.setCoinForEachYarane(Integer.parseInt(soldierCoinChoice_l.getText()));
            }
        });


        MenuItem effect_btn = new MenuItem(view, 50, 40, "OK");
        effect_btn.setOnAction(new Runnable() {
            @Override
            public void run() {
                Hero.setYaranePercent(Double.parseDouble(coinEffectChoice_l.getText()));

            }
        });
        buttons.getChildren().addAll(money_btn, coin_btn, effect_btn);
        buttons.setSpacing(150);
        buttons.relocate(135, 190);

        /***** ANIMATION ****/

        Text sprite = new Text("Hero's View");
        sprite.setFont(font);
        sprite.relocate(150, 250);

        MenuItem choose_view1 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view7 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view8 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view11 = new MenuItem(view, 90, 40, "Choose");
        MenuItem choose_view12 = new MenuItem(view, 90, 40, "Choose");
        HBox choose_btns = new HBox();
        choose_btns.setSpacing(40);
        choose_btns.getChildren().addAll(choose_view1, choose_view7, choose_view8, choose_view11, choose_view12);
        choose_btns.relocate(60, 400);

        choose_view1.setOnAction(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.removeElementFromGameRoot(hero.getWarriorView());
                hero.setHeroPic(1);
                hero.updateWarriorView();
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        hero.getWarriorView());
                stage.setScene(newAliensScene);

            }
        });

        choose_view7.setOnAction(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.removeElementFromGameRoot(hero.getWarriorView());
                hero.setHeroPic(7);
                hero.updateWarriorView();
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        hero.getWarriorView());
                stage.setScene(newAliensScene);
            }
        });

        choose_view8.setOnAction(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.removeElementFromGameRoot(hero.getWarriorView());
                hero.setHeroPic(8);
                hero.updateWarriorView();
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        hero.getWarriorView());
                stage.setScene(newAliensScene);
            }
        });

        choose_view11.setOnAction(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.removeElementFromGameRoot(hero.getWarriorView());
                hero.setHeroPic(11);
                hero.updateWarriorView();
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        hero.getWarriorView());
                stage.setScene(newAliensScene);
            }
        });

        choose_view12.setOnAction(new Runnable() {
            @Override
            public void run() {
                AlienCreeps.removeElementFromGameRoot(hero.getWarriorView());
                hero.setHeroPic(12);
                hero.updateWarriorView();
                AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                        hero.getWarriorView());
                stage.setScene(newAliensScene);
            }
        });
        String address = "res/hero/movement/";

        ImageView[] view1 = new ImageView[12];
        view1[0] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/down1.png").toExternalForm()));
        view1[1] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/down1.png").toExternalForm()));
        view1[2] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/down3.png").toExternalForm()));
        view1[3] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/left1.png").toExternalForm()));
        view1[4] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/left2.png").toExternalForm()));
        view1[5] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/left3.png").toExternalForm()));
        view1[6] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/up1.png").toExternalForm()));
        view1[7] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/up2.png").toExternalForm()));
        view1[8] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/up3.png").toExternalForm()));
        view1[9] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/right1.png").toExternalForm()));
        view1[10] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/right2.png").toExternalForm()));
        view1[11] = new ImageView(new Image(getClass()
                .getResource(address + 1 + "/right3.png").toExternalForm()));


        ImageView[] view7 = new ImageView[12];
        view7[0] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/down1.png").toExternalForm()));
        view7[1] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/down1.png").toExternalForm()));
        view7[2] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/down3.png").toExternalForm()));
        view7[3] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/left1.png").toExternalForm()));
        view7[4] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/left2.png").toExternalForm()));
        view7[5] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/left3.png").toExternalForm()));
        view7[6] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/up1.png").toExternalForm()));
        view7[7] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/up2.png").toExternalForm()));
        view7[8] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/up3.png").toExternalForm()));
        view7[9] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/right1.png").toExternalForm()));
        view7[10] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/right2.png").toExternalForm()));
        view7[11] = new ImageView(new Image(getClass()
                .getResource(address + 7 + "/right3.png").toExternalForm()));

        ImageView[] view8 = new ImageView[12];
        view8[0] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/down1.png").toExternalForm()));
        view8[1] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/down1.png").toExternalForm()));
        view8[2] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/down3.png").toExternalForm()));
        view8[3] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/left1.png").toExternalForm()));
        view8[4] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/left2.png").toExternalForm()));
        view8[5] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/left3.png").toExternalForm()));
        view8[6] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/up1.png").toExternalForm()));
        view8[7] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/up2.png").toExternalForm()));
        view8[8] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/up3.png").toExternalForm()));
        view8[9] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/right1.png").toExternalForm()));
        view8[10] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/right2.png").toExternalForm()));
        view8[11] = new ImageView(new Image(getClass()
                .getResource(address + 8 + "/right3.png").toExternalForm()));

        ImageView[] view11 = new ImageView[12];
        view11[0] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/down1.png").toExternalForm()));
        view11[1] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/down2.png").toExternalForm()));
        view11[2] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/down3.png").toExternalForm()));
        view11[3] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/left1.png").toExternalForm()));
        view11[4] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/left2.png").toExternalForm()));
        view11[5] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/left3.png").toExternalForm()));
        view11[6] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/up1.png").toExternalForm()));
        view11[7] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/up2.png").toExternalForm()));
        view11[8] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/up3.png").toExternalForm()));
        view11[9] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/right1.png").toExternalForm()));
        view11[10] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/right2.png").toExternalForm()));
        view11[11] = new ImageView(new Image(getClass()
                .getResource(address + 11 + "/right3.png").toExternalForm()));

        ImageView[] view12 = new ImageView[12];
        view12[0] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/down1.png").toExternalForm()));
        view12[1] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/down2.png").toExternalForm()));
        view12[2] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/down3.png").toExternalForm()));
        view12[3] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/left1.png").toExternalForm()));
        view12[4] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/left2.png").toExternalForm()));
        view12[5] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/left3.png").toExternalForm()));
        view12[6] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/up1.png").toExternalForm()));
        view12[7] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/up2.png").toExternalForm()));
        view12[8] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/up3.png").toExternalForm()));
        view12[9] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/right1.png").toExternalForm()));
        view12[10] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/right2.png").toExternalForm()));
        view12[11] = new ImageView(new Image(getClass()
                .getResource(address + 12 + "/right3.png").toExternalForm()));

        for (int i = 0; i < 12; i++){
            view1[i].setFitWidth(50);
            view1[i].setFitHeight(50);
            view1[i].setVisible(false);
            view1[i].relocate(100 - 5, 300);

            view7[i].setFitWidth(50);
            view7[i].setFitHeight(50);
            view7[i].setVisible(false);
            view7[i].relocate(220 -5, 300);

            view8[i].setFitWidth(50);
            view8[i].setFitHeight(50);
            view8[i].setVisible(false);
            view8[i].relocate(340 - 5, 300);

            view11[i].setFitWidth(50);
            view11[i].setFitHeight(50);
            view11[i].setVisible(false);
            view11[i].relocate(450 - 5, 300);

            view12[i].setFitWidth(50);
            view12[i].setFitHeight(50);
            view12[i].setVisible(false);
            view12[i].relocate(580 - 5, 300);
        }
        view1[0].setVisible(true);
        view7[0].setVisible(true);
        view8[0].setVisible(true);
        view11[0].setVisible(true);
        view12[0].setVisible(true);

        Timeline view_heros = new Timeline(new KeyFrame(Duration.millis(200),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        boolean flag = false;
                        for (int i = 0; i < 12; i++){
                            if (view7[i].isVisible()){
                                flag = true;
                                view1[i].setVisible(false);
                                view7[i].setVisible(false);
                                view8[i].setVisible(false);
                                view12[i].setVisible(false);
                                view11[i].setVisible(false);
                                if (i + 1 < 12){
                                    view1[i + 1].setVisible(true);
                                    view7[i + 1].setVisible(true);
                                    view8[i + 1].setVisible(true);
                                    view12[i + 1].setVisible(true);
                                    view11[i + 1].setVisible(true);
                                } else {
                                    view1[0].setVisible(true);
                                    view7[0].setVisible(true);
                                    view8[0].setVisible(true);
                                    view11[0].setVisible(true);
                                    view12[0].setVisible(true);
                                }
                                break;
                            }
                        }
                        if (!flag){
                            view1[0].setVisible(true);
                            view7[0].setVisible(true);
                            view8[0].setVisible(true);
                            view11[0].setVisible(true);
                            view12[0].setVisible(true);
                        }
                    }
                }));
        view_heros.setCycleCount(Animation.INDEFINITE);
        view_heros.play();

        root.getChildren().addAll(background);
        root.getChildren().addAll(hBox);
        root.getChildren().addAll(hBox1, hBox1_i, hBox1_l);
      root.getChildren().addAll(buttons);
        root.getChildren().addAll(choose_btns);
        root.getChildren().addAll(view1);
        root.getChildren().addAll(view7);
        root.getChildren().addAll(view8);
        root.getChildren().addAll(view12);
        root.getChildren().addAll(view11);

        return root;
    }

    private Parent createCustomizeGameMapContent(){
        Group root = new Group();

        ImageView background = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/bg.png").toExternalForm()));
        background.setFitWidth(800);
        Image view = new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm());

        Font font = Font.loadFont(MenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 32);

        ImageView q1 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter alien max rate .png").toExternalForm()));
        ImageView q2 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter max rate hour .png").toExternalForm()));
        ImageView q3 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter alien min rate .png").toExternalForm()));
        ImageView q4 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter min rate hour .png").toExternalForm()));
        ImageView q5 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter radius reduce hour .png").toExternalForm()));
        ImageView q6 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter radius reduce rate .png").toExternalForm()));
        ImageView q7 = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/gamemap/Enter radius reset hour .png").toExternalForm()));

        q1.setFitHeight(50);
        q2.setFitHeight(50);
        q3.setFitHeight(50);
        q4.setFitHeight(50);
        q5.setFitHeight(50);
        q6.setFitHeight(50);
        q7.setFitHeight(50);


        VBox commands = new VBox();
        commands.setSpacing(15);
        commands.setAlignment(Pos.BASELINE_LEFT);
        commands.setTranslateX(70);
        commands.setTranslateY(15);
        commands.getChildren().addAll(q1, q2, q3, q4, q5, q6, q7);

        TextField max_rate = new TextField();
        max_rate.setOpacity(0.0);
        Label max_rate_l = new Label(String.valueOf(GameMap.getPeakHourMaxAlienRate()));
        ImageView max_rate_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        max_rate.setOnKeyPressed(new TextEventHandler(max_rate_l));
        max_rate_l.setFont(font);
        max_rate_i.setFitHeight(50);
        max_rate_i.setFitWidth(50);


        TextField max_hour = new TextField();
        Label max_hour_l = new Label(String.valueOf(GameMap.getPeakHourMax()));
        ImageView max_hour_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        max_hour.setOpacity(0.0);
        max_hour.setOnKeyPressed(new TextEventHandler(max_hour_l));
        max_hour_l.setFont(font);
        max_hour_i.setFitWidth(50);
        max_hour_i.setFitHeight(50);

        TextField min_rate = new TextField();
        Label min_rate_l = new Label(String.valueOf(GameMap.getPeakHourMinAlienRate()));
        ImageView min_rate_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        min_rate.setOpacity(0.0);
        min_rate.setOnKeyPressed(new TextEventHandler(min_rate_l));
        min_rate_l.setFont(font);
        min_rate_i.setFitHeight(50);
        min_rate_i.setFitWidth(50);

        TextField min_hour = new TextField();
        Label min_hour_l = new Label(String.valueOf(GameMap.getPeakHourMin()));
        ImageView min_hour_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        min_hour.setOpacity(0.0);
        min_hour.setOnKeyPressed(new TextEventHandler(min_hour_l));
        min_hour_l.setFont(font);
        min_hour_i.setFitWidth(50);
        min_hour_i.setFitHeight(50);

        TextField reduce_hour = new TextField();
        Label reducd_hour_l = new Label(String.valueOf(GameMap.getWhenReduceRadius()));
        ImageView reduce_hour_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        reduce_hour.setOpacity(0.0);
        reduce_hour.setOnKeyPressed(new TextEventHandler(reducd_hour_l));
        reducd_hour_l.setFont(font);
        reduce_hour_i.setFitHeight(50);
        reduce_hour_i.setFitWidth(50);

        TextField reset_hour = new TextField();
        Label reset_hour_l = new Label(String.valueOf(GameMap.getWhenResetRadius()));
        ImageView reset_hour_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        reset_hour.setOpacity(0.0);
        reset_hour.setOnKeyPressed(new TextEventHandler(reset_hour_l));
        reset_hour_l.setFont(font);
        reset_hour_i.setFitWidth(50);
        reset_hour_i.setFitHeight(50);

        TextField reduce_rate = new TextField();
        Label reduce_rate_l = new Label(String.valueOf(GameMap.getReduceRadiusRate()));
        ImageView reduce_rate_i = new ImageView(new Image(getClass()
                .getResource("res/menu/custom/input.png").toExternalForm()));
        reduce_rate.setOpacity(0.0);
        reduce_rate.setOnKeyPressed(new TextEventHandler(reduce_rate_l));
        reduce_rate_l.setFont(font);
        reduce_rate_i.setFitHeight(50);
        reduce_rate_i.setFitWidth(50);

        VBox vbox1 = new VBox();
        vbox1.setSpacing(30);
        vbox1.setAlignment(Pos.BASELINE_LEFT);
        vbox1.getChildren().addAll(max_rate, max_hour, min_rate, min_hour, reduce_hour, reduce_rate, reset_hour);
        vbox1.relocate(600, 30);

        VBox vbox1_l = new VBox();
        vbox1_l.setSpacing(27);
        vbox1_l.getChildren().addAll(max_rate_l, max_hour_l, min_rate_l, min_hour_l, reducd_hour_l, reduce_rate_l, reset_hour_l);
        vbox1_l.relocate(603, 23);

        VBox vbo1_i = new VBox();
        vbo1_i.setSpacing(12);
        vbo1_i.getChildren().addAll(max_rate_i, max_hour_i, min_rate_i, min_hour_i, reduce_hour_i,reduce_rate_i,reset_hour_i);
        vbo1_i.relocate(600, 25);

        customizeGameMapScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    GameMap.setPeakHourMaxAlienRate(Integer.parseInt(max_rate_l.getText()));
                    GameMap.setPeakHourMax(Integer.parseInt(max_hour_l.getText()));
                    GameMap.setPeakHourMinAlienRate(Integer.parseInt(min_rate_l.getText()));
                    GameMap.setPeakHourMin(Integer.parseInt(min_hour_l.getText()));
                    GameMap.setWhenReduceRadius(Integer.parseInt(reducd_hour_l.getText()));
                    GameMap.setWhenResetRadius(Integer.parseInt(reset_hour_l.getText()));
                    GameMap.setReduceRadiusRate(Double.parseDouble(reduce_rate_l.getText()));
                    stage.setScene(askWeaponScene);
                }
            }
        });

        root.getChildren().addAll(background);
        root.getChildren().addAll(commands);
        root.getChildren().addAll(vbo1_i, vbox1, vbox1_l);
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
    boolean start = false;

    public TextEventHandler(Label label) {
        this.lbl = label;
        start = true;
    }

    @Override
    public void handle(KeyEvent event) {
        KeyCode letter = event.getCode();
        if (letter != KeyCode.TAB && letter != KeyCode.ENTER){
            if (start){
                lbl.setText("");
                start = false;
            }
            if (letter == KeyCode.BACK_SPACE){
                if (lbl.getText().length() > 0){
                    lbl.setText(lbl.getText().substring(0, lbl.getText().length() - 1));
                }
            } else {
                lbl.setText(lbl.getText() + event.getText());
            }
        }
        //lbl.setText(lbl.getText() + event.getText());
        /*switch (event.getCode()){
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
            case PERIOD:
                lbl.setText(lbl.getText() + ".");
                break;
            case F:
                lbl.setText(lbl.getText() + "F");
                break;
            case T:
                lbl.setText(lbl.getText() + "T");
                break;
            case BACK_SPACE:
                if (lbl.getText().length() > 0){
                    lbl.setText(lbl.getText().substring(0, lbl.getText().length() - 1));
                }
        }*/
        //System.out.println(event.getCode());
    }
}
