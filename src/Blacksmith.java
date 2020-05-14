import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Blacksmith implements Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;


    private static final String SMITH_KEY = "blacksmith";

    public Blacksmith(
            String id,
            Point position,
            List<PImage> images)
    {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }

    public void setPosition(Point pos) { position = pos;}

    public  PImage getCurrentImage()
    {
        return this.images.get(this.imageIndex);
    }

    public  void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public String getId() { return id;}

    public Point getPosition() {return position;}

    public List<PImage> getImages() { return images;}

    public int getImageIndex() { return imageIndex;}

    public String getBGND() { return SMITH_KEY;}

}
