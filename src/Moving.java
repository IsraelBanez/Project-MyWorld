import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class Moving extends AnimationEntity
{
    public Moving(int animationPeriod, int actionPeriod, String id, Point position, List<PImage> images,  String bgnd ){
        super(animationPeriod, actionPeriod, id, position, images, bgnd);

    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getAnimationPeriod());
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }


}
