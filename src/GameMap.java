import java.util.*;

public class GameMap {
    public static double XBOUND = 800;
    public static double YBOUND = 600; //we have to initialize both of these right here
    public static int UNIT = 15;

    private List<Route> routes = new ArrayList<>();
    private List<Wormhole> wormholes = new ArrayList<>();
    private Map<Dimension, Mappable> specifiedLocations = new HashMap<>();
    private Map<Integer, Dimension> specifiedNumbers = new HashMap<>();
    private Barrack barrack;

    private Dimension flag;
    private Alien[] reachedFlag = new Alien[5];

    private List<Alien> activeAliens = new ArrayList<>(); //not sure if this is necessary
    private Hero hero;

    private boolean heroIsDead = false;
    private int secondsLeftToResurrectHero = 0;

    private boolean canUpgradeSoldiers = true;

    public GameMap(Hero hero) {
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


        dimension = new Dimension(73, 75);
        specifiedLocations.put(dimension, null);
        specifiedNumbers.put(1, dimension);

        dimension = new Dimension(240, 145);
        specifiedNumbers.put(2, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(385, 240);
        specifiedNumbers.put(3, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(450, 295);
        specifiedNumbers.put(4, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(450, 305);
        specifiedNumbers.put(5, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(445, 300);
        specifiedNumbers.put(6, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(455, 300);
        specifiedNumbers.put(7, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(210, 455);
        specifiedNumbers.put(8, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(522, 375);
        specifiedNumbers.put(9, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(600, 148);
        specifiedNumbers.put(10, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(710, 240);
        specifiedNumbers.put(11, dimension);
        specifiedLocations.put(dimension, null);

        dimension = new Dimension(710, 360);
        specifiedNumbers.put(12, dimension);
        specifiedLocations.put(dimension, null);

        List<Dimension> wormholeDims = Dimension.randomDimension(6);
        wormholes.add(new Wormhole(1, wormholeDims.get(0)));
        wormholes.add(new Wormhole(0, wormholeDims.get(1)));
        wormholes.add(new Wormhole(3, wormholeDims.get(2)));
        wormholes.add(new Wormhole(2, wormholeDims.get(3)));
        wormholes.add(new Wormhole(5, wormholeDims.get(4)));
        wormholes.add(new Wormhole(4, wormholeDims.get(5)));
    }

    public boolean nextSecond(){
        /*if (AlienCreeps.getCurrentHour() == 20 && AlienCreeps.getCurrentSecond() == 0){
            reduceRadius();
        }
        if (AlienCreeps.getCurrentSecond() == 0 && AlienCreeps.getCurrentHour() == 4){
            resetRadius();
        }
        if (AlienCreeps.getCurrentHour() == 0 && AlienCreeps.getCurrentSecond() == 0){
            canUpgradeSoldiers = true;
            Soldier soldiers[] = new Soldier[3];
            for (int i = 0; i < 3; i++){
                if (soldiers[i] != null){
                    soldiers[i].resetRadious();
                }
            }
        }

        randomWormhole();

        updateTeslaStatus();
        if (this.heroIsDead){
            this.secondsLeftToResurrectHero--;
            if (this.secondsLeftToResurrectHero == 0){
                this.heroIsDead = false;
                this.hero.setEnergy(300);
            }
        }*/
        barrack.proceed();
        Soldier soldier = barrack.getSoldier();
        barrack.removeSoldier();
        if (soldier != null){
            for (int i = 0; i < 3; i++){
                if (hero.getSoldiers()[i] == null){
                    Dimension soldierDimension = hero.getDimension().add(hero.getSoldierDims()[i]);
                    soldier.setDimension(soldierDimension);
                    hero.getSoldiers()[i] = soldier;
                    System.out.println("BARRACK MADE NEW SOLDIER");
                    System.out.println("welcome soldier " + i);
                    break;
                }
            }
        }
        /*if ((int)(Math.random() * 10) == 1){
            List<Dimension> wormholeDims = Dimension.randomDimension(6);
            for (int i = 0; i < 6; i++){
                wormholes.get(i).setDimension(wormholeDims.get(i));
            }
        }
        if (AlienCreeps.getCurrentHour() <= 16 && AlienCreeps.getCurrentHour() >= 10){
            generateAliens(4);
        }else{
            generateAliens(8);
        }
        if (moveAliens()) {
            return true;
        }
        shootAliens();
        flyingAliensAttack();
        if (Alien.isSTART()){
            if (Alien.getNUM() <= 0){
                System.out.println("CONGRATULATIONS! YOU WON :D");
                return true;
            }
        }*/
        return false;
    }

    public void reduceRadius(){
        for (int i = 0; i < 3; i++){
            Soldier s = hero.getSoldiers()[i];
            if (s != null){
                s.reduceRadius();
            }
        }
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon){
                Weapon w = ((Weapon) specifiedLocations.get(dimension));
                w.reduceRadius();
            }
        }
    }

    public void resetRadius(){
        for (int i = 0; i < 3; i++){
            Soldier s = hero.getSoldiers()[i];
            if (s != null){
                s.resetRadious();
            }
        }
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon){
                Weapon w = ((Weapon) specifiedLocations.get(dimension));
                w.resetRadius();
            }
        }
    }

    public void upgradeSoldier(){
        if (canUpgradeSoldiers){
            Soldier soldiers[] = new Soldier[3];
            int num = 0;
            for (int i = 0; i < 3; i++){
                if (soldiers[i] != null){
                    num++;
                }
            }
            if (hero.getMoney() >= num * 10){
                hero.reduceMoney(num * 10);
                for (int i = 0; i < 3; i++){
                    if (soldiers[i] != null){
                        soldiers[i].increaseRadius();
                    }
                }
            }else{
                System.out.println("Not enough money.");
            }
        }else{
            System.out.println("Can't upgrade soldiers twice in one day. Try again tomorrow.");
        }
    }

    public void randomWormhole(){
        if ((int)Math.random() == 10){
            List<Dimension> wormholeDims = Dimension.randomDimension(6);
            for (int i = 0; i < 6; i++){
                wormholes.get(i).setDimension(wormholeDims.get(i));
            }
            System.out.println("New Wormhole Dimensions are:");
            wormholeDims.forEach(System.out::println);
        }

    }

    public void showRemainingAliens(){
        ArrayList<Alien> remainingAliens = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++){
            remainingAliens.addAll(routes.get(i).getAliens());
        }
        Collections.sort(remainingAliens);
        remainingAliens.forEach(System.out::println);
    }

    public void putWeaponInPlace(String weaponName, int whichPlace){
        if (whichPlace > specifiedLocations.keySet().size()){
            System.out.println("There are only " + specifiedLocations.keySet().size() + " available places.");
            return;
        }
        Dimension dimension = specifiedNumbers.get(whichPlace);
        if (specifiedLocations.get(dimension) == null){
            if (weaponName.equalsIgnoreCase("Barrack")){
                if (this.barrack == null){
                    if (hero.getMoney() >= 90){
                        this.barrack = new Barrack(dimension);
                        this.hero.reduceMoney(90);
                        barrack.requestSoldier(hero.getResurrectionTime());
                        barrack.requestSoldier(hero.getResurrectionTime());
                        barrack.requestSoldier(hero.getResurrectionTime());
                    }
                }else{
                    System.out.println("You already have a barrack.");
                }
            }else{
                Weapon bought = this.hero.buyWeapon(weaponName, dimension);
                specifiedLocations.put(dimension, bought);
            }
        }else{
            System.out.println("There is already a weapon in this location.");
        }
    }

    public void upgradeWeaponInPlace(String weaponName, int whichPlace){
        if (whichPlace > specifiedLocations.keySet().size()){
            System.out.println("There are only " + specifiedLocations.keySet().size() + " available places.");
            return;
        }
        Dimension dimension = specifiedNumbers.get(whichPlace);
        if (specifiedLocations.get(dimension) != null){
            if (!weaponName.equalsIgnoreCase("Barrack")){
                Weapon toUpgrade = ((Weapon) specifiedLocations.get(dimension));
                if (toUpgrade.getName().equalsIgnoreCase(weaponName)){
                    if (!hero.upgradeWeapon(toUpgrade)){
                        System.out.println("Not enough money.");
                    }else{
                        System.out.println("Upgraded successfully");
                    }
                }else{
                    System.out.println("Incorrect name");
                }
            }else{
                System.out.println("Can't upgrade barrack.");
            }
        }else{
            System.out.println("There is no weapon in this place");
        }
    }

    public void generateAliens(int probability){
        if (Alien.getNUM() < Alien.getMAXNUM()){
            if ((int)(Math.random() * probability) == 0){
                int routeNumber = chooseRandomRoute();
                Route whichRoute = routes.get(routeNumber);
                int whichAlien = (int)(Math.random() * 4);
                switch (whichAlien){
                    case 0:
                        //System.out.println("Adding Albertonion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Albertonion"), 0);
                        break;
                    case 1:
                       // System.out.println("Adding Algwasonion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Algwasonion"), 0);
                        break;
                    case 2:
                      //  System.out.println("Adding Activinion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Activionion"), 0);
                        break;
                    case 3:
                     //   System.out.println("Adding Aironion to route " + routeNumber);
                        whichRoute.addAlienToRoute(new Alien("Aironion"), 0);
                        break;
                }
            }
        }
    }

    public int chooseRandomRoute(){
        return (int) (Math.random() * routes.size());
    }

    public void showReachedFlag(){
        int num = 0;
        ArrayList<String> names = new ArrayList<>();
        for (Alien alien : reachedFlag) {
            if (alien != null){
                num++;
                names.add(alien.getName());
            }
        }
        System.out.println(num + " aliens have reached flag.");
        names.forEach(System.out::println);
    }

    public boolean reachFlag(Alien alien){
        //System.out.println(alien.getName() + " reached flag.");
        for (int i = 0; i < 5; i++){
            if (reachedFlag[i] == null){
                reachedFlag[i] = alien;
               // System.out.println((i + 1) + " aliens have reached flag.");
                if (i == 4){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public boolean gameStatus(){
        int numReached = 0;
        for (int i = 0; i < 5; i++){
            if (reachedFlag[i] != null){
                numReached++;
            }
        }
        return numReached >= 5;
    }

    public boolean moveAliens(){
        for (int i = 0; i < routes.size(); i++){
            List<Alien> reachedIntersectionOrFlag = routes.get(i).moveAliensOnRoute();
            for (int j = 0; j < reachedIntersectionOrFlag.size(); j++){
                Alien alien = reachedIntersectionOrFlag.get(j);
                if (alien.getDimension().equals(flag)){
                    return reachFlag(alien);
                }
                int randomNumber = chooseRandomRoute();
                System.out.println(alien.getName() + " was relocated to route number " + randomNumber);
                Route whichRoute = routes.get(randomNumber);
                int whichLine = whichRoute.whichLine(alien.getDimension());
                whichRoute.addAlienToRoute(alien, whichLine);
            }
        }
        backToNormalSpeed();
        return false;
    }

    public void backToNormalSpeed(){
        List<Alien> allAliens = new ArrayList<>();
        List<Alien> reducedSpeed = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++){
            allAliens.addAll(routes.get(i).getAliens());
            for (Dimension dimension : specifiedLocations.keySet()) {
                Mappable m = specifiedLocations.get(dimension);
                if (m instanceof Weapon){
                    Weapon weapon = ((Weapon) m);
                    reducedSpeed.addAll(routes.get(i).aliensWithinRadius(weapon));
                }
            }
        }
        allAliens.removeAll(reducedSpeed);
        for (int i = 0; i < allAliens.size(); i++){
            allAliens.get(i).backToNormalSpeed();
        }
    }

    public void moveHero(Dimension change){ /*** add bounds for hero. ***/
        //System.out.println(" before"+ hero);
        if (this.heroIsDead){
            System.out.println("Hero is dead :( Can't move hero.");
        }else{
            if (this.hero.move(change)) {
                Dimension newDim = hero.getDimension();
                for (int i = 0; i < this.wormholes.size(); i++){
                    if (wormholes.get(i).isWithinRadius(newDim)){
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
            }
        }
    }

    public void useTesla(Dimension dimension){
        if (Weapon.NUM_USED_TESLA < 2){
            if (!Weapon.TESLA_IN_USE){
                Weapon tesla = Weapon.WeaponFactory(dimension, "Tesla");
                List<Alien> aliensToKill = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++){
                    aliensToKill.addAll(routes.get(i).aliensWithinRadius(tesla));
                }
                if (this.hero.addExperienceLevel(aliensToKill.size() * 5)){
                    reduceAllWeaponsPrice();
                }
                this.hero.addMoney(aliensToKill.size() * 10);
                updateAchivements(aliensToKill, "weapon");
                for (int i = 0; i < routes.size(); i++){
                    this.removeAliensFromRoute(routes.get(i), aliensToKill);
                }
            }else{
                System.out.println("You must wait " + Weapon.SECONDS_LEFT_TO_USE_TESLA + " more seconds.");
            }
        }else{
            System.out.println("Can not use Tesla anymore.");
        }

    }

    public void updateTeslaStatus(){
        if (Weapon.TESLA_IN_USE){
            Weapon.SECONDS_LEFT_TO_USE_TESLA--;
            if (Weapon.SECONDS_LEFT_TO_USE_TESLA == 0){
                Weapon.TESLA_IN_USE = false;
            }
        }
    }

    public void shootAliens(){
        shootAliensByHeroAndSoldiers();
        shootAliensByWeapons();
    }

/*    public void flyingAliensAttack(){
        List<Alien> flying = new ArrayList<>();
        for (int i = 0;i < routes.size(); i++){
            List<Alien> onRoute = routes.get(i).getAliens();
            for (int j = 0;j < onRoute.size(); j++){
                if (onRoute.get(j).isCanFly()){
                    flying.add(onRoute.get(j));
                }
            }
        }

        for (int i = 0; i < flying.size(); i++){
            Alien flyingAlien = flying.get(i);
            Dimension alienDim = flyingAlien.getDimension();
            Warrior min = null;
            double minDistance = alienDim.distanceFrom(hero.getDimension());

            if (flyingAlien.isWithinRadius(this.hero.getDimension())){
                min = hero;
            }

            for (int j = 0; j < 3; j++){
                Soldier s = this.hero.getSoldiers()[i];
                if (flyingAlien.isWithinRadius(s.getDimension())){
                    if(alienDim.distanceFrom(s.getDimension()) < minDistance){
                        minDistance = alienDim.distanceFrom(s.getDimension());
                        min = s;
                    }
                }
            }

            if (min != null){
                flyingAlien.shoot(min);
            }
        }
    }*/

    public void shootAliensByWeapons(){
        for (Dimension dimension : specifiedLocations.keySet()){
            Mappable m = specifiedLocations.get(dimension);
            if (m instanceof Weapon){
                Weapon weapon = ((Weapon) m);
                List<Alien> aliensToShoot = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++){
                    aliensToShoot.addAll(routes.get(i).aliensWithinRadius(weapon));
                }
                List<Alien> deadAliens = weapon.shoot(aliensToShoot);
                if (deadAliens != null){
                    aliensToShoot.removeAll(deadAliens);
                    if (this.hero.addExperienceLevel(deadAliens.size() * 5)) {
                        reduceAllWeaponsPrice();
                    }
                    this.hero.addMoney(deadAliens.size() * 10);
                    updateAchivements(deadAliens, "weapon");
                    for (int i = 0; i < routes.size(); i++)
                        this.removeAliensFromRoute(routes.get(i), deadAliens);
                }
            }
        }
        //backToNormalSpeed();
    }

    public void shootAliensByHeroAndSoldiers(){
        List<Alien> killedByHero = new ArrayList<>();
        List<Alien> killedBySoldiers = new ArrayList<>();

        if (!this.hero.isDead()){
            List<Alien> aliensToShootByHero = new ArrayList<>();
            for (int i = 0; i < routes.size(); i++){
                aliensToShootByHero.addAll(routes.get(i).aliensWithinRadius(this.hero));
            }

            killedByHero.addAll(this.hero.shoot(aliensToShootByHero));
            if (this.hero.addExperienceLevel(killedByHero.size() * 15)) {
                reduceAllWeaponsPrice();
            }
            this.hero.addMoney(killedByHero.size() * 10);
            updateAchivements(killedByHero, "hero");
            if (this.hero.isDead()){
                this.heroIsDead = true;
                this.secondsLeftToResurrectHero = this.hero.getResurrectionTime();
            }
        }

        Soldier soldiers[] = this.hero.getSoldiers();
        for (int j = 0; j < 3; j++){
            if (soldiers[j] != null){
                List<Alien> aliensToShootBySoldier = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++){
                    aliensToShootBySoldier.addAll(routes.get(i).aliensWithinRadius(soldiers[j]));
                }
                killedBySoldiers.addAll(soldiers[j].shoot(aliensToShootBySoldier));
                if (soldiers[j].isDead()){
                    soldiers[j] = null;
                    barrack.requestSoldier(this.hero.getResurrectionTime());
                }
            }
        }
        this.hero.addMoney(killedBySoldiers.size() * 10);
        if (this.hero.addExperienceLevel(killedBySoldiers.size() * 5)) {
            reduceAllWeaponsPrice();
        }

        for (int i = 0; i < routes.size(); i++){
            this.removeAliensFromRoute(routes.get(i), killedByHero);
            this.removeAliensFromRoute(routes.get(i), killedBySoldiers);
        }
    }

    public void removeAliensFromRoute(Route route, List<Alien> deadAliens){
        for (int j = 0; j < deadAliens.size(); j++){
            Alien alienToRemove = deadAliens.get(j);
            int lineNumber = route.whichLine(alienToRemove.getDimension());
            route.removeAlienFromLine(alienToRemove, lineNumber);
        }
    }

    public void reduceAllWeaponsPrice(){
        for (Dimension dimension : specifiedLocations.keySet()) {
            Mappable m = specifiedLocations.get(dimension);
            if (m instanceof Weapon){
                Weapon weapon = ((Weapon) m);
                weapon.reducePrice(0.9);
            }
        }
    }

    public void updateAchivements(List<Alien> deadAliens, String killedBy){
        Achievement achievement = hero.getAchievement();
        if (killedBy.equalsIgnoreCase("hero")){
            for (int i = 0; i < deadAliens.size(); i++){
                achievement.killedHero(deadAliens.get(i));
            }
        }else if (killedBy.equalsIgnoreCase("weapon")){
            for (int i = 0; i < deadAliens.size(); i++){
                achievement.killedWeapon(deadAliens.get(i));
            }
        }
    }


    /***** GETTERS *******/

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Wormhole> getWormholes() {
        return wormholes;
    }

    public Map<Dimension, Mappable> getSpecifiedLocations() {
        return specifiedLocations;
    }

    public Dimension getFlag() {
        return flag;
    }

    public Alien[] getReachedFlag() {
        return reachedFlag;
    }

    public List<Alien> getActiveAliens() {
        return activeAliens;
    }

    public Hero getHero() {
        return hero;
    }

    public Barrack getBarrack() {
        return barrack;
    }

    public List<Weapon> getWeapons(){
        List<Weapon> weapons = new ArrayList<>();
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon){
                weapons.add((Weapon) specifiedLocations.get(dimension));
            }
        }
        Collections.sort(weapons);
        return weapons;
    }

    public List<Weapon> getWeapons(String type){
        List<Weapon> weapons = new ArrayList<>();
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon){
                Weapon weapon = ((Weapon) specifiedLocations.get(dimension));
                if (weapon.getName().equalsIgnoreCase(type)){
                    weapons.add(weapon);
                }
            }
        }
        Collections.sort(weapons);
        return weapons;
    }

    public void showAvailableLocations(){
        int number = 1;
        System.out.println("Available locations are: ");
        System.out.println("-------------------------");
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) == null){
                System.out.println(number + " - " + dimension);
            }
            number++;
        }
    }

    @Override
    public String toString() {
        String map = "\n\n**** Game Map ****\n\n\n";
       // System.out.println("to string");
        for (int i = 0; i < routes.size(); i++){
            map = map.concat("Route #" + (i + 1) + "\n");
            map = map.concat("----------\n");
            map = map.concat("Line Equations:\n\n");
            map = map.concat(routes.get(i).toString());
        }
        return map;
    }
}

