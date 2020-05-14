import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein implements ActivityEntity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;


    private static final Random rand = new Random();
    private static final String VEIN_KEY = "vein";
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final String ORE_KEY = "ore";

    public Vein(

            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;

    }

    public int getActionPeriod() {return actionPeriod;}

    public void setPosition(Point pos) { position = pos;}

    public  PImage getCurrentImage()
    {
        return this.images.get(this.imageIndex);
    }

    public  void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public String getId() { return id;}

    public Point getPosition() {return position;}

    public List<PImage> getImages() { return images;}

    public int getImageIndex() { return imageIndex;}

    public String getBGND() { return VEIN_KEY;}

    private  Action createActivityAction( WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore);
    }

    public  void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent()) {
            ActivityEntity ore = world.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore),
                this.actionPeriod);

    }

}
