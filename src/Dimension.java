import java.util.ArrayList;
import java.util.List;

public class Dimension {
    private double X;
    private double Y;


    public Dimension(double x, double y) {
        X = x;
        Y = y;
    }

    double getX() {
        return X;
    }

    double getY() {
        return Y;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    double distanceFrom(Dimension dimension) {
        return Math.sqrt((dimension.getX() - this.X) * (dimension.getX() - this.X) +
                (dimension.getY() - this.Y) * (dimension.getY() - this.Y));
    }

    boolean isWithinBounds(double upperBoundX, double lowerBoundX, double upperBoundY, double lowerBoundY) {
        return X >= lowerBoundX && X <= upperBoundX
                && Y >= lowerBoundY && Y <= upperBoundY;
    }

    Dimension add(Dimension otherDimension) {
        return new Dimension(otherDimension.getX() + X, otherDimension.getY() + Y);
    }

    static List<Dimension> randomDimension(int num) {
        List<Dimension> ds = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            double x = Math.round((Math.random() * (GameMap.XBOUND - 20)) + 20);
            double y = Math.round((Math.random() * (GameMap.YBOUND - 20)) + 20);
            ds.add(new Dimension(x, y));
        }
        return ds;
    }

    @Override
    public String toString() {
        return "(" + this.X + ", " + this.Y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        Dimension otherDimension = ((Dimension) obj);
        double yDif = otherDimension.getY() - this.Y;
        double xDif = otherDimension.getX() - this.X;
        return (yDif < 1 && yDif > -1) && (xDif < 1 && xDif > -1);
    }
}
