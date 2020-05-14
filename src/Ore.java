import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Ore implements ActivityEntity
{
    private String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;

    private static final Random rand = new Random();
    private static final String ORE_KEY = "ore";
    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    public Ore(
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
    public int getActionPeriod() { return actionPeriod;}

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

    public String getBGND() { return ORE_KEY;}

    public  Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }

    private  Action createActivityAction( WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore,
                                   EventScheduler scheduler)
    {
        Point pos = this.position;

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        AnimationEntity blob = world.createOreBlob(this.id + BLOB_ID_SUFFIX, pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(BLOB_ANIMATION_MAX
                        - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity( blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }


    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);


    }

}
