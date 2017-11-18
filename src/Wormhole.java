public class Wormhole {
    private int number;
    private int leadsTo;
    private Dimension dimension;

    public Wormhole(int number, int leadsTo, Dimension dimension) {
        this.number = number;
        this.leadsTo = leadsTo;
        this.dimension = dimension;
    }

    public void moveThrough(Moveable moveable){
    }

    public int getNumber() {
        return number;
    }

    public int getLeadsTo() {
        return leadsTo;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void transferMoveable(Moveable moveable){}
}
