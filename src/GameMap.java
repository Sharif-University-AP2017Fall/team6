import java.util.*;

public class GameMap {
    static double XBOUND = 800;
    static double YBOUND = 600;
    static int UNIT = 15;

    private List<Route> routes = new ArrayList<>();
    private List<Wormhole> wormholes = new ArrayList<>();
    private Map<Dimension, Mappable> specifiedLocations = new HashMap<>();
    private Map<Integer, Dimension> specifiedNumbers = new HashMap<>();
    private Barrack barrack;

    private Dimension flag;
    private Alien[] reachedFlag = new Alien[5];

    private Hero hero;

    private int secondsLeftToResurrectHero = 0;
    private int weatherCondition = 0;
    private double weatherConditionConstant = 1;

    private boolean canUpgradeSoldiers = true;
    private boolean SuperNaturalHelp = false;

    GameMap(Hero hero) {
        flag = new Dimension(800, 300);
        this.hero = hero;

        Line lines[] = new Line[5];
        ArrayList<Dimension> breakPoints = new ArrayList<>();
        ArrayList<Dimension> intersections = new ArrayList<>();
        intersections.add(new Dimension(450, 300));
        intersections.add(flag);

        breakPoints.add(new Dimension(0, 0));
        breakPoints.add(new Dimension(150, 150));
        breakPoints.add(new Dimension(300, 150));
        breakPoints.add(new Dimension(450, 300));
        breakPoints.add(new Dimension(600, 150));
        lines[0] = new Line(1.0, 0.0, breakPoints.get(0), breakPoints.get(1));
        lines[1] = new Line(0.0, 150.0, breakPoints.get(1), breakPoints.get(2));
        lines[2] = new Line(1.0, -150.0, breakPoints.get(2), breakPoints.get(3));
        lines[3] = new Line(-1.0, 750, breakPoints.get(3), breakPoints.get(4));
        lines[4] = new Line(0.75, -300, breakPoints.get(4), flag);
        routes.add(new Route(lines, intersections));


        breakPoints.set(0, new Dimension(0, 600));
        breakPoints.set(1, new Dimension(150, 450));
        breakPoints.set(2, new Dimension(300, 450));
        breakPoints.set(3, new Dimension(450, 300));
        breakPoints.set(4, new Dimension(600, 450));
        lines[0] = new Line(-1.0, 600, breakPoints.get(0), breakPoints.get(1));
        lines[1] = new Line(0, 450, breakPoints.get(1), breakPoints.get(2));
        lines[2] = new Line(-1.0, 750, breakPoints.get(2), breakPoints.get(3));
        lines[3] = new Line(1.0, 150, breakPoints.get(3), breakPoints.get(4));
        lines[4] = new Line(-0.75, 900, breakPoints.get(4), flag);
        routes.add(new Route(lines, intersections));

        Dimension dimension;


        dimension = new Dimension(52, 50);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(1, dimension);

        dimension = new Dimension(73, 75);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(2, dimension);

        dimension = new Dimension(102, 100);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(3, dimension);

        dimension = new Dimension(200, 155);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(4, dimension);

        dimension = new Dimension(240, 145);
        specifiedNumbers.put(5, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(270, 155);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(6, dimension);

        dimension = new Dimension(350, 202);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(7, dimension);

        dimension = new Dimension(385, 240);
        specifiedNumbers.put(8, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(420, 268);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(9, dimension);


        dimension = new Dimension(450, 295);
        specifiedNumbers.put(10, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(450, 305);
        specifiedNumbers.put(11, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(445, 300);
        specifiedNumbers.put(12, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(455, 300);
        specifiedNumbers.put(13, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(175, 445);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(14, dimension);

        dimension = new Dimension(210, 455);
        specifiedNumbers.put(15, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(260, 445);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(16, dimension);

        dimension = new Dimension(350, 402);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(17, dimension);

        dimension = new Dimension(400, 348);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(18, dimension);

        dimension = new Dimension(500, 352);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(19, dimension);

        dimension = new Dimension(522, 375);
        specifiedNumbers.put(20, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(550, 402);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(21, dimension);

        dimension = new Dimension(500, 248);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(22, dimension);

        dimension = new Dimension(550, 202);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(23, dimension);

        dimension = new Dimension(600, 148);
        specifiedNumbers.put(24, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(680, 212);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(25, dimension);

        dimension = new Dimension(710, 240);
        specifiedNumbers.put(26, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(750, 265);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(27, dimension);

        dimension = new Dimension(680, 398);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(28, dimension);

        dimension = new Dimension(710, 360);
        specifiedNumbers.put(29, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(720, 325);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(30, dimension);

        List<Dimension> wormholeDims = Dimension.randomDimension(6);
        wormholes.add(new Wormhole(1, wormholeDims.get(0)));
        wormholes.add(new Wormhole(0, wormholeDims.get(1)));
        wormholes.add(new Wormhole(3, wormholeDims.get(2)));
        wormholes.add(new Wormhole(2, wormholeDims.get(3)));
        wormholes.add(new Wormhole(5, wormholeDims.get(4)));
        wormholes.add(new Wormhole(4, wormholeDims.get(5)));
    }

    boolean nextSecond() {
        if (AlienCreeps.getCurrentHour() == 20 && AlienCreeps.getCurrentSecond() == 0) {
            //System.out.println("Reducing soldiers' radius");
            reduceRadius();
        }
        if (AlienCreeps.getCurrentSecond() == 0 && AlienCreeps.getCurrentHour() == 4) {
            //System.out.println("Soldiers' radius back to normal");
            resetRadius();
        }

        randomizeWormholes();

        updateTeslaStatus();

        if (this.hero.isDead()) {
            this.secondsLeftToResurrectHero--;
            if (this.secondsLeftToResurrectHero == 0) {
                this.hero.setEnergy(300);
            }
        }

        if (barrack != null) {
            barrack.proceed();
            Soldier soldier = barrack.getSoldier();
            barrack.removeSoldier();
            if (soldier != null) {
                for (int i = 0; i < 3; i++) {
                    if (hero.getSoldiers()[i] == null) {
                        Dimension soldierDimension = hero.getDimension().add(hero.getSoldierDims()[i]);
                        soldier.setDimension(soldierDimension);
                        hero.getSoldiers()[i] = soldier;
                        //System.out.println("BARRACK MADE NEW SOLDIER");
                        //System.out.println("welcome soldier " + i);
                        break;
                    }
                }
            }
        }

        if (moveAliens()) {
            return true;
        }

        if (AlienCreeps.getCurrentHour() <= 16 && AlienCreeps.getCurrentHour() >= 10) {
            generateAliens(1);
        } else {
            generateAliens(2);
        }

        shootAliens();

        if (Alien.isSTART()) {
            //System.out.println("There are " + Alien.getNUM() + " aliens in total.");
            if (Alien.getNUM() <= 0 && AlienCreeps.getCurrentHour() > 2) {
                System.out.println("CONGRATULATIONS! YOU WON :D");
                return true;
            }
        }
        return false;
    }

    private void reduceRadius() {
        for (int i = 0; i < 3; i++) {
            Soldier s = hero.getSoldiers()[i];
            if (s != null) {
                //System.out.println("Soldier #" + (i + 1));
                s.reduceRadius();
            }
        }
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon) {
                Weapon w = ((Weapon) specifiedLocations.get(dimension));
                w.reduceRadius();
            }
        }
    }

    private void resetRadius() {
        for (int i = 0; i < 3; i++) {
            Soldier s = hero.getSoldiers()[i];
            if (s != null) {
                //System.out.println("Soldier #" + (i + 1));
                s.resetRadius();
            }
        }
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon) {
                Weapon w = ((Weapon) specifiedLocations.get(dimension));
                w.resetRadius();
            }
        }
    }

    void upgradeSoldier() {
        if (canUpgradeSoldiers) {
            this.hero.upgradeSoldiers();
        } else {
            System.out.println("Can't upgrade soldiers twice in one day. Try again tomorrow.");
        }
    }

    private void randomizeWormholes() {
        if ((int) (Math.random() * 5) == 1) {
            List<Dimension> wormholeDims = Dimension.randomDimension(6);

            /*** check that they're not on the routes ***/
            List<Line> lines = new ArrayList<>();

            for (int i = 0; i < routes.size(); i++){
                lines.addAll(Arrays.asList(routes.get(i).getLines()));
            }

            for (int i = 0; i < 6; i++){
                for (int j = 0; j < lines.size(); j++){
                    while (lines.get(j).isOnLine(wormholeDims.get(i))){
                        wormholeDims.set(i, Dimension.randomDimension(1).get(0));
                    }
                }
            }

            for (int i = 0; i < 6; i++) {
                wormholes.get(i).setDimension(wormholeDims.get(i));
            }
            System.out.println("New Wormhole Dimensions are:");
            wormholeDims.forEach(System.out::println);
        }

    }

    void showRemainingAliens() {
        System.out.println("*** Aliens ***");
        System.out.println("------------");
        ArrayList<Alien> remainingAliens = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            remainingAliens.addAll(routes.get(i).getAliens());
        }
        if (remainingAliens.size() > 0) {
            Collections.sort(remainingAliens);
            remainingAliens.forEach(System.out::println);
        } else {
            System.out.println("No aliens.");
        }
        System.out.print("\n\n");
    }

    void putWeaponInPlace(String weaponName, int whichPlace) {
        if (whichPlace > specifiedLocations.keySet().size()) {
            System.out.println("There are only " + specifiedLocations.keySet().size() + " available places.");
            return;
        }
        Dimension dimension = specifiedNumbers.get(whichPlace);
        if (specifiedLocations.get(dimension) == null) {
            if (weaponName.equalsIgnoreCase("Barrack")) {
                if (this.barrack == null) {
                    if (hero.getMoney() >= 90) {
                        this.barrack = new Barrack(dimension);
                        specifiedLocations.put(dimension, this.barrack);
                        this.hero.reduceMoney(90);
                        barrack.requestSoldier(hero.getResurrectionTime());
                        barrack.requestSoldier(hero.getResurrectionTime());
                        barrack.requestSoldier(hero.getResurrectionTime());
                    }
                } else {
                    System.out.println("You already have a barrack.");
                }
            } else {
                Weapon bought = this.hero.buyWeapon(weaponName, dimension);
                specifiedLocations.put(dimension, bought);
            }
        } else {
            System.out.println("There is already a weapon in this location.");
        }
    }

    void upgradeWeaponInPlace(String weaponName, int whichPlace) {
        if (whichPlace > specifiedLocations.keySet().size()) {
            System.out.println("There are only " + specifiedLocations.keySet().size() + " available places.");
            return;
        }
        Dimension dimension = specifiedNumbers.get(whichPlace);
        if (specifiedLocations.get(dimension) != null) {
            if (!(specifiedLocations.get(dimension) instanceof Barrack)) {
                Weapon toUpgrade = ((Weapon) specifiedLocations.get(dimension));
                if (toUpgrade.getName().equalsIgnoreCase(weaponName)) {
                    if (!hero.upgradeWeapon(toUpgrade)) {
                        System.out.println("Not enough money.");
                    } else {
                        //System.out.println("Upgraded successfully");
                        //System.out.println(hero.getMoney());
                    }
                } else {
                    System.out.println("Incorrect name");
                }
            } else {
                if (weaponName.equalsIgnoreCase("Barrack")) {
                    System.out.println("Can't upgrade barrack.");
                } else {
                    System.out.println("Incorrect name");
                }
            }
        } else {
            System.out.println("There is no weapon in this place");
        }
    }

    private void generateAliens(int probabilityInv) {
        if (Alien.getNUM() < Alien.getMAXNUM()) {
            if ((int) (Math.random() * probabilityInv) == 0) {
                int routeNumber = chooseRandomRoute();
                Route whichRoute = routes.get(routeNumber);
                int whichAlien = (int) (Math.random() * 4);
                switch (whichAlien) {
                    case 0:
                        //System.out.println("Adding Albertonion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Albertonion"), 0);
                        System.out.println("An Albertonion entered!!");
                        break;
                    case 1:
                        // System.out.println("Adding Algwasonion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Algwasonion"), 0);
                        System.out.println("An Algwasonion entered!!");
                        break;
                    case 2:
                        //  System.out.println("Adding Activinion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Activionion"), 0);
                        System.out.println("An Activionion entered!!");
                        break;
                    case 3:
                        //   System.out.println("Adding Aironion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Aironion"), 0);
                        System.out.println("An Aironion entered!!");
                        break;
                }
            }
        }
    }

    private int chooseRandomRoute() {
        return (int) (Math.random() * routes.size());
    }

    void showReachedFlag() {
        int num = 0;
        ArrayList<String> names = new ArrayList<>();
        for (Alien alien : reachedFlag) {
            if (alien != null) {
                num++;
                names.add(alien.getName());
            }
        }
        System.out.println(num + " aliens have reached flag.");
        names.forEach(System.out::println);
    }

    private boolean reachFlag(Alien alien) {
        //System.out.println(alien.getName() + " reached flag.");
        for (int i = 0; i < 5; i++) {
            if (reachedFlag[i] == null) {
                reachedFlag[i] = alien;
                // System.out.println((i + 1) + " aliens have reached flag.");
                if (i == 4) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public boolean gameStatus() {
        int numReached = 0;
        for (int i = 0; i < 5; i++) {
            if (reachedFlag[i] != null) {
                numReached++;
            }
        }
        return numReached >= 5;
    }

    private boolean moveAliens() {
        for (int i = 0; i < routes.size(); i++) {
            List<Alien> reachedIntersectionOrFlag = routes.get(i).moveAliensOnRoute();
            for (int j = 0; j < reachedIntersectionOrFlag.size(); j++) {
                Alien alien = reachedIntersectionOrFlag.get(j);
                if (alien.getDimension().equals(flag)) {
                    Alien.reduceNum(1);
                    return reachFlag(alien);
                }
                int randomNumber = chooseRandomRoute();
                //System.out.println(alien.getName() + " was relocated to route number " + randomNumber);
                Route whichRoute = routes.get(randomNumber);
                int whichLine = whichRoute.whichLine(alien.getDimension());
                whichRoute.addAlienToRoute(alien, whichLine);
            }
        }
        backToNormalSpeed();
        return false;
    }

    private void backToNormalSpeed() {
        List<Alien> allAliens = new ArrayList<>();
        List<Alien> reducedSpeed = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            allAliens.addAll(routes.get(i).getAliens());
            for (Dimension dimension : specifiedLocations.keySet()) {
                Mappable m = specifiedLocations.get(dimension);
                if (m instanceof Weapon) {
                    Weapon weapon = ((Weapon) m);
                    reducedSpeed.addAll(routes.get(i).aliensWithinRadius(weapon));
                }
            }
            reducedSpeed.addAll(routes.get(i).aliensWithinRadius(this.hero));
            for (int j = 0; j < 3; j++) {
                Soldier soldier = hero.getSoldiers()[j];
                if (soldier != null) {
                    reducedSpeed.addAll(routes.get(i).aliensWithinRadius(soldier));
                }
            }
        }
        allAliens.removeAll(reducedSpeed);
        for (int i = 0; i < allAliens.size(); i++) {
            allAliens.get(i).backToNormalSpeed();
        }
    }

    void moveHero(Dimension change) {
        if (this.hero.isDead()) {
            System.out.println("Hero is dead :( Can't move hero.");
        } else {
            if (this.hero.move(change)) {
                Dimension newDim = hero.getDimension();
                for (int i = 0; i < this.wormholes.size(); i++) {
                    if (wormholes.get(i).isWithinRadius(newDim)) {
                        Wormhole in = wormholes.get(i);
                        Wormhole out = wormholes.get(in.getLeadsTo());
                        Dimension newChange = new Dimension(out.getDimension().getX() - hero.getDimension().getX(),
                                out.getDimension().getY() - hero.getDimension().getY());
                        System.out.println("hero went into wormhole " + (i + 1) + " and came out from wormhole " + (in.getLeadsTo() + 1));
                        System.out.println("she says hi :)");
                        this.hero.move(newChange);
                        break;
                    }
                }
                backToNormalSpeed();
            }
        }
    }

    void useTesla(Dimension dimension) {
        if (Weapon.NUM_USED_TESLA < 2) {
            if (!Weapon.TESLA_IN_USE) {

                Weapon tesla = Weapon.WeaponFactory(dimension, "Tesla");
                List<Alien> aliensToKill = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++) {
                    aliensToKill.addAll(routes.get(i).aliensWithinRadius(tesla));
                }
                if (this.hero.addExperienceLevel(aliensToKill.size() * 5)) {
                    reduceAllWeaponsPrice();
                }
                /*** test ***/
                //aliensToKill.forEach(System.out::println);

                this.hero.addMoney(aliensToKill.size() * 10);
                updateAchievements(aliensToKill, "weapon");
                for (int i = 0; i < routes.size(); i++) {
                    this.removeAliensFromRoute(routes.get(i), aliensToKill);
                }
            } else {
                System.out.println("You must wait " + Weapon.SECONDS_LEFT_TO_USE_TESLA + " more seconds.");
            }
        } else {
            System.out.println("Can't use tesla more than twice.");
        }

    }

    private void updateTeslaStatus() {
        if (Weapon.TESLA_IN_USE) {
            Weapon.SECONDS_LEFT_TO_USE_TESLA--;
            if (Weapon.SECONDS_LEFT_TO_USE_TESLA == 0) {
                Weapon.TESLA_IN_USE = false;
            }
        }
    }

    private void shootAliens() {
        shootAliensByHeroAndSoldiers();
        shootAliensByWeapons();
    }

    private void shootAliensByWeapons() {
        for (Dimension dimension : specifiedLocations.keySet()) {
            Mappable m = specifiedLocations.get(dimension);
            if (m instanceof Weapon) {
                Weapon weapon = ((Weapon) m);
                List<Alien> aliensToShoot = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++) {
                    aliensToShoot.addAll(routes.get(i).aliensWithinRadius(weapon));
                }
                List<Alien> deadAliens = weapon.shoot(aliensToShoot);
                if (deadAliens != null && !deadAliens.isEmpty()) {
                    //System.out.println(weapon.getName() + " killed " + deadAliens.size() + " aliens.");
                    aliensToShoot.removeAll(deadAliens);
                    if (this.hero.addExperienceLevel(deadAliens.size() * 5)) {
                        reduceAllWeaponsPrice();
                    }
                    this.hero.addMoney(deadAliens.size() * 10);
                    updateAchievements(deadAliens, "weapon");
                    for (int i = 0; i < routes.size(); i++)
                        this.removeAliensFromRoute(routes.get(i), deadAliens);
                    Alien.reduceNum(deadAliens.size());
                }
            }
        }
        //backToNormalSpeed();
    }

    private void shootAliensByHeroAndSoldiers() {
        List<Alien> dead = new ArrayList<>();

        if (!this.hero.isDead()) {
            //System.out.println("hero start shooting");
            List<Alien> toShoot = new ArrayList<>();
            for (int i = 0; i < routes.size(); i++) {
                toShoot.addAll(routes.get(i).aliensWithinRadius(this.hero));
            }

            if (!toShoot.isEmpty()) {
                List<Alien> killedByHero = this.hero.shoot(toShoot);
                if (!killedByHero.isEmpty()) {
                    if (this.hero.addExperienceLevel(killedByHero.size() * 15)) {
                        reduceAllWeaponsPrice();
                    }
                    this.hero.addMoney(killedByHero.size() * 10);
                    updateAchievements(killedByHero, "hero");
                    dead.addAll(killedByHero);
                }
                if (this.hero.isDead()) {
                    this.secondsLeftToResurrectHero = this.hero.getResurrectionTime();
                }
            } else {
                //System.out.println("no aliens in hero radius");
            }
        }

        Soldier soldiers[] = this.hero.getSoldiers();
        for (int j = 0; j < 3; j++) {
            if (soldiers[j] != null) {
                //System.out.println("Soldier #" + (j + 1) + " start shooting");
                List<Alien> toShoot = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++) {
                    toShoot.addAll(routes.get(i).aliensWithinRadius(soldiers[j]));
                }

                if (!toShoot.isEmpty()) {
                    List<Alien> killedBySoldier = soldiers[j].shoot(toShoot);
                    if (!killedBySoldier.isEmpty()) {
                        if (this.hero.addExperienceLevel(killedBySoldier.size() * 5)) {
                            reduceAllWeaponsPrice();
                        }
                        this.hero.addMoney(killedBySoldier.size() * 10);
                        dead.addAll(killedBySoldier);
                    }
                    if (soldiers[j].isDead()) {
                        soldiers[j] = null;
                        barrack.requestSoldier(this.hero.getResurrectionTime());
                    }
                } else {
                    //System.out.println("no aliens in soldier radius.");
                }
            }
        }


        for (int i = 0; i < routes.size(); i++) {
            this.removeAliensFromRoute(routes.get(i), dead);
        }

        Alien.reduceNum(dead.size());
    }

    private void removeAliensFromRoute(Route route, List<Alien> deadAliens) {
        for (int j = 0; j < deadAliens.size(); j++) {
            Alien alienToRemove = deadAliens.get(j);
            int lineNumber = route.whichLine(alienToRemove.getDimension());
            route.removeAlienFromLine(alienToRemove, lineNumber);
        }
    }

    private void reduceAllWeaponsPrice() {
        for (Dimension dimension : specifiedLocations.keySet()) {
            Mappable m = specifiedLocations.get(dimension);
            if (m instanceof Weapon) {
                Weapon weapon = ((Weapon) m);
                weapon.reducePrice(0.9);
            }
        }
    }

    private void updateAchievements(List<Alien> deadAliens, String killedBy) {
        Achievement achievement = hero.getAchievement();
        if (killedBy.equalsIgnoreCase("hero")) {
            for (int i = 0; i < deadAliens.size(); i++) {
                achievement.killedHero(deadAliens.get(i));
            }
        } else if (killedBy.equalsIgnoreCase("weapon")) {
            for (int i = 0; i < deadAliens.size(); i++) {
                achievement.killedWeapon(deadAliens.get(i));
            }
        }
    }


    /***** GETTERS *******/

    void setCanUpgradeSoldiers() {
        this.canUpgradeSoldiers = true;
        Soldier soldiers[] = new Soldier[3];
        for (int i = 0; i < 3; i++) {
            if (soldiers[i] != null) {
                soldiers[i].resetRadius();
            }
        }
    }

    List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon) {
                weapons.add((Weapon) specifiedLocations.get(dimension));
            }
        }
        Collections.sort(weapons);
        return weapons;
    }

    public Map<Dimension, Mappable> getSpecifiedLocations() {
        return specifiedLocations;
    }

    List<Weapon> getWeapons(String type) {
        List<Weapon> weapons = new ArrayList<>();
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon) {
                Weapon weapon = ((Weapon) specifiedLocations.get(dimension));
                if (weapon.getName().equalsIgnoreCase(type)) {
                    weapons.add(weapon);
                }
            }
        }
        Collections.sort(weapons);
        return weapons;
    }

