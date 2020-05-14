import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Miner_Not_Full implements AnimationEntity, ActivityEntity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    private static final String MINER_KEY = "miner";

    public Miner_Not_Full(

            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getActionPeriod() { return actionPeriod;}

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

    public String getBGND() { return MINER_KEY;}

    public  Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }

    private  Action createActivityAction( WorldModel world, ImageStore imageStore)
    {
        return new Activity( this, world, imageStore);
    }


    public  void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.position, Ore.class);

        if (!notFullTarget.isPresent() || !this.moveToNotFull(world,
                notFullTarget.get(),
                scheduler)
                || !this.transformNotFull( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction( world, imageStore),
                    this.actionPeriod);
        }
    }

    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction( world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction( 0),
                this.getAnimationPeriod());
    }

    private  boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            AnimationEntity miner = world.createMinerFull(this.id, this.resourceLimit, this.position,
                    this.actionPeriod,
                    this.animationPeriod,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity( miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private  boolean moveToNotFull( WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent( target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity( target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return false;
        }
    }

    private  Point nextPositionMiner( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        if (horiz == 0 || world.isOccupied( newPos)) {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);

            if (vert == 0 || world.isOccupied( newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
