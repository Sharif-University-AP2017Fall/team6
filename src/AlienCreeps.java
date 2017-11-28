import java.util.Scanner;

public class AlienCreeps {
    public static Scanner scanner = new Scanner(System.in);
    public Hero hero = new Hero(new Dimension(0, 0));
    public GameMap gameMap = new GameMap(hero);
    public static void main(String[] args) {
        AlienCreeps game = new AlienCreeps();
        game.initialize();
        game.launch();
       // game.launch();
    }

    public void test(){
        for (int i = 0; i < 50; i++){
            System.out.println("second " + (i + 1));
            System.out.println("**********");
            gameMap.generateAliens();
            gameMap.moveAliens();
            gameMap.shootAliensByWeapons();
        }
    }
    public void initialize(){
        System.out.println("Choose one of the weapons to put in 12 specified locations");
        Weapon.showWeaponList();
        System.out.println("Type \'start\' to start game");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("start")){
            if (input.matches("put [\\w]*[\\s]*[\\w]* in place [\\d]*")){
                //System.out.println("lasjf");
                String info[] = input.split(" ");
                String weaponName = null;
                int locationNum = 0;
                if (info.length == 5){
                    weaponName = info[1];
                    locationNum = Integer.parseInt(info[4]);
                }else{
                    weaponName = info[1] + " " + info[2];
                    locationNum = Integer.parseInt(info[5]);
                }
                gameMap.putWeaponInPlace(weaponName, locationNum);
            }
            input = scanner.nextLine();
        }
        System.out.println(gameMap);
        System.out.println("Hero in : " + hero.getDimension());
    }

    public void launch(){
        //code to start game and put weapons in predefined locations.
        while (true){
            String input = scanner.nextLine();
            String info[] = input.split(" ");
            if (input.matches("put [\\w]* in place [\\d]*")){
                String weaponName = info[1];
                int locationNum = Integer.parseInt(info[4]);
                gameMap.putWeaponInPlace(weaponName, locationNum);
            }else if(input.matches("upgrade [\\w]*[\\s]*[\\w]* in place [\\d]*")){
                String weaponName = info[1];
                int locationNum;
                if (!info[2].equals("in"))
                    {weaponName+=" "+info[2];
                    locationNum = Integer.parseInt(info[5]);}
                else{locationNum = Integer.parseInt(info[4]); }
                gameMap.upgradeWeaponInPlace(weaponName, locationNum);
            }else if (input.matches("show details")){
                gameMap.showRemainingAliens();
                System.out.println(hero);
                gameMap.getWeapons().forEach(System.out::println);
                gameMap.showReachedFlag();
            }else if (input.matches("move hero for \\([\\d]*,[\\s]*[\\d]*\\)")){
                String dimInfo[] = input.substring(15, input.length() - 1).split(",[\\s]*");
                Dimension change = new Dimension(Integer.parseInt(dimInfo[0]),
                        Integer.parseInt(dimInfo[1]));
                gameMap.moveHero(change);
            }else if (input.matches("tesla in \\([\\d]*,[\\s]*[\\d]*\\)")){
                String dimInfo[] = input.substring(10, input.length() - 1).split(",[\\s]*");
                Dimension dimension = new Dimension(Integer.parseInt(dimInfo[0]),
                        Integer.parseInt(dimInfo[1]));
                gameMap.useTesla(dimension);
            }else if(input.matches("hero status")){
                hero.showStatus();
            }else if(input.matches("knights status")){
                //if game map has barrack:
                hero.showKnightStatus();
            }else if(input.matches("weapons status")){
                gameMap.getWeapons().forEach(System.out::println);
            }else if(input.matches("status [\\w]* weapon")){
                gameMap.getWeapons(info[2]).forEach(System.out::println);
            }else if(input.matches("show achievements")){
                System.out.println(hero.getAchievement());
            }else if(input.matches("go ahead")){
                gameMap.nextSecond();
            }else{
                System.out.println("invalid command");
            }
        }
    }
}
