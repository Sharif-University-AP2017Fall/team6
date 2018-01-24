import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Scanner;

public class AlienCreeps extends Application {
    private static int CURRENT_SECOND = 0;
    private static int CURRENT_HOUR = 0;
    private static int CURRENT_DAY = 0;
    private boolean gameOver = false;
    Thread gameTime;
    Thread gameInput;

    private Scanner scanner = new Scanner(System.in);
    private Hero hero = new Hero(new Dimension(400, 300));
    private GameMap gameMap = new GameMap(hero);
    static AlienCreeps game = new AlienCreeps();
    public static void main(String[] args) {
    //    game.initWeapons();
      //  game.launchGame();
        launch(args);
    }

    private void initWeapons() {

        System.out.println("Choose one of the weapons to put in " +
                gameMap.getSpecifiedLocations().keySet().size() +
                " specified locations");
        Weapon.showWeaponList();
        System.out.println("Type \'start\' to start game");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("start")) {
            if (input.matches("put [\\w]*[\\s]*[\\w]* in place [\\d]+")) {
                String info[] = input.split(" ");
                String weaponName;
                int locationNum;
                if (info.length == 5) {
                    weaponName = info[1];
                    locationNum = Integer.parseInt(info[4]);
                } else {
                    weaponName = info[1] + " " + info[2];
                    locationNum = Integer.parseInt(info[5]);
                }
                gameMap.putWeaponInPlace(weaponName, locationNum);
            } else {
                System.out.println("invalid command");
            }
            input = scanner.nextLine();
        }
        System.out.println(gameMap);
        System.out.println("Hero in : " + hero.getDimension());
    }

    private void launchGame() {

        Runnable r1 = new Runnable() {
            private final Object lock = new Object();

            @Override
            public void run() {

                Thread heroLifeCycle = new Thread(hero);
                heroLifeCycle.start();

                while (true){
                    try {
                        Thread.sleep(6000); //TODO: change this to one second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (CURRENT_SECOND == 0 && CURRENT_HOUR == 0 && CURRENT_DAY == 0) {
                        //      gameMap.randomWeather();
                    }
                    if (CURRENT_SECOND < 9) { //9
                        CURRENT_SECOND++;
                        //     gameMap.plague();
                    } else if (CURRENT_HOUR < 23) { //23
                        //      gameMap.superNaturalHelp();
                       //      gameMap.naturalDisaster();
                        CURRENT_HOUR++;
                        CURRENT_SECOND = 0;
                    } else {
                        //      gameMap.setCanUpgradeSoldiers();
                        //      gameMap.randomWeather();
                        CURRENT_DAY++;
                        CURRENT_HOUR = 0;
                        CURRENT_SECOND = 0;
                    }
                    System.out.println("------------------------");
                    System.out.println("Current second = " + CURRENT_SECOND);
                    System.out.println("Current hour = "  + CURRENT_HOUR);
                    System.out.println("Current day = " + CURRENT_DAY);
                    synchronized (lock){
                        if (gameMap.nextSecond()) {
                            gameOver = true;
                            gameTime.stop();
                            System.exit(0);
                            return;
                        }
                    }
                }
            }
        };
        gameTime = new Thread(r1);
        gameTime.start();

        Runnable r2 = () -> {
            while (true) {
                //System.out.println("taking input");
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

                if (gameOver){
                    gameInput.stop();
                    return;
                }
            }
        };
        gameInput = new Thread(r2);
        gameInput.start();

        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        gameMap.moveHero(event);
                    }
                });/*
                gameScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("should move for " + (event.getX() - hero.getShootingPoint().getX()) +
                                " " +
                                (event.getY() - hero.getShootingPoint().getY()));
                        gameMap.moveHero(new Dimension(Math.round(event.getX()) - hero.getShootingPoint().getX(),
                                Math.round(event.getY()) - hero.getShootingPoint().getY()));
                    }
                });*/
            }
        };
        gameInput = new Thread(r3);
        gameInput.start();


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
    static Scene gameScene = new Scene(new Group(), GameMap.XBOUND, GameMap.YBOUND);
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        menuScene.setRoot(createMenuContent());
        stage.setScene(menuScene);
        stage.show();

        /*gameScene.setRoot(createGameContent());
        stage.setTitle("Alien Creeps");
        stage.show();*/
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
            gameScene.setRoot(createGameContent());
            stage.setScene(gameScene);
            stage.show();
        });
        new_item.setDim(390, 300);

        MenuItem load_item = new MenuItem("LoadGame");
        load_item.setDim(300, 450);

        MenuItem exit_item = new MenuItem("Exit");
        exit_item.setOnAction(() -> System.exit(0));
        exit_item.setDim(390, 600);

        root.getChildren().addAll(background, new_item, load_item, exit_item);

        //TODO DON'T FORGET THIS PART BEFORE YOU PULL

        Canvas canvas = new Canvas(540, 200);
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

    private Parent createGameContent(){
        Group root = new Group();
        Canvas canvas = new Canvas(GameMap.XBOUND, GameMap.YBOUND);
        root.getChildren().add(canvas);
        MapView mapView = new MapView(canvas);

        //TODO bring these in the application window
      //  initWeapons();
       root.getChildren().add(hero.getWarriorView());

        launchGame();
        return root;
    }

    public static void addElementToGameRoot(Node node){
        ((Group) gameScene.getRoot()).getChildren().add(node);
    }
}
