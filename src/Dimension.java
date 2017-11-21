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
