import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

public class PaintingTile3 extends Tile {
    static Image painting1;
    static Image painting2;
    public PaintingTile3() {
        isSolid = true;
        tileType = 8;
    }

    @Override
    void drawTile(int x, int y, Graphics gx) {

    }

    public void drawPainting(int x, int y, boolean second, Graphics gx) {
        if(painting1 == null){
            try {
                painting1 = ImageIO.read(Game.class.getResource("1Painting12864.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(painting2 == null){
            try {
                painting2 = ImageIO.read(Game.class.getResource("2Painting12864.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(painting1 != null) {
            gx.drawImage(painting1, x, y, null);
        }
        if(painting2 != null && second) {
            gx.drawImage(painting2, x, y, null);
        }
    }

}