import processing.core.PImage;

import java.util.*;

public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    private static final int ORE_REACH = 1;

    private static final int PROPERTY_KEY = 0;


    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String MINER_KEY = "miner";
    private static final int MINER_NUM_PROPERTIES = 7;
    private static final int MINER_ID = 1;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_ANIMATION_PERIOD = 6;

    private static final String VEIN_KEY = "vein";
    private static final int VEIN_NUM_PROPERTIES = 5;
    private static final int VEIN_ID = 1;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ROW = 3;
    private static final int VEIN_ACTION_PERIOD = 4;

    private static final String SMITH_KEY = "blacksmith";
    private static final int SMITH_NUM_PROPERTIES = 4;
    private static final int SMITH_ID = 1;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;

    private static final String TOMB_KEY = "tomb";
    private static final int TOMB_NUM_PROPERTIES = 4;
    private static final int TOMB_ID = 1;
    private static final int TOMB_COL = 2;
    private static final int TOMB_ROW = 3;

    private static final String FIRE_KEY = "fire";
    private static final int FIRE_NUM_PROPERTIES = 4;
    private static final int FIRE_ID = 1;
    private static final int FIRE_COL = 2;
    private static final int FIRE_ROW = 3;


    private static final String SMOKE_KEY = "smoke";
    private static final int SMOKE_NUM_PROPERTIES = 4;
    private static final int SMOKE_ID = 1;
    private static final int SMOKE_COL = 2;
    private static final int SMOKE_ROW = 3;

    private static final String MAGE_KEY = "mage";
    private static final int MAGE_NUM_PROPERTIES = 4;
    private static final int MAGE_ID = 1;
    private static final int MAGE_COL = 2;
    private static final int MAGE_ROW = 3;

    private static final String ORE_KEY = "ore";
    private static final int ORE_NUM_PROPERTIES = 5;
    private static final int ORE_ID = 1;
    private static final int ORE_COL = 2;
    private static final int ORE_ROW = 3;
    private static final int ORE_ACTION_PERIOD = 4;


    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }


    public int getNumCols() { return numCols; }

    public int getNumRows() { return numRows; }

    public Background[][] getBackground(){
        return this.background;
    }

    public Entity[][] getOccupancy(){
        return this.occupancy;
    }

    public Set<Entity> getEntities() { return entities;}

    public  Optional<PImage> getBackgroundImage( Point pos)
    {
        if (this.withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public  void setBackground( Point pos, Background background)
    {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell( pos, background);
        }
    }

    public  Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell( pos));
        }
        else {
            return Optional.empty();
        }
    }

    public  Entity getOccupancyCell( Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    private  void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    private  Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    private  void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }

    public  void load(Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!this.processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private  boolean processLine(String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return this.parseBackground(properties, imageStore);
                case MINER_KEY:
                    return this.parseMiner(properties, imageStore);
                case OBSTACLE_KEY:
                    return this.parseObstacle(properties, imageStore);
                case ORE_KEY:
                    return this.parseOre(properties, imageStore);
                case SMITH_KEY:
                    return this.parseSmith(properties, imageStore);
                case VEIN_KEY:
                    return this.parseVein(properties, imageStore);
                case TOMB_KEY:
                    return this.parseTomb(properties, imageStore);
                case FIRE_KEY:
                    return this.parseFire(properties, imageStore);
                case SMOKE_KEY:
                    return this.parseSmoke(properties, imageStore);
                case MAGE_KEY:
                    return this.parseMage(properties, imageStore);

            }
        }

        return false;
    }

    private  boolean parseBackground(String[] properties, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            this.setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    private  boolean parseMiner(String[] properties, ImageStore imageStore)
    {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            Entity entity = Factory.createMinerNotFull(properties[MINER_ID],
                    Integer.parseInt(properties[MINER_LIMIT]), pt,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]), Integer.parseInt(
                            properties[MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList( MINER_KEY));
            this.tryAddEntity( entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    private  boolean parseObstacle(String[] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = Factory.createObstacle(properties[OBSTACLE_ID], pt,
                    imageStore.getImageList(OBSTACLE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    private  boolean parseOre(String[] properties, ImageStore imageStore)
{
    if (properties.length == ORE_NUM_PROPERTIES) {
        Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                Integer.parseInt(properties[ORE_ROW]));
        Entity entity = Factory.createOre(properties[ORE_ID],pt, Integer.parseInt(
                properties[ORE_ACTION_PERIOD]),
                imageStore.getImageList( ORE_KEY));
        this.tryAddEntity(entity);
    }

    return properties.length == ORE_NUM_PROPERTIES;
}

    private  boolean parseSmith(String[] properties, ImageStore imageStore)
    {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = Factory.createBlacksmith(properties[SMITH_ID], pt,
                    imageStore.getImageList( SMITH_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }



    private  boolean parseVein(String[] properties, ImageStore imageStore)
    {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = Factory.createVein(properties[VEIN_ID], pt,
                    Integer.parseInt(
                            properties[VEIN_ACTION_PERIOD]),
                    imageStore.getImageList(VEIN_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }
    public  Optional<Entity> findNearest(Point pos, Class kind)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities) {
            if (kind.isInstance(entity)) {
                ofType.add(entity);
            }
        }

        return pos.nearestEntity(ofType);
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    public  void addEntity( Entity entity) {
        if (this.withinBounds(entity.getPosition())) {
            this.setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public  void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds( pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell( oldPos, null);
            this.removeEntityAt( pos);
            this.setOccupancyCell( pos, entity);
            entity.setPosition(pos);
        }
    }

    public  void removeEntity(Entity entity) {
        this.removeEntityAt( entity.getPosition());
    }

    public  void removeEntityAt(Point pos) {
        if (this.withinBounds( pos) && this.getOccupancyCell( pos) != null) {
            Entity entity = this.getOccupancyCell( pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    private  void tryAddEntity( Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity( entity);
    }

    public  boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }

    public  boolean isOccupied( Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell( pos) != null;
    }

    public  Optional<Point> findOpenAround( Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (this.withinBounds( newPt) && !this.isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    private  boolean parseTomb(String[] properties, ImageStore imageStore)
    {
        if (properties.length == TOMB_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TOMB_COL]),
                    Integer.parseInt(properties[TOMB_ROW]));
            Entity entity = Factory.createTomb(properties[TOMB_ID], pt,
                    imageStore.getImageList(TOMB_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == TOMB_NUM_PROPERTIES;
    }

    private  boolean parseFire(String[] properties, ImageStore imageStore)
    {
        if (properties.length == FIRE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FIRE_COL]),
                    Integer.parseInt(properties[FIRE_ROW]));
            Entity entity = Factory.createFire(properties[FIRE_ID], pt,
                    imageStore.getImageList(FIRE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == FIRE_NUM_PROPERTIES;
    }

    private  boolean parseSmoke(String[] properties, ImageStore imageStore)
    {
        if (properties.length == SMOKE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMOKE_COL]),
                    Integer.parseInt(properties[SMOKE_ROW]));
            Entity entity = Factory.createSmoke(properties[SMOKE_ID], pt,
                    imageStore.getImageList(SMOKE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == SMOKE_NUM_PROPERTIES;
    }

    private  boolean parseMage(String[] properties, ImageStore imageStore)
    {
        if (properties.length == MAGE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MAGE_COL]),
                    Integer.parseInt(properties[MAGE_ROW]));
            Entity entity = Factory.createMage(properties[MAGE_ID], pt,
                    imageStore.getImageList(MAGE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == MAGE_NUM_PROPERTIES;
    }


}