class Route{
    private Line[] lines = new Line[5];
    private List<Dimension> intersections = new ArrayList<>();
    Map<Line, ArrayList<Alien>> alienMap = new HashMap<>();

    public Route(Line[] lines, List<Dimension> intersections) {
        for (int i = 0; i < 5; i++){
            this.lines[i] = lines[i];
        }
        for (int i = 0; i < lines.length; i++){
            alienMap.put(lines[i], new ArrayList<>());
        }
        this.intersections.addAll(intersections);
    }

    public List<Alien> moveAliensOnRoute(){
        List<Alien> reachedIntersection = new ArrayList<>();
        for (int i = 4; i >= 0; i--){
            List<Alien> aliensToMove = alienMap.get(lines[i]);
            for (int j = 0; j < aliensToMove.size(); j++){
                Alien currentAlienToMove = aliensToMove.get(j);
                Dimension dimensionToMove = lines[i].moveAlienOnLine(currentAlienToMove);
                if (dimensionToMove == null){ //has reached the end of current line and the start of next line
                    //System.out.println(currentAlienToMove.getName() + " has reached end of line " + i);
                    dimensionToMove = lines[i].getEndPoint();
                    currentAlienToMove.move(dimensionToMove);

                    alienMap.get(lines[i]).remove(currentAlienToMove);

                    if (intersections.contains(dimensionToMove)){
                        //System.out.println(currentAlienToMove.getName() + " has reached intersection that is also end of line");
                        reachedIntersection.add(currentAlienToMove);
                    }else{
                        alienMap.get(lines[i + 1]).add(currentAlienToMove);
                    }
                }else{
                    currentAlienToMove.move(dimensionToMove);
                    if (intersections.contains(dimensionToMove)){
                        //System.out.println(currentAlienToMove.getName() + " has reached intersection");
                        alienMap.get(lines[i]).remove(currentAlienToMove);
                        reachedIntersection.add(currentAlienToMove);
                    }
                }
            }
        }
        return reachedIntersection;
    }

