import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MenuItem extends StackPane{
    private ImageView normal;
    private Glow glow;

    public MenuItem(String type){
        setAlignment(Pos.CENTER);

        normal = new ImageView(new Image(getClass()
                .getResource("res/menu/item/" + type + ".png").toExternalForm()));
        normal.setFitHeight(200);
        normal.setFitWidth(145);

        getChildren().add(normal);

        setOnMouseEntered(e -> onSelect());

        setOnMouseExited(e -> onDeselect());
    }

    public void setDim(double x, double y){
        relocate(x, y);
    }

    private void onSelect(){
        normal.setEffect(new Glow(10));
    }

    private void onDeselect(){
        normal.setEffect(new Glow(0));
    }

    public void setOnAction(Runnable action){
        setOnMouseClicked(event -> {
            Timeline act = new Timeline(new KeyFrame(Duration.millis(100),
                    event1 -> action.run()));
            act.setCycleCount(1);
            act.play();
        });
    }
}
