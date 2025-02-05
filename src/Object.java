import java.awt.*;

public abstract class Object extends Entity{
    public boolean moveable;
    public boolean solid;
    public int size;
    public int x;
    public int y;
    public Object() {

    }
    public abstract void Paint(int paintX, int paintY, Graphics g);

}
