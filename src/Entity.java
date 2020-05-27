import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private String bgnd;
    private int imageIndex;

    public Entity( String id, Point position, List<PImage> images, int imageIndex, String bgnd){
        this.id = id;
        this.position= position;
        this.images = images;
        this.bgnd = bgnd;
        this.imageIndex = imageIndex;
    }

    public PImage getCurrentImage() {return images.get(imageIndex);}
    public void setPosition(Point pos) {position = pos;}
    public String getId() {return id;}
    public Point getPosition() {return position;}
    public List<PImage> getImages() {return images;}
    public int getImageIndex() {return imageIndex;}
    protected void ImageIndex(int nImage) { imageIndex = nImage;}
    public String getBGND() {return bgnd;}

}
