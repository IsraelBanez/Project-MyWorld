import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Miner_Full extends Miner
{
    private int resourceLimit;
    private int resourceCount;

    public Miner_Full(

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


    public  void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && this.moveTo(world,
                fullTarget.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    private  void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        AnimationEntity miner = Factory.createMinerNotFull(this.getId(), this.resourceLimit, this.getPosition(),
                this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity( miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }


    public  boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent( target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


}
