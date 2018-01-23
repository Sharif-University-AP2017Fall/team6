import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
                });
                gameScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("should move for " + (event.getX() - hero.getShootingPoint().getX()) +
                                " " +
                                (event.getY() - hero.getShootingPoint().getY()));
                        gameMap.moveHero(new Dimension(Math.round(event.getX()) - hero.getShootingPoint().getX(),
                                Math.round(event.getY()) - hero.getShootingPoint().getY()));
                    }
                });
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


    static Scene menuScene = new Scene(new Group(),GameMap.XBOUND, GameMap.YBOUND);
    static Scene gameScene = new Scene(new Group(), GameMap.XBOUND, GameMap.YBOUND);
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        gameScene.setRoot(createGameContent());
        stage.setTitle("Alien Creeps");
        stage.setScene(gameScene);
        stage.show();
    }

    private Parent createMenuContent(){
        return new Group();
    }

    private Parent createGameContent(){
        Group root = new Group();
        Canvas canvas = new Canvas(GameMap.XBOUND, GameMap.YBOUND);
        root.getChildren().add(canvas);
        MapView mapView = new MapView(canvas);

        //TODO bring these in the application window
      //  initWeapons();
       root.getChildren().add(hero.getHeroView());

        launchGame();
        return root;
    }
}
