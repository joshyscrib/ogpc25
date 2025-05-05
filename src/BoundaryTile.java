import javax.imageio.ImageIO;
import java.awt.*;

public class BoundaryTile extends Tile{
    static Image WallImage;
    public BoundaryTile(){
        isSolid = true;
        tileType = 3;
    }

    @Override
    public void drawTile(int x, int y, Graphics gx) {
        if(WallImage == null){
            try {
                WallImage = ImageIO.read(Game.class.getResource("placeholdwall.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(WallImage != null) {
            gx.drawImage(WallImage, x, y, null);
        }
    }

}
