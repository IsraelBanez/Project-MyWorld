

public interface AnimationEntity extends ActivityEntity
{
    public int getAnimationPeriod();
    public void nextImage();
    public Action createAnimationAction(int repeatCount);

}
