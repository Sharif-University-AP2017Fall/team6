import java.util.List;

public interface Shooter {
    List<Alien> shoot(List<Alien> aliens); //needs an argument but don't know what yet
    Dimension getShootingPoint();
    boolean isWithinRadius(Dimension dimension);
}
