public class Barrack implements Mappable {
    private int price;
    private Dimension dimension;
    private boolean inUse;
    private int soldiersInDemand;
    private int timeNeeded; //pay attention :))) vip :)) we know it, you don't. hahahhahaha :)))
    private int currentTime;
    private Soldier training;


    public Barrack(int price, Dimension dimension, int timeNeeded, int currentTime) {
        this.price = price;
        this.dimension = dimension;
        this.inUse = false;
        this.soldiersInDemand = 3;
        this.timeNeeded = timeNeeded;
        this.currentTime = 0;
    }

    @Override
    public void mapTo(Dimension dimension) {
        this.dimension = dimension;
    }

    public Barrack(Dimension dimension) {
        this.mapTo(dimension);
        this.price = 90;
        this.inUse = false;
        soldiersInDemand = 0;
        this.currentTime = 0;
        this.training = null;
    }

    public void requestSoldier(int timeNeeded){
        if (!inUse){
            currentTime = 0;
        }
        this.timeNeeded = timeNeeded;
        inUse = true;
        soldiersInDemand++;
    }

    public void proceed(){
        if (inUse){
            currentTime++;
            if (currentTime == timeNeeded){
                training = new Soldier();
                currentTime = 0;
                soldiersInDemand--;
                if (soldiersInDemand == 0){
                    inUse = false;
                }
            }
        }
    }

    public Soldier getSoldier() {
        Soldier trained = training;
        training = null;
        return trained;
    }
}
