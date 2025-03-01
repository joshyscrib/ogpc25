import java.awt.*;

public class TargetCrate extends Object{

    public TargetCrate(int placeX, int placeY){
        moveable = false;
        size = 96;
        x = placeX;
        y = placeY;
        solid = false;
        gravity = 0;
    }

    @Override
    public void Paint(int paintX, int paintY, Graphics g) {

    }
}
