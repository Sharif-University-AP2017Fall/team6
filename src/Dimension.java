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

    /*Dimension[]*/
    /** ARRAYS OF BOUNDS. FIRST DIMENSION IS THE LOWER BOUND AND THE SECOND THE HIGHER BOUND */
    private static double[] xbounds1 = new double[2];
    private static double[] ybounds1 = new double[2];

    private static double[] xbounds2 = new double[2];
    private static double[] ybounds2 = new double[2];

    private static double[] xbounds3 = new double[2];
    private static double[] ybounds3 = new double[2];

    private static double[] xbounds4 = new double[2];
    private static double[] ybounds4 = new double[2];

    private static double[] xbounds5 = new double[2];
    private static double[] ybounds5 = new double[2];

    private static double[] xbounds6 = new double[2];
    private static double[] ybounds6 = new double[2];

    static List<Dimension> randomDimension(int num) {
        xbounds1[0] = (5.0 / 100.0) * GameMap.XBOUND;
        xbounds1[1] = (30.0 / 100.0) * GameMap.XBOUND;
        ybounds1[0] = (40.0 / 100.0) * GameMap.YBOUND;
        ybounds1[1] = (60.0 / 100.0) * GameMap.YBOUND;

        xbounds2[0] = (15.0 / 100.0) * GameMap.XBOUND;
        xbounds2[1] = (70.0 / 100.0) * GameMap.XBOUND;
        ybounds2[0] = (7.0 / 100.0) * GameMap.YBOUND;
        ybounds2[1] = (10.0 / 100.0) * GameMap.YBOUND;

        xbounds3[0] = (35.0 / 100.0) * GameMap.XBOUND;
        xbounds3[1] = (60.0 / 100.0) * GameMap.XBOUND;
        ybounds3[0] = (15.0 / 100.0) * GameMap.YBOUND;
        ybounds3[1] = (17.0 / 100.0) * GameMap.YBOUND;

        xbounds4[0] = (75.0 / 100.0) * GameMap.XBOUND;
        xbounds4[1] = (80.0 / 100.0) * GameMap.XBOUND;
        ybounds4[0] = (5.0 / 100.0) * GameMap.YBOUND;
        ybounds4[1] = (17.0 / 100.0) * GameMap.YBOUND;

        xbounds5[0] = (32.0 / 100.0) * GameMap.XBOUND;
        xbounds5[1] = (65.0 / 100.0) * GameMap.XBOUND;
        ybounds5[0] = (78.0 / 100.0) * GameMap.YBOUND;
        ybounds5[1] = (85.0 / 100.0) * GameMap.YBOUND;

        xbounds6[0] = (80.0 / 100.0) * GameMap.XBOUND;
        xbounds6[1] = (90.0 / 100.0) * GameMap.XBOUND;
        ybounds6[0] = (75.0 / 100.0) * GameMap.YBOUND;
        ybounds6[1] = (80.0 / 100.0) * GameMap.YBOUND;

        List<Dimension> ds = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            double xLowerBound = 0.0;
            double xUpperBound = 0.0;
            double yLowerBound = 0.0;
            double yUpperBound = 0.0;
            switch (i + 1){
                case 1:
                    xLowerBound = xbounds1[0];
                    xUpperBound = xbounds1[1];
                    yLowerBound = ybounds1[0];
                    yUpperBound = ybounds1[1];
                    break;
                case 2:
                    xLowerBound = xbounds2[0];
                    xUpperBound = xbounds2[1];
                    yLowerBound = ybounds2[0];
                    yUpperBound = ybounds2[1];
                    break;
                case 3:
                    xLowerBound = xbounds3[0];
                    xUpperBound = xbounds3[1];
                    yLowerBound = ybounds3[0];
                    yUpperBound = ybounds3[1];
                    break;
                case 4:
                    xLowerBound = xbounds4[0];
                    xUpperBound = xbounds4[1];
                    yLowerBound = ybounds4[0];
                    yUpperBound = ybounds4[1];
                    break;
                case 5:
                    xLowerBound = xbounds5[0];
                    xUpperBound = xbounds5[1];
                    yLowerBound = ybounds5[0];
                    yUpperBound = ybounds5[1];
                    break;
                case 6:
                    xLowerBound = xbounds6[0];
                    xUpperBound = xbounds6[1];
                    yLowerBound = ybounds6[0];
                    yUpperBound = ybounds6[1];
                    break;
            }
            double x = Math.round((Math.random() * (xUpperBound - xLowerBound)) + xLowerBound);
            double y = Math.round((Math.random() * (yUpperBound - yLowerBound)) + yLowerBound);
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

    public static void correctDim(Dimension dimension){
        double x = Math.round(dimension.getX() * 10);
        double y = Math.round(dimension.getY() * 10);
        dimension.setX(x / 10);
        dimension.setY(y / 10);
    }
}
