public class Barrack implements Mappable {
    private int price;
    private Dimension dimension;
    private boolean inUse;
    private int soldiersInDemand;
    private int timeNeeded; //pay attention :))) vip :)) we know it, you don't. hahahhahaha :)))
    private int currentTime;
    private Soldier training;


    public Barrack(int price, Dimension dimension) {
        this.price = price;
        this.dimension = dimension;
        this.inUse = false;
        this.currentTime = 0;
    }

    @Override
    public void mapTo(Dimension dimension) {
        this.dimension = dimension;
    }

    Barrack(Dimension dimension) {
        this.mapTo(dimension);
        this.price = 90;
        this.inUse = false;
        soldiersInDemand = 0;
        this.currentTime = 0;
        this.training = null;
    }

    void requestSoldier(int timeNeeded) {
        //   System.out.println("adding one soldier in line");
        // System.out.println("waiting time = " + timeNeeded);
        this.timeNeeded = timeNeeded;
        inUse = true;
        soldiersInDemand++;
    }

    void proceed() {
        if (inUse) {
            //        System.out.println("barrack in currently in use");
            currentTime++;
            //        System.out.println("current time is " + currentTime);
            //       System.out.println("time left is " + (timeNeeded - currentTime));
            if (currentTime >= timeNeeded) {
                training = new Soldier();
                currentTime = 0;
                soldiersInDemand--;
                //           System.out.println(soldiersInDemand + " soldiers left to make");
                if (soldiersInDemand == 0) {
                    inUse = false;
                    timeNeeded = 0;
                }
            }
        } else {
            //       System.out.println("barrack is not in use");
        }
    }

    Soldier getSoldier() {
        if (inUse) {
            if (training == null) {
                //          System.out.println("not ready yet");
            }
        }
        return training;
    }

    void removeSoldier() {
        this.training = null;
    }
}
