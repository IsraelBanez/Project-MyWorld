import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Zombie_Spawn extends ScheduleEntity
{
    private static int counter = 0;

    public Zombie_Spawn(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {

        super(actionPeriod, id, position, images, 0 , "smoke");
    }



    public void executeActivity(WorldModel world, ImageStore imageStore,
                                   EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent() && counter < 20) {
            Zombie zombie = Factory.createZombie(openPt.get(), imageStore.getImageList("zombies"));
            world.addEntity(zombie);
            zombie.scheduleActions(scheduler, world, imageStore);
            counter ++;

        }


        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

    }
}