    public void addAlienToRoute(Alien alien, int lineNumber){
        alien.move(lines[lineNumber].getStartPoint());
        alienMap.get(lines[lineNumber]).add(alien);
    }

    public void removeAlienFromLine(Alien alien, int lineNumber){
        if (lineNumber >= 0){
            alienMap.get(lines[lineNumber]).remove(alien);
        }
    }

    public int whichLine(Dimension dimension){
        for (int i = 0; i < 5; i++){
            if (lines[i].isOnLine(dimension)){
                return i;
            }
        }
        return -1;
    }

    public List<Alien> aliensWithinRadius(Shooter shooter){
        List<Alien> toShoot = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            List<Alien> checking = alienMap.get(lines[i]); //get the aliens of each line
            for (int j = 0; j < checking.size(); j++){
                Alien a = checking.get(j);
                if (shooter.isWithinRadius(a.getDimension())){
                    toShoot.add(a);
                }
            }
        }
        return toShoot;
    }

    public List<Alien> getAliens(){
        List<Alien> aliens = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            aliens.addAll(alienMap.get(lines[i]));
        }
        return aliens;
    }

    @Override
    public String toString() {
        String description = "";
        for (int i = 0; i < 5; i++){
            description = description.concat("Line #" + (i + 1) + "\n");
            description = description.concat("*********\n");
            description = description.concat(lines[i].toString());
        }
        return description;
    }
}