    public void showAvailableLocations() {
        int number = 1;
        System.out.println("Available locations are: ");
        System.out.println("-------------------------");
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) == null) {
                System.out.println(number + " - " + dimension);
            }
            number++;
        }
    }

    int randomWeather() {

        int weather = (int) (Math.random() * 6);
        System.out.println("Today's weather forcast:");
        switch (weather) {
            case 0:
                System.out.println("\t Sunny");
                updateRadiusWeather(1 / weatherConditionConstant);
                weatherConditionConstant = 1;
                break;
            case 1:
                System.out.println("\t Partly Clouldy");
                updateRadiusWeather(0.95 / weatherConditionConstant);
                weatherConditionConstant = 0.95;
                break;
            case 2:
                System.out.println("\t Rainy");
                updateRadiusWeather(0.9 / weatherConditionConstant);
                weatherConditionConstant = 0.9;
                break;
            case 3:
                System.out.println("\t Thunders expected");
                updateRadiusWeather(0.95 / weatherConditionConstant);
                weatherConditionConstant = 0.95;
                break;
            case 4:
                System.out.println("\t Hail O.o");
                updateRadiusWeather(0.8 / weatherConditionConstant);
                weatherConditionConstant = 0.8;
                break;
            case 5:
                System.out.println("\t What a Cool and pleasing day to kill Aliens");
                updateRadiusWeather(1.5 / weatherConditionConstant);
                weatherConditionConstant = 1.5;
                break;
        }

        weatherCondition = weather;
        return weather;
    }

    private void updateRadiusWeather(double a) {
        List<Weapon> weapon = getWeapons();
        double currentR;
        if (a < 0) {
            a = -a;
        }
        for (int i = 0; i < weapon.size(); i++) {
            currentR = weapon.get(i).getRadius();
            weapon.get(i).setRadius(currentR * a);
        }
        currentR = hero.getRadius();
        hero.setRadius(currentR);
        Soldier[] soldiers = hero.getSoldiers();
        for (int i = 0; i < soldiers.length; i++) {
            if (soldiers[i] != null) {
                currentR = soldiers[i].getRadius();
                soldiers[i].setRadius(a * currentR);
            }
        }
        if (a > 1) {
            System.out.println("\t Radius has increased by " + a + " :)");
        } else if (a == 1) {
            System.out.println("\t normal weather condition, no change in radius");
        } else {
            System.out.println("\t Radius has decreased by " + a + " :(");
        }
    }

    public int getWeather() {
        return weatherCondition;
    }

    void naturalDisater() {
        int prob = (int) (Math.random() * 3);
        if (prob == 2) {
            List<Weapon> weapon = getWeapons();
            int prob2 = (int) Math.random() * weapon.size();
            weapon.get(prob2).naturalDisasterWeapon();

        }
    }

    void superNaturalHelp() {
        if (!SuperNaturalHelp) {
            int prob = (int) (Math.random() * 3);

            if (prob == 2) {
                List<Alien> aliensToKill = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++) {
                    aliensToKill.addAll(routes.get(i).getAliens());
                }
                this.hero.addExperienceLevel(aliensToKill.size() * 5);
                this.hero.addMoney(aliensToKill.size() * 10);
                updateAchievements(aliensToKill, "hero");
                for (int i = 0; i < routes.size(); i++) {
                    this.removeAliensFromRoute(routes.get(i), aliensToKill);

                }
                SuperNaturalHelp = true;
                System.out.println("Super  Natural  Help :)))))");
            }
        }
    }

    void plague() {
        int prob = (int) (Math.random() * 10);

        if (prob == 2) {
            Soldier[] soldiers = hero.getSoldiers();
            soldiers[0] = null;
            soldiers[1] = null;
            soldiers[2] = null;
            hero.setSoldiers(soldiers);
            barrack.requestSoldier(hero.getResurrectionTime());
            barrack.requestSoldier(hero.getResurrectionTime());
            barrack.requestSoldier(hero.getResurrectionTime());
            System.out.println("Unfortunately your Soldiers died as a result of a plague epidemic :(");
        }
    }


    @Override
    public String toString() {
        String map = "\n\n**** Game Map ****\n\n\n";
        for (int i = 0; i < routes.size(); i++) {
            map = map.concat("Route #" + (i + 1) + "\n");
            map = map.concat("----------\n");
            map = map.concat("Line Equations:\n\n");
            map = map.concat(routes.get(i).toString());
        }
        return map;
    }
}

