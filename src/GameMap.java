import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    public static double XBOUND;
    public static double YBOUND; //we have to initialize both of these right here

    private List<Route> routes = new ArrayList<>();
    private List<Wormhole> wormholes = new ArrayList<>();
    private Map<Dimension, Mappable> specifiedLocations = new HashMap<>();
    private Barrack barrack;

    private Dimension flag;
    private Alien[] reachedFlag = new Alien[5];

    private List<Alien> activeAliens = new ArrayList<>(); //not sure if this is necessary
    private Hero hero;

    private boolean heroIsDead = false;
    private int secondsLeftToResurrectHero = 0;

    /*public GameMap(List<Route> routes,
                   List<Wormhole> Wormholes,
                   Map<Dimension, Mappable> specifiedLocations,
                   Dimension flag) {
        this.routes = routes;
        this.wormholes = Wormholes;
        this.specifiedLocations = specifiedLocations;
        this.flag = flag;
    }*/

    public GameMap(Hero hero) {
        this.hero = hero;
        //code for initializing the routes and the line equations.
    }

    public void putWeaponInPlace(String weaponName, int whichPlace){
        int i = 0;
        for (Dimension dimension : specifiedLocations.keySet()) {
            i++;
            if (i == whichPlace){
                if (specifiedLocations.get(dimension) == null){
                    Weapon bought = this.hero.buyWeapon(weaponName, dimension);
                    if (bought != null){
                        specifiedLocations.put(dimension, bought);
                    }else{
                        System.out.println("not enough money.");
                    }
                }else{
                    System.out.println("there is already a weapon in this location.");
                }
                break;
            }
        }
    }

    public void upgradeWeaponInPlace(String weaponName, int whichPlace){
        int i = 0;
        for (Dimension dimension : specifiedLocations.keySet()) {
            i++;
            if (i == whichPlace){
                if (specifiedLocations.get(dimension) != null){
                    Weapon toUpgrade = ((Weapon) specifiedLocations.get(dimension));
                    if (toUpgrade.getName().equalsIgnoreCase(weaponName)){
                        if (!hero.upgradeWeapon(toUpgrade)) {
                            System.out.println("not enough money");
                        }else{
                            System.out.println("upgraded successfully");
                        }
                    }else{
                        System.out.println("incorrect name.");
                    }
                }else{
                    System.out.println("there is no weapon in this place.");
                }
            }
        }
    }

    public void generateAliens(){
        if ((int)(Math.random() * 3) == 0){ //generate alien with probability 1/3 for now. when adding clock, this will change
            Route whichRoute = routes.get(chooseRandomRoute());
            int whichAlien = (int)(Math.random() * 4);
            switch (whichAlien){
                case 0:
                    whichRoute.addAlienToRoute(new Alien("Albertonion"), 0);
                    break;
                case 1:
                    whichRoute.addAlienToRoute(new Alien("Algwasonion"), 0);
                    break;
                case 2:
                    whichRoute.addAlienToRoute(new Alien("Activionion"), 0);
                    break;
                case 3:
                    whichRoute.addAlienToRoute(new Alien("Aironion"), 0);
                    break;
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

    public void reachFlag(Alien alien){
        for (int i = 0; i < 5; i++){
            if (reachedFlag[i] == null){
                reachedFlag[i] = alien;
                if (i == 4){
                    System.out.println("Game Over");
                }
                break;
            }
        }
    }

    public void moveAliens(){
        for (int i = 0; i < routes.size(); i++){
            List<Alien> reachedIntersectionOrFlag = routes.get(i).moveAliensOnRoute();
            for (int j = 0; i < reachedIntersectionOrFlag.size(); i++){
                Alien alien = reachedIntersectionOrFlag.get(j);
                if (alien.getDimension().equals(flag)){
                    reachFlag(alien);
                    return;
                }
                Route whichRoute = routes.get(chooseRandomRoute());
                int whichLine = whichRoute.whichLine(alien.getDimension());
                whichRoute.addAlienToRoute(alien, whichLine);
            }
        }
        backToNormalSpeed();
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

    public void moveHero(Dimension change){
        this.hero.move(change);
        Dimension newDim = hero.getDimension();
        Wormhole in = null;
        for (int i = 0; i < this.wormholes.size(); i++){
            if (wormholes.get(i).getDimension().equals(newDim)){
                in = wormholes.get(i);
                break;
            }
        }
        if (in != null){
            Wormhole out = wormholes.get(in.getLeadsTo());
            Dimension newChange = new Dimension(out.getDimension().getX() - in.getDimension().getX(),
                    out.getDimension().getY() - in.getDimension().getY());
            this.hero.move(newChange);
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
        Achivement achivement = hero.getAchivement();
        if (killedBy.equalsIgnoreCase("hero")){
            for (int i = 0; i < deadAliens.size(); i++){
                achivement.killedHero(deadAliens.get(i));
            }
        }else if (killedBy.equalsIgnoreCase("weapon")){
            for (int i = 0; i < deadAliens.size(); i++){
                achivement.killedWeapon(deadAliens.get(i));
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

    public List<Weapon> getWeapons(){
        List<Weapon> weapons = new ArrayList<>();
        for (Dimension dimension : specifiedLocations.keySet()) {
            if (specifiedLocations.get(dimension) instanceof Weapon){
                weapons.add((Weapon) specifiedLocations.get(dimension));
            }
        }
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
        return weapons;
    }
}

class Route{
    private Line[] lines = new Line[5];
    private List<Dimension> intersections = new ArrayList<>();
    Map<Line, ArrayList<Alien>> alienMap = new HashMap<>();

    /**** custom constructor *****/
    public Route(Line[] lines) {
        this.lines = lines;
        for (int i = 0; i < lines.length; i++){
            alienMap.put(lines[i], new ArrayList<>());
        }
    }

    public Route(){
        // line equations
        // intersection dimension
        for (int i = 0; i < lines.length; i++){
            alienMap.put(lines[i], new ArrayList<>());
        }
    }

    public List<Alien> moveAliensOnRoute(){
        List<Alien> reachedIntersection = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            List<Alien> aliensToMove = alienMap.get(lines[i]);
            for (int j = 0; j < aliensToMove.size(); j++){
                Alien currentAlienToMove = aliensToMove.get(j);
                Dimension dimensionToMove = lines[i].moveAlienOnLine(currentAlienToMove);
                if (dimensionToMove == null){ //has reached the end of current line and the start of next line
                    currentAlienToMove.setDimension(lines[i + 1].getStartPoint());
                    alienMap.get(lines[i + 1]).add(currentAlienToMove);
                }else{
                    if (intersections.contains(dimensionToMove)){
                        currentAlienToMove.move(dimensionToMove);
                        alienMap.get(lines[i]).remove(currentAlienToMove);
                        reachedIntersection.add(currentAlienToMove);
                    }
                }
            }
        }
        return reachedIntersection;
    }

    /**** when we generate an alien in the GameMap class, we randomly select a route to add it to. afterwards,
     * we pass that alien to this function of the chosen route. ****/
    public void addAlienToRoute(Alien alien, int lineNumber){
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
        List<Integer> linesWithinRadius = new ArrayList<>();
        List<Alien> aliensToShoot = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Dimension nearestPoint = lines[i].getNearestPoint(shooter.getShootingPoint());
            if (shooter.isWithinRadius(nearestPoint)){
                linesWithinRadius.add(i);
            }
        }
        for (Integer withinRadius : linesWithinRadius) {
            List<Alien> aliensToCheck = alienMap.get(lines[withinRadius]);
            for (int i = 0; i < aliensToCheck.size(); i++){
                Alien currentAlienToCheck = aliensToCheck.get(i);
                if (shooter.isWithinRadius(currentAlienToCheck.getDimension())){
                    aliensToShoot.add(currentAlienToCheck);
                }
            }
        }
        return aliensToShoot;
    }

    public List<Alien> getAliens(){
        List<Alien> aliens = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            aliens.addAll(alienMap.get(lines[i]));
        }
        return aliens;
    }
}

class Line{
    private double slope;
    private double intercept;
    private Dimension startPoint;
    private Dimension endPoint;

    public static double UNIT; //we change the x of each movable by this amount, we will finalize this.

    public Line(double slope, double intercept, Dimension startPoint, Dimension endPoint) {
        this.slope = slope;
        this.intercept = intercept;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Line(double slope, double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    public Dimension moveAlienOnLine(Alien alien){
        double currentX = alien.getDimension().getX();
        double currentY = alien.getDimension().getY();

        double newX = currentX + UNIT * alien.getSpeed();
        double newY = slope * newX + intercept;

        if (newX < endPoint.getX()){
            return new Dimension(newX, newY);
        }
        return null;
    }

    /*** mathematical line calculations ****/

    public boolean isOnLine(Dimension dimension){
        double xToCheck = dimension.getX();
        double yToCheck = dimension.getY();

        if (xToCheck >= startPoint.getX() && xToCheck <= endPoint.getX()){
            if (xToCheck * slope + intercept == yToCheck){
                return true;
            }
        }
        return false;
    }

    public Line getPerpendicularToLine(Dimension point){
        double slope = (double)-1 / this.slope;
        double intercept = (double)point.getX() * -1 * slope + (double)point.getY();
        return new Line(slope, intercept);
    }

    public Dimension getIntersectionWithLine(Line line){
        double x = (this.intercept - line.getIntercept()) / (line.getSlope() - this.slope);
        double y = x * slope + intercept;
        return new Dimension(x, y);
    }

    public Dimension getNearestPoint(Dimension dimension){
        Line perpendicularToThis = this.getPerpendicularToLine(dimension);
        Dimension intersection = this.getIntersectionWithLine(perpendicularToThis);
        if (this.isOnLine(intersection)){
            return intersection;
        }else if (dimension.distanceFrom(startPoint) < dimension.distanceFrom(endPoint)){
            return startPoint;
        }
        return endPoint;
    }

    /**** GETTER AND SETTER ****/

    public Dimension getStartPoint() {
        return startPoint;
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }
}

class Wormhole {
    private int leadsTo; //this equals the index of the Wormhole it points to.
    private Dimension dimension;

    public Wormhole(int leadsTo, Dimension dimension) {
        this.leadsTo = leadsTo;
        this.dimension = dimension;
    }

    public int getLeadsTo() {
        return leadsTo;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
