import java.awt.*;

public class SolidCrate extends Object{

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
        g.setColor(Color.gray);
        g.fillRect(paintX, paintY, size, size);
    }
}
