import java.util.Scanner;

public class AlienCreeps {
    public static Scanner scanner = new Scanner(System.in);
    public Hero hero = new Hero(new Dimension(0, 0));
    public GameMap gameMap = new GameMap(hero);
    public static void main(String[] args) {
        AlienCreeps game = new AlienCreeps();
        game.launch();
    }
    public void launch(){
        //code to start came and put weapons in predefined locations.
        while (true){
            /*actionsList();
            int input = Integer.parseInt(scanner.nextLine());
            switch (input){
                case 0:
                    actionsList();
                    break;
                case 1:
                    gameMap.showRemainingAliens();
                    System.out.println(hero);
                    gameMap.showWeapons();
                    gameMap.showReachedFlag();
                    break;
                case 2:
                    System.out.println("Choose a weapon:");
                    System.out.println();
                    Weapon.showWeaponList();
                    String weaponName = scanner.nextLine();
                    System.out.println("Choose a location number:");
                    gameMap.showAvailableLocations();
                    int locationNumber = Integer.parseInt(scanner.nextLine());
                    gameMap.putWeaponInPlace(weaponName, locationNumber);
                    break;
                case 3:
                    //code
                    break;
                case 4:

            }*/
            String input = scanner.nextLine();
            String info[] = input.split(" ");
            if (input.matches("put [\\w]* in place [\\d]*")){
                String weaponName = info[1];
                int locationNum = Integer.parseInt(info[4]);
                gameMap.putWeaponInPlace(weaponName, locationNum);
            }else if(input.matches("upgrade [\\w]* in place [\\d]*")){
                String weaponName = info[1];
                int locationNum = Integer.parseInt(info[4]);
                gameMap.upgradeWeaponInPlace(weaponName, locationNum);
            }else if (input.matches("show details")){
                gameMap.showRemainingAliens();
                System.out.println(hero);
                gameMap.showWeapons();
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
                gameMap.showWeapons();
            }else if(input.matches("status [\\w]* weapon")){
                gameMap.showWeapons(info[2]);
            }else if(input.matches("show achievements")){
                System.out.println(hero.getAchievement());
            }else if(input.matches("go ahead")){
                gameMap.nextSecond();
            }else{
                System.out.println("invalid command");
            }
        }
    }

    public void actionsList(){
        System.out.println("0 - show actions list\n" +
                "1 - show details\n" +
                "2 - put (weapon name) in place (location number()\n" +
                "3 - upgrade (weapon name) in place (location number)\n" +
                "4 - move hero for (x, y)\n" +
                "5 - tesla in (x, y)\n" +
                "6 - hero status\n" +
                "7 - knights status\n" +
                "8 - weapons status\n" +
                "9 - status (weapon name) status\n" +
                "10 - show achievements\n" +
                "11 - go ahead");
    }
}
