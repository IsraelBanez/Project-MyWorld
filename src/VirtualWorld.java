
import java.io.File;
import java.io.FileNotFoundException;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";
    private static final String LOAD_FILE_RED = "myWorld.sav";
    private static final String LOAD_FILE_TOMB = "raiseTomb.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime( time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;

            }
            view.shiftView( dx, dy);
        }
    }

    public void mouseClicked(MouseEvent e) {
        int actionPeriod = 10000;
        List<Point> radius = new ArrayList<>();
        List<Point> radius2 = new ArrayList<>();
        List<Point> radius3 = new ArrayList<>();
        int x = mouseX / TILE_WIDTH;
        int y = mouseY / TILE_HEIGHT;
        int pressedX = x + view.getViewport().getCol();
        int pressedY = y + view.getViewport().getRow();
        Point point = new Point(pressedX, pressedY);
        if (e.getCount() == 2) {
            loadWorld(world, LOAD_FILE_RED, imageStore);
            loadWorld(world, LOAD_FILE_TOMB, imageStore);


            Background redFloor = new Background("red", imageStore.getImageList("red"));

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Point p = new Point(point.getX() + i, point.getY() + j);
                    radius.add(p);
                }
            }


            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Point p = new Point(point.getX() + i, point.getY() + j);
                    radius2.add(p);
                }
            }
            for (Point p : radius2) {
                world.removeEntityAt(p);
                world.setBackground(p, redFloor);

            }
            for (Point p : radius) {
                world.removeEntityAt(p);
                world.addEntity(new Smoke("smoke", p, imageStore.getImageList("smoke")));
            }

            for (int i = -8; i <= 8; i++) {
                for (int j = -8; j <= 8; j++) {
                    Point p = new Point(point.getX() + i, point.getY() + j);
                    radius3.add(p);
                }
            }
            for (Point p : radius3) {
                if (world.isOccupied(p) && (world.getOccupant(p).get().getClass() == Miner_Not_Full.class || world.getOccupant(p).get().getClass() == Miner_Full.class)) {
                    scheduler.unscheduleAllEvents(world.getOccupant(p).get());
                    world.removeEntity(world.getOccupant(p).get());

                    Skeleton zombie = new Skeleton("skeleton", p,
                            imageStore.getImageList("skeleton"), 100,
                            100);

                    world.addEntity(zombie);
                    zombie.scheduleActions(scheduler, world, imageStore);
                }
                Zombie_Spawn spawner = Factory.createZombie_Spawn("zombie", new Point(point.getX(), point.getY() + 2)
                        , actionPeriod, imageStore.getImageList("purpleSmoke"));
                world.addEntity(spawner);
                spawner.scheduleActions(scheduler, world, imageStore);
            }

            world.removeEntityAt(new Point(point.getX(), point.getY()));
            world.setBackground(new Point(point.getX(), point.getY()), new Background("rocks", imageStore.getImageList("rocks")));

            String fire = "fire";
            Fire firespawn = new Fire(fire, new Point(point.getX() + 2, point.getY() + 2), imageStore.getImageList(fire));
            world.addEntity(firespawn);
            Fire firespawn2 = new Fire(fire, new Point(point.getX() + 2, point.getY() - 2), imageStore.getImageList(fire));
            world.addEntity(firespawn2);
            Fire firespawn3 = new Fire(fire, new Point(point.getX() - 2, point.getY() + 2), imageStore.getImageList(fire));
            world.addEntity(firespawn3);
            Fire firespawn4 = new Fire(fire, new Point(point.getX() - 2, point.getY() - 2), imageStore.getImageList(fire));
            world.addEntity(firespawn4);
            String mage = "mage";
            Mage mageSpawn = new Mage(mage, point, imageStore.getImageList(mage));
            world.addEntity(mageSpawn);

        }

        if (e.getCount() == 1 ) {

            world.removeEntityAt(new Point(point.getX(), point.getY()));
            world.removeEntityAt(new Point(point.getX() - 1, point.getY()));
            world.removeEntityAt( new Point(point.getX() + 1, point.getY()));
            Obstacle voidspawn = new Obstacle("midVoid", new Point(point.getX(), point.getY()), imageStore.getImageList("midVoid"));
            world.addEntity(voidspawn);
            Obstacle voidspawn2 = new Obstacle("leftVoid", new Point(point.getX() - 1, point.getY()), imageStore.getImageList("leftVoid"));
            world.addEntity(voidspawn2);
            Obstacle voidspawn3 = new Obstacle("rightVoid", new Point(point.getX() + 1, point.getY()), imageStore.getImageList("rightVoid"));
            world.addEntity(voidspawn3);

        }

    }
    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if(entity instanceof ActivityEntity)
                ((ActivityEntity)entity).scheduleActions(scheduler, world, imageStore);
        }
    }

    private static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
