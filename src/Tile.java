import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {

    public static final int WIDTH = 32, HEIGHT = 32; //pixels

    private Image view;

    public Tile(Image view) {
        this.view = view;
    }

    public void draw(GraphicsContext gc, double x, double y){
        gc.drawImage(view, x, y, WIDTH, HEIGHT);
    }
}