class Line{
    private double slope;
    private double intercept;
    private Dimension startPoint;
    private Dimension endPoint;

    public Line(double slope, double intercept, Dimension startPoint, Dimension endPoint) {
        this.slope = slope;
        this.intercept = intercept;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Dimension moveAlienOnLine(Alien alien){
        double currentX = alien.getDimension().getX();

        double newX = currentX + GameMap.UNIT * alien.getSpeed();
        double newY = slope * newX + intercept;

        if (newX < endPoint.getX()){
            return new Dimension(newX, newY);
        }
        return null;
    }

    public boolean isOnLine(Dimension dimension){
        double xToCheck = dimension.getX();
        double yToCheck = dimension.getY();
        if (Double.compare(xToCheck, startPoint.getX()) > 0 && Double.compare(xToCheck, endPoint.getX()) < 0){
            if (xToCheck * slope + intercept == yToCheck){
                return true;
            }
        }
        return dimension.equals(startPoint);
    }

    public Dimension getStartPoint() {
        return startPoint;
    }

    public Dimension getEndPoint() {
        return endPoint;
    }

    @Override
    public String toString() {
        String equation = "";

        if (slope == 0){
            equation = "y = " + intercept;
        }else{
            if (slope == 1){
                equation = equation.concat("y = x");
            }else if (slope == -1){
                equation = equation.concat("y = -x");
            }else{
                equation = equation.concat("y = " + slope + "x");
            }

            if (intercept > 0){
                equation = equation.concat(" + " + intercept);
            }else if (intercept < 0){
                equation = equation.concat(" - " + (-1 * intercept));
            }
        }
        /*else if(intercept == 0){
            if (slope == 1){
                equation = "y = x";
            }else if (slope == -1){
                equation = "y = -x";
            }else {
                equation = "y = " + slope + "x";
            }
        }else{
            if (intercept > 0){
                equation = "y = " + slope + "x + " + intercept;
            }else{
                equation = "y = " + slope + "x - " + (-1 * intercept);
            }
        }*/
        return "Start Point: " + startPoint + "\n" +
                "End Point: " + endPoint + "\n" +
                "Equation: " + equation + "\n\n";
    }
}

class Wormhole {
    private int leadsTo;
    private Dimension dimension;
    private double radius;

    public Wormhole(int leadsTo, Dimension dimension) {
        this.leadsTo = leadsTo;
        this.dimension = dimension;
        radius = 1;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public int getLeadsTo() {
        return leadsTo;
    }

    public boolean isWithinRadius(Dimension otherDim){
        return otherDim.distanceFrom(dimension) <= radius * GameMap.UNIT;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
