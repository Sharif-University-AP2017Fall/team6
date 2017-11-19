import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    public static int XBOUND;
    public static int YBOUND; //we have to initialize both of these right here

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
        if ((int)(Math.random() * 3) == 0){ //generate alien with probability 1/3
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
                if (dimensionToMove == null){ //has reached the end of current line
                    lines[i + 1].addAlienToLine(currentAlienToMove);
                }else{
                    if (intersections.contains(dimensionToMove)){
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
        lines[lineNumber].addAlienToLine(alien);
    }

    public int whichLine(Dimension dimension){
        for (int i = 0; i < 5; i++){
            if (lines[i].isOnLine(dimension)){
                return i;
            }
        }
        return -1;
    }
}

class Line{
    private int slope;
    private int intercept;
    private Dimension startPoint;
    private Dimension endPoint;

    private int unit; //we change the x of each movable by this amount, we will finalize this.

    public Line(int slope, int intercept, Dimension startPoint, Dimension endPoint) {
        this.slope = slope;
        this.intercept = intercept;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Dimension moveAlienOnLine(Alien alien){
        int currentX = alien.getDimension().getX();
        int currentY = alien.getDimension().getY();

        int newX = currentX + unit;
        int newY = slope * newX + intercept;

        if (newX <= endPoint.getX()){
            alien.setDimension(new Dimension(newX, newY));
            return alien.getDimension();
        }
        return null;
    }

    public void addAlienToLine(Alien alien){
        alien.setDimension(startPoint);
    }

    public boolean isOnLine(Dimension dimension){
        int xToCheck = dimension.getX();
        int yToCheck = dimension.getY();

        if (xToCheck >= startPoint.getX() && xToCheck < endPoint.getX()){
            if (xToCheck * slope + intercept == yToCheck){
                return true;
            }
        }
        return false;
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
