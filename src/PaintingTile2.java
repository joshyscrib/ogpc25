import javax.imageio.ImageIO;
        import java.awt.*;
        import java.awt.image.ImageObserver;
        import java.net.URL;

public class PaintingTile2 extends Tile {
    static Image painting2;

    public PaintingTile2() {
        isSolid = true;
        tileType = 7;
    }

    public void drawTile(int x, int y, Graphics gx) {
        if(painting2 == null){
            try {
                painting2 = ImageIO.read(Game.class.getResource("paint2.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(painting2 != null) {
            gx.drawImage(painting2, x, y, null);
        }
    }

}