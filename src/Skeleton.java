import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Skeleton extends Miner
{

    public Skeleton(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod) {

        super(animationPeriod, actionPeriod, id, position, images, "skeleton");
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> skullTarget = world.findNearest(this.getPosition(), Blacksmith.class);
        Optional<Entity> noneTarget = world.findNearest(this.getPosition(), Ore_Blob.class);
        long nextPeriod = this.getActionPeriod();

        if (skullTarget.isPresent()) {
            Point tgtPos = skullTarget.get().getPosition();

            if (this.moveTo(world, skullTarget.get(), scheduler)) {
                ActivityEntity destroy = Factory.createQuake( tgtPos, imageStore.getImageList("rubble"));

                world.addEntity(destroy);
                nextPeriod += this.getActionPeriod();
                destroy.scheduleActions( scheduler, world, imageStore);

            }
        }
        else{
            Point tgtPos = noneTarget.get().getPosition();

            if (this.moveTo(world, noneTarget.get(), scheduler)) {
                ActivityEntity burn = Factory.createQuake(tgtPos, imageStore.getImageList("fire"));

                world.addEntity(burn);
                nextPeriod += this.getActionPeriod();
                burn.scheduleActions( scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                this.createActivityAction(world, imageStore), nextPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

}