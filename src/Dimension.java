public class Dimension {
    private int x;
    private int y;
    
    public Dimension(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distance(int X,int Y){
        return Math.sqrt((double)Math.pow((X-getX()),2) + (double)Math.pow((Y-getY()),2));
    }
    public double distance(Dimension a){
        int X=a.getX();
        int Y=a.getY();
        return Math.sqrt((double)Math.pow((X-getX()),2) + (double)Math.pow((Y-getY()),2));
    }
    @Override
    public boolean equals(Object obj) {
        Dimension otherDimension = ((Dimension) obj);
        return otherDimension.getY() == y && otherDimension.getX() == x;
    }
}
