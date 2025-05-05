import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

public class PaintingTile4 extends Tile {
    static Image painting4;

    public PaintingTile4() {
        isSolid = true;
        tileType = 9;
    }

    public void drawTile(int x, int y, Graphics gx) {
        if(painting4 == null){
            try {
                painting4 = ImageIO.read(Game.class.getResource("paint4.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(painting4 != null) {
            gx.drawImage(painting4, x, y, null);
        }
    }

}