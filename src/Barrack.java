import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Barrack implements Mappable {
    private int price;
    private Dimension dimension;
    private boolean inUse;
    private int soldiersInDemand;
    private List<Integer> timeNeeded;
    private int currentTime;
    private Soldier training;
    private ArrayList<Soldier> requested = new ArrayList<>();

    private ImageView barrackView;


    public Barrack(int price, Dimension dimension) {
        this.price = price;
        this.dimension = dimension;
        this.inUse = false;
        this.currentTime = 0;
        timeNeeded = new ArrayList<>();

    }

    public ImageView getBarrackView() {
        return barrackView;
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
        timeNeeded = new ArrayList<>();

        barrackView = new ImageView(new Image(getClass()
                .getResource("res/weapons/barrack/barrack.png").toExternalForm()));
        barrackView.setFitWidth(64);
        barrackView.setFitHeight(64);
        barrackView.setVisible(true);
        barrackView.relocate(dimension.getX() - 32, dimension.getY() - 32);
    }

    void requestSoldier(int timeNeeded) {
  //      System.out.println("adding one soldier in line");
    //    System.out.println("waiting time = " + timeNeeded);
        this.timeNeeded.add(timeNeeded);
        inUse = true;
        soldiersInDemand++;
    }

    void requestSoldier(int timeNeeded, Soldier rip) {
        if (!requested.contains(rip)){
            requestSoldier(timeNeeded);
            requested.add(rip);
        }
    }

    void proceed() {
        if (AlienCreeps.START){
            if (inUse) {
                //        System.out.println("barrack in currently in use");
                currentTime++;
            //            System.out.println("current time is " + currentTime);
                //       System.out.println("time left is " + (timeNeeded - currentTime));
                if (currentTime >= timeNeeded.get(0)) {
                    training = new Soldier();
                    new Thread(training).start();

                    /*Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            AlienCreeps.addElementToGameRoot(AlienCreeps.gameScene.getRoot().getChildrenUnmodifiable().size(),
                                    training.getBulletView());
                        }
                    });*/
                    //soldierLifeCycle.start();
                    currentTime = 0;
                    timeNeeded.remove(0);
                    soldiersInDemand--;
               //                System.out.println(soldiersInDemand + " soldiers left to make");
                    if (soldiersInDemand == 0) {
                        inUse = false;
                        //timeNeeded = 0;
                    }
                }
            } else {
                //       System.out.println("barrack is not in use");
            }
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
