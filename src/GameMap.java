import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    public static double XBOUND;
    public static double YBOUND; //we have to initialize both of these right here

    private List<Route> routes = new ArrayList<>();
    private List<Wormhole> Wormholes = new ArrayList<>();
    private Map<Dimension, Mappable> specifiedLocations = new HashMap<>();

    private Dimension flag;
    private Alien[] reachedFlag = new Alien[5];

    private List<Alien> activeAliens = new ArrayList<>(); //not sure if this is necessary
    private Hero hero;

    public GameMap(List<Route> routes,
                   List<Wormhole> Wormholes,
                   Map<Dimension, Mappable> specifiedLocations,
                   Dimension flag) {
        this.routes = routes;
        this.Wormholes = Wormholes;
        this.specifiedLocations = specifiedLocations;
        this.flag = flag;
    }

    public GameMap(Hero hero) {
        this.hero = hero;
        //code for initializing the routes and the line equations.
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
    }

    public int chooseRandomRoute(){
        return (int) (Math.random() * routes.size());
    }
    
    public void moveHero(Dimension a){}

    public void shootAliensByWeapons(){
        for (Dimension dimension : specifiedLocations.keySet()){
            Mappable m = specifiedLocations.get(dimension);
            if (m instanceof Weapon){
                Weapon weapon = ((Weapon) m);
                List<Alien> aliensToShoot;
                for (int i = 0; i < routes.size(); i++){
                    aliensToShoot = routes.get(i).aliensWithinRadius(weapon);
                    weapon.shoot(aliensToShoot);
                }
            }
        }
    }

    public void shootAliensByHeroAndSoldiers(){
        List<Alien> aliensToShoot;
        for (int i = 0; i < routes.size(); i++){
            aliensToShoot = routes.get(i).aliensWithinRadius(this.hero);
            hero.shoot(aliensToShoot);
            Soldier soldiers[] = this.hero.getSoldiers();
            for (int j = 0; j < 3; j++){
                if (soldiers[j] != null){
                    aliensToShoot = routes.get(i).aliensWithinRadius(soldiers[j]);
                    soldiers[j].shoot(aliensToShoot);
                }
            }
        }
    }
    /***** GETTERS *******/

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Wormhole> getWormholes() {
        return Wormholes;
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

    public int whichLine(Dimension dimension){
        for (int i = 0; i < 5; i++){
            if (lines[i].isOnLine(dimension)){
                return i;
            }
        }
        return -1;
    }

    public List<Alien> aliensWithinRadius(Shootable shootable){
        List<Integer> linesWithinRadius = new ArrayList<>();
        List<Alien> aliensToShoot = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Dimension nearestPoint = lines[i].getNearestPoint(shootable.getShootingPoint());
            if (shootable.isWithinRadius(nearestPoint)){
                linesWithinRadius.add(i);
            }
        }

        for (Integer withinRadius : linesWithinRadius) {
            List<Alien> aliensToCheck = alienMap.get(lines[withinRadius]);
            for (int i = 0; i < aliensToCheck.size(); i++){
                Alien currentAlienToCheck = aliensToCheck.get(i);
                if (shootable.isWithinRadius(currentAlienToCheck.getDimension())){
                    aliensToShoot.add(currentAlienToCheck);
                }
            }
        }
        return aliensToShoot;
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
