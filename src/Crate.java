import java.awt.*;

public class Crate extends Object{

    public Crate(int placeX, int placeY){
        moveable = false;
        size = 96;
        x = placeX;
        y = placeY;
        solid = true;
    }

    @Override
    public void Paint(int paintX, int paintY, Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(paintX, paintY, size, size);
    }
}
