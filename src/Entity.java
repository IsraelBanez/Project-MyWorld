import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public interface Entity
{
    public PImage getCurrentImage();
    public void setPosition(Point position);
    public void nextImage();
    public String getId();
    public Point getPosition();
    public List<PImage> getImages();
    public int getImageIndex();
    public String getBGND();

}
