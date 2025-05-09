import javax.imageio.ImageIO;
import java.awt.*;

public class DoorTile extends Tile{
        static Image doorImage;
    public DoorTile(){
        isSolid = false;
        tileType = 5;
    }

    @Override
    public void drawTile(int x, int y, Graphics gx) {
        if(doorImage == null){
            try {
                doorImage = ImageIO.read(Game.class.getResource("door.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(doorImage != null) {
            gx.drawImage(doorImage, x, y, null);
        }
    }

    @Override
    void drawPainting(int x, int y, boolean second, Graphics gx) {

    }
}
