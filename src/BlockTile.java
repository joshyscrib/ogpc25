import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

public class BlockTile extends Tile {
    static Image blockImage;

    public BlockTile() {
        isSolid = true;
        tileType = 2;
    }

    public void drawTile(int x, int y, Graphics gx) {
        if(blockImage == null){
            try {
                //blockImage = ImageIO.read(Game.class.getResource("wallpaper.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(blockImage != null) {
            gx.drawImage(blockImage, x, y, null);
        }
    }

}