class Route {
    private Line[] lines = new Line[5];
    private List<Dimension> intersections = new ArrayList<>();
    private Map<Line, ArrayList<Alien>> alienMap = new HashMap<>();

    Route(Line[] lines, List<Dimension> intersections) {
        for (int i = 0; i < 5; i++) {
            this.lines[i] = lines[i];
        }
        for (int i = 0; i < lines.length; i++) {
            alienMap.put(lines[i], new ArrayList<>());
        }
        this.intersections.addAll(intersections);
    }

    List<Alien> moveAliensOnRoute() {
        List<Alien> reachedIntersection = new ArrayList<>();
        for (int i = 4; i >= 0; i--) {
            List<Alien> aliensToMove = alienMap.get(lines[i]);
            for (int j = 0; j < aliensToMove.size(); j++) {
                Alien currentAlienToMove = aliensToMove.get(j);
                Dimension dimensionToMove = lines[i].moveAlienOnLine(currentAlienToMove);
                if (dimensionToMove == null) { //has reached the end of current line and the start of next line
                    //System.out.println(currentAlienToMove.getName() + " has reached end of line " + i);
                    dimensionToMove = lines[i].getEndPoint();
                    currentAlienToMove.move(dimensionToMove);

                    alienMap.get(lines[i]).remove(currentAlienToMove);

                    if (intersections.contains(dimensionToMove)) {
                        //System.out.println(currentAlienToMove.getName() + " has reached intersection that is also end of line");
                        reachedIntersection.add(currentAlienToMove);
                    } else {
                        alienMap.get(lines[i + 1]).add(currentAlienToMove);
                    }
                } else {
                    currentAlienToMove.move(dimensionToMove);
                    if (intersections.contains(dimensionToMove)) {
                        //System.out.println(currentAlienToMove.getName() + " has reached intersection");
                        alienMap.get(lines[i]).remove(currentAlienToMove);
                        reachedIntersection.add(currentAlienToMove);
                    }
                }
            }
        }
        return reachedIntersection;
    }

