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

    @Override
    public boolean equals(Object obj) {
        Dimension otherDimension = ((Dimension) obj);
        return otherDimension.getY() == y && otherDimension.getX() == x;
    }
}
