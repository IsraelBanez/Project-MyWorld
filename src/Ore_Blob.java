import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Ore_Blob implements AnimationEntity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;

    private static final String BLOB_KEY = "blob";

    private static final String QUAKE_KEY = "quake";

    public Ore_Blob(

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

    public String getBGND() { return BLOB_KEY;}

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
        Optional<Entity> blobTarget = world.findNearest(this.position, Vein.class);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveToOreBlob(world, blobTarget.get(), scheduler)) {
                ActivityEntity quake = world.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions( scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                this.createActivityAction(world, imageStore), nextPeriod);
    }

    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent( this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction( 0),
                this.getAnimationPeriod());
    }

    private  boolean moveToOreBlob(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent( target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents( target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionOreBlob( world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private  Point nextPositionOreBlob(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        Optional<Entity> occupant = world.getOccupant( newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant( newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get() instanceof
                   Ore)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