    void addAlienToRoute(Alien alien, int lineNumber) {
        alien.move(lines[lineNumber].getStartPoint());
        alienMap.get(lines[lineNumber]).add(alien);
    }

    void removeAlienFromLine(Alien alien, int lineNumber) {
        if (lineNumber >= 0) {
            alienMap.get(lines[lineNumber]).remove(alien);
        }
    }

    int whichLine(Dimension dimension) {
        for (int i = 0; i < 5; i++) {
            if (lines[i].isOnLine(dimension)) {
                return i;
            }
        }
        return -1;
    }

    List<Alien> aliensWithinRadius(Shooter shooter) {
        List<Alien> toShoot = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            List<Alien> checking = alienMap.get(lines[i]); //get the aliens of each line
            for (int j = 0; j < checking.size(); j++) {
                Alien a = checking.get(j);
                if (shooter.isWithinRadius(a.getDimension())) {
                    toShoot.add(a);
                }
            }
        }
        return toShoot;
    }

    List<Alien> getAliens() {
        List<Alien> aliens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            aliens.addAll(alienMap.get(lines[i]));
        }
        return aliens;
    }

    public Line[] getLines() {
        return lines;
    }

    @Override
    public String toString() {
        String description = "";
        for (int i = 0; i < 5; i++) {
            description = description.concat("Line #" + (i + 1) + "\n");
            description = description.concat("*********\n");
            description = description.concat(lines[i].toString());
        }
        return description;
    }
}

