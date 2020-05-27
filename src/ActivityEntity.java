import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public abstract class ActivityEntity extends Entity
{
    private int actionPeriod;

    public ActivityEntity(int actionPeriod, String id, Point position, List<PImage> images, int imageIndex, String bgnd){
        super(id, position, images, imageIndex, bgnd);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {return actionPeriod;}
    abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore);
    }

}
