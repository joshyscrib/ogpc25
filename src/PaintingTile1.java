import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

public class PaintingTile1 extends Tile {
    static Image painting1;

    public PaintingTile1() {
        isSolid = true;
        tileType = 6;
    }

    public void drawTile(int x, int y, Graphics gx) {
        if(painting1 == null){
            try {
                painting1 = ImageIO.read(Game.class.getResource("paint1.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(painting1 != null) {
            gx.drawImage(painting1, x, y, null);
        }
    }

}