import java.awt.*;

public abstract class Object extends Entity{
    public Direction moveDirection = Direction.None;
    public int speed = 3;
    public boolean moveable;
    public boolean solid;
    public int size;
    public int x;
    public int y;
    public int gravity;
    public Object() {

    }
    public abstract void Paint(int paintX, int paintY, Graphics g);

}
