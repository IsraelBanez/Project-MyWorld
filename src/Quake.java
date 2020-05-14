import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Quake implements AnimationEntity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private  int animationPeriod;

    private static final String QUAKE_KEY = "quake";
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public int getActionPeriod() {return actionPeriod;}

    public int getAnimationPeriod() { return animationPeriod;}

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

    public String getBGND() { return QUAKE_KEY;}

    public  Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }

    public  Action createActivityAction( WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents( this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction( world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

}
