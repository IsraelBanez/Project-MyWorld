import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Factory
{

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public static Miner_Full createMinerFull(String id, int resourceLimit, Point pos, int actionPeriod, int animationPeriod,
                                       List<PImage> images)
    {
        return new Miner_Full( id, pos, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public static Miner_Not_Full createMinerNotFull(String id, int resourceLimit,  Point pos, int actionPeriod,
                                              int animationPeriod,
                                              List<PImage> images) {
        return new Miner_Not_Full(id, pos, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Ore_Blob createOreBlob(String id, Point pos, int actionPeriod, int animationPeriod,
                                   List<PImage> images)
    {
        return new Ore_Blob(id, pos, images, actionPeriod, animationPeriod);
    }

    public static Zombie createZombie(Point position, List<PImage> images)
    {
        return new Zombie("zombie", position, images, 1000, 1000);
    }

    public static Quake createQuake( Point position, List<PImage> images)
    {
        return new Quake( QUAKE_ID, position, images,
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Ore createOre(String id, Point pos, int actionPeriod, List<PImage> images)
    {
        return new Ore(id, pos, images, actionPeriod);
    }

    public static Zombie_Spawn createZombie_Spawn(String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Zombie_Spawn(id, position, images, actionPeriod);
    }
    public static Blacksmith createBlacksmith(String id, Point pos, List<PImage> images)
    {
        return new Blacksmith(id, pos, images);
    }
    public static Vein createVein(String id, Point pos, int actionPeriod, List<PImage> images)
    {
        return new Vein( id, pos, images, actionPeriod);
    }
    public static Obstacle createObstacle(String id, Point pos, List<PImage> images)
    {
        return new Obstacle(id, pos, images);
    }
    public static Tomb createTomb(String id, Point pos, List<PImage> images)
    {
        return new Tomb(id, pos, images);
    }

    public static Fire createFire(String id, Point pos, List<PImage> images)
    {
        return new Fire(id, pos, images);
    }

    public static Smoke createSmoke(String id, Point pos, List<PImage> images)
    {
        return new Smoke(id, pos, images);
    }

    public static Mage createMage(String id, Point pos, List<PImage> images)
    {
        return new Mage(id, pos, images);
    }

    public static Skeleton createSkeleton(Point position, List<PImage> images)
    {
        return new Skeleton("skeleton", position, images, 425, 200);}

}
