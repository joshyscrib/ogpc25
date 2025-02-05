import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Game extends JPanel {
    private static final int TILE_SIZE = 32;
    
    private static final int GRAVITY = 4;
    
    private static final int WINDOW_MARGIN = 50;

    private int screenWidth;
    private int screenHeight;
    private int gridRows;
    private int gridCols;
    private int mouseX;
    private int mouseY;

    private int velocityY = 0;
    private boolean isJumping = false;
    private boolean[] keys = new boolean[256];
    private boolean showGrid = false;
    private Tile[][] tiles;

    private boolean placingTile = false;
    private boolean mousePressed = false;
    public int tilePlaceType = 1;
    private static final int WORLD_WIDTH = 4562; // Larger world width
    private static final int WORLD_HEIGHT = 1312; // Larger world height
    private int cameraX = 0; // Camera position
    private int cameraY = 128;
    public Integer[][] tileNums = new Integer[gridRows][gridCols];
    public ArrayList<Object> objects = new ArrayList<Object>();
    Player player;

    public Game() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width - WINDOW_MARGIN;
        screenHeight = screenSize.height - WINDOW_MARGIN;
        System.out.println(screenWidth + " : " + screenHeight);

        tiles = new Tile[gridRows][gridCols];
        initializeTiles();

        setFocusable(true);

        // Add key listener for movement and jumping
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    showGrid = !showGrid;
                }
               /* if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    for(int i = 0; i < gridCols; i++){
                       for(int j = 0; j < gridRows; j++){
                           tileNums[i][j] = tiles[i][j].tileType;
                       }

                    }

                }
                */
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });

        // Add MouseListener for tile placement
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getY() < gridRows * 32 && e.getX() < gridCols * 32) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getY() < gridRows * 32 && e.getX() < gridCols * 32) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                mouseX = e.getX();
                mouseY = e.getY();

                // Adjust mouse coordinates to account for camera offset
                int adjustedMouseX = mouseX + cameraX;
                int adjustedMouseY = mouseY + cameraY;

                // Convert adjusted coordinates to grid indices
                int col = adjustedMouseX / TILE_SIZE;
                int row = adjustedMouseY / TILE_SIZE;

                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Left click: Place tile
                    if (col >= 0 && col < gridCols && row >= 0 && row < gridRows) {
                        placingTile = true;
                        placeTile(tilePlaceType, col, row);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right click: Cycle tile type
                    tilePlaceType++;
                    if (tilePlaceType > 3) {
                        tilePlaceType = 1;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }
        });
        player = new Player(250, WORLD_HEIGHT - 450);
        gridCols = WORLD_WIDTH / TILE_SIZE; // Update based on world size
        gridRows = WORLD_HEIGHT / TILE_SIZE;
        tiles = new Tile[gridRows][gridCols];
        initializeTiles();
        Crate startCrate = new Crate(400,700);
        objects.add(startCrate);
    }

    public void placeTile(int type, int col, int row) {
        switch (type) {
            case 1:
                tiles[row][col] = new AirTile();
                break;
            case 2:
                tiles[row][col] = new BlockTile();
                break;
            case 3:
                tiles[row][col] = new BoundaryTile();
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game with Player and Floor");
        Game game = new Game();

        frame.setSize(game.screenWidth, game.screenHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setVisible(true);

        Timer timer = new Timer(16, e -> { // 16 ms for ~60 FPS
            game.update();
            game.repaint();
        });
        timer.start();
    }

    private void initializeTiles() {
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                if (row >= gridRows - 7 || row <= 8 || col <= 1 || col >= gridCols - 2) {
                    tiles[row][col] = new BoundaryTile(); // Solid floor tiles
                } else {
                    tiles[row][col] = new AirTile(); // Transparent tiles
                }
            }
        }
    }

    public void update() {

        // Handle horizontal movement with collision detection
        if (keys[KeyEvent.VK_A]) {
            // Move left
            int targetX = player.x - player.MOVE_SPEED;  // Target position if no collision
            int colLeft = targetX / TILE_SIZE;
            int colRight = (targetX + player.width - 1) / TILE_SIZE;

            // Check for collision with the tiles
            if (colLeft >= 0 && colRight < gridCols) {
                boolean collision = false;
                for (int row = (player.y / TILE_SIZE); row < (player.y + player.height) / TILE_SIZE; row++) {
                    if (tiles[row][colLeft].isSolid() || tiles[row][colRight].isSolid()) {
                        collision = true;
                        break;
                    }
                }

                if (!collision) {
                    player.x = targetX;  // Move the player to the new position
                } else {
                    // Stop the player at the left boundary of the wall
                    player.x = (colLeft + 1) * TILE_SIZE;
                }
            }
            for(Object obj : objects){
                if(obj.solid == true && player.x >= obj.x && player.x <= obj.x + obj.size && player.y == 0){}
            }
        }
//test
        if (keys[KeyEvent.VK_D]) {
            // Move right
            int targetX = player.x + player.MOVE_SPEED;  // Target position if no collision
            int colLeft = targetX / TILE_SIZE;
            int colRight = (targetX + player.width - 1) / TILE_SIZE;

            // Check for collision with the tiles
            if (colLeft >= 0 && colRight < gridCols) {
                boolean collision = false;
                for (int row = (player.y / TILE_SIZE); row <= (player.y + player.height - 1) / TILE_SIZE; row++) {
                    if (tiles[row][colRight].isSolid()) {
                        collision = true;
                        break;
                    }
                }

                if (!collision) {
                    player.x = targetX;  // Move the player to the new position
                } else {
                    // Align the player to the left edge of the obstacle
                    player.x = colRight * TILE_SIZE - player.width;
                }
            }
        }


        // Handle vertical movement and gravity
        velocityY += GRAVITY;
        int step = Integer.signum(velocityY);
        int remaining = Math.abs(velocityY);

        // place tiles if clicking
        if (placingTile) {
            // Adjust mouse coordinates to account for camera offset
            int adjustedMouseX = mouseX + cameraX;
            int adjustedMouseY = mouseY + cameraY;

            // Convert adjusted coordinates to grid indices
            int col = adjustedMouseX / TILE_SIZE;
            int row = adjustedMouseY / TILE_SIZE;

            // Place tile if indices are within bounds
            if (col >= 0 && col < gridCols && row >= 0 && row < gridRows) {
                placeTile(tilePlaceType, col, row);
            }
        }

        if (!mousePressed) {
            placingTile = false;
        }

        // Vertical movement
        while (remaining > 0) {
            int move = Math.min(TILE_SIZE / 4, remaining);
            int nextY = player.y + step * move;
            int row = (nextY + player.height) / TILE_SIZE;
            int colLeft = player.x / TILE_SIZE;
            int colRight = (player.x + player.width - 1) / TILE_SIZE;

            // Check for collisions below (falling and landing)
            if (step > 0) {
                if (row < gridRows && (tiles[row][colLeft].isSolid() || tiles[row][colRight].isSolid())) {
                    player.y = row * TILE_SIZE - player.height;
                    velocityY = 0;
                    isJumping = false;
                    break;
                }
            }

            // Check for collisions above (ceiling while jumping)
            if (step < 0) {
                int ceilingRow = (nextY) / TILE_SIZE;
                if (ceilingRow >= 0 && (tiles[ceilingRow][colLeft].isSolid() || tiles[ceilingRow][colRight].isSolid())) {
                    player.y = (ceilingRow + 1) * TILE_SIZE;
                    velocityY = 0;
                    break;
                }
            }

            // Update the player position if no collision
            player.y = nextY;
            remaining -= move;
        }

        // Prevent player from moving off-screen horizontally
        if (player.x < 0) player.x = 0;
        if (player.x + player.width > WORLD_WIDTH) {
            player.x = WORLD_WIDTH - player.width;
        }

        // Jumping mechanic
        if (keys[KeyEvent.VK_SPACE] && !isJumping) {
            velocityY = player.JUMP_STRENGTH;
            isJumping = true;
        }
        cameraX = Math.max(0, Math.min(player.x - screenWidth / 2, WORLD_WIDTH - screenWidth));
        cameraY = Math.max(0, Math.min(player.y - screenHeight / 2, WORLD_HEIGHT - screenHeight));

    }

    // testing
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate visible tile range
        int startCol = Math.max(0, cameraX / TILE_SIZE);
        int endCol = Math.min(gridCols, (cameraX + screenWidth) / TILE_SIZE + 1);
        int startRow = Math.max(0, cameraY / TILE_SIZE);
        int endRow = Math.min(gridRows, (cameraY + screenHeight) / TILE_SIZE + 1);

        // Draw tiles in the visible range
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                Tile tile = tiles[row][col];
                switch (tile.tileType) {
                    case 1 -> g.setColor(new Color(118, 77, 70));
                    case 2 -> g.setColor(new Color(10, 190, 30));
                    case 3 -> g.setColor(new Color(70, 47, 40));
                }

                int drawX = col * TILE_SIZE - cameraX;
                int drawY = row * TILE_SIZE - cameraY;
                g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);

                if (showGrid) {
                    g.setColor(Color.GRAY);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        for(Object o : objects){
            o.Paint(o.x - cameraX, o.y - cameraY, g);
        }
        // Draw the player
        int playerDrawX = player.x - cameraX;
        int playerDrawY = player.y - cameraY;
        g.setColor(Color.RED);
        g.fillRect(playerDrawX, playerDrawY, player.width, player.height);
    }
}
