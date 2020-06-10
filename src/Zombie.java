import processing.core.PImage;

import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Zombie extends Miner
{

    public Zombie(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod) {

        super(animationPeriod, actionPeriod, id, position, images, "zombie");
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Miner_Not_Full.class);
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                Miner_Full.class);

        long nextPeriod = this.getActionPeriod();

        if (fullTarget.isPresent()) {
            Point tgtPos = fullTarget.get().getPosition();

            if (this.moveTo(world, fullTarget.get(), scheduler)) {
                ActivityEntity turn = Factory.createZombie(tgtPos, imageStore.getImageList("minerZombie"));

                world.addEntity(turn);
                nextPeriod += this.getActionPeriod();
                turn.scheduleActions( scheduler, world, imageStore);
            }
        }
        else if (notFullTarget.isPresent()){
            Point tgtPos = notFullTarget.get().getPosition();

            if (this.moveTo(world, notFullTarget.get(), scheduler)) {
                ActivityEntity turn = Factory.createZombie(tgtPos, imageStore.getImageList("minerZombie"));

                world.addEntity(turn);
                nextPeriod += this.getActionPeriod();
                turn.scheduleActions( scheduler, world, imageStore);
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