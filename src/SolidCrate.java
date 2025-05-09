import javax.imageio.ImageIO;
import java.awt.*;

public class SolidCrate extends Object{
    Image crateImage;
    public SolidCrate(int placeX, int placeY){
        moveable = false;
        size = 96;
        x = placeX;
        y = placeY;
        solid = true;
        gravity = 3;
        moveDirection = Direction.Down;
        width = 96;
        height = 96;
    }

    @Override
    public void Paint(int paintX, int paintY, Graphics g) {
        if(crateImage == null){
            try {
                crateImage = ImageIO.read(Game.class.getResource("SolidCrate.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(crateImage != null) {
            g.drawImage(crateImage, paintX, paintY, null);
        }
    }
}
