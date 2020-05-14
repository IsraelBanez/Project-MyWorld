
public interface ActivityEntity extends Entity
{
    public int getActionPeriod();
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
