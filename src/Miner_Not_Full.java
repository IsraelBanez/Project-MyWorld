import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Miner_Not_Full extends Miner
{

    private int resourceLimit;
    private int resourceCount;


    public Miner_Not_Full(

            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {

        super(animationPeriod, actionPeriod, id, position, images, "miner" );
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;

    }


    public  void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !this.moveTo(world,
                notFullTarget.get(),
                scheduler)
                || !this.transform( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction( world, imageStore),
                    this.getActionPeriod());
        }
    }


    private  boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            AnimationEntity miner = Factory.createMinerFull(this.getId(), this.resourceLimit, this.getPosition(),
                    this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity( miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public  boolean moveTo( WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent( target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity( target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return false;
        }
    }

}
