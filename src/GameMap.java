import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    private List<Route> routes = new ArrayList<>(); //each map has at least two routes
    private List<Dimension> intersections = new ArrayList<>(); //the routes
    private Map<Dimension, Weapon> specifiedLocations;

    public GameMap(List<Route> routes, List<Dimension> intersections, ArrayList<Dimension> dimensions) {
        this.routes = routes;
        this.intersections = intersections;
        for (int i = 0; i < dimensions.size(); i++){
            specifiedLocations.put(dimensions.get(i), null);
        }
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Dimension> getIntersections() {
        return intersections;
    }
}
class Route{
    private Line[] lines = new Line[5]; //each route consists of 5 lines
    Map<Line, ArrayList<Alien>> alienMap = new HashMap<>();

    public Route(Line[] lines) {
        this.lines = lines;
        for (int i = 0; i < lines.length; i++){
            alienMap.put(lines[i], new ArrayList<Alien>());
        }
    }

    public void addAlienToRoute(Alien alien){}

    public void moveAliens(){}
}

class Line{
    private int slope; //y = ax + b; slope = a
    private int intercept; //y = ax + b; intercept = b
    private Dimension startPoint;
    private Dimension endPoint;

    public Line(int slope, int intercept, Dimension startPoint, Dimension endPoint) {
        this.slope = slope;
        this.intercept = intercept;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public boolean moveAlien(Alien alien){} //if the alien's dimension proceeds the endPoint return false; which means move to next line

}
