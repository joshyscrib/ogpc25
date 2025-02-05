public class Player extends Entity{
    public static final int JUMP_STRENGTH = -35;
    public static final int MOVE_SPEED = 10;
    public Player(int startX, int startY){
        x = startX;
        y = startY;
        width = 32;
        height = 64;
    }
}