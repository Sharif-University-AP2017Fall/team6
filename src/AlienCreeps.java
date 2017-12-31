import java.util.List;
import java.util.Scanner;

public class AlienCreeps {
    private static int CURRENT_SECOND = 0;
    private static int CURRENT_HOUR = 0;
    private static int CURRENT_DAY = 0;
    Thread gameTime;

    private Scanner scanner = new Scanner(System.in);
    private Hero hero = new Hero(new Dimension(400, 300));
    private GameMap gameMap = new GameMap(hero);

    public static void main(String[] args) {
        AlienCreeps game = new AlienCreeps();
        game.initialize();
        game.launch();
    }

    private void initialize() {
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

    private void launch() {

        Runnable r = () -> {
            while (true){
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (CURRENT_SECOND == 0 && CURRENT_HOUR == 0 && CURRENT_DAY == 0) {
                    gameMap.randomWeather();
                }
                if (CURRENT_SECOND < 9) { //9
                    CURRENT_SECOND++;
                    gameMap.plague();
                } else if (CURRENT_HOUR < 23) { //23
                    gameMap.superNaturalHelp();
                    gameMap.naturalDisaster();
                    CURRENT_HOUR++;
                    CURRENT_SECOND = 0;
                } else {
                    gameMap.setCanUpgradeSoldiers();
                    gameMap.randomWeather();
                    CURRENT_DAY++;
                    CURRENT_HOUR = 0;
                    CURRENT_SECOND = 0;
                }
                System.out.println("------------------------");
                System.out.println("Current second = " + CURRENT_SECOND);
                System.out.println("Current hour = "  + CURRENT_HOUR);
                System.out.println("Current day = " + CURRENT_DAY);
                if (gameMap.nextSecond()) {
                    return;
                }
            }
        };
        gameTime = new Thread(r);
        gameTime.start();

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
                System.out.println(this.hero.getMoney());
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
        }
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
}
