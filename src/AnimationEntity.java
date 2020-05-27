import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public abstract class AnimationEntity extends ActivityEntity
{
    private int animationPeriod;

    public AnimationEntity(int animationPeriod, int actionPeriod, String id, Point position, List<PImage> images, String bgnd ){
        super(actionPeriod, id, position, images, 0, bgnd);
        this.animationPeriod = animationPeriod;

    }
    public int getAnimationPeriod() {return animationPeriod;}
    public void nextImage(){this.ImageIndex((this.getImageIndex() + 1) % this.getImages().size());}
    public Action createAnimationAction(int repeatCount) {return new Animation(this, repeatCount); }

}
