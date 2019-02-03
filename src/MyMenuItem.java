import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MyMenuItem extends StackPane{
    private ImageView normal;
    private Text msg;
    private Glow glow;

    
    Media sound=new Media(getClass().getResource("res/sound/Gasp.wav").toExternalForm()); // TODO: CLICK
    MediaPlayer player=new MediaPlayer(sound);
    
    public MyMenuItem(String type){
        setAlignment(Pos.CENTER);

        normal = new ImageView(new Image(getClass()
                .getResource("res/menu/item/" + type + ".png").toExternalForm()));
        normal.setFitHeight(200);
        normal.setFitWidth(145);

        getChildren().add(normal);

        setOnMouseEntered(e -> onSelect());

        setOnMouseExited(e -> onDeselect());
    }

    public MyMenuItem(Image view){
        setAlignment(Pos.CENTER);

        normal = new ImageView(view);
        normal.setFitWidth(110);
        normal.setFitHeight(30);

        getChildren().add(normal);

        setOnMouseEntered(e -> onSelect());

        setOnMouseExited(e -> onDeselect());
    }
    
    public MyMenuItem(Image view, int w, int h){
        setAlignment(Pos.CENTER);

        normal = new ImageView(view);
        normal.setFitWidth(w);
        normal.setFitHeight(h);

        getChildren().add(normal);

        setOnMouseEntered(e -> onSelect());

        setOnMouseExited(e -> onDeselect());
    }

    public MyMenuItem(Image view, int w, int h, String text){
        setAlignment(Pos.CENTER);

        normal = new ImageView(view);
        normal.setFitWidth(w);
        normal.setFitHeight(h);
        msg = new Text(text);
        Font font = Font.loadFont(MyMenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);
        msg.setFont(font);
        msg.setFill(Color.rgb(57, 34, 18));

        getChildren().addAll(normal, msg);

        setOnMouseEntered(e -> onSelect());

        setOnMouseExited(e -> onDeselect());
    }

    public MyMenuItem(Image view, String text){
        setAlignment(Pos.CENTER);
        msg = new Text(text);
        Font font = Font.loadFont(MyMenuItem.
                class.
                getResource("res/Font/Pieces_of_Eight.ttf").
                toExternalForm(), 35);
        msg.setFont(font);
        msg.setFill(Color.rgb(57, 34, 18));
        normal = new ImageView(view);
        normal.setFitWidth(50);
        normal.setFitHeight(50);

        getChildren().addAll(normal, msg);

        setOnMouseEntered(e -> onSelect());

        setOnMouseExited(e -> onDeselect());
    }

    public void setDim(double x, double y){
        relocate(x, y);
    }

    private void onSelect(){
        AlienCreeps.menuScene.setCursor(Cursor.HAND);
        normal.setEffect(new Glow(10));
    }

    private void onDeselect(){
        AlienCreeps.menuScene.setCursor(Cursor.DEFAULT);
        normal.setEffect(new Glow(0));
    }

    public void setOnAction(Runnable action){
        setOnMouseClicked(event -> {
            
            player.play();
            action.run();
            player=new MediaPlayer(sound);
            /*Timeline act = new Timeline(new KeyFrame(Duration.millis(100),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            action.run();
                            player.play();
                        }
                    }));*/
            /*act.setCycleCount(1);
            act.play();*/
        });
    }
}































