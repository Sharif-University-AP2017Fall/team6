import java.util.ArrayList;
import java.util.List;

public class Dimension {
    private double X;
    private double Y;


    public Dimension(double x, double y) {
        X = x;
        Y = y;
    }

    public double getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public double distanceFrom(Dimension dimension){
        return Math.sqrt((dimension.getX() - this.X) * (dimension.getX() - this.X) +
                (dimension.getY() - this.Y) * (dimension.getY() - this.Y));
    }

    public boolean isWithinBounds(){
        return X >= 0 && X <= GameMap.XBOUND
                && Y >= 0 && Y <= GameMap.YBOUND;
    }

    public Dimension add(Dimension otherDimension){
        return new Dimension(otherDimension.getX() + X, otherDimension.getY() + Y);
    }

    public static List<Dimension> randomDimension(int num){
        List<Dimension> ds = new ArrayList<>();
        for (int i = 0; i < num; i++){
            double x = ((Math.random() * (GameMap.XBOUND - 20)) + 10);
            double y = ((Math.random() * (GameMap.YBOUND - 20)) + 10);
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
        return otherDimension.getY() == this.Y && otherDimension.getX() == this.X;
    }
}
