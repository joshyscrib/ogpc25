import javax.imageio.ImageIO;
import java.awt.*;

public class GrandmaJump {
    public int x1 = -1000;
    public int y1 = 1400;
    public int x2 = 1400;
    public int y2 = 1400;
    public int x3 = -1000;
    public int y3 = 1400;
    public int x4= -1000;
    public int y4 = 1400;
    public int x5 = -1000;
    public int y5 = 1400;
    public int x6 = -1000;
    public int y6 = 1400;
    static Image frame1;
    static Image frame2;
    static Image frame3;
    static Image frame4;
    static Image frame5;
    static Image frame6;

    public void paint(int x, int y, Graphics gx) {
        if(frame1 == null){
            try {
                frame1 = ImageIO.read(Game.class.getResource("GrandmaJump1.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(frame1 != null) {
            gx.drawImage(frame1, x1, y1,1060,1060, null);
        }
        if(frame2 == null){
            try {
                frame2 = ImageIO.read(Game.class.getResource("GrandmaJump2.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(frame2 != null) {
            gx.drawImage(frame2, x2, y2,1060,1060, null);
        }
        if(frame3 == null){
            try {
                frame3 = ImageIO.read(Game.class.getResource("GrandmaJump3.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(frame3 != null) {
            gx.drawImage(frame3, x3, y3,1060,1060, null);
        }
        if(frame4 == null){
            try {
                frame4 = ImageIO.read(Game.class.getResource("GrandmaJump4.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(frame4 != null) {
            gx.drawImage(frame4, x4, y4,1060,1060, null);
        }
        if(frame5 == null){
            try {
                frame5 = ImageIO.read(Game.class.getResource("GrandmaJump5.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(frame5 != null) {
            gx.drawImage(frame5, x5, y5,1060,1060, null);
        }
        if(frame6 == null){
            try {
                frame6 = ImageIO.read(Game.class.getResource("GrandmaJump6.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(frame6 != null) {
            gx.drawImage(frame6, x6, y6,1060,1060, null);
        }
    }
}
