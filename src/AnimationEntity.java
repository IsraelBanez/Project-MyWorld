

public interface AnimationEntity extends Entity
{
    public int getAnimationPeriod();
    public void nextImage();
    public Action createAnimationAction(int repeatCount);
    public int getActionPeriod();
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
