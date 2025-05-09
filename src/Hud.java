import javax.imageio.ImageIO;
import java.awt.*;

public class Hud {
    static Image breath;
    public void paint(int x, int y, int playerBreath, Graphics gx) {
        int breathStart = y-28+256-16;
        if(breath == null){
            try {
                breath = ImageIO.read(Game.class.getResource("Breath.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(breath != null) {
            gx.setColor(Color.green);
            gx.fillRect(x+12+20+90, breathStart + 2,32, (int) -(playerBreath*1.065));
            gx.drawImage(breath, x + 12, y - 28, 256, 256, null);

        }
    }
}