class Line {
    private double slope;
    private double intercept;
    private Dimension startPoint;
    private Dimension endPoint;

    Line(double slope, double intercept, Dimension startPoint, Dimension endPoint) {
        this.slope = slope;
        this.intercept = intercept;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    Dimension moveAlienOnLine(Alien alien) {
        double currentX = alien.getDimension().getX();

        double newX = currentX + GameMap.UNIT * alien.getSpeed();
        double newY = slope * newX + intercept;

        if (newX < endPoint.getX()) {
            return new Dimension(newX, newY);
        }
        return null;
    }

    boolean isOnLine(Dimension dimension) {
        double xToCheck = dimension.getX();
        double yToCheck = dimension.getY();
        if (Double.compare(xToCheck, startPoint.getX()) > 0 && Double.compare(xToCheck, endPoint.getX()) < 0) {
            if (xToCheck * slope + intercept == yToCheck) {
                return true;
            }
        }
        return dimension.equals(startPoint);
    }

    Dimension getStartPoint() {
        return startPoint;
    }

    Dimension getEndPoint() {
        return endPoint;
    }

    @Override
    public String toString() {
        String equation = "";

        if (slope == 0) {
            equation = "y = " + intercept;
        } else {
            if (slope == 1) {
                equation = equation.concat("y = x");
            } else if (slope == -1) {
                equation = equation.concat("y = -x");
            } else {
                equation = equation.concat("y = " + slope + "x");
            }

            if (intercept > 0) {
                equation = equation.concat(" + " + intercept);
            } else if (intercept < 0) {
                equation = equation.concat(" - " + (-1 * intercept));
            }
        }
        return "Start Point: " + startPoint + "\n" +
                "End Point: " + endPoint + "\n" +
                "Equation: " + equation + "\n\n";
    }
}

class Wormhole {
    private int leadsTo;
    private Dimension dimension;
    private double radius;

    Wormhole(int leadsTo, Dimension dimension) {
        this.leadsTo = leadsTo;
        this.dimension = dimension;
        radius = 1;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    int getLeadsTo() {
        return leadsTo;
    }

    boolean isWithinRadius(Dimension otherDim) {
        return otherDim.distanceFrom(dimension) <= radius * GameMap.UNIT;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
