import java.util.List;

import processing.core.PImage;

public final class Background
{
    private String id;
    private List<PImage> images;
    private int imageIndex;
    private static final String BGND_KEY = "background";

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
        this.imageIndex = 0;
    }

    public  PImage getCurrentImage()
    {
            return this.images.get(this.imageIndex);
    }
    public String getBGND(){
        return BGND_KEY;
    }
}

