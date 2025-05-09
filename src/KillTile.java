import javax.imageio.ImageIO;
import java.awt.*;

public class KillTile extends Tile{
static Image wallpaperImage;
    public KillTile(){
        isSolid = false;
        tileType = 4;
    }

    @Override
    public void drawTile(int x, int y, Graphics gx) {
        if(wallpaperImage == null){
            try {
                wallpaperImage = ImageIO.read(Game.class.getResource("wallpaper.png"));
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }

        if(wallpaperImage != null) {
            gx.drawImage(wallpaperImage, x, y, null);
        }
    }

    @Override
    void drawPainting(int x, int y, boolean second, Graphics gx) {

    }
}
