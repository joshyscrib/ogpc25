import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Game extends JPanel {
    public boolean dying = false;
    public boolean darking = false;
    public boolean lighting = false;

    public int creditsY = 2500;
    private Clip clip;  // 👈 declared here so all methods can access it
    private static final int TILE_SIZE = 32;
    private float volume = 0;
    private static final int GRAVITY = 4;

    private static final int WINDOW_MARGIN = 50;

    private int screenWidth;
    private int screenHeight;
    private int gridRows;
    private int gridCols;
    private int mouseX;
    private int mouseY;
    public int playerTick = 0;
    int deathOpacity = 0;
    private int velocityY = 0;
    private boolean isJumping = false;
    public int curFloor = 1;
    public int waterHeight = 0;
    private boolean[] keys = new boolean[256];
    private boolean showGrid = false;
    private Tile[][] tiles;
    public int timeDrowning = 0;

    private boolean placingTile = false;
    private boolean mousePressed = false;
    public int tilePlaceType = 1;
    public boolean paused = true;
    Hud hud = new Hud();
    private static final int WORLD_WIDTH = 4562; // Larger world width
    private static final int WORLD_HEIGHT = 1312; // Larger world height
    private int cameraX = 0; // Camera position
    private int cameraY = 128;
    public Integer[][] tileNums = new Integer[gridCols][gridRows];
    public ArrayList<Object> objects = new ArrayList<>();
    public boolean title = true;
    Player player;
    Grandma grandma = new Grandma();
    Grandpa grandpa = new Grandpa();
    GrandmaJump grandmaJump = new GrandmaJump();
    GrandpaJump grandpaJump = new GrandpaJump();
    int ghostframe = 0;
    Image player1;
    Image playerL1;
    Image player2;
    Image playerL2;
    Image player3;
    Image playerL3;
    public boolean credits = false;
    static Image creditImg;

    public Position spawnPoint = new Position(250, WORLD_HEIGHT - 450);
    public boolean respawning = false;
    public void startGame(){
        title = false;
        paused = false;
    }
    public Game() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width - WINDOW_MARGIN;
        screenHeight = screenSize.height - WINDOW_MARGIN;
        creditsY = screenHeight;
        System.out.println(screenWidth + " : " + screenHeight);

        //   tiles = new Tile[gridCols][gridRows];
        //   initializeTiles();

        setFocusable(true);

        // Add key listener for movement and jumping

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                startGame();
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
        //asldjfklsdfklsdjfkdlsfj

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
                    if (tilePlaceType > 10) {
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
        gridCols = WORLD_WIDTH / TILE_SIZE;
        gridRows = WORLD_HEIGHT / TILE_SIZE;
        tiles = new Tile[gridCols][gridRows];
        initializeTiles();
    }

    public void PlayMusic(String file) {
        try {
            if (clip != null) {
                clip.stop();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(file));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            setVolume(-0.10f);
            System.out.println("Music started!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String filepath) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filepath));
                Clip soundClip = AudioSystem.getClip();
                soundClip.open(audioInputStream);
                soundClip.start();

                // auto close the clip when done
                soundClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        soundClip.close();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setVolume(float decibels) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(decibels);
        }
    }

    public void placeTile(int type, int col, int row) {

        switch (type) {
            case 1:
                tiles[col][row] = new AirTile();
                break;
            case 2:
                tiles[col][row] = new BlockTile();
                break;
            case 3:
                tiles[col][row] = new BoundaryTile();
                break;
            case 4:
                tiles[col][row] = new KillTile();
                break;
            case 5:
                tiles[col][row] = new DoorTile();
                break;
            case 6:
                tiles[col][row] = new PaintingTile1();
                break;
            case 7:
                tiles[col][row] = new PaintingTile2();
                break;
            case 8:
                tiles[col][row] = new PaintingTile3();
                break;
            case 9:
                tiles[col][row] = new PaintingTile4();
                break;
            case 10:
                tiles[col][row] = new BarrierTile();
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Fate of Pittock Mansion");
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

    public ArrayList<Position> cratesLvl1 = new ArrayList<Position>();
    public ArrayList<Position> cratesLvl2 = new ArrayList<Position>();
    public ArrayList<Position> cratesLvl3 = new ArrayList<Position>();

    private void initializeTiles() {
        grandma.x = -35;
        grandma.y = 945;
        grandpa.x = gridCols * 31;
        grandpa.y = 720;
        PlayMusic("/bgmusic.wav");
        // if x is even then solid
        //if x is odd then moveable
        cratesLvl1.add(new Position(462, 989));
        cratesLvl1.add(new Position(2095, 989));

        cratesLvl2.add(new Position(2248, 989));
        cratesLvl2.add(new Position(3901, 989));

        cratesLvl3.add(new Position(1061, 989));
        cratesLvl3.add(new Position(3157, 989));
        cratesLvl3.add(new Position(3758, 989));


        for (int col = 0; col < gridCols; col++) {
            for (int row = 0; row < gridRows; row++) {

                tiles[col][row] = new AirTile(); // Transparent tiles
                tiles[col][row].x = col;
                tiles[col][row].y = row;
                if (row >= gridRows - 7 || row <= 8 || col <= 1 || col >= gridCols - 2) {
                    tiles[col][row] = new BoundaryTile(); // Solid floor tiles
                    tiles[col][row].x = col;
                    tiles[col][row].y = row;
                }

            }
        }

        load(1);
        curFloor = 1;
    }

    public void load(int level) {
        respawn();
        curFloor = level;
        waterHeight = 0;
        waterTick = 0;
        objects.clear();
        switch (level) {
            case 1:

                for (Position o : cratesLvl1) {
                    if (o.x % 2 == 0) {
                        objects.add(new SolidCrate(o.x, o.y));
                    } else {
                        objects.add(new Crate(o.x, o.y));
                    }
                }
                ;
                break;
            case 2:
                for (Position o : cratesLvl2) {
                    if (o.x % 2 == 0) {
                        objects.add(new SolidCrate(o.x, o.y));
                    } else {
                        objects.add(new Crate(o.x, o.y));
                    }
                }
                ;
                break;
            case 3:
                PlayMusic("/chasemusic.wav");
                for (Position o : cratesLvl3) {
                    if (o.x % 2 == 0) {
                        objects.add(new SolidCrate(o.x, o.y));
                    } else {
                        objects.add(new Crate(o.x, o.y));
                    }
                }
                ;
                break;

        }
        try {
            String loadLvl = Files.readString(Paths.get("C:\\Users\\josht\\OneDrive\\Documents\\GameLevels\\game" + level + ".txt"), StandardCharsets.UTF_8);
            String[] stringArray = loadLvl.split(",");
            for (int i = 0; i < stringArray.length; i++) {
                int col = 0;
                int row = 0;
                col = i / gridRows;
                row = i % gridRows;
                int tileId = Integer.parseInt(stringArray[i]);
                switch (tileId) {
                    case 1:
                        tiles[col][row] = new AirTile();
                        break;
                    case 2:
                        tiles[col][row] = new BlockTile();
                        break;
                    case 3:
                        tiles[col][row] = new BoundaryTile();
                        break;
                    case 4:
                        tiles[col][row] = new KillTile();
                        break;
                    case 5:
                        tiles[col][row] = new DoorTile();
                        break;
                    case 6:
                        tiles[col][row] = new PaintingTile1();
                        break;
                    case 7:
                        tiles[col][row] = new PaintingTile2();
                        break;
                    case 8:
                        tiles[col][row] = new PaintingTile3();
                        break;
                    case 9:
                        tiles[col][row] = new PaintingTile4();
                        break;
                    case 10:
                        tiles[col][row] = new BarrierTile();
                        break;
                }
                tiles[col][row].x = col;
                tiles[col][row].y = row;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int curPlayerFrame = 0;
    boolean playerLeft = false;

    public void save(int level) {
        /*StringBuilder tileSave = new StringBuilder();
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                tileSave.append(tiles[i][j].tileType);
                tileSave.append(",");
            }
        }
        String tileNums = tileSave.toString();

        try {
            Files.writeString(Paths.get("C:\\Users\\josht\\OneDrive\\Documents\\GameLevels\\game" + level + ".txt"), tileNums, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } */
    }

    int lastCrate = 0;
    int waterTick = 0;
    int jumpx = (screenWidth / 2) + 750 - 50;
    int jumpy = (screenHeight / 2) + 120 - 50;
    boolean scaring = false;

    public void jumpScare(boolean granpa) {
        playSoundEffect("/jumpscareah.wav");
        scaring = true;
        if (granpa) {
            Timer timer = new Timer(90, null);  // fire every 20 ms
            ActionListener[] actions = new ActionListener[20];

            actions[0] = e -> {
                grandpaJump.x1 = jumpx;
                grandpaJump.y1 = jumpy;
                timer.setActionCommand("1");
            };
            actions[1] = e -> {
                grandpaJump.x1 = -2000;
                grandpaJump.y1 = 1400;
                grandpaJump.x2 = jumpx;
                grandpaJump.y2 = jumpy;
                timer.setActionCommand("2");
            };
            actions[2] = e -> {
                grandpaJump.x2 = -2000;
                grandpaJump.y2 = 1400;
                grandpaJump.x3 = jumpx;
                grandpaJump.y3 = jumpy;
            };
            actions[3] = e -> {
                grandpaJump.x3 = -2000;
                grandpaJump.y3 = 1400;
                grandpaJump.x4 = jumpx;
                grandpaJump.y4 = jumpy;
            };
            actions[4] = e -> {
                grandpaJump.x4 = -2000;
                grandpaJump.y4 = 1400;
                grandpaJump.x5 = jumpx;
                grandpaJump.y5 = jumpy;
            };
            actions[5] = e -> {
                grandpaJump.x5 = -2000;
                grandpaJump.y5 = 1400;
                grandpaJump.x6 = jumpx;
                grandpaJump.y6 = jumpy;
            };
            actions[6] = e -> {
            };
            actions[7] = e -> {
            };
            actions[8] = e -> {
            };
            actions[9] = e -> {
            };
            actions[10] = e -> {
            };
            actions[11] = e -> {
            };
            actions[12] = e -> {
            };
            actions[13] = e -> {
            };
            actions[14] = e -> {
            };
            actions[15] = e -> {
            };
            actions[16] = e -> {
            };
            actions[17] = e -> {
            };
            actions[18] = e -> {
            };

            actions[19] = e -> {
                grandpaJump.x6 = -2000;
                grandpaJump.y6 = 1400;
                timer.stop();
                scaring = false;
            };

            timer.addActionListener(new ActionListener() {
                int step = 0;

                public void actionPerformed(ActionEvent e) {
                    actions[step].actionPerformed(e);
                    step++;
                }
            });
            timer.start();
        } else {
            Timer timer = new Timer(90, null);  // fire every 20 ms
            ActionListener[] actions = new ActionListener[20];

            actions[0] = e -> {
                grandmaJump.x1 = jumpx;
                grandmaJump.y1 = jumpy;
                timer.setActionCommand("1");
            };
            actions[1] = e -> {
                grandmaJump.x1 = -2000;
                grandmaJump.y1 = 1400;
                grandmaJump.x2 = jumpx;
                grandmaJump.y2 = jumpy;
                timer.setActionCommand("2");
            };
            actions[2] = e -> {
                grandmaJump.x2 = -2000;
                grandmaJump.y2 = 1400;
                grandmaJump.x3 = jumpx;
                grandmaJump.y3 = jumpy;
            };
            actions[3] = e -> {
                grandmaJump.x3 = -2000;
                grandmaJump.y3 = 1400;
                grandmaJump.x4 = jumpx;
                grandmaJump.y4 = jumpy;
            };
            actions[4] = e -> {
                grandmaJump.x4 = -2000;
                grandmaJump.y4 = 1400;
                grandmaJump.x5 = jumpx;
                grandmaJump.y5 = jumpy;
            };
            actions[5] = e -> {
                grandmaJump.x5 = -2000;
                grandmaJump.y5 = 1400;
                grandmaJump.x6 = jumpx;
                grandmaJump.y6 = jumpy;
            };
            actions[6] = e -> {
            };
            actions[7] = e -> {
            };
            actions[8] = e -> {
            };
            actions[9] = e -> {
            };
            actions[10] = e -> {
            };
            actions[11] = e -> {
            };
            actions[12] = e -> {
            };
            actions[13] = e -> {
            };
            actions[14] = e -> {
            };
            actions[15] = e -> {
            };
            actions[16] = e -> {
            };
            actions[17] = e -> {
            };
            actions[18] = e -> {
            };

            actions[19] = e -> {
                grandmaJump.x6 = -2000;
                grandmaJump.y6 = 1400;
                timer.stop();
                scaring = false;
            };

            timer.addActionListener(new ActionListener() {
                int step = 0;

                public void actionPerformed(ActionEvent e) {
                    actions[step].actionPerformed(e);
                    step++;
                }
            });
            timer.start();
        }
        respawn();
    }
    public void win(){
        credits = true;
    }

    public void update() {
        if(!player.alive && deathOpacity < 255){
            deathOpacity++;
        }
        if(player.alive && deathOpacity > 0){
            deathOpacity--;
        }
        if(credits){
            paused = true;
            creditsY -= 2;
            if(creditsY < 0 - 1795 * 4){
                System.exit(0);
            }
        }

        playerTick++;
        if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_D]) {
            if ((playerTick % 6) == 0) {
                curPlayerFrame++;
            }
        }
        if (curPlayerFrame > 2) {
            curPlayerFrame = 0;
        }
        if (keys[KeyEvent.VK_P]) {

            for (Object c : objects) {
                if (c.getClass() == Crate.class) {
                    System.out.println("Crate, " + c.x + " : " + c.y);
                }
                if (c.getClass() == SolidCrate.class) {
                    System.out.println("Solid, " + c.x + " : " + c.y);
                }
            }
            System.out.println("END---------------END-------------------END----------------------END");
        }
        waterTick++;
        if (waterTick % 10 == 0 && !paused) {
            waterHeight++;
        }
        if (timeDrowning >= 175) {
            die();
        }

        lastCrate++;
        if (tiles[(int) Math.floor(player.x / TILE_SIZE)][(int) Math.floor((player.y + player.height) / TILE_SIZE)].tileType == 4) {
            die();

        }
        if (tiles[(int) Math.floor(player.x / TILE_SIZE + 1)][(int) Math.floor((player.y) / TILE_SIZE)].tileType == 5) {
            if(curFloor == 3){
                win();
            }
            else{
                load(curFloor + 1);
            }
        }
        boolean crateCollision = false;

        // Handle horizontal movement with collision detection
        if (keys[KeyEvent.VK_ENTER]) {
            if (keys[KeyEvent.VK_1]) {
                save(1);
            }
            if (keys[KeyEvent.VK_2]) {
                save(2);
            }
            if (keys[KeyEvent.VK_3]) {
                save(3);
            }
        }
        if (keys[KeyEvent.VK_L]) {
            if (keys[KeyEvent.VK_1]) {
                load(1);
            }
            if (keys[KeyEvent.VK_2]) {
                load(2);
            }
            if (keys[KeyEvent.VK_3]) {
                load(3);
            }
        }
        if (keys[KeyEvent.VK_C] && lastCrate > 20) {
            Crate crate = new Crate(player.x + player.width, player.y - 128);
            objects.add(crate);
            lastCrate = 0;
        }

        if (keys[KeyEvent.VK_V] && lastCrate > 20) {
            SolidCrate scrate = new SolidCrate(player.x + player.width, player.y - 128);
            objects.add(scrate);
            lastCrate = 0;
        }
        if (keys[KeyEvent.VK_A] && !paused) {
            playerLeft = true;
            // Move left
            int targetX = player.x - player.MOVE_SPEED;  // Target position if no collision
            int colLeft = targetX / TILE_SIZE;
            int colRight = (targetX + player.width - 1) / TILE_SIZE;

            // Check for collision with the tiles
            if (colLeft >= 0 && colRight < gridCols) {
                boolean collision = false;
                //crate stuff
                for (Object o : objects) {
                    if (o.getClass() == Crate.class || o.getClass() == SolidCrate.class) {
                        if (tiles[(o.x - 1) / TILE_SIZE][(o.y + (o.height - 2)) / TILE_SIZE].isSolid) {
                            if (CollisionDetection.DoThingsCollide(new Position(targetX, player.y - 4), player.width, player.height, new Position(o.x, o.y + 5), o.width, o.height)) {
                                crateCollision = true;
                            }
                        }
                        if (!tiles[(o.x - 1) / TILE_SIZE][(o.y + (o.height - 2)) / TILE_SIZE].isSolid) {
                            if (CollisionDetection.DoThingsCollide(new Position(targetX, player.y), player.width, player.height, new Position(o.x, o.y + 5), o.width, o.height)) {
                                if (player.y + player.height >= o.y + o.height - 6 && player.y + player.height <= o.y + o.height + 6 && o.getClass() == Crate.class) {
                                    o.x -= player.MOVE_SPEED;
                                } else {
                                    crateCollision = true;
                                }
                            }
                        }


                    }
                }


                for (int row = (player.y / TILE_SIZE); row < (player.y + player.height) / TILE_SIZE; row++) {
                    if (tiles[colLeft][row].isSolid() || tiles[colRight][row].isSolid()) {
                        collision = true;
                        break;
                    }
                    if (tiles[colLeft][row].tileType == 4 || tiles[colRight][row].tileType == 4) {
                        die();
                        targetX = player.x;
                        break;
                    }
                }

                if (!collision && !crateCollision) {
                    player.x = targetX;  // Move the player to the new position
                } else if (!crateCollision) {
                    // Stop the player at the left boundary of the wall
                    player.x = (colLeft + 1) * TILE_SIZE;
                }
            }
            for (Object obj : objects) {
                if (obj.solid == true && player.x >= obj.x && player.x <= obj.x + obj.size && player.y == 0) {
                }
            }
        }
//test
        if (CollisionDetection.DoThingsCollide(new Position(player.x, player.y), 32, 64, new Position(grandma.x + 15, grandma.y), 100, 128)) {
            jumpScare(false);
        }
        if (CollisionDetection.DoThingsCollide(new Position(player.x, player.y), 32, 64, new Position(grandpa.x + 15, grandpa.y), 100, 128)) {
            jumpScare(true);
        }
        if (keys[KeyEvent.VK_D] && !paused) {
            playerLeft = false;
            // Move right
            int targetX = player.x + player.MOVE_SPEED;  // Target position if no collision
            int colLeft = targetX / TILE_SIZE;
            int colRight = (targetX + player.width - 1) / TILE_SIZE;

            // Check for collision with the tiles
            if (colLeft >= 0 && colRight < gridCols) {

                boolean collision = false;
                for (Object o : objects) {
                    if (o.getClass() == Crate.class || o.getClass() == SolidCrate.class) {
                        if (tiles[(o.x + o.width + 1) / TILE_SIZE][(o.y + (o.height - 2)) / TILE_SIZE].isSolid) {
                            if (CollisionDetection.DoThingsCollide(new Position(targetX, player.y), player.width, player.height, new Position(o.x, o.y + 5), o.width, o.height)) {
                                crateCollision = true;
                            }
                        }

                        if (!tiles[(o.x + o.width + 1) / TILE_SIZE][(o.y + (o.height - 2)) / TILE_SIZE].isSolid) {
                            if (CollisionDetection.DoThingsCollide(new Position(targetX, player.y), player.width, player.height, new Position(o.x, o.y + 5), o.width, o.height)) {
                                if (player.y + player.height >= o.y + o.height - 6 && player.y + player.height <= o.y + o.height + 6 && o.getClass() == Crate.class) {
                                    o.x += player.MOVE_SPEED;
                                } else {
                                    crateCollision = true;
                                }
                            }
                        }


                    }
                }
                for (int row = (player.y / TILE_SIZE); row <= (player.y + player.height - 1) / TILE_SIZE; row++) {
                    if (tiles[colRight][row].isSolid()) {
                        collision = true;
                        break;
                    }
                    if (tiles[colRight][row].tileType == 4) {
                        player.alive = false;
                        die();
                        targetX = player.x;
                        break;
                    }
                }
                if (!collision && !crateCollision) {
                    player.x = targetX;  // Move the player to the new position
                } else if (!crateCollision) {
                    // Align the player to the left edge of the obstacle
                    player.x = colRight * TILE_SIZE - player.width;
                }
            }
        }
        if (curFloor == 3 && !paused) {
            if (grandpa.direction == Direction.Left) {
                grandpa.x -= 4;
                if (grandpa.x <= 0) {
                    grandpa.direction = Direction.Right;
                }
                if (grandpa.x >= gridCols * 32) {
                    grandpa.direction = Direction.Left;
                }
            }
            if (grandpa.direction == Direction.Right) {
                grandpa.x += 4;
                if (grandpa.x <= 0) {
                    grandpa.direction = Direction.Right;
                }
                if (grandpa.x >= gridCols * 32) {
                    grandpa.direction = Direction.Left;
                }
            }
            if (grandma.direction == Direction.Left) {
                grandma.x -= 3;
                if (grandma.x <= 0) {
                    grandma.direction = Direction.Right;
                }
                if (grandma.x >= gridCols * 32) {
                    grandma.direction = Direction.Left;
                }
            }
            if (grandma.direction == Direction.Right) {
                grandma.x += 3;
                if (grandma.x <= 0) {
                    grandma.direction = Direction.Right;
                }
                if (grandma.x >= gridCols * 32) {
                    grandma.direction = Direction.Left;
                }
            }
        }
        for (Object o : objects) {
            int targetX = 0;
            int targetY = 0;
            switch (o.moveDirection) {
                case Up:
                    targetX = o.x;
                    targetY = o.y - o.speed;
                    break;
                case Down:
                    targetX = o.x;
                    targetY = o.y + o.speed;
                    break;
                case Left:
                    targetX = o.x - o.speed;
                    targetY = o.y;
                    break;
                case Right:
                    targetX = o.x + o.speed;
                    targetY = o.y;
                    break;
                case None:
                    targetX = o.x;
                    targetY = o.y;
                    break;
            }
            Position targetPosition = new Position(targetX, targetY);
            boolean didCollide = false;
            for (int i = 0; i < gridCols; i++) {
                for (int j = 0; j < gridRows; j++) {
                    if (tiles[i][j].isSolid) {
                        if ((CollisionDetection.DoThingsCollide(targetPosition, o.width, o.height, new Position(i * TILE_SIZE, j * TILE_SIZE), 32, 32))) {
                            didCollide = true;
                            break;
                        }
                    }
                }
            }

            if (!didCollide) {
                o.x = targetPosition.x;
                o.y = targetPosition.y;
            }
        }

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
        boolean crateCollide = false;
        while (remaining > 0) {
            int move = Math.min(TILE_SIZE / 4, remaining);
            int nextY = player.y + step * move;
            int row = (nextY + player.height) / TILE_SIZE;
            int colLeft = player.x / TILE_SIZE;
            int colRight = (player.x + player.width - 1) / TILE_SIZE;
            for (Object o : objects) {
                if (o.getClass() == Crate.class || o.getClass() == SolidCrate.class) {
                    if (CollisionDetection.DoThingsCollide(new Position(player.x, player.y), player.width, player.height, new Position(o.x, o.y), o.width, o.height)) {
                        crateCollide = true;
                        player.y = o.y - player.height - 1;
                    }
                }
            }
            // Check for collisions below (falling and landing)
            if (step > 0) {
                if (row < gridRows && (tiles[colLeft][row].isSolid() || tiles[colRight][row].isSolid()) || crateCollide) {
                    player.y = row * TILE_SIZE - player.height;
                    velocityY = 0;
                    isJumping = false;
                    break;
                }

            }

            // Check for collisions above (ceiling while jumping)
            if (step < 0) {
                int ceilingRow = (nextY) / TILE_SIZE;
                if (ceilingRow >= 0 && (tiles[colLeft][ceilingRow].isSolid() || tiles[colRight][ceilingRow].isSolid())) {
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
        if (keys[KeyEvent.VK_SPACE] && !isJumping && !paused) {
            playSoundEffect("/jump.wav");
            velocityY = player.JUMP_STRENGTH;
            isJumping = true;
        }
        cameraX = Math.max(0, Math.min(player.x - screenWidth / 2, WORLD_WIDTH - screenWidth));
        cameraY = Math.max(0, Math.min(player.y - screenHeight / 2, WORLD_HEIGHT - screenHeight));

    }

    // testing
    public void respawn() {
        player.alive = true;
        timeDrowning = 0;
        grandma.x = -250;
        grandpa.x = 4500;
        player.x = spawnPoint.x;
        player.y = spawnPoint.y;
        respawning = true;
        waterHeight = 0;
        waterTick = 0;
    }
    public void die(){
        paused = true;
        player.alive = false;

    }
    static Image titleImage;
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setColor(new Color(80,40,17));
        g.fillRect(0,0,5000,5000);
        // Calculate visible tile range
        int startCol = Math.max(0, cameraX / TILE_SIZE);
        int endCol = Math.min(gridCols, (cameraX + screenWidth) / TILE_SIZE + 1);
        int startRow = Math.max(0, cameraY / TILE_SIZE);
        int endRow = Math.min(gridRows, (cameraY + screenHeight) / TILE_SIZE + 1);
        ArrayList<Position> paintingsToDraw = new ArrayList<Position>();
        ArrayList<Position> boundariesToDraw = new ArrayList<Position>();
        // Draw tiles in the visible range
        for (int col = startCol; col < endCol; col++) {
            for (int row = startRow; row < endRow; row++) {
                Tile tile = tiles[col][row];

                switch (tile.tileType) {
                    case 1 -> g.setColor(new Color(118, 77, 70));
                    case 2 -> g.setColor(new Color(40, 20, 20));
                    case 3 -> boundariesToDraw.add(new Position(col, row));
                    case 4 -> g.setColor(new Color(255, 1, 1));
                    case 5 -> boundariesToDraw.add(new Position(col, row));
                    case 6 -> paintingsToDraw.add(new Position(col, row));
                    case 7 -> paintingsToDraw.add(new Position(col, row));
                    case 8 -> paintingsToDraw.add(new Position(col, row));
                    case 9 -> paintingsToDraw.add(new Position(col, row));
                    case 10 -> g.setColor(new Color(0, 0, 0, 0));

                }

                int drawX = col * TILE_SIZE - cameraX;
                int drawY = row * TILE_SIZE - cameraY;
                g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                tile.drawTile(drawX, drawY, g);


                if (showGrid) {
                    g.setColor(Color.GRAY);
                    g.drawRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        // paintings
        for (Position t : paintingsToDraw) {
            if (t.x % 2 == 0) {
                tiles[t.x][t.y].drawPainting(t.x * 32 - cameraX, t.y * 32 - cameraY, true, g);
            } else {
                tiles[t.x][t.y].drawPainting(t.x * 32 - cameraX, t.y * 32 - cameraY, false, g);
            }
        }
        if (curFloor == 3) {
            if (waterTick % 12 == 0) {
                ghostframe++;
            }
            if (ghostframe % 2 == 0) {
                grandma.Paint(grandma.x - cameraX, grandma.y - cameraY, g, true);
                grandpa.Paint(grandpa.x - cameraX, grandpa.y - cameraY, g, true);
            } else {
                grandma.Paint(grandma.x - cameraX, grandma.y - cameraY, g, false);
                grandpa.Paint(grandpa.x - cameraX, grandpa.y - cameraY, g, false);
            }
        }
        int playerDrawX = player.x - cameraX - 22;
        int playerDrawY = player.y - cameraY;
        if (playerLeft) {
            switch (curPlayerFrame) {
                case 0:
                    if (playerL1 == null) {
                        try {
                            playerL1 = ImageIO.read(Game.class.getResource("Peter1Left.png"));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                    if (playerL1 != null) {
                        g.drawImage(playerL1, playerDrawX, playerDrawY, null);
                    }
                    break;
                case 1:
                    if (playerL2 == null) {
                        try {
                            playerL2 = ImageIO.read(Game.class.getResource("Peter2Left.png"));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                    if (playerL2 != null) {
                        g.drawImage(playerL2, playerDrawX, playerDrawY, null);
                    }
                    break;
                case 2:
                    if (playerL3 == null) {
                        try {
                            playerL3 = ImageIO.read(Game.class.getResource("Peter3Left.png"));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                    if (playerL3 != null) {
                        g.drawImage(playerL3, playerDrawX, playerDrawY, null);
                    }
                    break;
            }
        } else {
            switch (curPlayerFrame) {
                case 0:
                    if (player1 == null) {
                        try {
                            player1 = ImageIO.read(Game.class.getResource("Peter1.png"));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                    if (player1 != null) {
                        g.drawImage(player1, playerDrawX, playerDrawY, null);
                    }
                    break;
                case 1:
                    if (player2 == null) {
                        try {
                            player2 = ImageIO.read(Game.class.getResource("Peter2.png"));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                    if (player2 != null) {
                        g.drawImage(player2, playerDrawX, playerDrawY, null);
                    }
                    break;
                case 2:
                    if (player3 == null) {
                        try {
                            player3 = ImageIO.read(Game.class.getResource("Peter3.png"));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                    if (player3 != null) {
                        g.drawImage(player3, playerDrawX, playerDrawY, null);
                    }
                    break;
            }
        }
        // water
        g.setColor(new Color(20, 170, 250, 40));
        g.fillRect(0, (34 * TILE_SIZE) - waterHeight - cameraY, 9999999, screenHeight);
        // borders
        for (Position t : boundariesToDraw) {
            tiles[t.x][t.y].drawTile(t.x * 32 - cameraX, t.y * 32 - cameraY, g);
        }

        for (Object o : objects) {
            o.Paint(o.x - cameraX, o.y - cameraY, g);
        }

        if ((34 * TILE_SIZE) - waterHeight - cameraY <= playerDrawY) {
            g.setColor(new Color(80, 150, 255, 50));
            g.fillRect(0, 0, screenWidth, screenHeight);
            timeDrowning++;
        }
        else if(timeDrowning > 0){
            if(playerTick % 14 == 0){
                timeDrowning --;
            }
        }
        hud.paint(-20, screenHeight - 300+32,175-timeDrowning, g);
        if (scaring || !player.alive) {
            g.setColor(new Color(40, 40, 40, 145));
            g.fillRect(0, 0, 5000, 5000);
        }
        grandpaJump.paint(0, 0, g);
        grandmaJump.paint(0, 0, g);
        if(title){
            if(titleImage == null){
                try {
                    titleImage = ImageIO.read(Game.class.getResource("title.png"));
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            if(titleImage != null) {
                g.drawImage(titleImage, 0, 0,screenWidth, screenHeight, null);
            }
        }
        if(credits){
            if(creditImg == null){
                try {
                    creditImg = ImageIO.read(Game.class.getResource("gameCredits.png"));
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }

            if(creditImg != null) {
                g.drawImage(creditImg, 0, creditsY,screenWidth, 1700 * 4, null);
            }
        }
        g.setColor(new Color(10,10,10,deathOpacity));
        g.fillRect(0,0,10000,10000);
    }


}
