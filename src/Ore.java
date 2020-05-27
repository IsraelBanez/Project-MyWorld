import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Ore extends ScheduleEntity
{

    private static final Random rand = new Random();
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

        super(actionPeriod, id, position, images, 0 , "ore");
    }


    public void executeActivity(WorldModel world, ImageStore imageStore,
                                   EventScheduler scheduler)
    {
        Point pos = this.getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        AnimationEntity blob = Factory.createOreBlob(this.getId() + BLOB_ID_SUFFIX, pos,
                this.getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(BLOB_ANIMATION_MAX
                        - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity( blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }


}
