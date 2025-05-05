import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

public class PaintingTile3 extends Tile {
    static Image painting3;

    public PaintingTile3() {
        isSolid = true;
        tileType = 8;
    }

    public void drawTile(int x, int y, Graphics gx) {
        if(painting3 == null){
            try {
                painting3 = ImageIO.read(Game.class.getResource("paint3.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(painting3 != null) {
            gx.drawImage(painting3, x, y, null);
        }
    }

}