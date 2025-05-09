import javax.imageio.ImageIO;
import java.awt.*;

public class Grandpa {
    static Image gpa;
    static Image gpa2;
    public int x = 0;
    public int y = 0;
    public Direction direction = Direction.Left;

    public void Paint(int paintX, int paintY, Graphics g, boolean frame) {
        if(frame){
            if(gpa == null){
                try {
                    gpa = ImageIO.read(Game.class.getResource("Grandpa.png"));
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            if(gpa != null) {
                g.drawImage(gpa, paintX, paintY, 128, 128, null);
            }
        }
        else{
            if(gpa2 == null){
                try {
                    gpa2 = ImageIO.read(Game.class.getResource("Grandpa2.png"));
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            if(gpa2 != null) {
                g.drawImage(gpa2, paintX, paintY,128,128, null);
            }
        }
    }
}

