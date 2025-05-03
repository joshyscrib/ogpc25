import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Tile extends Entity{
    public boolean isSolid;
    public int tileType = 1;
    public Tile() {

    }
     abstract void drawTile(int x, int y, Graphics gx);
    public Image loadImage(String filePath){
        Image img = null;
        try {
            img = ImageIO.read(new File(filePath));
        } catch (IOException e) {
        }
        return img;
    }

    public boolean isSolid() {
        return isSolid;
    }
}
