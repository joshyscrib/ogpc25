import javax.imageio.ImageIO;
import java.awt.*;

public class Grandma{
    static Image gma;
    static Image gma2;
    public int x = 0;
    public int y = 0;
    public Direction direction = Direction.Right;
    public void Paint(int paintX, int paintY, Graphics g, boolean frame) {
        if(frame){
            if(gma == null){
                try {
                    gma = ImageIO.read(Game.class.getResource("Grandma.png"));
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            if(gma != null) {
                g.drawImage(gma, paintX, paintY,128,128, null);
            }
        }
        else{
            if(gma2 == null){
                try {
                    gma2 = ImageIO.read(Game.class.getResource("Grandma2.png"));
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            if(gma2 != null) {
                g.drawImage(gma2, paintX, paintY,128,128, null);
            }
        }
    }
}
