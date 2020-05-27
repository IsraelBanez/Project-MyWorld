import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class ScheduleEntity extends ActivityEntity {

    public ScheduleEntity(int actionPeriod, String id, Point position, List<PImage> images, int imageIndex, String bgnd) {
        super(actionPeriod, id, position, images, imageIndex, bgnd);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

    }
}
